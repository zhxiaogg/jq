package com.github.zhxiaogg.jq.parser;

import com.github.zhxiaogg.jq.antlr.SQLLexer;
import com.github.zhxiaogg.jq.antlr.SQLParser;
import com.github.zhxiaogg.jq.parser.utils.SQLListenerImpl;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Parser {
    public void parse(String sql) {
        SQLListenerImpl listener = new SQLListenerImpl();
        CodePointCharStream input = CharStreams.fromString(sql);
        SQLLexer lexer = new SQLLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser(tokenStream);

        SQLParser.SqlContext context = parser.sql();
        ParseTreeWalker.DEFAULT.walk(listener, context);
    }
}
