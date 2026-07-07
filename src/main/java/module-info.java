module org.example.proyectopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.proyectopoo to javafx.fxml;
    exports org.example.proyectopoo;
}