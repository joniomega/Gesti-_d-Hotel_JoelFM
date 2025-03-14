package com.mycompany.gestio_hotel_dam_joelfm;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToclients() throws IOException {
        App.setRoot("clients");
    }
    @FXML
    private void switchToempleats() throws IOException {
        App.setRoot("empleats");
    }
    @FXML
    private void switchTofactures() throws IOException {
        App.setRoot("factures");
    }
    @FXML
    private void switchToreserves() throws IOException {
        App.setRoot("reserves");
    }
    @FXML
    private void switchTotasques() throws IOException {
        App.setRoot("tasques");
    }
}
