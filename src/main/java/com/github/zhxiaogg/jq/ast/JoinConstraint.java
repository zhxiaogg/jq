package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class JoinConstraint implements AstNode {
    private final Expr expr;

}
