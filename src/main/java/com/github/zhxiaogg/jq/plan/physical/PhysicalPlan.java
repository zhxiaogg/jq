package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.plan.Node;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;

public interface PhysicalPlan extends Node<PhysicalPlan> {
    RecordBag exec();

    AttributeSet outputs();
}
