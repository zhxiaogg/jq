package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Batch {
    private final List<Rule<LogicalPlan>> rules;
}
