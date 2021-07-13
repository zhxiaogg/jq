package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.schema.Attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.zhxiaogg.jq.utils.ListUtils.zipList;

public class ResolveAttributesRule implements Rule<LogicalPlan> {
    private final Catalog dataSource;

    public ResolveAttributesRule(Catalog dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        return node.transformUp(r -> {
            if (!r.leafNode()) {
                // read attributes from children and index them
                // TODO: make this lazy
                List<LogicalPlan> children = r.getChildren();
                Map<String, ResolvedAttribute> attributeMap = new HashMap<>();
                int i = 0;
                for (LogicalPlan child : children) {
                    List<Attribute> attributes = child.outputs(dataSource);
                    for (Attribute attribute : attributes) {
                        attributeMap.put(attribute.getName(), ResolvedAttribute.create(attribute.getName(), attribute.getDataType(), i++));
                    }
                }

                // resolve UnResolvedAttribute according to the attributesMap
                List<Optional<Expression>> newExpressions = r.getExpressions().stream().map(expr -> expr.transformUp(e -> {
                    if (e instanceof UnResolvedAttribute) {
                        return Optional.of(attributeMap.get(((UnResolvedAttribute) e).getName()));
                    } else {
                        return Optional.empty();
                    }
                })).collect(Collectors.toList());

                // create a new node if needed
                if (newExpressions.stream().anyMatch(Optional::isPresent)) {
                    List<Expression> expressions = zipList(r.getExpressions(), newExpressions);
                    return Optional.of(r.withExpressions(expressions));
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        });
    }
}
