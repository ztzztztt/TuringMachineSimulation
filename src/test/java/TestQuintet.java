import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import model.Quintet;

import java.util.ArrayList;

/**
 * @author by zhoutao
 * @description 测试
 * @date 2020/12/10 8:56
 */
public class TestQuintet {

    public static void main(String[] args) {
        Quintet quintet = new Quintet("q1", "1", "q2", "0", "L");

        String str = Quintet.toString(quintet);
        System.out.println(str);

        Quintet quintet1 = Quintet.fromString(str);
        System.out.println(quintet1);
        System.out.println(Quintet.toString(quintet1));
    }
}
