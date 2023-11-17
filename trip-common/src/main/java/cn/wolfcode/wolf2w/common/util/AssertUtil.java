package cn.wolfcode.wolf2w.common.util;

import cn.wolfcode.wolf2w.common.exception.LogicException;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

//参数断言工具类
public class AssertUtil {
    private AssertUtil(){}

    /**
     * 判断  text 是否为null 或者 空串
     * @param text
     * @param message
     */
    public static void hasText(@Nullable String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new LogicException(message);
        }
    }

    public static void isEqauls(String v1, String v2, String msg) {
        if(v1== null || v2 == null){
            throw new RuntimeException("参数不能为null");
        }
        if(!v1.equals(v2)){
            throw new LogicException(msg);
        }
    }
}
