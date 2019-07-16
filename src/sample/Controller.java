package sample;


import Java.Settings;
import Java.repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.mail.Session;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public class Controller {

    @FXML private TextField textFieldUser;
    @FXML private ListView<String> listError;
    @FXML private TextArea additionalDescription;
    @FXML private Label messageError;
    @FXML private AnchorPane mainForm;
    @FXML private Button buttonGetAccess;
    @FXML private Button buttonSendClaim;

    private String path = System.getProperty("user.dir");
    private String pictureName = "";
    private String messageText = "";
    Map<String, String> settings = Settings.getSettings();

    ObservableList<String> errors = FXCollections.observableArrayList();


    // инициализация приложения
    @FXML
    public void initialize() {
        checkFolderPictures();
        getListErrorFromFile();
        listError.getItems().addAll(errors );

        // Only allowed to select single row in the ListView.
        listError.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        textFieldUser.setText(getCurrentUser());
        additionalDescription.setWrapText(true);//перенос строк

    }

    private void getListErrorFromFile() {
        try {
            FileInputStream inF = new FileInputStream(path+"/ListError.txt");
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

        ///////////////////////////////////////////////////////////////////
        //get data change file
        Path file = Paths.get("E://ListError.txt");
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(file, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());



        /////////////////////////////////////////////////////////
    }



    private String getCurrentUser() {
        System.out.println(path+"\\user.txt");
        String user = "";
        try {
            FileInputStream inF = new FileInputStream(path+"\\user.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inF,"Cp1251"));

            while ((user = bufferedReader.readLine()) != null) {
                break;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }




    public void SendClaim() {
        String error = listError.getSelectionModel().getSelectedItem();
        if(error==null){
            System.out.println( "выберите ошибку из списка");
            messageError.setText("выберите ошибку из списка");
            return;
        }
        if(!UserRepository.checkUser(textFieldUser.getText().trim())){
            System.out.println( "такого пользователя нет");
            messageError.setText( "такого пользователя нет");
            return;
        }
        createMessage();
        writeToFile();
        sendMail("");

    }
    public void GetAccess() {

        runTeamViewer();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        createScreenShot();
        sendMail(path+"\\pictures\\"+pictureName);
    }

    private String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void createMessage(){
        messageText = getCurrentDateTime()+" "+textFieldUser.getText() +" "+ listError.getSelectionModel().getSelectedItem()+" "+additionalDescription.getText();
    }

    private void writeToFile(){
        String filename= path+"/log.txt";
        FileWriter fw = null; //the true will append the new data
        try {
            fw = new FileWriter(filename,true);

            fw.write(messageText+ " \n");//appends the string to the file
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            Settings.writeError(e);
        }


    }
    private void checkFolderPictures(){
        Path pathPictures = Paths.get(path+"\\pictures\\");

        if (!Files.exists(pathPictures)) {
            // действия, если папка существует
            try {
                Files.createDirectories(pathPictures);
            } catch (IOException e) {
                e.printStackTrace();
                Settings.writeError(e);
            }
        }
    }
    private void runTeamViewer(){
        ProcessBuilder p = new ProcessBuilder();
        p.command(settings.get("pathTeamviewer"));

        try {
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
            Settings.writeError(e);
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
            pictureName = formatter.format(now.getTime())+".jpg";
            ImageIO.write(screenShot, "JPG", new File(path+"\\pictures\\"+pictureName));
        } catch (IOException e) {
            e.printStackTrace();
            Settings.writeError(e);
        }
    }
    private void sendMail(String file){
System.out.println(file);
        //Отправить E-Mail

        //Скачать javamail api (javax.mail.jar) отсюда http://www.oracle.com/technetwork/java/javamail/index.html
        //В Intellij IDEA в меню File->Project Structure...->Libraries нажать плюсик и добавить этот файл к проекту

        //Тто же самое сделать для JAF (activation.jar): http://www.oracle.com/technetwork/java/javase/jaf-136260.html

        //Если что-нибудь не получается, возможно сам почтовик блокирует авторизацию через ненадёжные приложения. (так по-умалчанию делает gmail.com и это отключается в личном кабинета)


        final String eMailFrom = settings.get("eMailFrom");
        final String password = settings.get("password");

        Properties props = new Properties();
        props.put("mail.smtp.auth",settings.get("mailSmtpAuth"));
        System.out.println(settings.get("mailSmtpStarttlsEnable"));
        props.put("mail.smtp.starttls.enable", settings.get("mailSmtpStarttlsEnable"));
        props.put("mail.smtp.host", settings.get("mailSmtpHost"));
        props.put("mail.smtp.port",settings.get("mailSmtpPort"));

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(eMailFrom, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(eMailFrom));//
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(settings.get("eMailTo")));
            message.setSubject("Testing Subject");
            message.setText(messageText
                    + "\n\n No spam to my email, please!");

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            Multipart multipart = new MimeMultipart();

            messageBodyPart = new MimeBodyPart();

            if(!file.equals("")) {

                DataSource source = new FileDataSource(path+"\\pictures\\"+pictureName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }


}
