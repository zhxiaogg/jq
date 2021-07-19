package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ResolveExpressionAttributeRule implements Rule<Expression> {
    private final AttributeSet attributes;
    // TODO: fix this
    private final boolean ignoreResolved;

    public ResolveExpressionAttributeRule(AttributeSet attributes) {
        this(attributes, true);
    }

    @Override
    public Optional<Expression> apply(Expression node) {
        return node.transformUp(e -> {
            if (e.isResolved() && ignoreResolved) {
                return Optional.empty();
            } else {
                int ordinal = attributes.byId(node.getId());
                // TODO: is byName valid?
                if (ordinal < 0) {
                    ordinal = attributes.byName(e.toString());
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
