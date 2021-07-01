package com.github.zhxiaogg.jq.parser;

import com.github.zhxiaogg.jq.antlr.SQLLexer;
import com.github.zhxiaogg.jq.antlr.SQLParser;
import com.github.zhxiaogg.jq.ast.Select;
import com.github.zhxiaogg.jq.parser.utils.SQLListenerImpl;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Parser {
    public Select parse(String sql) {
        CodePointCharStream input = CharStreams.fromString(sql);
        SQLLexer lexer = new SQLLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser(tokenStream);

        SQLParser.SqlContext context = parser.sql();

        SQLListenerImpl listener = new SQLListenerImpl();
        ParseTreeWalker.DEFAULT.walk(listener, context);
        return listener.result();
    }
}
