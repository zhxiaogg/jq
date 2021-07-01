package com.github.zhxiaogg.jq.ast;

public interface ResultColumn extends AstNode {

    class ResultColumnStar implements ResultColumn {
        @Override
        public String toString() {
            return "*";
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
    }

}
