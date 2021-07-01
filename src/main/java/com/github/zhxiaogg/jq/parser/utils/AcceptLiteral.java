package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Literal;

interface AcceptLiteral {
    void accept(Literal literal);
}
