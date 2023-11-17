package cn.wolfcode.wolf2w.article.vo;

import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CatalogVO {
    private String destName;
    private List<StrategyCatalog> catalogList;
}
