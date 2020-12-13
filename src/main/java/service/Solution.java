package service;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.Quintet;
import model.TuringModel;

/**
 * @author by zhoutao
 * @description 根据规则求解图灵机
 * @date 2020/12/10 20:03
 */
public class Solution extends Task<Integer> {
    private final TuringModel turingModel;

    public Solution(){
        turingModel = TuringModel.getInstance();
    }

    /**
     * 为了解决动态显示，使用JavaFX自带的多线程求解
     * @return 运行状态
     */
    @Override
    protected Integer call() {
        String currentState = turingModel.getCurrentStateProperty().get();
        String readSymbol;
        String writeSymbol;
        int pos = turingModel.getPositionProperty().get();
        while (true){
            readSymbol = turingModel.getPaperListProperty().get(pos);
            Quintet quintet = turingModel.getRuleSet().searchQuintet(currentState, readSymbol);
            // 获取到当前的规则所在的顺序
            int result = turingModel.getRuleSet().indexOf(quintet);
            this.updateRuleListPosition(result);

            if (quintet.getNewState().equals(turingModel.getEndStateProperty().get())){
                this.updateCurrentStateProperty(turingModel.getEndStateProperty().get());
                System.out.println("Finish");
                break;
            }
            currentState = quintet.getNewState();
            writeSymbol = quintet.getNewWriteSymbol();

            this.updatePaperIntListByIndex(pos, writeSymbol);
            this.updateCurrentStateProperty(currentState);

            if (quintet.getOperator().equals("L")){
                pos --;
            } else if(quintet.getOperator().equals("R")){
                pos ++;
            }
            this.updatePosition(pos);
            try{
                Thread.sleep(800);
            }catch (Exception e){
                System.err.println(e.toString());
            }
        }
        return 0;
    }

    public void updatePaperIntListByIndex(int pos, String writeSymbol){
        Platform.runLater(() -> turingModel.setPaperIntListByIndex(pos, writeSymbol));
    }

    public void updateCurrentStateProperty(String currentState){
        Platform.runLater(() -> turingModel.setCurrentStateProperty(currentState));
    }

    public void updatePosition(int pos){
        Platform.runLater(() -> turingModel.setPositionProperty(pos));
    }

    public void updateRuleListPosition(int pos){
        Platform.runLater(() -> turingModel.setRuleListPositionProperty(pos));
    }

    /**
     * 编译设置的规则是否可行
     * @return boolean
     */
    public boolean buildRuleSet(){
        return false;
    }

    /**
     * 图灵机模拟
     */
    public void solve() {
        String currentState = turingModel.getCurrentStateProperty().get();
        String readSymbol;
        String writeSymbol;
        int pos = turingModel.getPositionProperty().get();
        while (true){
            readSymbol = turingModel.getPaperListProperty().get(pos);
            Quintet quintet = turingModel.getRuleSet().searchQuintet(currentState, readSymbol);
            if (quintet.getNewState().equals(turingModel.getEndStateProperty().get())){
                turingModel.setCurrentStateProperty(turingModel.getEndStateProperty().get());
                System.out.println("Finish");
                break;
            }
            currentState = quintet.getNewState();
            writeSymbol = quintet.getNewWriteSymbol();
            turingModel.setPaperIntListByIndex(pos, writeSymbol);
            turingModel.setCurrentStateProperty(currentState);
            if (quintet.getOperator().equals("L")){
                pos --;
            } else if(quintet.getOperator().equals("R")){
                pos ++;
            }
            turingModel.setPositionProperty(pos);
        }
    }

    /**
     * 图灵机单步模拟
     */
    public void solveStep() {
        String currentState = turingModel.getCurrentStateProperty().get();
        String readSymbol;
        String writeSymbol;
        int pos = turingModel.getPositionProperty().get();
        if (currentState.equals(turingModel.getEndStateProperty().get())){
            System.out.println("Already Finish");
            return;
        }
        readSymbol = turingModel.getPaperListProperty().get(pos);
        Quintet quintet = turingModel.getRuleSet().searchQuintet(currentState, readSymbol);
        if (quintet.getNewState().equals(turingModel.getEndStateProperty().get())){
            turingModel.setCurrentStateProperty(turingModel.getEndStateProperty().get());
            System.out.println("Finish");
        }
        currentState = quintet.getNewState();
        writeSymbol = quintet.getNewWriteSymbol();
        turingModel.setPaperIntListByIndex(pos, writeSymbol);
        turingModel.setCurrentStateProperty(currentState);
        if (quintet.getOperator().equals("L")){
            pos --;
        } else if(quintet.getOperator().equals("R")){
            pos ++;
        }
        turingModel.setPositionProperty(pos);
    }
}
