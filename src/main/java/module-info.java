module com.example.wiggelsconcert {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.example.wiggelsconcert to javafx.fxml;
    exports com.example.wiggelsconcert;
}