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
    @FXML TextField txtUsuario;
    @FXML PasswordField txtClave;
    @FXML ComboBox<String> cbRol;
    @FXML Label lblMensaje;
    @FXML Button btnRegistrar, btnVolver;
    @FXML
    public void initialize(){
        cbRol.getItems().addAll("ADMIN", "ESTANDAR", "INVITADO");
    }

    private final UsarioDAO dao = new UsarioDAO();

    private String encriptado(String contra){
        String cifrado = BCrypt.hashpw(contra, BCrypt.gensalt());
        return cifrado;
    }

    public void registrar(){
        if (txtUsuario.getText().isEmpty() || txtClave.getText().isEmpty()
        || cbRol.getValue() == null){
            Alertas.advertencia("ADVERTENCIA", "Todos los campos deben llenarse.");
            return;
        }

        Usuario u = new Usuario(
                0,
                txtUsuario.getText(),
                encriptado(txtClave.getText()),
                cbRol.getValue()
        );

        boolean confirmacion = Alertas.confirmacion("Confirmar usuario", "¿Desea registrar este usuario?");

        if (!confirmacion){
            Alertas.informacion("INFORMACION", "Se cancelo el registro");
            return;
        }

        dao.nuevo(u);
        Alertas.informacion("INFORMACION", "Usuario registrado correctamente");
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