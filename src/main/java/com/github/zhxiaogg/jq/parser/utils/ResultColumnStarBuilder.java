package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ResultColumn;

class ResultColumnStarBuilder implements ResultColumnBuilder<ResultColumn.ResultColumnStar> {
    @Override
    public ResultColumn.ResultColumnStar build() {
        return new ResultColumn.ResultColumnStar();
    }
}
