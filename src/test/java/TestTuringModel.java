import model.TuringModel;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author by zhoutao
 * @description 测试
 * @date 2020/12/10 16:24
 */
public class TestTuringModel {

    public static void main(String[] args) {
        TuringModel turingModel = TuringModel.getInstance();
        System.out.println(turingModel);

        turingModel.setStartState("q2");

        String str = turingModel.toString();
        System.out.println(str);

        turingModel = TuringModel.fromString(str);
        System.out.println(turingModel);
    }
}
