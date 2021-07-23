package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exprs.literals.LiteralImpl;
import com.github.zhxiaogg.jq.plan.exprs.math.Div;
import com.github.zhxiaogg.jq.plan.exprs.math.Plus;
import com.github.zhxiaogg.jq.datatypes.DataType;
import org.junit.Assert;
import org.junit.Test;

public class BinaryExpressionTest {

    @Test
    public void div_equals_div() {
        boolean result = new Div(new LiteralImpl(0, DataType.Int), new LiteralImpl(1, DataType.Int))
                .semanticEqual(new Div(new LiteralImpl(0, DataType.Int), new LiteralImpl(1, DataType.Int)));
        Assert.assertTrue(result);
    }

    @Test
    public void div_not_equals_plus() {
        boolean result = new Div(new LiteralImpl(0, DataType.Int), new LiteralImpl(1, DataType.Int))
                .semanticEqual(new Plus(new LiteralImpl(0, DataType.Int), new LiteralImpl(1, DataType.Int)));
        Assert.assertFalse(result);
    }
}
