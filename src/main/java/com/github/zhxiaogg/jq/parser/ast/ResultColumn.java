package com.github.zhxiaogg.jq.parser.ast;

public interface ResultColumn extends AstNode {

    interface ResultColumnBuilder<T extends ResultColumn> extends AstBuilder<T> {
        @Override
        public T build();
    }

    class ResultColumnStar implements ResultColumn {
        @Override
        public String toString() {
            return "*";
        }
    }

    class ResultColumnStarBuilder implements ResultColumnBuilder<ResultColumnStar> {
        @Override
        public ResultColumnStar build() {
            return new ResultColumnStar();
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

    class ResultColumnTableStarBuilder implements ResultColumnBuilder<ResultColumnTableStar> {
        private final String tableName;

        public ResultColumnTableStarBuilder(String tableName) {
            this.tableName = tableName;
        }

        @Override
        public ResultColumnTableStar build() {
            return new ResultColumnTableStar(tableName);
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

    class ResultColumnExprBuilder implements ResultColumnBuilder<ResultColumnExpr>, Expr.AcceptExpr {
        private Expr expr;

        @Override
        public ResultColumnExpr build() {
            return new ResultColumnExpr(expr);
        }

        @Override
        public void accept(Expr expr) {
            this.expr = expr;
        }
    }

    interface AcceptResultColumn {
        void accept(ResultColumn resultColumn);
    }
}
