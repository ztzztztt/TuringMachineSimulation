import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import model.RuleSet;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author by zhoutao
 * @description 测试
 * @date 2020/12/10 9:31
 */
public class TestRuleSet {

    public static void main(String[] args) {
        RuleSet ruleSet = RuleSet.getInstance();

        ruleSet.addRule("['q1', '0', 'q3', '1', 'R']");
        ruleSet.addRule("['q1', '0', 'q3', '0', 'L']");

        String str = RuleSet.toString(ruleSet);
        System.out.println(str);

        RuleSet set = RuleSet.fromString(str);
        System.out.println(set);
    }
}
