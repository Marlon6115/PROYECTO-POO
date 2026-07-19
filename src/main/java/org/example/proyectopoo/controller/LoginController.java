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

    @FXML
    public void initialize() {
        cbRol.getItems().addAll("ADMIN", "ESTANDAR", "INVITADO");
    }

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String clave = txtPassword.getText();
        String rol = cbRol.getValue();

        if(usuario.isEmpty() || clave.isEmpty() || rol == null){
            lblMensaje.setText("Tiene que llenar todos los campos");
            return;
        }

        Usuario user = new Usuario(0, usuario, clave, rol);

        Usuario existenciaUsuario = dao.buscarUsuario(user);

        if (existenciaUsuario != null) {
            Alertas.informacion("INFORMACIÓN", "Bienvenido al sistema, " + usuario);

            try {
                FXMLLoader loader = new FXMLLoader(ProyectoApplication.class.getResource("/org/example/proyectopoo/estudiantes.fxml"));
                Scene scene = new Scene(loader.load());

                EstudianteController controller = loader.getController();
                controller.setRolUsuario(existenciaUsuario.getRol());

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Gestor de Estudiantes");
                stage.show();

                Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                loginStage.close();
            } catch (Exception e) {
                Alertas.error("ERROR", "Error al abrir la ventana de estudiantes: " + e.getMessage());
            }

        } else {
            lblMensaje.setText("Usuario, contraseña o rol incorrectos.");
        }
    }

    @FXML
    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(ProyectoApplication.class.getResource("/org/example/proyectopoo/register.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Registro de Usuario");
            stage.show();

            Stage currentStage = (Stage) txtUsuario.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            Alertas.error("ERROR", "No se pudo abrir la ventana de registro: " + e.getMessage());
        }
    }

}