package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.catalog.Relation;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.plan.logical.*;
import com.github.zhxiaogg.jq.plan.physical.*;
import com.github.zhxiaogg.jq.utils.Pair;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class PhysicalPlanner {
    private final Catalog catalog;

    public PhysicalPlan plan(LogicalPlan plan) {
        PhysicalPlan result = new PlanLater(plan);
        while (true) {
            Optional<PhysicalPlan> optResolved = result.transformUp(p -> {
                if (p instanceof PlanLater) {
                    PhysicalPlan resolved = this.apply(((PlanLater) p).getPlan());
                    if (resolved instanceof PlanLater) {
                        return Optional.empty();
                    } else {
                        return Optional.of(resolved);
                    }
                } else {
                    return Optional.empty();
                }
            });
            if (optResolved.isPresent()) {
                result = optResolved.get();
            } else {
                break;
            }
        }
        return result;
    }

    public PhysicalPlan apply(LogicalPlan plan) {
        if (plan instanceof Scan) {
            Optional<Relation> relation = catalog.relationOf(((Scan) plan).getRelation());
            if (relation.isPresent()) {
                return new ScanExec(relation.get());
            } else {
                return new PlanLater(plan);
            }
        } else if (plan instanceof Filter) {
            return new FilterExec(((Filter) plan).getCondition(), new PlanLater(((Filter) plan).getChild()));
        } else if (plan instanceof Project) {
            return new ProjectExec(((Project) plan).getProjections(), new PlanLater(((Project) plan).getChild()));
        } else if (plan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) plan;
            Pair<List<AggExpression>, List<Expression>> extractResult = AggregatorUtil.extractAggregators(aggregate.getAggregators());
            return new AggregateExec(aggregate.getGroupingKeys(), new ArrayList<>(extractResult.getLeft()), extractResult.getRight(), new PlanLater(aggregate.getChild()));
        } else {
            return new PlanLater(plan);
        }
    }
}
