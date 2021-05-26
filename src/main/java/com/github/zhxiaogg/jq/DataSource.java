package com.github.zhxiaogg.jq;

import com.github.zhxiaogg.jq.nodes.plans.LogicalPlan;
import com.github.zhxiaogg.jq.schema.Schema;
import com.github.zhxiaogg.jq.stream.Streaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ToString
@EqualsAndHashCode
public class DataSource {
    private final List<Relation> relations;

    public DataSource(List<Relation> relations) {
        this.relations = relations;
    }

    public static DataSource create(Relation... relations) {
        return new DataSource(Arrays.asList(relations.clone()));
    }

    public Streaming streamQuery(LogicalPlan plan) {
        // change the logical plan into physical plan
        // optional: code gen
        // return the streaming object:
        // 1. contains aggregating states
        // 2. optional: provides a concise api to dump the internal states
        // 3. optional: load the states back when reboot a streaming object
        return new Streaming(plan, this);
    }

    public Optional<Schema> schemaOf(Class<?> clazz) {
        List<Schema> schemas = relations.stream().filter(ds -> ds.getSchema().getName().getClazz() == clazz).map(Relation::getSchema).collect(Collectors.toList());
        if (schemas.size() > 1)
            throw new IllegalArgumentException("multiple schemas are registered with class " + clazz.getCanonicalName());
        return schemas.stream().findFirst();

    }

    public Optional<Relation> relationOf(String name) {
        return relations.stream().filter(ds -> ds.getSchema().getName().getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Relation> relationOf(Class<?> clazz) {
        return relations.stream().filter(ds -> ds.getSchema().getName().getClazz() == clazz).findFirst();
    }
}
