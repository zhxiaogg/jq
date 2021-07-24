package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.JoinType;
import com.github.zhxiaogg.jq.antlr.SQLListener;
import com.github.zhxiaogg.jq.antlr.SQLParser;
import com.github.zhxiaogg.jq.ast.*;
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

    public Select result() {
        AstBuilder<Select> builder = builders.pop();
        Select result = null;
        if (builder != null) {
            result = builder.build();
        }
        return result;
    }

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
        builders.push(new SelectBuilder());
    }

    @Override
    public void exitSelect_stmt(SQLParser.Select_stmtContext ctx) {
        AstBuilder<Select> builder = builders.pop();
        if (builders.peek() != null) {
            ((AcceptSelect) builders.peek()).accept(builder.build());
        } else {
            builders.push(builder);
        }
    }

    @Override
    public void enterResult_column(SQLParser.Result_columnContext ctx) {
        if (ctx.STAR() != null) {
            builders.push(new ResultColumnStarBuilder());
        } else if (ctx.table_name() != null) {
            SQLParser.Any_nameContext anyName = ctx.table_name().any_name();
            String tableName;
            if (anyName.IDENTIFIER() != null) {
                tableName = anyName.IDENTIFIER().getText();
            } else {
                tableName = anyName.STRING_LITERAL().getText();
            }
            builders.push(new ResultColumnTableStarBuilder(tableName));
        } else {
            String alias = null;
            if (ctx.expr_alias() != null) {
                alias = ctx.expr_alias().getText();
            }
            builders.push(new ResultColumnExprBuilder(alias));
        }
    }

    @Override
    public void exitResult_column(SQLParser.Result_columnContext ctx) {
        AstBuilder<ResultColumn> builder = builders.pop();
        ((AcceptResultColumn) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterFrom_clause(SQLParser.From_clauseContext ctx) {
        if (ctx.join_clause() != null) {
            builders.push(new JoinsBuilder());
        } else if (ctx.table_or_subquery() != null) {
            builders.push(new TableOrSubQueriesBuilder());
        } else {
            throw new IllegalArgumentException("unsupported from table!");
        }
    }

    @Override
    public void exitFrom_clause(SQLParser.From_clauseContext ctx) {
        AstBuilder<FromTable> builder = builders.pop();
        ((AcceptFromTable) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterWhere_clause(SQLParser.Where_clauseContext ctx) {
        builders.push(new WhereBuilder());
    }

    @Override
    public void exitWhere_clause(SQLParser.Where_clauseContext ctx) {
        AstBuilder<Where> builder = builders.pop();
        ((AcceptWhere) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterGroup_by_clause(SQLParser.Group_by_clauseContext ctx) {
        builders.push(new GroupByBuilder());
    }

    @Override
    public void exitGroup_by_clause(SQLParser.Group_by_clauseContext ctx) {
        AstBuilder<GroupBy> builder = builders.pop();
        ((AcceptGroupBy) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterHaving_clause(SQLParser.Having_clauseContext ctx) {
        builders.push(new HavingBuilder());
    }

    @Override
    public void exitHaving_clause(SQLParser.Having_clauseContext ctx) {
        AstBuilder<Having> builder = builders.pop();
        ((AcceptHaving) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterTable_or_subquery(SQLParser.Table_or_subqueryContext ctx) {
        if (ctx.select_stmt() != null) {
            builders.push(new SubQueryBuilder());
        } else if (ctx.table_name() != null) {
            builders.push(new TableBuilder());
        } else {
            throw new IllegalArgumentException("unsupported table or sub query!");
        }
    }

    @Override
    public void exitTable_or_subquery(SQLParser.Table_or_subqueryContext ctx) {
        AstBuilder<TableOrSubQuery> builder = builders.pop();
        ((AcceptTableOrSubQuery) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterJoin_clause(SQLParser.Join_clauseContext ctx) {
        builders.push(new JoinBuilder());
    }

    @Override
    public void exitJoin_clause(SQLParser.Join_clauseContext ctx) {
        AstBuilder<Join> builder = builders.pop();
        ((AcceptJoin) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterJoin_constraint(SQLParser.Join_constraintContext ctx) {
        builders.push(new JoinConstraintBuilder());
    }

    @Override
    public void exitJoin_constraint(SQLParser.Join_constraintContext ctx) {
        AstBuilder<JoinConstraint> builder = builders.pop();
        ((AcceptJoinConstraint) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterJoin_operator(SQLParser.Join_operatorContext ctx) {
        boolean naturalJoin = ctx.NATURAL_() != null;
        if (ctx.LEFT_() != null) {
            builders.push(new JoinOpBuilder(JoinType.LEFT, naturalJoin));
        } else if (ctx.INNER_() != null) {
            builders.push(new JoinOpBuilder(JoinType.INNER, naturalJoin));
        } else if (ctx.CROSS_() != null) {
            builders.push(new JoinOpBuilder(JoinType.CROSS, naturalJoin));
        } else if (ctx.JOIN_() != null) {
            builders.push(new JoinOpBuilder(JoinType.LEFT, naturalJoin));
        } else if (ctx.COMMA() != null) {
            builders.push(new JoinOpBuilder(JoinType.LEFT, naturalJoin));
        }
    }

    @Override
    public void exitJoin_operator(SQLParser.Join_operatorContext ctx) {
        AstBuilder<JoinOp> builder = builders.pop();
        ((AcceptJoinOp) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterExpr(SQLParser.ExprContext ctx) {
        if (ctx.literal_value() != null) {
            builders.push(new ExprLiteralBuilder());
        } else if (!ctx.column_name().isEmpty()) {
            builders.push(new ExprColumnRefBuilder());
        } else if (ctx.unary_operator() != null) {
            builders.push(new ExprUnaryBuilder());
        } else if (ctx.expr() != null) {
            Optional<TerminalNode> op;
            if ((op = ObjectUtils.firstNonNull(ctx.STAR(), ctx.DIV(), ctx.MOD(), ctx.PLUS(), ctx.MINUS())).isPresent()) {
                builders.push(new ExprBinaryMathBuilder(op.get().getText()));
            } else if ((op = ObjectUtils.firstNonNull(ctx.LT(), ctx.LT_EQ(), ctx.GT(), ctx.GT_EQ(),
                    ctx.ASSIGN(), ctx.EQ(), ctx.NOT_EQ1(), ctx.NOT_EQ2(), ctx.LIKE_())).isPresent()) {
                builders.push(new ExprCompareBuilder(op.get().getText()));
            } else if (ctx.AND_() != null) {
                builders.push(new ExprAndBuilder());
            } else if (ctx.OR_() != null) {
                builders.push(new ExprOrBuilder());
            } else if (ctx.BETWEEN_() != null) {
                builders.push(new ExprBetweenBuilder(ctx.NOT_() != null));
            } else if (ctx.IN_() != null) {
                builders.push(new ExprInBuilder(ctx.NOT_() != null));
            } else if (ctx.func() != null) {
                builders.push(new ExprFunctionCallBuilder());
            } else {
                throw new IllegalArgumentException("unsupported expression!");
            }
        }
    }

    @Override
    public void exitExpr(SQLParser.ExprContext ctx) {
        AstBuilder<Expr> exprBuilder = builders.pop();
        ((AcceptExpr) builders.peek()).accept(exprBuilder.build());
    }

    @Override
    public void enterUnary_operator(SQLParser.Unary_operatorContext ctx) {
        if (ctx.MINUS() != null) {
            builders.push(new UnaryOpBuilder(ctx.MINUS().getText()));
        } else if (ctx.PLUS() != null) {
            builders.push(new UnaryOpBuilder(ctx.PLUS().getText()));
        } else if (ctx.NOT_() != null) {
            builders.push(new UnaryOpBuilder(ctx.NOT_().getText()));
        } else {
            throw new IllegalArgumentException("unsupported unary operator.");
        }
    }

    @Override
    public void exitUnary_operator(SQLParser.Unary_operatorContext ctx) {
        AstBuilder<UnaryOp> builder = builders.pop();
        ((AcceptUnaryOp) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterFunc(SQLParser.FuncContext ctx) {
        builders.push(new FuncNameBuilder(ctx.getText()));
    }

    @Override
    public void exitFunc(SQLParser.FuncContext ctx) {
        AstBuilder<FuncName> builder = builders.pop();
        ((AcceptFuncName) builders.peek()).accept(builder.build());
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
            builders.push(new LiteralBuilder(n));
        } else if (ctx.STRING_LITERAL() != null) {
            builders.push(new LiteralBuilder(ctx.STRING_LITERAL().getText()));
        } else if (ctx.NULL_() != null) {
            builders.push(new LiteralBuilder(null));
        } else if (ctx.FALSE_() != null) {
            builders.push(new LiteralBuilder(false));
        } else if (ctx.TRUE_() != null) {
            builders.push(new LiteralBuilder(true));
        } else {
            throw new IllegalArgumentException("unsupported literal value.");
        }
    }

    @Override
    public void exitLiteral_value(SQLParser.Literal_valueContext ctx) {
        AstBuilder<Literal> builder = builders.pop();
        ((AcceptLiteral) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterTable_name(SQLParser.Table_nameContext ctx) {
        builders.push(new TableNameBuilder(ctx.any_name().getText()));
    }

    @Override
    public void exitTable_name(SQLParser.Table_nameContext ctx) {
        AstBuilder<TableName> builder = builders.pop();
        ((AcceptTableName) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterColumn_name(SQLParser.Column_nameContext ctx) {
        builders.push(new ColumnNameBuilder(ctx.any_name().getText()));
    }

    @Override
    public void exitColumn_name(SQLParser.Column_nameContext ctx) {
        AstBuilder<ColumnName> builder = builders.pop();
        ((AcceptColumnName) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterTable_alias(SQLParser.Table_aliasContext ctx) {
        builders.push(new TableAliasBuilder(ctx.getText()));
    }

    @Override
    public void exitTable_alias(SQLParser.Table_aliasContext ctx) {
        AstBuilder<TableAlias> builder = builders.pop();
        ((AcceptTableAlias) builders.peek()).accept(builder.build());
    }

    @Override
    public void enterExpr_alias(SQLParser.Expr_aliasContext ctx) {

    }

    @Override
    public void exitExpr_alias(SQLParser.Expr_aliasContext ctx) {

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
