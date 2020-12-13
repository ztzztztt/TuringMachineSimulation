package model;

import javafx.beans.property.SimpleSetProperty;

/**
 * @author by zhoutao
 * @description 状态集
 * @date 2020/12/13 10:55
 */
public class StateSet {
    private static StateSet stateSet = null;

    private static StateSet getInstance(){
        if (stateSet == null){
            stateSet = new StateSet();
        }
        return stateSet;
    }

    private final SimpleSetProperty<String> simpleSetProperty;

    public StateSet(){
        simpleSetProperty = new SimpleSetProperty<>();
    }


    public String toString(){
        return "";
    }
}
