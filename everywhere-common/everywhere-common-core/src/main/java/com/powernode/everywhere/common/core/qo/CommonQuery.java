package com.powernode.everywhere.common.core.qo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonQuery {
    private String keyword;
    private Integer size=10;
    private Integer current=1;
}
