package sample;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    public Button buttonGetAccess;
    public Button buttonSendClaim;
    public TextField textFieldUser;
    public ListView listUsers;
    public TextArea additionalDescription;

    
    public void SendClaim() {
        System.out.println(" SendClaim() ");
    }
    public void GetAccess() {
        System.out.println(" GetAccess ");
    }
}
