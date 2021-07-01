package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class FuncName implements AstNode {
    private final String name;
}
