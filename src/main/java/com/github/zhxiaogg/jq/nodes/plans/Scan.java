package com.github.zhxiaogg.jq.nodes.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.Relation;
import com.github.zhxiaogg.jq.nodes.plans.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@ToString
@EqualsAndHashCode
public class Scan implements LeafPlanNode {
    private final String relation;

    public static Scan from(String relation) {
        return new Scan(relation);
    }

    @Override
    public RecordBag partialEval(DataSource dataSource) {
        Optional<Relation> dataSet = dataSource.relationOf(relation);
        if (dataSet.isPresent()) {
            return dataSet.get().records();
        } else {
            throw new IllegalArgumentException("dataset not registered: " + relation);
        }
    }

    @Override
    public List<Attribute> getAttributes(DataSource dataSource) {
        Optional<Relation> relation = dataSource.relationOf(this.relation);
        if (relation.isPresent()) {
            return Collections.unmodifiableList(relation.get().getSchema().getAttributes());
        } else {
            throw new IllegalArgumentException(String.format("relation %s not found!", relation));
        }
    }

    @Override
    public boolean leafNode() {
        return true;
    }
}
