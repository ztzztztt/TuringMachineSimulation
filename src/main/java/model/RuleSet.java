package model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * @author by zhoutao
 * @description 图灵机规则集
 * @date 2020/12/9 17:09
 */
public class RuleSet {
    private static RuleSet ruleSet;

    SimpleSetProperty<Quintet> ruleList;

    private RuleSet(){
        ObservableSet<Quintet> observableSet = FXCollections.observableSet();
        ruleList = new SimpleSetProperty<>(observableSet);
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
    public ObservableList<String> toList(){
        ObservableList<String> observableList = FXCollections.observableArrayList();
        this.ruleList.forEach(quintet -> observableList.add(quintet.toString()));
        return observableList;
    }

    /**
     * 添加规则
     * @param quintet 规则五元组
     */
    public void addRule(Quintet quintet){
        ruleList.add(quintet);
    }

    /**
     * 添加规则
     * @param string 规则字符串
     */
    public void addRule(String string){
        try{
            Quintet quintet = Quintet.fromString(string);
            ruleList.add(quintet);
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }

    public void clear(){
        this.ruleList.clear();
    }

    public static String toString(RuleSet ruleSet){
        ArrayList<String> arrayList = new ArrayList<>();
        for (Quintet quintet : ruleSet.ruleList){
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

    public ObservableSet<Quintet> getRuleList() {
        return ruleList.get();
    }

    public SimpleSetProperty<Quintet> ruleListProperty() {
        return ruleList;
    }

    public Quintet searchQuintet(String currentState, String currentReadInSymbol){
        List<Quintet> arrayList = this.ruleList.stream().filter(q ->
                q.getCurrentReadInSymbol().equals(currentReadInSymbol) && q.getCurrentState().equals(currentState))
                .collect(Collectors.toList());

        System.out.println(arrayList.get(0));
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


//    public void loadQuintetList(JSONArray jsonArray){
//        jsonArray.forEach(array.forEach());
//    }
}
