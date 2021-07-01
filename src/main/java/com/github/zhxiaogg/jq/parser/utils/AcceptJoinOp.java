package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.JoinOp;

interface AcceptJoinOp {
    void accept(JoinOp joinOp);
}
