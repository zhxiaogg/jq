package com.github.zhxiaogg.jq.plan.physical;

import com.github.zhxiaogg.jq.plan.exec.*;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggExpression;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AggregateFunction;
import com.github.zhxiaogg.jq.utils.ListUtils;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class AggregateExec implements PhysicalPlan {
    private final List<Expression> groupings;
    private final List<AggExpression> aggExpressions;
    private final List<Expression> aggregators;
    private final PhysicalPlan child;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public PhysicalPlan withChildren(List<PhysicalPlan> children) {
        return new AggregateExec(groupings, aggExpressions, aggregators, children.get(0));
    }

    @Override
    public RecordBag exec() {
        Map<Record, MutableRecord> groups = new HashMap<>();
        Projection groupingProjection = Projection.create(groupings, child.outputs());
        List<AggregateFunction> aggFunctions = aggExpressions.stream().map(AggExpression::getAggregateFunction).collect(Collectors.toList());
        AttributeSet attributeSet = AttributeSet.create(
                aggFunctions.stream().flatMap(f -> f.updateOutputs().allAttributes().stream()).collect(Collectors.toList()),
                child.outputs().allAttributes()
        );
        MutableProjection aggExprsProjection = Projection.createMutable((List) aggFunctions.stream().flatMap(f -> f.updateExpressions().stream()).collect(Collectors.toList()), attributeSet);

        List<Expression> initExpressions = aggFunctions.stream().flatMap(f -> f.initExpressions().stream()).collect(Collectors.toList());
        Projection initProjection = Projection.create(initExpressions, child.outputs());

        for (Record record : child.exec().getRecords()) {
            Record key = groupingProjection.apply(record);
            MutableRecord values = groups.computeIfAbsent(key, k -> initProjection.apply(record).toMutable());
            aggExprsProjection.apply(values, new JoinRecord(values, record));
        }

        List<Expression> resultExpressions = new ArrayList<>(groupings);
        resultExpressions.addAll(aggregators);
        AttributeSet attributeSet1 = AttributeSet.create(
                groupings.stream().map(Expression::toAttribute).collect(Collectors.toList()),
                aggExpressions.stream().map(Expression::toAttribute).collect(Collectors.toList())
        );
        Projection resultProjection = Projection.create(resultExpressions, attributeSet1);
        List<Record> results = groups.entrySet().stream().map(entry -> resultProjection.apply(new JoinRecord(entry.getKey(), entry.getValue()))).collect(Collectors.toList());
        return new RecordBag(results);
    }

    @Override
    public AttributeSet outputs() {
        ResolvedAttribute[] attributes = ListUtils.concat(groupings, aggregators).stream().map(Expression::toAttribute).toArray(ResolvedAttribute[]::new);
        return AttributeSet.create(attributes);
    }
}
