package controllers;

import beauty_app.BeautyApp;
import business_logic.Employee;
import business_logic.Master;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.Facade;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationController implements Initializable {

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private TextField loginText;

    @FXML
    private TextField passwordText;


    public void initialize(URL location, ResourceBundle resources) {
        loginText.setStyle("-fx-text-fill: red;");
        passwordText.setStyle("-fx-text-fill: red;");
    }

    public void cancel(){
        BeautyApp.setClient();
    }

    public void go(){
        String log = login.getText();
        String pas = password.getText();

        if((!log.isEmpty())&&(!pas.isEmpty())){
            Employee employee = Facade.getInstance().authorize(log, pas);
            if(employee == null){
                String info = "Неверный логин или пароль!";
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Авторизация");
                alert.setHeaderText(null);
                alert.setContentText(info);
                alert.showAndWait();
                password.setText("");
                loginText.setVisible(false);
                passwordText.setVisible(false);
            }
            else {
                if(employee.getEmployeeType().equals(Employee.EmployeeType.ADMIN))
                    BeautyApp.setAdmin();
                else
                    BeautyApp.setMaster(employee.getId());
            }
        }
        else {
            if (log.isEmpty())
                loginText.setVisible(true);
            else
                loginText.setVisible(false);
            if (pas.isEmpty())
                passwordText.setVisible(true);
            else
                passwordText.setVisible(false);
        }
    }
}
