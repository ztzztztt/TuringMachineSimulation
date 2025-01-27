package controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Quintet;
import model.TuringModel;
import service.Solution;
import utils.DialogBoxUtil;

import java.io.*;
import java.nio.file.Paths;

/**
 * @author by zhoutao
 * @description 布局入口
 * @date 2020/11/29 14:43
 */
public class LauncherController {

    private static final StringProperty title = new SimpleStringProperty();

    public static StringProperty getTitle(){
        return title;
    }

    @FXML
    private JFXTextArea jFXTextArea;
    @FXML
    private Text stateText;
    @FXML
    private Text charText;
    @FXML
    private Text currentText;

    @FXML
    private Button runButton;
    @FXML
    private Button stepButton;

    @FXML
    private TableView<Quintet> tableView;
    @FXML
    private ListView<String> paperListView;

    private final Solution solution;
    private final TuringModel turingModel;

    public LauncherController(){
        turingModel = TuringModel.getInstance();
        solution = new Solution();
    }

    @FXML
    void initialize() {
        // 状态集绑定
        stateText.textProperty().bindBidirectional(turingModel.getStateStringProperty());
        // 字符集绑定
        charText.textProperty().bindBidirectional(turingModel.getCharStringProperty());
        // 当前状态绑定
        currentText.textProperty().bindBidirectional(turingModel.getCurrentStateProperty());
        // 图灵机整个字符串绑定
        jFXTextArea.textProperty().bindBidirectional(turingModel.turingMachineProperty());
        // 纸带数据绑定
        paperListView.itemsProperty().bind(turingModel.getPaperListProperty());
        // 自定义列表格式
        this.customListView();
        tableView.setItems(turingModel.getRuleSet().getRuleListProperty());
        this.customTableView();

        focusPaperPosition();
        listenPositionChange();
        listenRuleChange();
        listenValueProperty();
    }

    @FXML
    void customTableView(){
        TableColumn<Quintet, String> currentStateColumn = new TableColumn<>("当前状态");
        currentStateColumn.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        tableView.getColumns().add(currentStateColumn);

        TableColumn<Quintet, String> readSymbolColumn = new TableColumn<>("当前读写符号");
        readSymbolColumn.setCellValueFactory(new PropertyValueFactory<>("currentReadInSymbol"));
        tableView.getColumns().add(readSymbolColumn);

        TableColumn<Quintet, String> newStateColumn = new TableColumn<>("新状态");
        newStateColumn.setCellValueFactory(new PropertyValueFactory<>("newState"));
        tableView.getColumns().add(newStateColumn);

        TableColumn<Quintet, String> writerSymbolColumn = new TableColumn<>("新写入符号");
        writerSymbolColumn.setCellValueFactory(new PropertyValueFactory<>("newWriteSymbol"));
        tableView.getColumns().add(writerSymbolColumn);

        TableColumn<Quintet, String> operatorColumn = new TableColumn<>("读写头移动");
        operatorColumn.setCellValueFactory(new PropertyValueFactory<>("operator"));
        tableView.getColumns().add(operatorColumn);
        // 禁止选择排序的功能
        tableView.setSortPolicy(param -> null);
        // 平均每一列的宽度
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

//        // 详细的过程关于加载数据
//        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Quintet, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Quintet, String> param) {
//                return new SimpleStringProperty(param.getValue().getCurrentState());
//            }
//        });
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
                // 取消listview每一个点击选中
                listCell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    System.out.println("不可点击");
                    event.consume();
                });
                return listCell;
            }
        });
    }

    /**
     * 设置listview的已选择的item
     */
    @FXML
    public void focusPaperPosition(){
        paperListView.requestFocus();
        paperListView.getSelectionModel().select(turingModel.getPositionProperty().get());
        System.out.println("已选中纸带" + turingModel.getPositionProperty().get() + "这一列");
    }

    @FXML
    public void focusTableView(){
        tableView.getSelectionModel().select(turingModel.getRuleListPositionProperty().get());
    }

    /**
     * 监听坐标值的变化
     */
    void listenPositionChange(){
        TuringModel.getInstance().getPositionProperty().addListener((observable, oldValue, newValue) -> focusPaperPosition());
    }

    void listenRuleChange(){
        TuringModel.getInstance().getRuleListPositionProperty().addListener((observable, oldValue, newValue) -> focusTableView());
    }

    /**
     * 事件监听
     * ActionEvent actionEvent
     */
    public void loadingButtonClick(){
        TuringModel.fromString(jFXTextArea.textProperty().get());
        solution.reset();
        focusPaperPosition();
    }

    public void resetButtonClick(){
        if (!solution.isRunning()){
            TuringModel.getInstance().loadTemplate();
            solution.reset();
        }
        title.set("主页");
        focusPaperPosition();
    }

    public void exportToFile(){
        // 文件选择器
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // 设置初始路径为项目根路径
        fileChooser.setInitialDirectory(new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "TuringMachine"))));
        Stage stage = new Stage();
        File file = fileChooser.showSaveDialog(stage);
        if (file == null){
            return;
        }
        try{
            String exportFilePath = file.getAbsolutePath();
            System.out.println("导出文件的路径" + exportFilePath);
            OutputStream outputStream = new FileOutputStream(exportFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(jFXTextArea.textProperty().get());
            outputStreamWriter.close();
        } catch(IOException e){
            System.err.println(e.toString());
        }
    }

    public void importFile(){
        // 文件选择器
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // 设置初始路径为项目根路径
        fileChooser.setInitialDirectory(new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "TuringMachine"))));
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        try{
            if (file == null){
                return;
            }
            String exportFilePath = file.getAbsolutePath();
            String filename = file.getName();
            title.set(filename);
            FileReader fileReader = new FileReader(exportFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null){
                stringBuffer.append(str);
            }
            bufferedReader.close();
            fileReader.close();
            jFXTextArea.textProperty().set(stringBuffer.toString());
//            TuringModel.fromString(stringBuffer.toString());
        } catch(IOException e){
            System.err.println(e.toString());
        }
    }

    public void runOperator(){
        if (!solution.isRunning()) {
            runButton.setDisable(true);
            stepButton.setDisable(true);
        }
        solution.start();
    }

    public void listenValueProperty(){
        solution.valueProperty().addListener((observable, oldValue, newValue) -> {
            focusPaperPosition();
            if (newValue != null && 0 == newValue){
                System.out.println("任务结束");
                runButton.setDisable(false);
                stepButton.setDisable(false);
                DialogBoxUtil.showAlertDialog("完成", turingModel.getPaperString(), Alert.AlertType.CONFIRMATION);
            } else if (newValue != null && -1 == newValue){
                System.out.println("任务出错");
                runButton.setDisable(false);
                stepButton.setDisable(false);
                DialogBoxUtil.showAlertDialog(
                        "错误",
                        "寻找规则失败\n" +
                                "寻找的规则为: " +
                                "[" + turingModel.getCurrentStateProperty().get() +
                                ", " + turingModel.getPaperListProperty().get(turingModel.getPositionProperty().get())
                                + ", *, *, *]",
                        Alert.AlertType.ERROR);
            }
        });
    }

    public void runStepOperator(){
        int result = solution.solveStep();
        if (result != 0){
            runButton.setDisable(true);
        } else{
            runButton.setDisable(false);
            DialogBoxUtil.showAlertDialog("完成", turingModel.getPaperString(), Alert.AlertType.CONFIRMATION);
        }
        focusPaperPosition();
        focusTableView();
    }
}
