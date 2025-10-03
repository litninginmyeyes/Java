module org.example.task1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.task1 to javafx.fxml;
    exports org.example.task1;
}