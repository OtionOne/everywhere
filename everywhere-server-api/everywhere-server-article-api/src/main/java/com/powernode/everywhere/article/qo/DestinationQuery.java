package com.powernode.everywhere.article.qo;

import com.powernode.everywhere.common.core.qo.CommonQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationQuery extends CommonQuery {
    private  Long parentId;
}
