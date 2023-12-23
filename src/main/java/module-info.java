module com.example.genetic_algorithm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.genetic_algorithm to javafx.fxml;
    exports com.example.genetic_algorithm;
}