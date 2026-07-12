package org.example.proyectopoo.controller;

import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.proyectopoo.HelloApplication;
import org.example.proyectopoo.util.Alertas;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbRol;
    @FXML private Label lblMensaje;
    @FXML private Button btnLogin;
    @FXML private Button btnSalir;

    @FXML
    public void initialize() {
        cbRol.getItems().addAll("ADMIN", "ESTANDAR", "INVITADO");
    }

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String clave = txtPassword.getText();
        String rol = cbRol.getValue();

        if (usuario.equals("admin") && clave.equals("123") && "ADMIN".equals(rol)) {
            VentanaEstudiantes(rol);
        } else if (usuario.equals("user") && clave.equals("123") && "ESTANDAR".equals(rol)) {
            VentanaEstudiantes(rol);
        } else if (usuario.equals("guest") && clave.equals("123") && "INVITADO".equals(rol)) {
            VentanaEstudiantes(rol);
        } else {
            lblMensaje.setText("Credenciales incorrectas. Intente nuevamente.");
        }
    }

    private void VentanaEstudiantes(String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/proyectopoo/estudiantes.fxml"));
            Scene scene = new Scene(loader.load());
            EstudianteController controller = loader.getController();
            controller.setRolUsuario(rol);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Gestor de Estudiantes");
            stage.show();

            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            Alertas.error("ERROR", "Error al abrir la ventana: " + e.getMessage());
        }
    }

    @FXML
    private void salir() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }
}
