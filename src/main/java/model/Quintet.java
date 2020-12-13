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
    public static Quintet fromList(ArrayList<String> arrayList){
        return new Quintet(arrayList.get(0), arrayList.get(1), arrayList.get(2), arrayList.get(3), arrayList.get(4));
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

    public boolean isEqualTo(Quintet quintet){
        boolean result;
        if (this.equals(quintet)){
            result = true;
        } else{
            result =
                    this.currentState.equals(quintet.currentState) &&
                            this.currentReadInSymbol.equals(quintet.currentReadInSymbol) &&
                            this.newState.equals(quintet.newState) &&
                            this.newWriteSymbol.equals(quintet.newWriteSymbol) &&
                            this.operator.equals(quintet.operator);
        }
        return result;
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
}
