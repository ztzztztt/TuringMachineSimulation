package model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;

/**
 * @author by zhoutao
 * @description 一条规则的五元组
 * @date 2020/12/10 8:47
 */
public class Quintet {
    private String currentState;
    private String currentReadInSymbol;
    private String newState;
    private String newWriteSymbol;
    private String operator;

    public Quintet(String currentState, String currentReadInSymbol, String newState, String newWriteSymbol, String operator){
        this.currentState = currentState;
        this.currentReadInSymbol = currentReadInSymbol;
        this.newState = newState;
        this.newWriteSymbol = newWriteSymbol;
        this.operator = operator;
    }

    /**
     * 将一条规则的对象转换成列表形式
     * @param quintet 规则
     * @return 列表
     */
    public static ArrayList<String> toList(Quintet quintet){
        ArrayList<String> arrayList = new ArrayList<>(5);
        arrayList.add(quintet.currentState);
        arrayList.add(quintet.currentReadInSymbol);
        arrayList.add(quintet.newState);
        arrayList.add(quintet.newWriteSymbol);
        arrayList.add(quintet.operator);
        return arrayList;
    }

    /**
     * 将一条规则转换成字符串对象
     * @param quintet 规则对象
     * @return 字符串
     */
    public static String toString(Quintet quintet){
        return JSONObject.toJSONString(Quintet.toList(quintet));
    }

    public String toString(){
        return JSONObject.toJSONString(Quintet.toList(this));
    }

    /**
     * 将列表转换成规则对象
     * @param arrayList 列表
     * @return 规则对象
     */
    public static Quintet fromList(JSONArray arrayList){
        return new Quintet(
                arrayList.get(0).toString(),
                arrayList.get(1).toString(),
                arrayList.get(2).toString(),
                arrayList.get(3).toString(),
                arrayList.get(4).toString());
    }

    /**
     * 将字符串格式的json转换成对象
     * @param string 字符串
     * @return Quintet
     */
    public static Quintet fromString(String string){
        JSONArray jsonArray = (JSONArray) JSONArray.parse(string);
        return Quintet.fromList(jsonArray);
    }

    public JSONArray toJsonArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(currentState);
        jsonArray.add(currentReadInSymbol);
        jsonArray.add(newState);
        jsonArray.add(newWriteSymbol);
        jsonArray.add(operator);
        return jsonArray;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getCurrentReadInSymbol() {
        return currentReadInSymbol;
    }

    public String getNewState() {
        return newState;
    }

    public String getNewWriteSymbol() {
        return newWriteSymbol;
    }

    public String getOperator() {
        return operator;
    }

    public int compareTo(Quintet quintet){
        int result = 0;
        result = this.getCurrentState().compareTo(quintet.getCurrentState());
        if (result == 0){
            return this.getCurrentReadInSymbol().compareTo(quintet.getCurrentReadInSymbol());
        } else {
            return result;
        }
    }

}
