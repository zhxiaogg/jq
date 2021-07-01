package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.FuncName;
import lombok.Data;

@Data
class FuncNameBuilder implements AstBuilder<FuncName> {
    private final String name;

    @Override
    public FuncName build() {
        return new FuncName(name);
    }
}
