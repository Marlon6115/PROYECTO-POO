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

public class EstudianteController {

    @FXML private TextField txtCedula, txtNombre, txtApellido, txtCorreo, txtBuscar;
    @FXML private ComboBox<String> cbCarrera, cbFiltroCarrera;
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colNombreCompleto;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TableColumn<Estudiante, String> colEmail;
    @FXML private Label lblUsuario;
    @FXML private Button btnGuardar, btnRefrescar, btnEliminar, btnEditar, btnSalir, btnBuscar;

    private final EstudianteDAO dao = new EstudianteDAO();

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
                "Seleccione...", "Agua y Saneamiento Ambiental", "Desarrollo de Software", "Electromecánica", "Redes y Telecomunicaciones");
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
                        txtNombre.setText(newSelection.getNombre());
                        txtApellido.setText(newSelection.getApellido());
                        txtCorreo.setText(newSelection.getCorreo());
                        cbCarrera.setValue(newSelection.getCarrera());
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
        cbCarrera.getSelectionModel().selectFirst();

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
            return;
        }
        cargarTabla();
    }

    @FXML
    private void eliminar() {
        try {
            String cedula = txtCedula.getText();
            dao.eliminar(cedula);
            boolean validacion = Alertas.confirmacion("CONFIRMACION", "Esta seguro de eliminar este registro");

            if (!validacion){
                Alertas.informacion("Cancelado", "Se cancelo la eliminacion.");
                return;
            }else {
                Alertas.informacion("Informacion", "Se elimino el registro correctamente");
            }
        } catch (Exception e) {
            Alertas.error("ERROR", "No se pudo eliminar: " + e.getMessage());
        }

        cargarTabla();
    }

    @FXML
    private void editar() {
        Estudiante seleccion = tablaEstudiantes.getSelectionModel().getSelectedItem();

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
    }

    @FXML
    private void buscar() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            Alertas.advertencia("ADVERTENCIA", "Tiene que ingresar una cedula o nombre en el campo");
            return;
        }
        ObservableList<Estudiante> listaFiltrada = FXCollections.observableArrayList();

        for (Estudiante est : dao.seleccionarTodo()) {
            String cedula = est.getCedula() != null ? est.getCedula().toLowerCase() : "";
            String nombre = est.getNombre() != null ? est.getNombre().toLowerCase() : "";
            String apellido = est.getApellido() != null ? est.getApellido().toLowerCase() : "";

            if (cedula.contains(textoBusqueda) || nombre.contains(textoBusqueda) ||
                    apellido.contains(textoBusqueda)) {
                listaFiltrada.add(est);
            }
        }
        tablaEstudiantes.setItems(listaFiltrada);
        if (listaFiltrada.isEmpty()) {
            Alertas.error("ERROR", "No se encontro el estudiante");
        }
    }
}