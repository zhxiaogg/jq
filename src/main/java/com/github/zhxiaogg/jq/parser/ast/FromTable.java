package com.github.zhxiaogg.jq.parser.ast;

public interface FromTable extends AstNode {
    interface AcceptFromTable {
        void accept(FromTable fromTable);
    }
}
