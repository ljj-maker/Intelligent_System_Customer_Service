package com.kefu.icsscommon.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {

    private Long total;
    private List<T> rows;
}
