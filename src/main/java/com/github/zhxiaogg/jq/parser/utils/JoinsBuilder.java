package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.FromTable;
import com.github.zhxiaogg.jq.ast.Join;

class JoinsBuilder implements FromTableBuilder<FromTable.Joins>, AcceptJoin {
    private Join join;

    @Override
    public FromTable.Joins build() {
        return new FromTable.Joins(join);
    }

    @Override
    public void accept(Join join) {
        this.join = join;
    }
}
