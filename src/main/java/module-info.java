module com.example.wiggelsconcert {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;


    opens com.example.wiggelsconcert to javafx.fxml;
    opens com.example.wiggelsconcert.Entities to org.hibernate.orm.core;
    exports com.example.wiggelsconcert;
    exports com.example.wiggelsconcert.DAO;
    opens com.example.wiggelsconcert.DAO to javafx.fxml;
}