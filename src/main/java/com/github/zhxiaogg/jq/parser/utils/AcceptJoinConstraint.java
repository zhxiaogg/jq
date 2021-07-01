package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.JoinConstraint;

interface AcceptJoinConstraint {
    void accept(JoinConstraint constraint);
}
