package service;

import model.Quintet;
import model.TuringModel;

/**
 * @author by zhoutao
 * @description 根据规则求解图灵机
 * @date 2020/12/10 20:03
 */
public class Solution {
    private final TuringModel turingModel;


    public Solution(){
        turingModel = TuringModel.getInstance();
    }

    /**
     * 编译设置的规则是否可行
     * @return boolean
     */
    public boolean buildRuleSet(){
        return false;
    }

    public void solve() {
        turingModel.getPaperIntList().forEach(System.out::println);
        String currentState = turingModel.getStartState();
        String readSymbol;
        String writeSymbol;
        int pos = 1;
        while (true){
            readSymbol = turingModel.getPaperIntList().get(pos);
            Quintet quintet = turingModel.getRuleSet().searchQuintet(currentState, readSymbol);
            if (quintet.getNewState().equals(turingModel.getEndState())){
                turingModel.setStartStateProperty(turingModel.getEndState());
                System.out.println("Finish");
                break;
            }
            currentState = quintet.getNewState();
            writeSymbol = quintet.getNewWriteSymbol();
            turingModel.setPaperIntListByIndex(pos, writeSymbol);
            turingModel.setStartStateProperty(currentState);
            if (quintet.getOperator().equals("L")){
                pos --;
            } else if(quintet.getOperator().equals("R")){
                pos ++;
            }
            turingModel.setStartPosProperty(pos);
        }
    }

}
