package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class ColumnName implements AstNode {
    private final String name;
}
