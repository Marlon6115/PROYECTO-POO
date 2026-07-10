package org.example.proyectopoo.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alertas {
        private Alertas() {
        }

        public static void informacion(String titulo, String mensaje) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        public static void error(String titulo, String mensaje) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        public static void advertencia(String titulo, String mensaje) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        public static boolean confirmacion(String titulo, String mensaje) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);

            Optional<ButtonType> resultado = alert.showAndWait();

            return resultado.isPresent() && resultado.get() == ButtonType.OK;
        }

    }
