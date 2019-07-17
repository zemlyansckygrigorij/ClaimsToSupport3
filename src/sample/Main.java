package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
/**
 * создать и вывести на экран основную форму(sample.fxml)
 * назначить ей заголовок ("Программа автоматизации заявок")
* */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Программа автоматизации заявок");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();

    }

// запуск программы
    public static void main(String[] args) {
        launch(args);
    }
}
