package com.github.zhxiaogg.jq;

import com.github.zhxiaogg.jq.analyzer.Analyser;
import com.github.zhxiaogg.jq.analyzer.Batch;
import com.github.zhxiaogg.jq.analyzer.PhysicalPlanner;
import com.github.zhxiaogg.jq.analyzer.rules.*;
import com.github.zhxiaogg.jq.ast.Select;
import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.catalog.Relation;
import com.github.zhxiaogg.jq.catalog.UnStructuralRelation;
import com.github.zhxiaogg.jq.parser.Parser;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exec.RecordBag;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.physical.PhysicalPlan;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UnStructuralTest {
    @Test
    public void support_simple_selects() {
        Parser parser = new Parser();
        Select select = parser.parse("select id, name from events where value > 1");
        LogicalPlan plan = select.toPlanNode();

        UnStructuralRelation relation = Relation.createUnStructural(new String[]{"events"});
        List<Relation> relations = Collections.singletonList(relation);
        Catalog catalog = new Catalog(relations);

        Analyser analyser = getAnalyser(catalog);
        LogicalPlan analysedPlan = analyser.analysis(plan);
        PhysicalPlanner physicalPlanner = new PhysicalPlanner(catalog);
        PhysicalPlan executablePlan = physicalPlanner.plan(analysedPlan);

        HashMap<Object, Object> record1 = new HashMap<>();
        record1.put("id", 1L);
        record1.put("name", "x");
        record1.put("value", 10);
        record1.put("value2", 10);
        relation.insert(record1);
        RecordBag result = executablePlan.exec();

        Assert.assertEquals(RecordBag.of(Arrays.asList(Record.create(Arrays.asList(1L, "x")))), result);
    }

    @Test
    public void support_aggregate_selects() {
        Parser parser = new Parser();
        Select select = parser.parse("select id, sum(value) from events group by id having max(value) > 1");
        LogicalPlan plan = select.toPlanNode();

        UnStructuralRelation relation = Relation.createUnStructural(new String[]{"events"});
        List<Relation> relations = Collections.singletonList(relation);
        Catalog catalog = new Catalog(relations);

        Analyser analyser = getAnalyser(catalog);
        LogicalPlan analysedPlan = analyser.analysis(plan);
        PhysicalPlanner physicalPlanner = new PhysicalPlanner(catalog);
        PhysicalPlan executablePlan = physicalPlanner.plan(analysedPlan);

        HashMap<Object, Object> record1 = new HashMap<>();
        record1.put("id", 1L);
        record1.put("name", "x");
        record1.put("value", 10);
        record1.put("value2", 10);
        relation.insert(record1);
        relation.insert(record1);
        RecordBag result = executablePlan.exec();

        Assert.assertEquals(RecordBag.of(Arrays.asList(Record.create(Arrays.asList(1L, 20.0D)))), result);
    }

    @Test
    public void support_aggregate_and_nested_fields() {
        Parser parser = new Parser();
        Select select = parser.parse("select id, sum(value), max(user.age) from events group by id having max(value) > 1");
        LogicalPlan plan = select.toPlanNode();

        UnStructuralRelation relation = Relation.createUnStructural(new String[]{"events"});
        List<Relation> relations = Collections.singletonList(relation);
        Catalog catalog = new Catalog(relations);

        Analyser analyser = getAnalyser(catalog);
        LogicalPlan analysedPlan = analyser.analysis(plan);
        PhysicalPlanner physicalPlanner = new PhysicalPlanner(catalog);
        PhysicalPlan executablePlan = physicalPlanner.plan(analysedPlan);

        HashMap<String, Object> user = new HashMap<>();
        user.put("age", 18);
        HashMap<Object, Object> record1 = new HashMap<>();
        record1.put("id", 1L);
        record1.put("name", "x");
        record1.put("value", 10);
        record1.put("value2", 10);
        record1.put("user", user);
        relation.insert(record1);
        relation.insert(record1);
        RecordBag result = executablePlan.exec();

        Assert.assertEquals(RecordBag.of(Arrays.asList(Record.create(Arrays.asList(1L, 20.0D, 18.0D)))), result);
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
                        )),
                        new Batch(Arrays.asList(new UpdateUnStructuralRelationRule(catalog))),
                        new Batch(Arrays.asList(
                                new ResolveAttributesRule(catalog),
                                new CastDataTypesRule()
                        ))
                );
            }
        };
    }
}
