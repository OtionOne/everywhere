package cn.wolfcode.wolf2w.common.redis.test;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MyDate2 {
    public static MyDate2 DATE1 = new MyDate2("DATE1", 1L);
    public static MyDate2 DATE2 = new MyDate2("DATE2", 2L);
    @Setter
    private String prefix;
    @Setter
    private Long time;

    private MyDate2(String prefix, Long time){
        this.prefix = prefix;
        this.time = time;
    }

    public String join(String value){
        return value;
    }
}
