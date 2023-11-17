package cn.wolfcode.wolf2w.common.redis.test;

import lombok.Getter;
import lombok.Setter;

/**
 * 枚举类最大特点
 *   1>枚举类构造器是私有
 *   2>枚举类定义完成之后，实例个数是固定
 *   3>其他操作跟普通类一样
 */
@Getter
public enum MyDate {

    DATE1("DATE1", 1L),
    DATE2("DATE2", 2L);

    @Setter
    private String prefix;
    @Setter
    private Long time;

    private MyDate(String prefix, Long time){
        this.prefix = prefix;
        this.time = time;
    }

    public String join(String value){
        return value;
    }

}
