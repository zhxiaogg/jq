package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupBy implements AstNode {
    private final List<Expr> groupBys;
    private final Having having;

    public interface AcceptGroupBy {
        void accept(GroupBy groupBy);
    }

    public static class GroupByBuilder implements AstBuilder<GroupBy>, Expr.AcceptExpr, Having.AcceptHaving {
        private final List<Expr> groupBys = new ArrayList<>();
        private Having having;

        @Override
        public GroupBy build() {
            return new GroupBy(groupBys, having);
        }

        @Override
        public void accept(Expr expr) {
            groupBys.add(expr);
        }

        @Override
        public void accept(Having having) {
            this.having = having;
        }
    }

}
