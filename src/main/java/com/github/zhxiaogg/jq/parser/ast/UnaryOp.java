package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class UnaryOp implements AstNode, AstBuilder<UnaryOp> {
    private final String op;

    @Override
    public UnaryOp build() {
        return this;
    }

    public interface AcceptUnaryOp {
        void accept(UnaryOp op);
    }
}
