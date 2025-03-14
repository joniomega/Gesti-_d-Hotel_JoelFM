module com.mycompany.gestio_hotel_dam_joelfm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.gestio_hotel_dam_joelfm to javafx.fxml;
    exports com.mycompany.gestio_hotel_dam_joelfm;
}
