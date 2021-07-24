package com.github.zhxiaogg.jq.analyzer.rules;

import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.UnResolvedAttribute;
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
                int[] ordinals = attributes.byId(node.getId());

                if (ordinals.length > 0) {
                    return Optional.of(attributes.getAttribute(ordinals, 0).withOrdinals(ordinals).withNames(new String[]{e.toString()}));
                } else if (e instanceof UnResolvedAttribute) {
                    String[] names = ((UnResolvedAttribute) e).getNames();
                    ordinals = attributes.byName(names, 0);
                    if (ordinals.length > 0) {
                        return Optional.of(attributes.getAttribute(ordinals, 0).withOrdinals(ordinals).withNames(names));
                    } else {
                        return Optional.empty();
                    }
                } else {
                    return Optional.empty();
                }
            }
        });
    }
}
