package com.github.zhxiaogg.jq;


import com.github.zhxiaogg.jq.analyzer.*;
import com.github.zhxiaogg.jq.analyzer.rules.CastDataTypesRule;
import com.github.zhxiaogg.jq.analyzer.rules.CleanGroupByAggregatorsRule;
import com.github.zhxiaogg.jq.analyzer.rules.ResolveAttributesRule;
import com.github.zhxiaogg.jq.analyzer.rules.ResolveHavingConditionRule;
import com.github.zhxiaogg.jq.annotations.Field;
import com.github.zhxiaogg.jq.ast.Select;
import com.github.zhxiaogg.jq.parser.Parser;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.streaming.StreamingQuery;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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

    Analyser getAnalyser(Catalog catalog) {
        return new Analyser() {
            @Override
            public List<Batch> getBatches() {
                return Arrays.asList(
                        new Batch(Arrays.asList(
                                new ResolveAttributesRule(catalog),
                                new CastDataTypesRule(),
                                new ResolveHavingConditionRule(catalog),
                                new CleanGroupByAggregatorsRule()
                        ))
                );
            }
        };
    }

    @Test
    public void should_work() {
        Relation relation = Relation.create("orders", Order.class);
        Catalog ds = Catalog.create(relation);

        // select item_id, sum(price) as value from orders where time > "1h" having sum(price) >= 100 limit 10;
        LogicalPlan plan = createPlan();
        LogicalPlan analysedPlan = getAnalyser(ds).analysis(plan);
        System.out.println(analysedPlan);
        StreamingQuery streaming = ds.streamQuery(analysedPlan);
        RecordBag r1 = streaming.fire(new Order(1, 100, Instant.now()));
        System.out.println(r1);
        RecordBag r2 = streaming.fire(new Order(1, 100, Instant.now()));
        System.out.println(r2);
    }

    // select item_id, sum(price) as value from orders where time > "1h" having sum(price) > 100 limit 10;
    private LogicalPlan createPlan() {
        Parser parser = new Parser();
        Select select = parser.parse("select item_id, max(price) as value from orders where time > '2021-05-31T00:00:00Z' group by item_id having sum(price) > 100");
        LogicalPlan plan = select.toPlanNode();

        return plan;
//        Scan scan = Scan.from("orders");
//        Filter filter = Filter.create(Expressions.gt("time", Instant.parse("2021-05-31T00:00:00Z")), scan);
//        Aggregate aggregate = Aggregate.create(Arrays.asList("item_id"), Arrays.asList(Expressions.alias(Expressions.max("price"), "value")), filter);
//        return new Filter(Expressions.gt(Expressions.sum("price"), 100), aggregate);
    }

}
