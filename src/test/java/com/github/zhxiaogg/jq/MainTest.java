package com.github.zhxiaogg.jq;


import com.github.zhxiaogg.jq.analyzer.Analyser;
import com.github.zhxiaogg.jq.analyzer.Batch;
import com.github.zhxiaogg.jq.analyzer.PhysicalPlanner;
import com.github.zhxiaogg.jq.analyzer.rules.CastDataTypesRule;
import com.github.zhxiaogg.jq.analyzer.rules.CleanGroupByAggregatorsRule;
import com.github.zhxiaogg.jq.analyzer.rules.ResolveAttributesRule;
import com.github.zhxiaogg.jq.analyzer.rules.ResolveHavingConditionRule;
import com.github.zhxiaogg.jq.annotations.Field;
import com.github.zhxiaogg.jq.ast.Select;
import com.github.zhxiaogg.jq.parser.Parser;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.physical.PhysicalPlan;
import com.github.zhxiaogg.jq.streaming.StreamingQuery;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
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
                                new CleanGroupByAggregatorsRule(),
                                new ResolveHavingConditionRule(catalog)
                        ))
                );
            }
        };
    }

    @Test
    public void should_work() {
        Relation relation = Relation.create("orders", Order.class);
        Catalog ds = Catalog.create(relation);

        Parser parser = new Parser();
        Select select = parser.parse("select item_id, max(o.price) as value from orders as o where time > '2021-05-31T00:00:00Z' group by o.item_id having sum(price) > 100");
        LogicalPlan plan = select.toPlanNode();
        LogicalPlan analysedPlan = getAnalyser(ds).analysis(plan);

        PhysicalPlanner physicalPlanner = new PhysicalPlanner(ds);
        PhysicalPlan executablePlan = physicalPlanner.plan(analysedPlan);
        StreamingQuery streaming = ds.streamQuery(executablePlan);
        RecordBag r1 = streaming.fire(new Order(1, 100, Instant.now()));
        Assert.assertEquals(RecordBag.empty(), r1);
        RecordBag r2 = streaming.fire(new Order(1, 100, Instant.now()));
        Assert.assertEquals(RecordBag.of(Collections.singletonList(Record.create(Arrays.asList(1, 100.0D)))), r2);
    }

}
