package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.StarAttribute;

public interface ResultColumn extends AstNode {

    Expression toExpression();

    class ResultColumnStar implements ResultColumn {
        @Override
        public String toString() {
            return "*";
        }

        @Override
        public Expression toExpression() {
            return new StarAttribute(null);
        }
    }

    class ResultColumnTableStar implements ResultColumn {
        private final String tableName;

        public ResultColumnTableStar(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }

        @Override
        public String toString() {
            return tableName + ".*";
        }

        @Override
        public Expression toExpression() {
            return new StarAttribute(tableName);
        }
    }


    class ResultColumnExpr implements ResultColumn {
        private final Expr expr;

        public ResultColumnExpr(Expr expr) {
            this.expr = expr;
        }

        @Override
        public String toString() {
            return expr.toString();
        }

        @Override
        public Expression toExpression() {
            return expr.toExpression();
        }
    }

}
