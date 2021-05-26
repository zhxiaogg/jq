package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.nodes.plans.LogicalPlan;
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
