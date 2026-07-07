module org.example.proyectopoo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.proyectopoo to javafx.fxml;
    exports org.example.proyectopoo;
}