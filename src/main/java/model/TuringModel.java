package model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;


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


    private int pos;
    private String startState;
    private String endState;
    private final RuleSet ruleSet;
    private final SimpleSetProperty<String> stateSet;
    private final SimpleSetProperty<String> charSet;
    private final SimpleListProperty<String> paperIntList;

    private final SimpleStringProperty turingMachine;
    private final SimpleStringProperty startStateProperty;
    private final SimpleStringProperty endStateProperty;
    private final SimpleStringProperty charSetProperty;
    private final SimpleStringProperty stateSetProperty;
    private final SimpleIntegerProperty startPosProperty;

    private TuringModel(){
        // 初始化状态集
        ObservableSet<String> observableStateSet = FXCollections.observableSet();
        stateSet = new SimpleSetProperty<>(observableStateSet);
        // 初始化字母表
        ObservableSet<String> observableCharSet = FXCollections.observableSet();
        charSet = new SimpleSetProperty<>(observableCharSet);
        // 初始化纸带上的数值
        ObservableList<String> observablePaperIntListList = FXCollections.observableArrayList();
        paperIntList = new SimpleListProperty<>(observablePaperIntListList);
        // 初始化规则集合
        ruleSet = RuleSet.getInstance();
        loadTemplate();
        startStateProperty = new SimpleStringProperty(startState);
        endStateProperty = new SimpleStringProperty(endState);
        charSetProperty = new SimpleStringProperty(charSetToString());
        stateSetProperty = new SimpleStringProperty(stateSetToString());
        turingMachine = new SimpleStringProperty(toString());
        startPosProperty = new SimpleIntegerProperty(pos);
    }

    /**
     * 模板代码加载
     */
    public void loadTemplate(){
        pos = 1;
        startState = "q1";
        if (startStateProperty != null){
            startStateProperty.set(startState);
        }

        stateSet.clear();
        stateSet.add("q1");
        stateSet.add("q2");
        stateSet.add("q3");
        stateSet.add("qe");
        if (stateSetProperty != null){
            stateSetProperty.set(stateSetToString());
        }

        endState = "qe";

        charSet.clear();
        charSet.add("0");
        charSet.add("1");
        charSet.add("b");
        if (charSetProperty != null){
            charSetProperty.set(charSetToString());
        }

        paperIntList.clear();
        paperIntList.add("b");
        paperIntList.add("1");
        paperIntList.add("1");
        paperIntList.add("1");
        paperIntList.add("1");
        paperIntList.add("b");
        paperIntList.add("1");
        paperIntList.add("1");
        paperIntList.add("1");
        paperIntList.add("b");
        paperIntList.add("b");

        ruleSet.clear();
        ruleSet.addRule("['q1', '1', 'q1', '1', 'R']");
        ruleSet.addRule("['q1', 'b', 'q2', '1', 'R']");
        ruleSet.addRule("['q2', '1', 'q2', '1', 'R']");
        ruleSet.addRule("['q2', 'b', 'q3', 'b', 'L']");
        ruleSet.addRule("['q3', '1', 'q3', 'b', 'S']");
        ruleSet.addRule("['q3', 'b', 'qe', 'b', 'S']");

        if (turingMachine != null){
            turingMachine.set(toString());
        }
    }

    /**
     * 图灵机实体机转换成字符串
     * @return 转换的字符串
     */
    public String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("StartState", startState);

        JSONArray jsonArray = new JSONArray();
        ruleSet.ruleList.forEach(quintet -> jsonArray.add(quintet.toJsonArray()));
        jsonObject.put("Rules", jsonArray);

        JSONArray charSetArray = new JSONArray();
        charSetArray.addAll(charSet);
        jsonObject.put("CharSet", charSetArray);

        JSONArray stateSetArray = new JSONArray();
        stateSetArray.addAll(stateSet);
        jsonObject.put("StateSet", stateSetArray);

        JSONArray paperIntListArray = new JSONArray();
        paperIntListArray.addAll(paperIntList);
        jsonObject.put("PaperIntList", paperIntListArray);

        return JSON.toJSONString(
                jsonObject,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    public static TuringModel fromString(String str){
        TuringModel turingModel = TuringModel.getInstance();
        JSONObject jsonArray = (JSONObject) JSONObject.parse(str);
        // 加载开始状态
        turingModel.startState = jsonArray.get("StartState").toString();
        turingModel.startStateProperty.set(turingModel.startState);
        // 加载规则集
        JSONArray tmp = (JSONArray) jsonArray.get("Rules");
        turingModel.ruleSet.clear();
        tmp.forEach(string -> turingModel.ruleSet.addRule(string.toString()));
        // 加载状态集
        tmp = (JSONArray) jsonArray.get("StateSet");
        turingModel.stateSet.clear();
        tmp.forEach(string -> turingModel.stateSet.add(string.toString()));
        turingModel.stateSetProperty.set(turingModel.stateSetToString());
        // 加载字符集
        tmp = (JSONArray) jsonArray.get("CharSet");
        turingModel.charSet.clear();
        tmp.forEach(string -> turingModel.charSet.add(string.toString()));
        turingModel.charSetProperty.set(turingModel.charSetToString());
        // 加载纸带列表
        tmp = (JSONArray) jsonArray.get("PaperIntList");
        turingModel.paperIntList.clear();
        tmp.forEach(string -> turingModel.paperIntList.add(string.toString()));
        return turingModel;
    }

    public String stateSetToString(){
        JSONArray stateSetArray = new JSONArray();
        stateSetArray.addAll(stateSet);
        return JSON.toJSONString(
                stateSetArray,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    public String charSetToString(){
        JSONArray charSetArray = new JSONArray();
        charSetArray.addAll(charSet);
        return JSON.toJSONString(
                charSetArray,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    public SimpleStringProperty charsetToStringProperty(){
        return charSetProperty;
    }

    public SimpleStringProperty stateSetToStringProperty(){
        return stateSetProperty;
    }

    public String getStartState() {
        return startState;
    }

    public SimpleStringProperty startStateProperty() {
        return startStateProperty;
    }

    public void setStartStateProperty(String str) {
        this.startState = str;
        this.startStateProperty.set(str);
    }

    public String getEndState() {
        return endState;
    }

    public SimpleStringProperty endStateProperty() {
        return endStateProperty;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public ObservableSet<String> getStateSet() {
        return stateSet.get();
    }

    public SimpleSetProperty<String> stateSetProperty() {
        return stateSet;
    }

    public ObservableSet<String> getCharSet() {
        return charSet.get();
    }

    public SimpleSetProperty<String> charSetProperty() {
        return charSet;
    }

    public SimpleListProperty<String> getPaperIntList() {
        return paperIntList;
    }

    public void setPaperIntListByIndex(int index, String str){
        paperIntList.set(index, str);
    }

    public void setStartState(String startState){
        this.startState = startState;
    }

    public SimpleStringProperty turingMachineProperty(){
        return turingMachine;
    }


    public SimpleIntegerProperty getStartPosProperty(){
        return startPosProperty;
    }

    public void setStartPosProperty(int value){
        startPosProperty.set(value);
    }
}
