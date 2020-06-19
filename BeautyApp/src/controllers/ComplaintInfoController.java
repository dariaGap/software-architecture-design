package controllers;

import business_logic.Complaint;
import business_logic.Entry;
import business_logic.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import service.Facade;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ComplaintInfoController implements Initializable {
    Complaint complaint;

    @FXML
    private TextArea complaintText;

    @FXML
    private TextArea complaintInfo;

    @FXML
    private TextArea explanatoryText;

    @FXML
    private TextArea masterDecText;

    @FXML
    private TextArea clientDecText;

    @FXML
    private Button transfer;

    @FXML
    private Button save;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transfer.setVisible(true);
        save.setVisible(false);
    }

    public void initForMaster(Complaint complaint){
        transfer.setVisible(false);
        if(complaint.getState().equals(Complaint.ComplaintState.WAIT_EXPLANATORY)) {
            save.setVisible(true);
            explanatoryText.setEditable(true);
        }
        init(complaint);
    }

    public void init(Complaint complaint){
        if(!complaint.getState().equals(Complaint.ComplaintState.OPENED))
            transfer.setVisible(false);
        this.complaint = complaint;
        complaintInfo.clear();
        clientDecText.clear();
        complaintText.clear();
        explanatoryText.clear();
        masterDecText.clear();
        complaintInfo.appendText(Facade.getInstance().getComplaintInfo(complaint));
        clientDecText.appendText(Facade.getInstance().getClientDecText(complaint));
        complaintText.appendText(Facade.getInstance().getComplaintText(complaint));
        masterDecText.appendText(Facade.getInstance().getMasterDecText(complaint));
        explanatoryText.appendText(Facade.getInstance().getComplaintExpl(complaint));
    }

    public void transfer(){
        String info;
        if(complaint.getState().equals(Complaint.ComplaintState.OPENED)) {
            Boolean res = Facade.getInstance().transferComplaint(complaint);
            if (res)
                info = "Жалоба передана мастеру!";
            else
                info = "Произошла ошибка. Попробуйте еще раз!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Жалоба");
            alert.setHeaderText(null);
            alert.setContentText(info);
            alert.showAndWait();
            init(complaint);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Жалоба");
            alert.setHeaderText(null);
            alert.setContentText("Невозможно передать жалобу мастеру!");
            alert.showAndWait();
        }
    }

    public void save(){
        String info;
        if(complaint.getState().equals(Complaint.ComplaintState.WAIT_EXPLANATORY)) {
            Boolean res = Facade.getInstance().saveExplanatory(complaint, explanatoryText.getText());
            if (res)
                info = "Объяснительная сохранена!";
            else
                info = "Произошла ошибка. Попробуйте еще раз!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Жалоба");
            alert.setHeaderText(null);
            alert.setContentText(info);
            alert.showAndWait();
            explanatoryText.setEditable(false);
            init(complaint);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Жалоба");
            alert.setHeaderText(null);
            alert.setContentText("Невозможно сохранить объяснительную!");
            alert.showAndWait();
        }
    }
}
