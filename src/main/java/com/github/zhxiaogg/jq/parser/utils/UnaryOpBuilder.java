package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.UnaryOp;
import lombok.Data;

@Data
class UnaryOpBuilder implements  AstBuilder<UnaryOp> {
    private final String op;

    @Override
    public UnaryOp build() {
        return new UnaryOp(op);
    }

}
