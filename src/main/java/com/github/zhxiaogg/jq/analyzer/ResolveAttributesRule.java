package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

public class ResolveAttributesRule implements Rule<LogicalPlan> {
    private final Catalog dataSource;

    public ResolveAttributesRule(Catalog dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(n -> {
            if (!n.leafNode()) {
                // read attributes from children and resolve expressions against the attributes
                Attribute[] childrenOutputs = n.getChildren().stream().flatMap(p -> p.outputs(dataSource).stream()).toArray(Attribute[]::new);
                AttributeSet attributes = new AttributeSet(childrenOutputs);
                ResolveExpressionAttributeRule expressionRule = new ResolveExpressionAttributeRule(attributes);
                return n.transformExpressionsUp(expressionRule);
            } else {
                return Optional.empty();
            }
        });
    }

    @RequiredArgsConstructor
    private static class ResolveExpressionAttributeRule implements Rule<Expression> {
        private final AttributeSet attributes;

        @Override
        public Optional<Expression> apply(Expression node) {
            return node.transformUp(e -> {
                if (e.isResolved()) {
                    return Optional.empty();
                } else {
                    int ordinal;
                    if (e instanceof UnResolvedAttribute) {
                        ordinal = attributes.byName(((UnResolvedAttribute) e).getName());
                    } else {
                        ordinal = attributes.byId(node.getId());
                    }
                    if (ordinal > -1) {
                        Attribute attribute = attributes.getAttribute(ordinal);
                        return Optional.of(new ResolvedAttribute(attribute.getId(), attribute.getName(), attribute.getDataType(), ordinal));
                    } else {
                        return Optional.empty();
                    }
                }
            });
        }
    }
}
