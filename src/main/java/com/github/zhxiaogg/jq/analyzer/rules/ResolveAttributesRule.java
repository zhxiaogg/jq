package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.SimpleAttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResolveAttributesRule implements Rule<LogicalPlan> {
    private final Catalog dataSource;

    public ResolveAttributesRule(Catalog catalog) {
        this.dataSource = catalog;
    }

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(n -> {
            if (!n.leafNode()) {
                // read attributes from children and resolve expressions against the attributes
                List<AttributeSet> attributeSets = n.getChildren().stream().map(p -> p.outputs(dataSource)).collect(Collectors.toList());
                AttributeSet attributes = AttributeSet.createMerged(attributeSets);
                // TODO: make sure children outputs are all resolved before resolving current expressions
                ResolveExpressionAttributeRule expressionRule = new ResolveExpressionAttributeRule(attributes);
                return n.transformExpressionsUp(expressionRule);
            } else {
                return Optional.empty();
            }
        });
    }

}
