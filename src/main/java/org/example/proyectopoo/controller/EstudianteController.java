package org.example.proyectopoo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.proyectopoo.ProyectoApplication;
import org.example.proyectopoo.dao.EstudianteDAO;
import org.example.proyectopoo.modelo.Estudiante;
import org.example.proyectopoo.util.Alertas;

import java.sql.SQLException;
import java.text.Normalizer;

public class EstudianteController {

    @FXML private TextField txtCedula, txtNombre, txtApellido, txtCorreo, txtBuscar;
    @FXML private ComboBox<String> cbCarrera;
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colCedula;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TableColumn<Estudiante, String> colEmail;
    @FXML private Label lblUsuario;
    @FXML private Button btnGuardar, btnEliminar, btnEditar, btnSalir, btnBuscar;

    private final EstudianteDAO dao = new EstudianteDAO();

    private boolean validarDatos() {

        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || correo.isEmpty() || cbCarrera.getValue() == null) {

            Alertas.error("ERROR", "Todos los campos son obligatorios.");
            return false;
        }

        if (!cedula.matches("\\d{10}")) {
            Alertas.error("ERROR",
                    "La cédula debe contener exactamente 10 números.");
            return false;
        }

        if (!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            Alertas.error("ERROR",
                    "El nombre solo puede contener letras.");
            return false;
        }

        if (!apellido.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            Alertas.error("ERROR",
                    "El apellido solo puede contener letras.");
            return false;
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            Alertas.error("ERROR",
                    "Ingrese un correo electrónico válido. Ejemplo: usuario@gmail.com");
            return false;
        }

        return true;
    }


    public void setRolUsuario(String rol) {
        lblUsuario.setText("Usuario: " + rol);

        if (rol.equals("INVITADO")) {
            btnGuardar.setDisable(true);
            btnBuscar.setVisible(false);

            btnEditar.setDisable(true);
            btnEditar.setVisible(false);

            btnEliminar.setDisable(true);
            btnEliminar.setVisible(false);
        } else if (rol.equals("ESTANDAR")) {
            btnEliminar.setDisable(true);
            btnEliminar.setVisible(false);
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
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colCedula.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCedula()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getNombre() + " " + data.getValue().getApellido()));
        colCarrera.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCarrera()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));

        cargarTabla();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtCedula.setText(newSelection.getCedula());
                        txtNombre.setText(newSelection.getNombre());
                        txtApellido.setText(newSelection.getApellido());
                        txtCorreo.setText(newSelection.getCorreo());
                        cbCarrera.setValue(newSelection.getCarrera());
                    }
                });
        cbCarrera.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione...");
                } else {
                    setText(item);
                }
            }
        });
    }

    @FXML
    private void refrescar() {
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtBuscar.clear();
        cbCarrera.getSelectionModel().clearSelection();

        cargarTabla();
    }

    @FXML
    private void salir() {
        try {
            FXMLLoader loader = new FXMLLoader(ProyectoApplication.class.getResource("/org/example/proyectopoo/login.fxml"));
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

    @FXML
    private void guardar() {
        if (!validarDatos()) {
            return;
        }

        if (dao.existeCedula(txtCedula.getText().trim())) {
            Alertas.error("ERROR", "Ya existe un estudiante registrado con esa cédula.");
            return;
        }

        Estudiante estudiante = new Estudiante(
                0,
                txtCedula.getText(),
                txtNombre.getText(),
                txtApellido.getText(),
                txtCorreo.getText(),
                cbCarrera.getValue()
        );

        boolean confirmacion = Alertas.confirmacion("Confirmar registro", "¿Desea registrar este estudiante?");
        if (confirmacion) {
            try {
                dao.insertar(estudiante);
                Alertas.informacion("INFORMACION", "Estudiante registrado correctamente.");
                refrescar();
            } catch (SQLException e) {
                Alertas.error("ERROR", "No se pudo registrar el estudiante: " + e.getMessage());
            }
        } else {
            Alertas.informacion("INFORMACION", "El registro fue cancelado por el usuario.");
        }

    }

    @FXML
    private void eliminar() {

        Estudiante seleccion = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            Alertas.error("ERROR", "Seleccione un estudiante para eliminar.");
            return;
        }

        boolean validacion = Alertas.confirmacion(
                "CONFIRMACION",
                "¿Está seguro de eliminar este registro?"
        );

        if (!validacion) {
            Alertas.informacion("Cancelado", "Se canceló la eliminación.");
            return;
        }

        try {
            dao.eliminar(seleccion.getCedula());
            Alertas.informacion("Información", "Se eliminó el registro correctamente.");
            cargarTabla();
            refrescar();
        } catch (Exception e) {
            Alertas.error("ERROR", "No se pudo eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        Estudiante seleccion = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (!validarDatos()) {
            return;
        }

        if (seleccion != null){
            try {
                seleccion.setNombre(txtNombre.getText());
                seleccion.setApellido(txtApellido.getText());
                seleccion.setCorreo(txtCorreo.getText());
                seleccion.setCarrera(cbCarrera.getValue());

                dao.actualizar(seleccion);
                Alertas.informacion("INFORMACION", "Se actualizo el estudiante exitosamente");
            } catch (Exception e) {
                Alertas.error("ERROR", "No se pudo editar: " + e.getMessage());
            }
        }
        cargarTabla();
        refrescar();
    }

    @FXML
    private void buscar() {

        String textoBusqueda = txtBuscar.getText().trim();

        textoBusqueda = Normalizer.normalize(textoBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        if (textoBusqueda.isEmpty()) {
            Alertas.advertencia("ADVERTENCIA",
                    "Tiene que ingresar una cedula o nombre en el campo");
            return;
        }

        ObservableList<Estudiante> listaFiltrada = FXCollections.observableArrayList();

        for (Estudiante est : dao.seleccionarTodo()) {

            String cedula = est.getCedula() != null ? est.getCedula() : "";

            String nombreCompleto = ((est.getNombre() != null ? est.getNombre() : "")
                    + " "
                    + (est.getApellido() != null ? est.getApellido() : ""));

            nombreCompleto = Normalizer.normalize(nombreCompleto, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();

            if (cedula.contains(textoBusqueda)
                    || nombreCompleto.contains(textoBusqueda)) {

                listaFiltrada.add(est);
            }
        }

        tablaEstudiantes.setItems(listaFiltrada);

        if (listaFiltrada.isEmpty()) {
            Alertas.error("ERROR", "No se encontró el estudiante");
        }
    }
}