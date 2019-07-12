package sample;


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
import javax.mail.Session;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;



import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
     //   sendMail();
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
    private void sendMail(){
        /*
        String to = "receive@abc.om";         // sender email
        String from = "sender@abc.com";       // receiver email
        String host = "127.0.0.1";            // mail server host

        */


        String to = "cnk-120@mail.ru";         // sender email
        String from = "zemlyanscky.grigorij@yandex.ru";       // receiver email
        String host = "localhost";            // mail server host

        Properties properties = System.getProperties();
        System.out.println( "1");
        properties.setProperty("mail.smtp.host", host);
        System.out.println( "2");
        Session session = Session.getDefaultInstance(properties); // default session
        System.out.println( "3");

        try {
            MimeMessage message = new MimeMessage(session); // email message

            message.setFrom(new InternetAddress(from)); // setting header fields

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Test Mail from Java Program"); // subject line

            // actual mail body
            message.setText("You can send mail from Java program by using mail API, but you need" +
                    "couple of more JAR files e.g. smtp.jar and activation.jar");

            // Send message
            Transport.send(message); System.out.println("Email Sent successfully....");
        } catch (MessagingException mex){ mex.printStackTrace(); }








/*
        // Сюда необходимо подставить адрес получателя сообщения
        String to = "sendToMailAddress";
        String from = "sendFromMailAddress";
        // Сюда необходимо подставить SMTP сервер, используемый для отправки
        String host = "smtpserver.yourisp.net";

        // Создание свойств, получение сессии
        Properties props = new Properties();

        // При использовании статического метода Transport.send()
        // необходимо указать через какой хост будет передано сообщение
        props.put("mail.smtp.host", host);
        // Включение debug-режима
        props.put("mail.debug", "true");
        //Включение авторизации
        props.put("mail.smtp.auth", "true");
        // Получение сессии
        Session session = Session.getInstance(props);

        try {
            // Получение объекта транспорта для передачи электронного сообщения
            Transport bus = session.getTransport("smtp");

            // Устанавливаем соединение один раз
            // Метод Transport.send() отсоединяется после каждой отправки
            //bus.connect();
            // Обычно для SMTP сервера необходимо указать логин и пароль
            bus.connect("smtpserver.yourisp.net", "username", "password");

            // Создание объекта сообщения
            Message msg = new MimeMessage(session);

            // Установка атрибутов сообщения
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            // Парсинг списка адресов разделённых пробелами. Строгий синтаксис
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(to, true));
            // Парсинг списка адресов разделённых пробелами. Более мягкий синтаксис.
            msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(to, false));

            msg.setSubject("Тест отправки E-Mail с помощью Java");
            msg.setSentDate(new Date());

            // Установка контента сообщения и отправка
            setTextContent(msg);
            msg.saveChanges();
            bus.sendMessage(msg, address);

            setMultipartContent(msg);
            msg.saveChanges();
            bus.sendMessage(msg, address);

            setFileAsAttachment(msg, "C:/WINDOWS/CLOUD.GIF");
            msg.saveChanges();
            bus.sendMessage(msg, address);

            setHTMLContent(msg);
            msg.saveChanges();
            bus.sendMessage(msg, address);

            bus.close();

        }
        catch (MessagingException mex) {
            // Печать информации обо всех возможных возникших исключениях
            mex.printStackTrace();
            // Получение вложенного исключения
            while (mex.getNextException() != null) {
                // Получение следующего исключения в цепочке
                Exception ex = mex.getNextException();
                ex.printStackTrace();
                if (!(ex instanceof MessagingException)) break;
                else mex = (MessagingException)ex;
            }
        }

*/
    }


}
