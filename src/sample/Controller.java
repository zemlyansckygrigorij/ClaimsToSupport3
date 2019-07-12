package sample;

import Java.Connection.ConnectionDB;
import Java.repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        System.out.println(" initialize");
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
        System.out.println(" SendClaim() ");
        String error = listError.getSelectionModel().getSelectedItem();
        System.out.println( "1");
        if(error==null){
            System.out.println( "выберите ошибку из списка");
            return;
        }
        System.out.println( "2");
        if(UserRepository.checkUser(textFieldUser.getText())){
            System.out.println( "такого пользователя нет");
            return;
        }

        System.out.println( listError.getSelectionModel().getSelectedItem());
        writeToFile();

        System.out.println( getCurrentDateTime());

    }
    public void GetAccess() {
        System.out.println(" GetAccess ");
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
            fw.write(getCurrentDateTime()+" "+getCurrentUser() +" "+ listError.getSelectionModel().getSelectedItem()+" "+additionalDescription.getText()+ " \n");//appends the string to the file
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
