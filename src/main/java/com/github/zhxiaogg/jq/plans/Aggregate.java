package com.github.zhxiaogg.jq.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.values.Aggregator;
import com.github.zhxiaogg.jq.exprs.Expression;
import com.github.zhxiaogg.jq.exprs.UnResolvedAttribute;
import com.github.zhxiaogg.jq.plans.interpreter.Record;
import com.github.zhxiaogg.jq.plans.interpreter.RecordBag;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ToString
@EqualsAndHashCode
public class Aggregate implements LogicalPlan {
    private final List<Expression> groupingKeys;
    private final List<Expression> aggregators;
    private final LogicalPlan child;

    private Aggregate(List<Expression> groupingKeys, List<Expression> aggregators, LogicalPlan child) {
        this.groupingKeys = groupingKeys;
        this.aggregators = aggregators;
        this.child = child;
    }

    public static Aggregate create(List<String> groupingKeys, List<Expression> aggregators, LogicalPlan child) {
        return new Aggregate(groupingKeys.stream().map(UnResolvedAttribute::new).collect(Collectors.toList()), aggregators, child);
    }

    private final Map<List<LiteralValue>, List<Aggregator>> states = new HashMap<>();

    @Override
    public RecordBag partialEval(DataSource dataSource) {
        RecordBag recordBag = child.partialEval(dataSource);
        List<Record> rs = new ArrayList<>();
        for (Record record : recordBag.getRecords()) {
            // assembly a record based on  groupingKeys and aggregators
            List<LiteralValue> keys = groupingKeys.stream().map(e -> (LiteralValue) e.eval(record)).collect(Collectors.toList());
            List<Aggregator> aggregated = aggregators.stream().map(e -> (Aggregator) e.eval(record)).collect(Collectors.toList());
            List<Aggregator> existing = states.get(keys);
            final Record r;
            if (existing == null) {
                states.put(keys, aggregated);
                r = Record.create((List<Value>) ((List) aggregated));
            } else {
                List<Aggregator> updated = new ArrayList<>(existing.size());
                for (int i = 0; i < existing.size(); i++) {
                    Aggregator merged = existing.get(i).merge(aggregated.get(i));
                    updated.set(i, merged);
                }
                states.put(keys, updated);
                r = Record.create((List<Value>) ((List) updated));
            }
            // only emit those updated records
            rs.add(r);
        }
        return RecordBag.of(rs);
    }
}
