package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class TableAlias implements AstNode {
    private final String name;
}
