package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.utils.AttributeSearchUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.github.zhxiaogg.jq.utils.Requires.require;

/**
 * A {@link Relation} implementation that will update its schema on the fly.
 * <p>
 * The records are of type `Map<String,Object>`.
 */
@RequiredArgsConstructor
public class UnStructuralRelation implements Relation {
    private final List<Map<String, Object>> records = new LinkedList<>();
    private final AtomicReference<Schema> schema;

    @Override
    public RecordBag scan() {
        List<Record> records = this.records.stream().map(d -> schema.get().getReader().read(d)).collect(Collectors.toList());
        return new RecordBag(records);
    }

    @Override
    public void insert(Object data) {
        if (!(data instanceof Map)) {
            throw new IllegalArgumentException("only Map<String,Object> data is acceptable!");
        }

        records.add((Map<String, Object>) data);
    }

    @Override
    public Schema getSchema() {
        return schema.get();
    }

    /**
     * Update the schema to support the given {@link UnResolvedAttribute}s.
     *
     * @param attributes
     */
    public void update(List<UnResolvedAttribute> attributes) {
        List<ResolvedAttribute> resolvedAttributes = attributes.stream().map(this::toResolved).collect(Collectors.toList());
        Schema schema;
        Schema newSchema;
        do {
            schema = this.schema.get();
            AttributeSet existingAttributes = schema.getAttributes();
            AttributeSet updatedAttributes = existingAttributes.mergeWith(AttributeSet.create(resolvedAttributes));
            newSchema = schema.withAttributes(updatedAttributes).withReader(new UnStructuralRecordReader(updatedAttributes));
        } while (!this.schema.compareAndSet(schema, newSchema));
    }

    @Data
    private static class UnStructuralRecordReader implements RecordReader {
        private final AttributeSet attributes;

        @Override
        public Record read(Object data) {
            require(data instanceof Map, "invalid input data!");
            List<Object> values = attributes.allAttributes().stream().map(attri -> {
                String name = attri.getNames()[0];
                Object value = ((Map<?, ?>) data).get(name);
                if (value != null && attri.getDataType().equals(DataType.Struct)) {
                    value = new UnStructuralRecordReader(attri.getInner()).read(value);
                }
                return value;
            }).collect(Collectors.toList());
            return Record.create(values);
        }
    }

    private ResolvedAttribute toResolved(UnResolvedAttribute attribute) {
        String[] names = attribute.getNames();
        String[] relationNames = schema.get().getNames();
        int offset = AttributeSearchUtil.prefixMatches(names, relationNames, 0) ? relationNames.length : 0;
        if (offset > 0) {
            String[] newNames = new String[names.length - offset];
            for (int i = offset; i < names.length; i++) {
                newNames[i - offset] = names[i];
            }
            names = newNames;
        }
        ResolvedAttribute resolved = new ResolvedAttribute(attribute.getId(), names, DataType.Any, new int[0], AttributeSet.empty(new String[0]));
        return resolved.expand();
    }
}
