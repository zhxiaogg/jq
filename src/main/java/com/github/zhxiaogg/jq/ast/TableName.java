package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class TableName implements AstNode {
    private final String name;
}
