package controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import model.RuleSet;
import model.TuringModel;
import service.Solution;

/**
 * @author by zhoutao
 * @description 布局入口
 * @date 2020/11/29 14:43
 */
public class LauncherController {
    @FXML
    private JFXTextArea jFXTextArea;
    @FXML
    private Text stateText;
    @FXML
    private Text charText;
    @FXML
    private Text currentText;
    @FXML
//    private ListView<String> listView;
    private TableView<String> tableView;
    @FXML
    private ListView<String> paperListView;

    private final ListProperty<String> ruleList = new SimpleListProperty<>(TuringModel.getInstance().getRuleSet().toList());
    private final SimpleIntegerProperty startPosInteger = new SimpleIntegerProperty(TuringModel.getInstance().getStartPosProperty().get());

    private final Solution solution;

    public LauncherController(){
        solution = new Solution();
    }

    public void loadingButtonClick(ActionEvent actionEvent){
        System.out.println(actionEvent);
        TuringModel.fromString(jFXTextArea.textProperty().get());
    }

    public void resetButtonClick(ActionEvent actionEvent){
        System.out.println(actionEvent);
        TuringModel.getInstance().loadTemplate();
    }

    public void runOperator(ActionEvent actionEvent){
        System.out.println(actionEvent);
        solution.solve();
    }

    @FXML
    void initialize() {
        startPosInteger.bindBidirectional(TuringModel.getInstance().getStartPosProperty());

        jFXTextArea.textProperty().bindBidirectional(TuringModel.getInstance().turingMachineProperty());
        stateText.textProperty().bindBidirectional(TuringModel.getInstance().stateSetToStringProperty());
        charText.textProperty().bindBidirectional(TuringModel.getInstance().charsetToStringProperty());
        currentText.textProperty().bindBidirectional(TuringModel.getInstance().startStateProperty());
        paperListView.itemsProperty().bind(TuringModel.getInstance().getPaperIntList());
        customListView();
//        listView.itemsProperty().bindBidirectional(ruleList);

        TableColumn<RuleSet, String> tableColumn = new TableColumn<>("当前状态");
//        tableColumn.setCellFactory(new Callback<TableColumn<RuleSet, String>, TableCell<RuleSet, String>>(TuringModel.getInstance().getRuleSet()) {
//            @Override
//            public TableCell<RuleSet, String> call(TableColumn<RuleSet, String> param) {
//                SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
//                return null;
//            }
//        });
//        tableView.getColumns().add(tableColumn);
//        tableView.itemsProperty().bindBidirectional(TuringModel.getInstance().getRuleSet());
        listenPosInteger();
        focusPaper(startPosInteger.get());
    }

    /**
     * 自定义纸带的样式
     */
    @FXML
    void customListView(){
        paperListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> listCell = new ListCell<String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty){
                            Label label = new Label(item);
                            label.setAlignment(Pos.CENTER);
                            label.setTextAlignment(TextAlignment.CENTER);
                            label.setPrefSize(45.0, 30.0);
                            this.setGraphic(label);
                        }
                    }
                };
                listCell.setPrefSize(45.0, 30.0);
                listCell.setBorder(
                    new Border(
                         new BorderStroke(
                             Color.LIGHTGRAY,
                             BorderStrokeStyle.SOLID,
                             CornerRadii.EMPTY,
                             BorderWidths.DEFAULT
                         )
                    )
                );
                return listCell;
            }
        });
    }

    /**
     * 设置listview的已选择的item
     * @param index 选择的下标
     */
    @FXML
    public void focusPaper(int index){
//        paperListView.getSelectionModel().select(startPosInterger.get());
        paperListView.getSelectionModel().select(index);
        paperListView.requestFocus();
        System.out.println("正在选中" + index + "这一列");
    }

    /**
     * 监听坐标值的变化
     */
    void listenPosInteger(){
        startPosInteger.addListener((observable, oldValue, newValue) -> focusPaper(newValue.intValue()));
    }
}
