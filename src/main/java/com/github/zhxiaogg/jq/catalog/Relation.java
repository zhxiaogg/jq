package com.github.zhxiaogg.jq.catalog;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.exec.SimpleAttributeSet;
import com.github.zhxiaogg.jq.utils.Pair;

public interface Relation {
    static Relation create(String name, Class<?> clazz) {
        Pair<AttributeSet, RecordReader> attributeSet = ObjectRelation.resolveAttributeSet(clazz);
        Schema schema = new Schema(new String[]{name}, ((SimpleAttributeSet) attributeSet.getLeft()).withName(name), attributeSet.getRight());
        return new ObjectRelation(schema, clazz);
    }

    RecordBag scan();

    void insert(Object data);

    Schema getSchema();
}
