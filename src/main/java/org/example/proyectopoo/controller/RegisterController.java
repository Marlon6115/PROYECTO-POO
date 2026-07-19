package org.example.proyectopoo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.proyectopoo.ProyectoApplication;
import org.example.proyectopoo.dao.UsarioDAO;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.proyectopoo.modelo.Usuario;
import org.example.proyectopoo.util.Alertas;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private ComboBox<String> cbRol;
    @FXML private Label lblMensaje;
    @FXML private Button btnRegistrar, btnVolver;

    @FXML
    public void initialize(){
        cbRol.getItems().addAll("ADMIN", "ESTANDAR", "INVITADO");
    }

    private final UsarioDAO dao = new UsarioDAO();

    public void registrar(){
        if (txtUsuario.getText().isEmpty() || txtClave.getText().isEmpty()
        || cbRol.getValue() == null){
            lblMensaje.setText("Todos los campos deben llenarse.");
            return;
        }

        String claveHash = BCrypt.hashpw(txtClave.getText(), BCrypt.gensalt());

        Usuario u = new Usuario(
                0,
                txtUsuario.getText(),
                claveHash,
                cbRol.getValue()
        );

        boolean confirmacion = Alertas.confirmacion("Confirmar registro", "¿Desea registrar este usuario?");
        if (confirmacion) {
            dao.nuevo(u);
            Alertas.informacion("INFORMACION", "Usuario registrado correctamente.");
            volver();
        } else {
            Alertas.informacion("INFORMACION", "El registro fue cancelado por el usuario.");
        }
    }

    public void volver(){
        try {
            FXMLLoader loader = new FXMLLoader(ProyectoApplication.class.getResource("/org/example/proyectopoo/login.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Inicio de sesion");
            stage.show();

            Stage loginStage = (Stage) btnVolver.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            Alertas.error("ERROR", "Error al abrir la ventana: " + e.getMessage());
        }
    }
}