package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.JoinType;
import lombok.Data;

@Data
public class JoinOp implements AstNode {
    private final JoinType joinType;
    private final boolean natural;

}
