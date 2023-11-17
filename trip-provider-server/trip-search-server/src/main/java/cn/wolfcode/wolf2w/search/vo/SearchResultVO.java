package cn.wolfcode.wolf2w.search.vo;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.member.domain.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SearchResultVO implements Serializable{


    private Long total = 0L;

    private List<Strategy> strategys = new ArrayList<>();
    private List<Travel> travels = new ArrayList<>();
    private List<UserInfo> users = new ArrayList<>();
    private List<Destination> dests = new ArrayList<>();

}
