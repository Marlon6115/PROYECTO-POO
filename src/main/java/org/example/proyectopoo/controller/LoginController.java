package org.example.proyectopoo.controller;

import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.proyectopoo.ProyectoApplication;
import org.example.proyectopoo.dao.UsarioDAO;
import org.example.proyectopoo.modelo.Usuario;
import org.example.proyectopoo.util.Alertas;

public class LoginController {
    private final UsarioDAO dao = new UsarioDAO();

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

        if(txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty() || cbRol.getValue() == null){
            Alertas.error("ERROR", "Tiene que llenar todos los campos");
            return;
        }

        Usuario existenciaUsuario = dao.bucarUsuario(usuario, clave);

        if(existenciaUsuario == null){
            return;
        }

        if(!existenciaUsuario.getRol().equals(cbRol.getValue())){
            Alertas.error("ERROR", "El usuario, contraseña o rol estan incorrectos, intente de nuevo");
            return;
        }

        Alertas.informacion("INFORMACION", "Bienvenido al sistema " + txtUsuario.getText());
        VentanaEstudiantes(existenciaUsuario.getRol());
    }

    private void VentanaEstudiantes(String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(ProyectoApplication.class.getResource("/org/example/proyectopoo/estudiantes.fxml"));
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