package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.catalog.UnStructuralRelation;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plan.logical.Aggregate;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.logical.Scan;
import com.github.zhxiaogg.jq.utils.ListUtils;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * If there is a {@link com.github.zhxiaogg.jq.plan.logical.Scan} node that is reading a
 * {@link com.github.zhxiaogg.jq.catalog.UnStructuralRelation}, this rule will try to extract
 * all the {@link com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute}s and update the underlying
 * schema of the {@link com.github.zhxiaogg.jq.catalog.UnStructuralRelation}.
 * <p>
 * This rule is supposed to be run after {@link ResolveHavingConditionRule}. If there is no
 * {@link com.github.zhxiaogg.jq.plan.logical.Aggregate}, the rule will start applying at the head of the plan node.
 * Otherwise, the rule will start applying at the {@link com.github.zhxiaogg.jq.plan.logical.Aggregate} node.
 * <p>
 * For now, all the attributes will have type of {@link com.github.zhxiaogg.jq.datatypes.DataType#Any}.
 * <p>
 * Notes, for a plan with unstructural relation:
 * 1. only one unstructural relation is allowed
 * 2. there should be no {@link com.github.zhxiaogg.jq.plan.exprs.StarAttribute}
 * 3. no natural join is allowed
 * 4. TODO: sub query is not supported for now.
 */
@Data
public class UpdateUnStructuralRelationRule implements Rule<LogicalPlan> {
    private final Catalog catalog;

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        Optional<LogicalPlan> optUnStructuralScan = findFirst(node, this::isUnStructuralRelationScan);
        if (optUnStructuralScan.isPresent()) {
            Optional<LogicalPlan> optAggregate = findFirst(node, n -> n instanceof Aggregate);
            List<UnResolvedAttribute> unResolvedAttributes = collectUnResolvedAttributes(optAggregate.orElse(node));

            UnStructuralRelation relation = (UnStructuralRelation) catalog.relationOf(((Scan) optUnStructuralScan.get()).getRelation()).get();
            relation.update(unResolvedAttributes);
        }
        return Optional.empty();
    }

    private boolean isUnStructuralRelationScan(LogicalPlan node) {
        return node instanceof Scan && catalog.relationOf(((Scan) node).getRelation()).filter(r -> r instanceof UnStructuralRelation).isPresent();
    }

    private List<UnResolvedAttribute> collectUnResolvedAttributes(LogicalPlan node) {
        List<UnResolvedAttribute> unResolvedAttributes = node.getExpressions().stream()
                .flatMap(e -> findUnResolvedAttributes(e).stream())
                .collect(Collectors.toList());
        List<UnResolvedAttribute> unResolvedAttributes1 = node.getChildren().stream().flatMap(p -> collectUnResolvedAttributes(p).stream()).collect(Collectors.toList());
        return ListUtils.concat(unResolvedAttributes, unResolvedAttributes1);
    }


    private List<UnResolvedAttribute> findUnResolvedAttributes(Expression node) {
        if (node instanceof UnResolvedAttribute) {
            return Collections.singletonList((UnResolvedAttribute) node);
        } else {
            return node.getChildren().stream()
                    .flatMap(e -> findUnResolvedAttributes(e).stream())
                    .collect(Collectors.toList());
        }
    }

    private Optional<LogicalPlan> findFirst(LogicalPlan node, Predicate<LogicalPlan> predicate) {
        if (predicate.test(node)) {
            return Optional.of(node);
        } else {
            return node.getChildren().stream()
                    .map(n -> findFirst(n, predicate))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny();
        }
    }
}
