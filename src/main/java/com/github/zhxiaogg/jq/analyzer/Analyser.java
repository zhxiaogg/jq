package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.nodes.plans.LogicalPlan;

import java.util.List;
import java.util.Optional;

public interface Analyser {
    List<Batch> getBatches();

    default LogicalPlan analysis(LogicalPlan plan) {
        List<Batch> batches = getBatches();
        for (Batch batch : batches) {
            List<Rule<LogicalPlan>> rules = batch.getRules();
            boolean changed;
            do {
                changed = false;
                for (Rule<LogicalPlan> rule : rules) {
                    Optional<LogicalPlan> newOptPlan = rule.apply(plan);
                    if (newOptPlan.isPresent()) {
                        changed = true;
                        plan = newOptPlan.get();
                    }
                }
            } while (changed);
        }
        return plan;
    }
}
