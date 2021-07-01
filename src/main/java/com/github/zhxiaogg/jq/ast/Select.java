package com.github.zhxiaogg.jq.ast;

import lombok.Data;

import java.util.List;

@Data
public class Select implements AstNode {
    private final List<ResultColumn> resultColumns;
    private final FromTable fromTable;
    private final Where where;
    private final GroupBy groupBy;
}
