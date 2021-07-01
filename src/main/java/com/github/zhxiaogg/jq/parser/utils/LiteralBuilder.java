package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Literal;
import lombok.Data;

@Data
class LiteralBuilder implements AstBuilder<Literal> {
    private final Object value;

    @Override
    public Literal build() {
        return new Literal(value);
    }
}
