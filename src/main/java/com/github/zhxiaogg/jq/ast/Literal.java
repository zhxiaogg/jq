package com.github.zhxiaogg.jq.ast;

import lombok.Data;

@Data
public class Literal implements AstNode {
    private final Object value;
}
