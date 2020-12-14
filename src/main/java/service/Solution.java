package service;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Quintet;
import model.TuringModel;

import java.util.Random;

/**
 * @author by zhoutao
 * @description 根据规则求解图灵机
 * @date 2020/12/10 20:03
 */
public class Solution extends Service<Integer> {
    private final TuringModel turingModel;

    public Solution(){
        turingModel = TuringModel.getInstance();
    }

    @Override
    protected Task<Integer> createTask() {
        Task<Integer> task = new Task<Integer>() {
            /**
             * 为了解决动态显示，使用JavaFX自带的多线程求解
             * @return 运行状态
             */
            @Override
            protected Integer call() {
                boolean error = false;
                int step = 0;
                String readSymbol;
                String writeSymbol;
                String currentState = turingModel.getCurrentStateProperty().get();
                int pos = turingModel.getPositionProperty().get();
                while (true){
                    if (currentState.equals(turingModel.getEndStateProperty().get())){
                        System.out.println("Already Finish");
                        break;
                    }
                    readSymbol = turingModel.getPaperListProperty().get(pos);
                    Quintet quintet = turingModel.getRuleSet().searchQuintet(currentState, readSymbol);
                    if(quintet == null){
                        error = true;
                        break;
                    }
                    // 获取到当前的规则所在的顺序
                    int result = turingModel.getRuleSet().indexOf(quintet);
                    updateRuleListPosition(result);

                    if (quintet.getNewState().equals(turingModel.getEndStateProperty().get())){
                        updateCurrentStateProperty(turingModel.getEndStateProperty().get());
                        System.out.println("Finish");
                        break;
                    }
                    currentState = quintet.getNewState();
                    writeSymbol = quintet.getNewWriteSymbol();
                    // 更新参数
                    updatePaperIntListByIndex(pos, writeSymbol);
                    updateCurrentStateProperty(currentState);

                    if (quintet.getOperator().equals("L")){
                        pos --;
                        updatePosition(pos);
                    } else if(quintet.getOperator().equals("R")){
                        pos ++;
                        updatePosition(pos);
                    } else{
                        // 仅仅为了表示需要刷新组件
                        this.updateValue(step);
                    }
                    step ++;
                    try{
                        Thread.sleep(800);
                    }catch (InterruptedException | IllegalArgumentException e){
                        System.err.println(e.toString());
                    }
                }
                return error ? -1 : 0;
            }
        };
        return task;
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
    public int solveStep() {
        String currentState = turingModel.getCurrentStateProperty().get();
        String readSymbol;
        String writeSymbol;
        int pos = turingModel.getPositionProperty().get();
        if (currentState.equals(turingModel.getEndStateProperty().get())){
            System.out.println("Already Finish");
            return 0;
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
        return 1;
    }
}
