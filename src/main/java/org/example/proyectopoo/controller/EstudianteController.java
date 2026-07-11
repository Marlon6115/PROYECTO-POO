package org.example.proyectopoo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.proyectopoo.dao.EstudianteDAO;
import org.example.proyectopoo.modelo.Estudiante;
import org.example.proyectopoo.util.Alertas;

public class EstudianteController {

    @FXML private TextField txtCedula, txtNombre, txtApellido, txtCorreo, txtBuscar;
    @FXML private ComboBox<String> cbCarrera, cbFiltroCarrera;
    @FXML private Spinner<Integer> spEdad;
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colNombreCompleto;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TableColumn<Estudiante, String> colEmail;
    @FXML private Label lblUsuario;
    @FXML private Button btnGuardar, btnActualizar, btnEliminar, btnEditar, btnSalir;

    private EstudianteDAO dao = new EstudianteDAO();

    public void setRolUsuario(String rol) {
        lblUsuario.setText("Usuario: " + rol);

        if (rol.equals("INVITADO")) {
            btnGuardar.setDisable(true);
            btnActualizar.setDisable(true);
            btnEliminar.setDisable(true);
        } else if (rol.equals("ESTANDAR")) {
            btnEliminar.setDisable(true);
        }
    }

    private void cargarTabla() {
        ObservableList<Estudiante> lista = FXCollections.observableArrayList(dao.seleccionarTodo());
        tablaEstudiantes.setItems(lista);
    }

    @FXML
    public void initialize() {
        cbCarrera.getItems().addAll(
                "Agua y Saneamiento Ambiental", "Desarrollo de Software", "Electromecánica", "Redes y Telecomunicaciones");
        cbFiltroCarrera.getItems().addAll("Todas las carreras", "Agua y Saneamiento Ambiental", "Desarrollo de Software", "Electromecánica", "Redes y Telecomunicaciones");

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNombreCompleto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getNombre() + " " + data.getValue().getApellido()));
        colCarrera.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCarrera()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));

        cargarTabla();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtCedula.setText(newSelection.getCedula());
                        txtNombre.setText(newSelection.getCedula());
                        txtApellido.setText(newSelection.getCedula());
                        txtCorreo.setText(newSelection.getCedula());
                        cbCarrera.setValue(newSelection.getCarrera());
                    }
                });
    }

    @FXML
    private void guardar() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()
                || txtCorreo.getText().isEmpty() || cbCarrera.getValue() == null) {
            Alertas.advertencia("ADVERTENCIA", "Todos los campos deben llenarse.");
            return;
        }

        Estudiante e = new Estudiante(
                0,
                txtCedula.getText(),
                txtNombre.getText(),
                txtApellido.getText(),
                txtCorreo.getText(),
                cbCarrera.getValue()
        );

        boolean confirmacion = Alertas.confirmacion("Confirmar registro", "¿Desea registrar este estudiante?");
        if (confirmacion) {
            dao.insertar(e);
            cargarTabla();
            Alertas.informacion("INFORMACION", "Estudiante registrado correctamente.");
        } else {
            Alertas.informacion("INFORMACION", "El registro fue cancelado por el usuario.");
        }

    }

    @FXML
    private void actualizar() {
        cargarTabla();
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtBuscar.clear();
        cbCarrera.getSelectionModel().clearSelection();
        cbFiltroCarrera.getSelectionModel().clearSelection();
    }

    @FXML
    private void salir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestor_estudiantes/view/login.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

            Stage currentStage = (Stage) btnSalir.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            Alertas.error("ERROR", "No se pudo regresar al login: " + e.getMessage());
        }
    }


}
