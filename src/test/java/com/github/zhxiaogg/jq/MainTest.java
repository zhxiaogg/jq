package com.github.zhxiaogg.jq;


import com.github.zhxiaogg.jq.annotations.Field;
import com.github.zhxiaogg.jq.exprs.Expressions;
import com.github.zhxiaogg.jq.plans.*;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;
import com.github.zhxiaogg.jq.stream.Streaming;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;

public class MainTest {

    private static class Order {
        @Field(name = "item_id")
        private final int itemId;
        @Field()
        private final double price;
        @Field()
        private final Instant time;

        private Order(int itemId, double price, Instant time) {
            this.itemId = itemId;
            this.price = price;
            this.time = time;
        }

        public int getItemId() {
            return itemId;
        }


        public double getPrice() {
            return price;
        }


        public Instant getTime() {
            return time;
        }
    }

    @Test
    public void should_work() {
        Relation relation = Relation.create("orders", Order.class);
        DataSource ds = DataSource.create(relation);

        // select item_id, sum(price) as value from orders where time > "1h" having sum(price) >= 100 limit 10;
        Streaming streaming = ds.streamQuery(createPlan());
        RecordBag r1 = streaming.fire(new Order(1, 100, Instant.now()));
        System.out.println(r1);
    }

    // select item_id, sum(price) as value from orders where time > "1h" having sum(price) > 100 limit 10;
    private LogicalPlan createPlan() {
        Scan scan = Scan.from("orders");
        Filter filter = Filter.create(Expressions.gt("time", Instant.parse("2021-05-31T00:00:00Z")), scan);
        Aggregate aggregate = Aggregate.create(Arrays.asList("item_id"), Arrays.asList(Expressions.alias(Expressions.sum("price"), "value")), filter);
        Having having = new Having(Expressions.gte(Expressions.sum("price"), 100), aggregate);
        return new Limit(10, having);
    }

}
