package sample;

import Java.Connection.ConnectionDB;
import Java.repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Controller {
    public Button buttonGetAccess;
    public Button buttonSendClaim;
    public TextField textFieldUser;
    public ListView<String> listError;
    public TextArea additionalDescription;
    @FXML
    AnchorPane mainForm;
    ObservableList<String> errors = FXCollections.observableArrayList();

    // инициализация приложения
    @FXML
    public void initialize() {
        getListErrorFromFile();
        listError.getItems().addAll(errors );

        // Only allowed to select single row in the ListView.
        listError.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        textFieldUser.setText(getCurrentUser());
        additionalDescription.setWrapText(true);
    }

    private void getListErrorFromFile() {
        try {
            FileInputStream inF = new FileInputStream("E://ListError.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inF,"Cp1251"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                errors.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }



    private String getCurrentUser() {
        String username = System.getProperty("user.name");
        return username;
    }




    public void SendClaim() {
        String error = listError.getSelectionModel().getSelectedItem();
        if(error==null){
            System.out.println( "выберите ошибку из списка");
            return;
        }
        if(!UserRepository.checkUser(textFieldUser.getText().trim())){
            System.out.println( "такого пользователя нет");
            return;
        }
        writeToFile();

    }
    public void GetAccess() {

        runTeamViewer();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        createScreenShot();
    }

    private String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    private void writeToFile(){
        String filename= "e://log.txt";
        FileWriter fw = null; //the true will append the new data
        try {
            fw = new FileWriter(filename,true);
            fw.write(getCurrentDateTime()+" "+textFieldUser.getText() +" "+ listError.getSelectionModel().getSelectedItem()+" "+additionalDescription.getText()+ " \n");//appends the string to the file
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void runTeamViewer(){
        ProcessBuilder p = new ProcessBuilder();
        p.command("C:\\Program Files\\TeamViewerQS.exe");

        try {
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void createScreenShot(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
        Calendar now = Calendar.getInstance();
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        try {
            ImageIO.write(screenShot, "JPG", new File("d:\\"+formatter.format(now.getTime())+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
