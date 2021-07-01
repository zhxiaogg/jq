package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Select;

interface AcceptSelect {
    void accept(Select select);
}
