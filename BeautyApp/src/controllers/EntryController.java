package controllers;

import beauty_app.BeautyApp;
import business_logic.Master;
import business_logic.Service;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import service.Facade;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class EntryController implements Initializable {
    @FXML
    private ChoiceBox<Master> choiceMaster;

    @FXML
    private ChoiceBox<String> choiceTime;

    @FXML
    private ListView<Service> choiceService;

    @FXML
    private DatePicker choiceDate;

    @FXML
    private TextField dateText;

    @FXML
    private TextField timeText;

    @FXML
    private TextField masterText;

    @FXML
    private TextField serviceText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configChoiceTime();
        configChoiceMaster();
        configChoiseService();
        configChoiceDate();

        dateText.setStyle("-fx-text-fill: red;");
        timeText.setStyle("-fx-text-fill: red;");
        masterText.setStyle("-fx-text-fill: red;");
        serviceText.setStyle("-fx-text-fill: red;");

    }

    public void init(Master master, Calendar date, List<Service> serviceList){
        choiceMaster.getSelectionModel().select(master);
        for (Service s: serviceList) {
            choiceService.getSelectionModel().select(s);
        }
        choiceTime.getSelectionModel().select(Facade.getInstance().getStringTime(date));
        choiceDate.setValue(Instant.ofEpochMilli(date.getTime().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void cancel(){
        BeautyApp.setClient();
    }

    public void go(){
        Master selectedMaster = choiceMaster.getSelectionModel().getSelectedItem();
        List<Service> selectedServices = choiceService.getSelectionModel().getSelectedItems();
        String time = choiceTime.getSelectionModel().getSelectedItem();
        LocalDate date = choiceDate.getValue();
        if((selectedMaster != null)&&(!selectedServices.isEmpty())&&(time != null) && (date != null)) {
            Calendar selectedTime = Facade.getInstance().combineEntryDateTime(time, date);
            BeautyApp.setEntryClientInfo(selectedMaster,selectedTime,selectedServices);
        }
        else {
            if (date == null)
                dateText.setVisible(true);
            else
                dateText.setVisible(false);
            if (time == null)
                timeText.setVisible(true);
            else
                timeText.setVisible(false);
            if (selectedMaster == null)
                masterText.setVisible(true);
            else
                masterText.setVisible(false);
            if (selectedServices.isEmpty())
                serviceText.setVisible(true);
            else
                serviceText.setVisible(false);
        }
    }

    private void configChoiceMaster(){
        choiceMaster.setItems(Facade.getInstance().getMastersNames());

        ChangeListener<Master> changeMasterListener = new ChangeListener<Master>() {
            @Override
            public void changed(ObservableValue<? extends Master> observable, //
                                Master oldValue, Master newValue) {
                if (newValue != null) {
                    ObservableList<Service> serviceList = Facade.getInstance().getServicesNames(newValue);
                    choiceService.setItems(serviceList);
                    choiceService.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                    if(choiceDate.getValue() != null){
                        String selected = choiceTime.getSelectionModel().getSelectedItem();
                        ObservableList<String> timeList = Facade.getInstance().getFreeTime(choiceDate.getValue(), newValue);
                        choiceTime.setItems(timeList);
                        if (selected != null)
                            choiceTime.getSelectionModel().select(selected);
                    }
                }
            }
        };
        choiceMaster.getSelectionModel().selectedItemProperty().addListener(changeMasterListener);
    }

    private void configChoiceTime() {
        ObservableList<String> timeList = Facade.getInstance().getAllTime();
        choiceTime.setItems(timeList);
    }

    private void configChoiseService(){
        ObservableList<Service> serviceList = Facade.getInstance().getServicesNames();
        choiceService.setItems(serviceList);
        choiceService.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        choiceService.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends Service> observable, Service oldValue, Service newValue) -> {
                    if (choiceMaster.getSelectionModel().getSelectedItem() == null) {
                        ObservableList<Master> mastersList = Facade.getInstance().getMastersNames(newValue);
                        choiceMaster.setItems(mastersList);

                        ObservableList<Service> servicesList = Facade.getInstance().getServicesNames(newValue);
                        choiceService.setItems(servicesList);
                    }
                });
    }

    private void configChoiceDate(){
        choiceDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) <= 0 );
            }
        });

        choiceDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(choiceMaster.getSelectionModel().getSelectedItem() != null){
                String selected = choiceTime.getSelectionModel().getSelectedItem();
                ObservableList<String> timeList = Facade.getInstance().getFreeTime(newValue, choiceMaster.getSelectionModel().getSelectedItem());
                choiceTime.setItems(timeList);
                if (selected != null)
                    choiceTime.getSelectionModel().select(selected);
            }
        });
    }
}
