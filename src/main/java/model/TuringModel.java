package model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;


/**
 * @author by zhoutao
 * @description 图灵机实体类
 * @date 2020/12/10 15:44
 */
public class TuringModel {
    private static TuringModel turingModel;

    public static TuringModel getInstance(){
        if (TuringModel.turingModel == null){
            turingModel = new TuringModel();
        }
        return turingModel;
    }


    private final RuleSet ruleSet;
    private final SimpleIntegerProperty ruleListPositionProperty;
    private final SimpleIntegerProperty positionProperty;
    private final SimpleStringProperty currentStateProperty;
    private final SimpleStringProperty endStateProperty;

    private final SimpleSetProperty<String> stateSetProperty;
    private final SimpleSetProperty<String> charSetProperty;
    private final SimpleListProperty<String> paperListProperty;

    private final SimpleStringProperty charStringProperty;
    private final SimpleStringProperty stateStringProperty;
    private final SimpleStringProperty turingMachineStringProperty;


    private TuringModel(){
        // 初始化状态集
        stateSetProperty = new SimpleSetProperty<>(FXCollections.observableSet());
        // 初始化字母表
        charSetProperty = new SimpleSetProperty<>(FXCollections.observableSet());
        // 初始化纸带上的数值
        paperListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        // 初始化规则集合
        ruleSet = RuleSet.getInstance();
        positionProperty = new SimpleIntegerProperty();
        currentStateProperty = new SimpleStringProperty();
        endStateProperty = new SimpleStringProperty();

        charStringProperty = new SimpleStringProperty();
        stateStringProperty = new SimpleStringProperty();
        turingMachineStringProperty = new SimpleStringProperty();
        ruleListPositionProperty = new SimpleIntegerProperty();
        loadTemplate();
    }

    /**
     * 模板代码加载
     */
    public void loadTemplate(){
        ruleListPositionProperty.set(-1);

        positionProperty.set(1);
        currentStateProperty.set("q1");
        endStateProperty.set("qe");

        stateSetProperty.clear();
        stateSetProperty.add("q1");
        stateSetProperty.add("q2");
        stateSetProperty.add("q3");
        stateSetProperty.add("qe");
        stateStringProperty.set(stateSetToString());

        charSetProperty.clear();
        charSetProperty.add("0");
        charSetProperty.add("1");
        charSetProperty.add("b");
        charStringProperty.set(charSetToString());

        paperListProperty.clear();
        paperListProperty.add("b");
        paperListProperty.add("1");
        paperListProperty.add("1");
        paperListProperty.add("1");
        paperListProperty.add("1");
        paperListProperty.add("b");
        paperListProperty.add("1");
        paperListProperty.add("1");
        paperListProperty.add("1");
        paperListProperty.add("b");
        paperListProperty.add("b");

        ruleSet.clear();
        ruleSet.addRule("['q1', '1', 'q1', '1', 'R']");
        ruleSet.addRule("['q1', 'b', 'q2', '1', 'R']");
        ruleSet.addRule("['q2', '1', 'q2', '1', 'R']");
        ruleSet.addRule("['q2', 'b', 'q3', 'b', 'L']");
        ruleSet.addRule("['q3', '1', 'q3', 'b', 'S']");
        ruleSet.addRule("['q3', 'b', 'qe', 'b', 'S']");

        turingMachineStringProperty.set(toString());
    }

    /**
     * 图灵机实体机转换成字符串
     * @return 转换的字符串
     */
    public String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("StartState", currentStateProperty.get());

        JSONArray jsonArray = new JSONArray();
        ruleSet.ruleSetProperty.forEach(quintet -> jsonArray.add(quintet.toJsonArray()));
        jsonObject.put("Rules", jsonArray);

        JSONArray charSetArray = new JSONArray();
        charSetArray.addAll(charSetProperty);
        jsonObject.put("CharSet", charSetArray);

        JSONArray stateSetArray = new JSONArray();
        stateSetArray.addAll(stateSetProperty);
        jsonObject.put("StateSet", stateSetArray);

        JSONArray paperIntListArray = new JSONArray();
        paperIntListArray.addAll(paperListProperty);
        jsonObject.put("PaperIntList", paperIntListArray);

        return JSON.toJSONString(
                jsonObject,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 从字符串中载入图灵机
     * @param str 字符串
     * @return 图灵机实体类
     */
    public static TuringModel fromString(String str){
        TuringModel turingModel = TuringModel.getInstance();
        JSONObject jsonArray = (JSONObject) JSONObject.parse(str);
        // 加载开始状态
        turingModel.currentStateProperty.set(jsonArray.get("StartState").toString());
        // 加载规则集
        JSONArray tmp = (JSONArray) jsonArray.get("Rules");
        turingModel.ruleSet.clear();
        tmp.forEach(string -> turingModel.ruleSet.addRule(string.toString()));
        // 加载状态集
        tmp = (JSONArray) jsonArray.get("StateSet");
        turingModel.stateSetProperty.clear();
        tmp.forEach(string -> turingModel.stateSetProperty.add(string.toString()));
        turingModel.stateStringProperty.set(turingModel.stateSetToString());
        // 加载字符集
        tmp = (JSONArray) jsonArray.get("CharSet");
        turingModel.charSetProperty.clear();
        tmp.forEach(string -> turingModel.charSetProperty.add(string.toString()));
        turingModel.charStringProperty.set(turingModel.charSetToString());
        // 加载纸带列表
        tmp = (JSONArray) jsonArray.get("PaperIntList");
        turingModel.paperListProperty.clear();
        tmp.forEach(string -> turingModel.paperListProperty.add(string.toString()));
        turingModel.turingMachineStringProperty.set(turingModel.toString());
        return turingModel;
    }

    /**
     * 将状态集转换成字符串
     * @return 字符串
     */
    private String stateSetToString(){
        JSONArray stateSetArray = new JSONArray();
        stateSetArray.addAll(stateSetProperty);
        return JSON.toJSONString(
                stateSetArray,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 将字母集转换成字符串
     * @return 字符串
     */
    private String charSetToString(){
        JSONArray charSetArray = new JSONArray();
        charSetArray.addAll(charSetProperty);
        return JSON.toJSONString(
                charSetArray,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public SimpleIntegerProperty getPositionProperty(){
        return positionProperty;
    }

    public void setPositionProperty(int value){
        positionProperty.set(value);
    }

    public SimpleStringProperty getCurrentStateProperty() {
        return currentStateProperty;
    }

    public void setCurrentStateProperty(String str) {
        this.currentStateProperty.set(str);
    }

    public SimpleStringProperty getEndStateProperty() {
        return endStateProperty;
    }

    public SimpleStringProperty getCharStringProperty(){
        return charStringProperty;
    }

    public SimpleStringProperty getStateStringProperty(){
        return stateStringProperty;
    }

    public SimpleListProperty<String> getPaperListProperty() {
        return paperListProperty;
    }

    public void setPaperIntListByIndex(int index, String str){
        paperListProperty.set(index, str);
    }

    public SimpleStringProperty turingMachineProperty(){
        return turingMachineStringProperty;
    }

    public SimpleIntegerProperty getRuleListPositionProperty(){
        return ruleListPositionProperty;
    }

    public void setRuleListPositionProperty(int position){
        ruleListPositionProperty.set(position);
    }
}
