package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * @author by zhoutao
 * @description 对话框对象工具类
 * @date 2020/12/13 22:15
 */
public class DialogBoxUtil {

    public static void showDialogBox(String title, String content){
        Stage stage = new Stage();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.initOwner(stage);
        dialog.show();

    }


    public static void showAlertDialog(String title, String content, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText("实际的输出为: " + content);
        alert.show();
    }
}
