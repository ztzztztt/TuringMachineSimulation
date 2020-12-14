package model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by zhoutao
 * @description 图灵机规则集
 * @date 2020/12/9 17:09
 */
public class RuleSet {
    private static RuleSet ruleSet;

    SimpleSetProperty<Quintet> ruleSetProperty;
    ObservableList<Quintet> ruleListProperty;


    private RuleSet(){
        ruleSetProperty = new SimpleSetProperty<>(FXCollections.observableSet());
        ruleListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public static RuleSet getInstance() {
        if (ruleSet == null){
            ruleSet = new RuleSet();
        }
        return ruleSet;
    }

    /**
     * 将规则列表转换成观察列表
     * @return 观察列表
     */
    public ObservableList<String> toListString(){
        ObservableList<String> observableList = FXCollections.observableArrayList();
        this.ruleSetProperty.forEach(quintet -> observableList.add(quintet.toString()));
        return observableList;
    }

    /**
     * 添加规则
     * @param string 规则字符串
     */
    public void addRule(String string){
        try{
            Quintet quintet = Quintet.fromString(string);
            ruleSetProperty.add(quintet);
            ruleSet.ruleList();
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }

    public void clear(){
        this.ruleSetProperty.clear();
        this.ruleListProperty.clear();
    }

    public static String toString(RuleSet ruleSet){
        ArrayList<String> arrayList = new ArrayList<>();
        for (Quintet quintet : ruleSet.ruleSetProperty){
            arrayList.add(quintet.toString());
        }
        return JSONObject.toJSONString(arrayList);
    }

    public static RuleSet fromString(String str){
        RuleSet ruleSet = RuleSet.getInstance();
        ruleSet.clear();
        JSONArray jsonArray = JSONArray.parseArray(str);
        for (Object string:jsonArray){
            ruleSet.addRule(string.toString());
        }
        return ruleSet;
    }


    public SimpleSetProperty<Quintet> getRuleSetProperty() {
        return ruleSetProperty;
    }

    private void ruleList(){
        ruleListProperty.clear();
        ruleListProperty.addAll(ruleSetProperty);
        ruleListProperty.sort(Quintet::compareTo);
    }

    public ObservableList<Quintet> getRuleListProperty(){
        return ruleListProperty;
    }

    public Quintet searchQuintet(String currentState, String currentReadInSymbol){
        List<Quintet> arrayList = this.ruleSetProperty.stream().filter(q ->
                q.getCurrentReadInSymbol().equals(currentReadInSymbol) && q.getCurrentState().equals(currentState))
                .collect(Collectors.toList());
//        System.out.println(arrayList.get(0));
        if(arrayList.size() == 0){
            System.err.println("Run Error, can't find rules");
            return null;
        } else if(arrayList.size() > 1){
            System.err.println("Run Error, find multiple rules");
            return null;
        } else {
            return arrayList.get(0);
        }
    }

    public Integer indexOf(Quintet quintet){
        int result = ruleListProperty.indexOf(quintet);
        if (result < 0 || result >= ruleListProperty.size()){
            return -1;
        }
        return result;
    }
}
