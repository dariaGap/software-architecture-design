package controllers;

import beauty_app.BeautyApp;
import business_logic.Client;
import business_logic.Entry;
import business_logic.Master;
import business_logic.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.Facade;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class EntryClientInfoController implements Initializable {

    Master entryMaster;
    Calendar entryDate;
    List<Service> entryServices;

    @FXML
    private TextArea entryInfo;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private TextField nameText;

    @FXML
    private TextField phoneText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entryInfo.setEditable(false);
        nameText.setStyle("-fx-text-fill: red;");
        phoneText.setStyle("-fx-text-fill: red;");
    }

    public void init(Master master, Calendar date, List<Service> serviceList){
        entryMaster = master;
        entryDate = date;
        entryServices = serviceList;

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        entryInfo.appendText("Дата: " + format.format(date.getTime()) + "\n");
        entryInfo.appendText("Мастер: " + master + "\n");
        entryInfo.appendText("Услуги: \n");
        for(Service s: serviceList){
            entryInfo.appendText("\t" + s + "\n");
        }
    }

    public void cancel(){
        BeautyApp.setClient();
    }

    public void back(){
        BeautyApp.setEntry(entryMaster,entryDate,entryServices);
    }

    public void save(){
        String info;
        String clientName = name.getText();
        String clientPhone = phone.getText();
        if((!clientName.isEmpty())&&(!clientPhone.isEmpty())){
            Boolean res = Facade.getInstance().saveEntry(clientName, clientPhone, entryMaster,entryServices,entryDate);
            if(res)
                info = "Запись успешно сохранена!";
            else
                info = "Произошла ошибка. Попробуйте еще раз!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Новая запись");
            alert.setHeaderText(null);
            alert.setContentText(info);
            alert.showAndWait();

            BeautyApp.setClient();
        }
        else {
            if(clientName.isEmpty())
                nameText.setVisible(true);
            else
                nameText.setVisible(false);
            if(clientPhone.isEmpty())
                phoneText.setVisible(true);
            else
                phoneText.setVisible(false);
        }
    }

}
