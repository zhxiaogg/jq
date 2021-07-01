package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.JoinOp;
import lombok.Data;

@Data
class JoinOpBuilder implements AstBuilder<JoinOp> {
    private final JoinOp.JoinType joinType;
    private final boolean natural;

    @Override
    public JoinOp build() {
        return new JoinOp(joinType, natural);
    }
}
