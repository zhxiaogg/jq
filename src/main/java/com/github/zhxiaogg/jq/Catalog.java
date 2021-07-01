package com.github.zhxiaogg.jq;

import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.streaming.StreamingQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@ToString
@EqualsAndHashCode
public class Catalog {
    private final List<Relation> relations;

    public Catalog(List<Relation> relations) {
        this.relations = relations;
    }

    public static Catalog create(Relation... relations) {
        return new Catalog(Arrays.asList(relations.clone()));
    }

    public StreamingQuery streamQuery(LogicalPlan plan) {
        // change the logical plan into physical plan
        // optional: code gen
        // return the streaming object:
        // 1. contains aggregating states
        // 2. optional: provides a concise api to dump the internal states
        // 3. optional: load the states back when reboot a streaming object
        return new StreamingQuery(plan, this);
    }

    public Optional<Relation> relationOf(String name) {
        return relations.stream().filter(ds -> ds.getSchema().getName().getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Relation> relationOf(Class<?> clazz) {
        return relations.stream().filter(ds -> ds.getSchema().getName().getClazz() == clazz).findFirst();
    }
}
