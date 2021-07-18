package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.schema.Attribute;

import java.util.Optional;

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
                Attribute[] childrenOutputs = n.getChildren().stream().flatMap(p -> p.outputs(dataSource).stream()).toArray(Attribute[]::new);
                AttributeSet attributes = new AttributeSet(childrenOutputs);
                // TODO: make sure children outputs are all resolved before resolving current expressions
                ResolveExpressionAttributeRule expressionRule = new ResolveExpressionAttributeRule(attributes);
                return n.transformExpressionsUp(expressionRule);
            } else {
                return Optional.empty();
            }
        });
    }

}
