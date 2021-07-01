package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.UnaryOp;

interface AcceptUnaryOp {
    void accept(UnaryOp op);
}
