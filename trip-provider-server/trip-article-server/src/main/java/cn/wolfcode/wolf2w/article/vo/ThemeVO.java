package cn.wolfcode.wolf2w.article.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ThemeVO {
    private String  themeName;
    private List<DestVO> dests = new ArrayList<>();
}
