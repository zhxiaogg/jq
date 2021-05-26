package com.github.zhxiaogg.jq.nodes.exprs;

import java.util.Collections;
import java.util.List;

public interface LeafExprNode extends Expression {

    default boolean leafNode() {
        return true;
    }

    default List<Expression> getChildren() {
        return Collections.emptyList();
    }

    default Expression withChildren(List<Expression> children) {
        throw new IllegalStateException("");
    }

}
