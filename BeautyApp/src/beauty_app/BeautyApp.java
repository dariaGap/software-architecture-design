package beauty_app;

import business_logic.Master;
import business_logic.Service;
import controllers.EntryClientInfoController;
import controllers.EntryController;
import controllers.MasterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import service.Server;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class BeautyApp extends Application {
    static Stage stage;
    static Parent panel;
    static String title;

    public static void main(String[] args) {
        Server.getInstance().start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        title = "BeautyApp";
        stage = primaryStage;
        stage.setTitle(title);
        stage.setResizable(false);
        setClient();
        stage.show();
    }

    public static void setClient(){
        setView("/view/Client.fxml");
    }

    public static void setAdmin(){
        setView("/view/Admin.fxml");
    }

    public static void setMaster(Integer master){
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = BeautyApp.class.getResource("/view/Master.fxml");
            loader.setLocation(xmlUrl);
            panel = (Parent) loader.load();
            Scene scene = new Scene(panel);
            stage.setScene(scene);
            MasterController c = loader.getController();
            c.init(master);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setEntry(){
        setView("/view/Entry.fxml");
    }

    public static void setEntry(Master master, Calendar date, List<Service> serviceList){
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = BeautyApp.class.getResource("/view/Entry.fxml");
            loader.setLocation(xmlUrl);
            panel = (Parent) loader.load();
            Scene scene = new Scene(panel);
            stage.setScene(scene);
            EntryController c = loader.getController();
            c.init(master,date,serviceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setEntryClientInfo(Master master, Calendar date, List<Service> serviceList){
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = BeautyApp.class.getResource("/view/EntryClientInfo.fxml");
            loader.setLocation(xmlUrl);
            panel = (Parent) loader.load();
            Scene scene = new Scene(panel);
            stage.setScene(scene);
            EntryClientInfoController c = loader.getController();
            c.init(master,date,serviceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAuth(){
        setView("/view/Authorization.fxml");
    }

    private static void setView(String fxml){
       try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = BeautyApp.class.getResource(fxml);
            loader.setLocation(xmlUrl);
            panel = (Parent) loader.load();
            Scene scene = new Scene(panel);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
