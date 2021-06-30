package com.github.zhxiaogg.jq.parser.ast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public interface Expr extends AstNode {

    interface ExprBuilder<T extends Expr> extends AstBuilder<T> {
        @Override
        T build();
    }

    interface AcceptExpr {
        void accept(Expr expr);
    }

    @Data
    @AllArgsConstructor
    class ExprLiteral implements Expr {
        private final Literal literal;
    }

    class ExprLiteralBuilder implements ExprBuilder<ExprLiteral>, Literal.AcceptLiteral {
        private Literal literal;

        @Override
        public ExprLiteral build() {
            return new ExprLiteral(literal);
        }

        @Override
        public void accept(Literal literal) {
            this.literal = literal;
        }
    }

    @Data
    class ExprColumnRef implements Expr {
        private final TableName tableName;
        private final ColumnName columnName;
    }

    class ExprColumnRefBuilder implements ExprBuilder<ExprColumnRef>, ColumnName.AcceptColumnName, TableName.AcceptTableName {
        private TableName tableName;
        private ColumnName columnName;

        @Override
        public ExprColumnRef build() {
            return new ExprColumnRef(tableName, columnName);
        }

        @Override
        public void accept(ColumnName columnName) {
            this.columnName = columnName;
        }

        @Override
        public void accept(TableName tableName) {
            this.tableName = tableName;
        }
    }

    @Data
    class ExprNot implements Expr {
        private final Expr expr;
    }

    // TODO: convert to ExprNot when op is NOT
    @Data
    class ExprUnary implements Expr {
        private final UnaryOp op;
        private final Expr expr;
    }

    class ExprUnaryBuilder implements ExprBuilder<ExprUnary>, UnaryOp.AcceptUnaryOp, AcceptExpr {
        private UnaryOp op;
        private Expr expr;

        @Override
        public ExprUnary build() {
            return new ExprUnary(op, expr);
        }

        @Override
        public void accept(Expr expr) {
            this.expr = expr;
        }

        @Override
        public void accept(UnaryOp op) {
            this.op = op;
        }
    }

    @Data
    class ExprBinaryMath implements Expr {
        private final Expr left;
        private final Expr right;
        private final String op;
    }

    @RequiredArgsConstructor
    class ExprBinaryMathBuilder implements ExprBuilder<ExprBinaryMath>, AcceptExpr {
        private final String op;
        private Expr left;
        private Expr right;

        @Override
        public ExprBinaryMath build() {
            return new ExprBinaryMath(left, right, op);
        }

        @Override
        public void accept(Expr expr) {
            if (this.left == null) this.left = expr;
            else this.right = expr;
        }
    }

    @Data
    class ExprCompare implements Expr {
        private final Expr left;
        private final Expr right;
        private final String op;
    }

    @RequiredArgsConstructor
    class ExprCompareBuilder implements ExprBuilder<ExprCompare>, AcceptExpr {
        private final String op;
        private Expr left;
        private Expr right;

        @Override
        public ExprCompare build() {
            return new ExprCompare(left, right, op);
        }

        @Override
        public void accept(Expr expr) {
            if (this.left == null) this.left = expr;
            else this.right = expr;
        }
    }

    @Data
    class ExprAnd implements Expr {
        private final Expr left;
        private final Expr right;
    }


    class ExprAndBuilder implements ExprBuilder<ExprAnd>, AcceptExpr {
        private Expr left;
        private Expr right;

        @Override
        public ExprAnd build() {
            return new ExprAnd(left, right);
        }

        @Override
        public void accept(Expr expr) {
            if (this.left == null) this.left = expr;
            else this.right = expr;
        }
    }

    @Data
    class ExprOr implements Expr {
        private final Expr left;
        private final Expr right;
    }

    class ExprOrBuilder implements ExprBuilder<ExprOr>, AcceptExpr {
        private Expr left;
        private Expr right;

        @Override
        public ExprOr build() {
            return new ExprOr(left, right);
        }

        @Override
        public void accept(Expr expr) {
            if (this.left == null) this.left = expr;
            else this.right = expr;
        }
    }

    @Data
    class ExprBetween implements Expr {
        private final Expr expr;
        private final Expr left;
        private final Expr right;
    }

    @RequiredArgsConstructor
    class ExprBetweenBuilder implements ExprBuilder<Expr>, AcceptExpr {
        private final boolean not;
        private Expr expr;
        private Expr left;
        private Expr right;

        @Override
        public Expr build() {
            ExprBetween between = new ExprBetween(expr, left, right);
            if (not) {
                return new ExprNot(between);
            } else {
                return between;
            }
        }

        @Override
        public void accept(Expr expr) {
            if (this.expr == null) this.expr = expr;
            else if (this.left == null) this.left = expr;
            else this.right = expr;
        }
    }

    @Data
    class ExprIn implements Expr {
        private final Expr expr;
        private final List<Expr> exprs;
        private final Select select;
    }

    @RequiredArgsConstructor
    class ExprInBuilder implements ExprBuilder<Expr>, AcceptExpr, Select.AcceptSelect {
        private final boolean not;
        private Expr expr;
        private final List<Expr> exprs = new ArrayList<>();
        private Select select;

        @Override
        public Expr build() {
            ExprIn in = new ExprIn(expr, exprs, select);
            if (not) {
                return new ExprNot(in);
            } else {
                return in;
            }
        }

        @Override
        public void accept(Expr expr) {
            if (this.expr == null) this.expr = expr;
            else this.exprs.add(expr);
        }

        @Override
        public void accept(Select select) {
            this.select = select;
        }
    }

    @Data
    class ExprFunctionCall implements Expr {
        private final FuncName funcName;
        private final List<Expr> args;
    }

    public static class ExprFunctionCallBuilder implements ExprBuilder<ExprFunctionCall>, AcceptExpr, FuncName.AcceptFuncName {
        private FuncName funcName;
        private final List<Expr> args = new ArrayList<>();

        @Override
        public ExprFunctionCall build() {
            return new ExprFunctionCall(funcName, args);
        }

        @Override
        public void accept(Expr expr) {
            this.args.add(expr);
        }

        @Override
        public void accept(FuncName funcName) {
            this.funcName = funcName;
        }
    }


}
