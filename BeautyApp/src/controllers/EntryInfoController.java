package controllers;

import business_logic.Complaint;
import business_logic.Entry;
import business_logic.Master;
import business_logic.Service;
import database.Repository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import service.Facade;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class EntryInfoController {
    Entry entry;

    @FXML
    private TextArea entryInfo;

    @FXML
    private Button transfer;

    public void initForMaster(Entry entry){
        transfer.setVisible(false);
        init(entry);
    }

    public void init(Entry entry){
        this.entry = entry;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        entryInfo.clear();
        entryInfo.appendText("Дата: " + format.format(entry.getDate().getTime()) + "\n");
        entryInfo.appendText("Клиент: " + entry.getClient() + "\n");
        entryInfo.appendText("Мастер: " + entry.getMaster() + "\n");
        entryInfo.appendText("Услуги: \n");
        for(Service s: entry.getServices()){
            entryInfo.appendText("\t" + s + "\n");
        }
        entryInfo.appendText("Статус: " + entry.getState().toString() + "\n");
    }

    public void transfer(){
        String info;
        if(entry.getState().equals(Entry.EntryState.OPENED)) {
            Boolean res = Facade.getInstance().transferEntry(entry);
            if (res)
                info = "Запись передана мастеру!";
            else
                info = "Произошла ошибка. Попробуйте еще раз!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Запись");
            alert.setHeaderText(null);
            alert.setContentText(info);
            alert.showAndWait();
            init(entry);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Запись");
            alert.setHeaderText(null);
            alert.setContentText("Невозможно передать запись мастеру!");
            alert.showAndWait();
        }
    }
}
