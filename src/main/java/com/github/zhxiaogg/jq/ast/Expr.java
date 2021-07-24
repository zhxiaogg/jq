package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exprs.*;
import com.github.zhxiaogg.jq.plan.exprs.booleans.*;
import com.github.zhxiaogg.jq.plan.exprs.literals.BooleanLiteral;
import com.github.zhxiaogg.jq.plan.exprs.literals.LiteralImpl;
import com.github.zhxiaogg.jq.plan.exprs.math.*;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
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
            Object value = literal.getValue();
            if (value instanceof Boolean) {
                return new BooleanLiteral((Boolean) value);
            } else if (value instanceof String) {
                // stripping "\'" from the beginning and end of the string.
                String str = literal.getValue().toString();
                String stripped = str.substring(1, str.length() - 1);
                return new LiteralImpl(stripped, DataType.String);
            } else if (value instanceof Integer || value instanceof Long) {
                return new LiteralImpl(literal.getValue(), DataType.Int);
            } else if (value instanceof Float || value instanceof Double) {
                return new LiteralImpl(literal.getValue(), DataType.Float);
            } else {
                return new LiteralImpl(literal.getValue(), DataType.UnKnown);
            }
        }
    }

    @Data
    class ExprColumnRef implements Expr {
        private final List<ColumnName> columnNames;

        @Override
        public Expression toExpression() {
            String[] names = columnNames.stream().map(ColumnName::getName).toArray(String[]::new);
            return new UnResolvedAttribute(names);
        }
    }

    @Data
    class ExprNot implements Expr {
        private final Expr expr;

        @Override
        public Expression toExpression() {
            return new Not(expr.toExpression());
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
                    result = new Not(expr.toExpression());
                    break;
                case "+":
                    result = expr.toExpression();
                    break;
                case "-":
                    result = new Negative(expr.toExpression());
                    break;
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
                    result = new Product(left.toExpression(), right.toExpression());
                    break;
                case "/":
                    result = new Div(left.toExpression(), right.toExpression());
                    break;
                case "%":
                    result = new Mod(left.toExpression(), right.toExpression());
                    break;
                case "-":
                    result = new Minus(left.toExpression(), right.toExpression());
                    break;
                case "+":
                    result = new Plus(left.toExpression(), right.toExpression());
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

    @Data
    class ExprAlias implements Expr {
        private final Expr expr;
        private final String alias;

        @Override
        public Expression toExpression() {
            return new Alias(expr.toExpression(), alias);
        }
    }
}
