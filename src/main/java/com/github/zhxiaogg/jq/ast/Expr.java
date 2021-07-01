package com.github.zhxiaogg.jq.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public interface Expr extends AstNode {

    @Data
    @AllArgsConstructor
    class ExprLiteral implements Expr {
        private final Literal literal;
    }

    @Data
    class ExprColumnRef implements Expr {
        private final TableName tableName;
        private final ColumnName columnName;
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

    @Data
    class ExprBinaryMath implements Expr {
        private final Expr left;
        private final Expr right;
        private final String op;
    }

    @Data
    class ExprCompare implements Expr {
        private final Expr left;
        private final Expr right;
        private final String op;
    }

    @Data
    class ExprAnd implements Expr {
        private final Expr left;
        private final Expr right;
    }


    @Data
    class ExprOr implements Expr {
        private final Expr left;
        private final Expr right;
    }

    @Data
    class ExprBetween implements Expr {
        private final Expr expr;
        private final Expr left;
        private final Expr right;
    }

    @Data
    class ExprIn implements Expr {
        private final Expr expr;
        private final List<Expr> exprs;
        private final Select select;
    }

    @Data
    class ExprFunctionCall implements Expr {
        private final FuncName funcName;
        private final List<Expr> args;
    }


}
