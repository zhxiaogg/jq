package com.github.zhxiaogg.jq.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parse_sql() {
        Parser parser = new Parser();
        parser.parse("Select 1 + 2, id > 0");
    }
}
