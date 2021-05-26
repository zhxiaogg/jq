package com.github.zhxiaogg.jq.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;

public interface LogicalPlan {
    RecordBag partialEval(DataSource dataSource);
}
