package com.github.zhxiaogg.jq.parser;

import com.github.zhxiaogg.jq.ast.Select;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    Parser parser = new Parser();

    @Test
    public void parse_sql() {
        Select select = parser.parse("Select 1 + 2 * 3, id > 0");
        System.out.println(select);
    }

    @Test
    public void parse_from_table() {
        Select select = parser.parse("Select id, name from table1, table2");
        System.out.println(select);
    }

    @Test
    public void parse_from_sub_query() {
        Select select = parser.parse("Select u.id, u.name from (select id, name from users) as u");
        System.out.println(select);
    }

    @Test
    public void parse_from_join() {
        Select select = parser.parse("Select u.id, u.name, f.fid, f.name from users as u join friends as f on u.id = f.id");
        System.out.println(select);
    }

    @Test
    public void parse_where() {
        Select select = parser.parse("select id, name from users where id = 1");
        System.out.println(select);
    }

    @Test
    public void parse_group_by() {
        Select select = parser.parse("select id, name from users group by id, name having id > 100");
        System.out.println(select);
    }

    @Test
    public void parse_function_call() {
        Select select = parser.parse("select id, sum(score) from users group by id having max(score) > 100");
        System.out.println(select.toPlanNode());
    }
}
