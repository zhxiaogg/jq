package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.plan.exprs.*;
import com.github.zhxiaogg.jq.plan.exprs.booleans.*;
import com.github.zhxiaogg.jq.plan.exprs.math.*;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface Expr extends AstNode {
    Expression toExpression();

    @Data
    @AllArgsConstructor
    class ExprLiteral implements Expr {
        private final Literal literal;

        @Override
        public Expression toExpression() {
            return new com.github.zhxiaogg.jq.plan.exprs.Literal(literal.getValue(), DataType.UnKnown);
        }
    }

    @Data
    class ExprColumnRef implements Expr {
        private final TableName tableName;
        private final ColumnName columnName;

        @Override
        public Expression toExpression() {
            String tableName = (this.tableName == null) ? null : this.tableName.getName();
            return new UnResolvedAttribute(tableName, columnName.getName());
        }
    }

    @Data
    class ExprNot implements Expr {
        private final Expr expr;

        @Override
        public Expression toExpression() {
            return new NotExpr(expr.toExpression());
        }
    }

    // TODO: convert to ExprNot when op is NOT
    @Data
    class ExprUnary implements Expr {
        private final UnaryOp op;
        private final Expr expr;

        @Override
        public Expression toExpression() {
            Expression result;
            switch (op.getOp().toUpperCase()) {
                case "NOT":
                    result = new NotExpr(expr.toExpression());
                    break;
                case "+":
                    result = expr.toExpression();
                    break;
                case "-":
                    result = new Negative(expr.toExpression());
                default:
                    throw new IllegalArgumentException("unsupported unary operation: " + op.getOp());
            }
            return result;
        }
    }

    @Data
    class ExprBinaryMath implements Expr {
        private final Expr left;
        private final Expr right;
        private final String op;

        @Override
        public Expression toExpression() {
            Expression result;
            switch (op) {
                case "*":
                    result = new ProductExpr(left.toExpression(), right.toExpression());
                    break;
                case "/":
                    result = new DivExpr(left.toExpression(), right.toExpression());
                    break;
                case "%":
                    result = new ModExpr(left.toExpression(), right.toExpression());
                    break;
                case "-":
                    result = new MinusExpr(left.toExpression(), right.toExpression());
                    break;
                case "+":
                    result = new PlusExpr(left.toExpression(), right.toExpression());
                    break;
                default:
                    throw new IllegalArgumentException("unsupported math opeartion: " + op);
            }
            return result;
        }
    }

    @Data
    class ExprCompare implements Expr {
        private final Expr left;
        private final Expr right;
        private final String op;

        @Override
        public Expression toExpression() {
            CompareOp compareOp;
            switch (op.toUpperCase()) {
                case ">":
                    compareOp = CompareOp.GT;
                    break;
                case ">=":
                    compareOp = CompareOp.GTE;
                    break;
                case "<":
                    compareOp = CompareOp.LT;
                    break;
                case "<=":
                    compareOp = CompareOp.LTE;
                    break;
                case "=":
                case "==":
                    compareOp = CompareOp.EQ;
                    break;
                case "!=":
                case "<>":
                    compareOp = CompareOp.NE;
                    break;
                case "LIKE":
                    compareOp = CompareOp.LIKE;
                    break;
                default:
                    throw new IllegalArgumentException("unsupported compare operator: " + op);
            }
            return new Compare(compareOp, left.toExpression(), right.toExpression());
        }
    }

    @Data
    class ExprAnd implements Expr {
        private final Expr left;
        private final Expr right;

        @Override
        public Expression toExpression() {
            return new And(left.toExpression(), right.toExpression());
        }
    }

    @Data
    class ExprOr implements Expr {
        private final Expr left;
        private final Expr right;

        @Override
        public Expression toExpression() {
            return new Or(left.toExpression(), right.toExpression());
        }
    }

    @Data
    class ExprBetween implements Expr {
        private final Expr expr;
        private final Expr left;
        private final Expr right;

        @Override
        public Expression toExpression() {
            return new Between(expr.toExpression(), left.toExpression(), right.toExpression());
        }
    }

    @Data
    class ExprIn implements Expr {
        private final Expr expr;
        private final List<Expr> exprs;
        private final Select select;

        @Override
        public Expression toExpression() {
            LogicalPlan subQuery = select == null ? null : select.toPlanNode();
            List<Expression> values = exprs == null ? null : exprs.stream().map(Expr::toExpression).collect(Collectors.toList());
            return new In(expr.toExpression(), values, subQuery);
        }
    }

    @Data
    class ExprFunctionCall implements Expr {
        private final FuncName funcName;
        private final List<Expr> args;

        @Override
        public Expression toExpression() {
            List<Expression> arguments = args == null ? Collections.emptyList() : args.stream().map(Expr::toExpression).collect(Collectors.toList());
            return FunctionCall.create(funcName.getName(), arguments);
        }
    }
}
