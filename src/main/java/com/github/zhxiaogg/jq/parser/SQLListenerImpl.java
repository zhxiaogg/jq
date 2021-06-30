package com.github.zhxiaogg.jq.parser;

import com.github.zhxiaogg.jq.antlr.SQLListener;
import com.github.zhxiaogg.jq.antlr.SQLParser;
import com.github.zhxiaogg.jq.parser.ast.*;
import com.github.zhxiaogg.jq.utils.ObjectUtils;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Optional;

public class SQLListenerImpl implements SQLListener {
    private static final Logger log = LoggerFactory.getLogger(SQLListenerImpl.class);

    private LinkedList<AstBuilder> builders = new LinkedList<>();

    @Override
    public void enterSql(SQLParser.SqlContext ctx) {
        log.debug("begin parse sql");
    }

    @Override
    public void exitSql(SQLParser.SqlContext ctx) {
        log.debug("done parse sql");
    }

    @Override
    public void enterSelect_stmt(SQLParser.Select_stmtContext ctx) {
        builders.push(new Select.SelectBuilder());
    }

    @Override
    public void exitSelect_stmt(SQLParser.Select_stmtContext ctx) {
        AstBuilder<Select> builder = (AstBuilder<Select>) builders.pop();
        if (builders.peek() != null) {
            ((Select.AcceptSelect) builders.peek()).accept(builder.build());
        } else {
            Select select = builder.build();
            System.out.println("select = " + select);
        }
    }

    @Override
    public void enterResult_column(SQLParser.Result_columnContext ctx) {
        if (ctx.STAR() != null) {
            builders.push(new ResultColumn.ResultColumnStarBuilder());
        } else if (ctx.table_name() != null) {
            SQLParser.Any_nameContext anyName = ctx.table_name().any_name();
            String tableName;
            if (anyName.IDENTIFIER() != null) {
                tableName = anyName.IDENTIFIER().getText();
            } else {
                tableName = anyName.STRING_LITERAL().getText();
            }
            builders.push(new ResultColumn.ResultColumnTableStarBuilder(tableName));
        } else {
            builders.push(new ResultColumn.ResultColumnExprBuilder());
        }
    }

    @Override
    public void exitResult_column(SQLParser.Result_columnContext ctx) {
        ResultColumn.ResultColumnBuilder builder = (ResultColumn.ResultColumnBuilder) builders.pop();
        ((ResultColumn.AcceptResultColumn) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterFrom_clause(SQLParser.From_clauseContext ctx) {
        if (ctx.join_clause() != null) {
            builders.push(new FromTable.JoinsBuilder());
        } else if (ctx.table_or_subquery() != null) {
            builders.push(new FromTable.TableOrSubQueriesBuilder());
        } else {
            throw new IllegalArgumentException("unsupported from table!");
        }
    }

    @Override
    public void exitFrom_clause(SQLParser.From_clauseContext ctx) {
        AstBuilder<FromTable> builder = builders.pop();
        ((FromTable.AcceptFromTable) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterWhere_clause(SQLParser.Where_clauseContext ctx) {

    }

    @Override
    public void exitWhere_clause(SQLParser.Where_clauseContext ctx) {

    }

    @Override
    public void enterGroup_by_clause(SQLParser.Group_by_clauseContext ctx) {

    }

    @Override
    public void exitGroup_by_clause(SQLParser.Group_by_clauseContext ctx) {

    }

    @Override
    public void enterTable_or_subquery(SQLParser.Table_or_subqueryContext ctx) {
        if (ctx.select_stmt() != null) {
            builders.push(new TableOrSubQuery.SubQueryBuilder());
        } else if (ctx.table_name() != null) {
            builders.push(new TableOrSubQuery.TableBuilder());
        } else {
            throw new IllegalArgumentException("unsupported table or sub query!");
        }
    }

    @Override
    public void exitTable_or_subquery(SQLParser.Table_or_subqueryContext ctx) {
        AstBuilder<TableOrSubQuery> builder = builders.pop();
        ((TableOrSubQuery.AcceptTableOrSubQuery) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterJoin_clause(SQLParser.Join_clauseContext ctx) {
        builders.push(new Join.JoinBuilder());
    }

    @Override
    public void exitJoin_clause(SQLParser.Join_clauseContext ctx) {
        AstBuilder<Join> builder = builders.pop();
        ((Join.AcceptJoin) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterJoin_constraint(SQLParser.Join_constraintContext ctx) {
        builders.push(new JoinConstraint.JoinConstraintBuilder());
    }

    @Override
    public void exitJoin_constraint(SQLParser.Join_constraintContext ctx) {
        AstBuilder<JoinConstraint> builder = builders.pop();
        ((JoinConstraint.AcceptJoinConstraint) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterJoin_operator(SQLParser.Join_operatorContext ctx) {
        boolean naturalJoin = ctx.NATURAL_() != null;
        if (ctx.LEFT_() != null && ctx.OUTER_() != null) {
            builders.push(new JoinOp(JoinOp.JoinType.LEFT_OUTER, naturalJoin));
        } else if (ctx.LEFT_() != null) {
            builders.push(new JoinOp(JoinOp.JoinType.LEFT, naturalJoin));
        } else if (ctx.INNER_() != null) {
            builders.push(new JoinOp(JoinOp.JoinType.INNER, naturalJoin));
        } else if (ctx.CROSS_() != null) {
            builders.push(new JoinOp(JoinOp.JoinType.CROSS, naturalJoin));
        } else if (ctx.JOIN_() != null) {
            builders.push(new JoinOp(JoinOp.JoinType.LEFT, naturalJoin));
        } else if (ctx.COMMA() != null) {
            builders.push(new JoinOp(JoinOp.JoinType.LEFT, naturalJoin));
        }
    }

    @Override
    public void exitJoin_operator(SQLParser.Join_operatorContext ctx) {
        AstBuilder<JoinOp> builder = builders.pop();
        ((JoinOp.AcceptJoinOp) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterExpr(SQLParser.ExprContext ctx) {
        if (ctx.literal_value() != null) {
            builders.push(new Expr.ExprLiteralBuilder());
        } else if (ctx.table_name() != null || ctx.column_name() != null) {
            builders.push(new Expr.ExprColumnRefBuilder());
        } else if (ctx.unary_operator() != null) {
            builders.push(new Expr.ExprUnaryBuilder());
        } else if (ctx.expr() != null) {
            Optional<TerminalNode> op;
            if ((op = ObjectUtils.firstNonNull(ctx.STAR(), ctx.DIV(), ctx.MOD(), ctx.PLUS(), ctx.MINUS())).isPresent()) {
                builders.push(new Expr.ExprBinaryMathBuilder(op.get().getText()));
            } else if ((op = ObjectUtils.firstNonNull(ctx.LT(), ctx.LT_EQ(), ctx.GT(), ctx.GT_EQ(),
                    ctx.ASSIGN(), ctx.EQ(), ctx.NOT_EQ1(), ctx.NOT_EQ2())).isPresent()) {
                builders.push(new Expr.ExprCompareBuilder(op.get().getText()));
            } else if (ctx.AND_() != null) {
                builders.push(new Expr.ExprAndBuilder());
            } else if (ctx.OR_() != null) {
                builders.push(new Expr.ExprOrBuilder());
            } else if (ctx.BETWEEN_() != null) {
                builders.push(new Expr.ExprBetweenBuilder(ctx.NOT_() != null));
            } else if (ctx.IN_() != null) {
                builders.push(new Expr.ExprInBuilder(ctx.NOT_() != null));
            } else {
                throw new IllegalArgumentException("unsupported expression!");
            }
        }
    }

    @Override
    public void exitExpr(SQLParser.ExprContext ctx) {
        AstBuilder<Expr> exprBuilder = (AstBuilder<Expr>) builders.pop();
        ((Expr.AcceptExpr) builders.peek()).accept(exprBuilder.build());
    }

    @Override
    public void enterUnary_operator(SQLParser.Unary_operatorContext ctx) {
        if (ctx.MINUS() != null) {
            builders.push(new UnaryOp(ctx.MINUS().getText()));
        } else if (ctx.PLUS() != null) {
            builders.push(new UnaryOp(ctx.PLUS().getText()));
        } else if (ctx.NOT_() != null) {
            builders.push(new UnaryOp(ctx.NOT_().getText()));
        } else {
            throw new IllegalArgumentException("unsupported unary operator.");
        }
    }

    @Override
    public void exitUnary_operator(SQLParser.Unary_operatorContext ctx) {
        AstBuilder<UnaryOp> builder = (AstBuilder<UnaryOp>) builders.pop();
        ((UnaryOp.AcceptUnaryOp) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterLiteral_value(SQLParser.Literal_valueContext ctx) {
        if (ctx.NUMERIC_LITERAL() != null) {
            String text = ctx.NUMERIC_LITERAL().getText();
            Number n;
            try {
                n = NumberFormat.getInstance().parse(text);
            } catch (ParseException e) {
                throw new IllegalArgumentException("cannot parse number from " + text);
            }
            builders.push(new Literal(n));
        } else if (ctx.STRING_LITERAL() != null) {
            builders.push(new Literal(ctx.STRING_LITERAL().getText()));
        } else if (ctx.NULL_() != null) {
            builders.push(new Literal(null));
        } else if (ctx.FALSE_() != null) {
            builders.push(new Literal(false));
        } else if (ctx.TRUE_() != null) {
            builders.push(new Literal(true));
        } else {
            throw new IllegalArgumentException("unsupported literal value.");
        }
    }

    @Override
    public void exitLiteral_value(SQLParser.Literal_valueContext ctx) {
        AstBuilder<Literal> builder = (AstBuilder<Literal>) builders.pop();
        ((Literal.AcceptLiteral) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterTable_name(SQLParser.Table_nameContext ctx) {
        builders.push(new TableName(ctx.any_name().getText()));
    }

    @Override
    public void exitTable_name(SQLParser.Table_nameContext ctx) {
        AstBuilder<TableName> builder = (AstBuilder<TableName>) builders.pop();
        ((TableName.AcceptTableName) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterColumn_name(SQLParser.Column_nameContext ctx) {
        builders.push(new ColumnName(ctx.any_name().getText()));
    }

    @Override
    public void exitColumn_name(SQLParser.Column_nameContext ctx) {
        AstBuilder<ColumnName> builder = (AstBuilder<ColumnName>) builders.pop();
        ((ColumnName.AcceptColumnName) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterTable_alias(SQLParser.Table_aliasContext ctx) {
        builders.push(new TableAlias(ctx.getText()));
    }

    @Override
    public void exitTable_alias(SQLParser.Table_aliasContext ctx) {
        AstBuilder<TableAlias> builder = builders.pop();
        ((TableAlias.AcceptTableAlias) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterAny_name(SQLParser.Any_nameContext ctx) {

    }

    @Override
    public void exitAny_name(SQLParser.Any_nameContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
