module org.example.proyectopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;

    opens org.example.proyectopoo to javafx.fxml;
    opens org.example.proyectopoo.controller to javafx.fxml;

    exports org.example.proyectopoo;
    exports org.example.proyectopoo.controller;
}