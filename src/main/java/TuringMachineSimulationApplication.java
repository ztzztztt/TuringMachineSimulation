
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.jfoenix.assets.JFoenixResources;

import java.io.IOException;

/**
 * @author by zhoutao
 * @description 图灵机入口启动类
 * @date 2020/11/29 14:38
 */
public class TuringMachineSimulationApplication extends Application  {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
            Scene scene = new Scene(root);
            primaryStage.getIcons().add(new Image("images/favicon.png"));
            primaryStage.setTitle("图灵机模型-0.1");
            primaryStage.setScene(scene);
            // 设置窗口大小不可更改
            primaryStage.setResizable(false);
            // 显示窗口大小
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> Platform.exit());
        } catch (NullPointerException | IOException e){
            System.out.println("加载程序样式文件出错");
            System.err.println(e);
        }
    }
}
