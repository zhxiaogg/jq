package com.github.zhxiaogg.jq.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    Parser parser = new Parser();

    @Test
    public void parse_sql() {
        parser.parse("Select 1 + 2 * 3, id > 0");
    }

    @Test
    public void parse_from_table() {
        parser.parse("Select id, name from table1, table2");
    }

    @Test
    public void parse_from_sub_query() {
        parser.parse("Select u.id, u.name from (select id, name from users) as u");
    }

    @Test
    public void parse_from_join() {
        parser.parse("Select u.id, u.name, f.fid, f.name from users as u join friends as f on u.id = f.id");
    }

    @Test
    public void parse_where() {
        parser.parse("select id, name from users where id = 1");
    }

    @Test
    public void parse_group_by() {
        parser.parse("select id, name from users group by id, name having id > 100");
    }
}
