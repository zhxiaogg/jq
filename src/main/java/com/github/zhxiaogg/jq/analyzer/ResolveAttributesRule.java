package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.schema.Attribute;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResolveAttributesRule implements Rule<LogicalPlan> {
    private final Catalog dataSource;

    public ResolveAttributesRule(Catalog dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(r -> {
            if (!r.leafNode()) {
                // read attributes from children and resolve expressions against the attributes
                List<LogicalPlan> children = r.getChildren();
                Attribute[] childrenOutputs = children.stream().flatMap(p -> p.outputs(dataSource).stream()).toArray(Attribute[]::new);
                AttributeSet attributes = new AttributeSet(childrenOutputs);
                List<Optional<Expression>> optResolvedExpressions = r.getExpressions().stream().map(expr -> {
                    return expr.transformUp(e -> {
                        int ordinal;
                        if (e instanceof UnResolvedAttribute) {
                            ordinal = attributes.byName(((UnResolvedAttribute) e).getName());
                        } else {
                            ordinal = attributes.byId(expr.getId());
                        }
                        if (ordinal > -1) {
                            Attribute attribute = attributes.getAttribute(ordinal);
                            return Optional.of(new ResolvedAttribute(attribute.getId(), attribute.getName(), attribute.getDataType(), ordinal));
                        } else {
                            return Optional.empty();
                        }
                    });
                }).collect(Collectors.toList());

                // create a new node if needed
                if (optResolvedExpressions.stream().allMatch(Optional::isPresent)) {
                    return Optional.of(r.withExpressions(optResolvedExpressions.stream().map(Optional::get).collect(Collectors.toList())));
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        });
    }
}
