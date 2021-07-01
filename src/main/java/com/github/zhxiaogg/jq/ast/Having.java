package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class Having implements AstNode {
    private final Expr expr;
}
