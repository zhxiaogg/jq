package com.github.zhxiaogg.jq.ast;

import lombok.Data;

import java.util.List;

@Data
public class GroupBy implements AstNode {
    private final List<Expr> groupBys;
    private final Having having;
}
