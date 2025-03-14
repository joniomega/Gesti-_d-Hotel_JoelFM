package com.mycompany.gestio_hotel_dam_joelfm;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class FacturesController {

    @FXML
    private TextField idReservaField, baseImposableField, ivaField, totalField, dataEmisioField;
    @FXML
    private ComboBox<String> metodePagamentComboBox;
    @FXML
    private ListView<String> facturesListView;

    private final ObservableList<String> facturesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inicialitzar el ComboBox amb les opcions de pagament
        metodePagamentComboBox.getItems().addAll("Targeta", "Efectiu", "Transferència");
        metodePagamentComboBox.setValue("Targeta"); // Valor per defecte

        // Configurar el càlcul automàtic del total
        baseImposableField.textProperty().addListener((observable, oldValue, newValue) -> calcularTotal());
        ivaField.textProperty().addListener((observable, oldValue, newValue) -> calcularTotal());

        // Validar que només s'introdueixin números i punts en els camps numèrics
        baseImposableField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) { // Permet números i un punt decimal
                return change;
            }
            return null; // Rebutja la canvi si no és un número vàlid
        }));

        ivaField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) { // Permet números i un punt decimal
                return change;
            }
            return null; // Rebutja la canvi si no és un número vàlid
        }));

        carregarFactures();
        facturesListView.setItems(facturesList);
    }

    @FXML
    private void generarFactura() {
        try {
            // Obtenim els valors dels camps de text
            int idReserva = Integer.parseInt(idReservaField.getText());
            
            // Substituir comes per punts en els camps numèrics
            String baseImposableText = baseImposableField.getText().replace(",", ".");
            String ivaText = ivaField.getText().replace(",", ".");
            
            double baseImposable = Double.parseDouble(baseImposableText);
            double iva = Double.parseDouble(ivaText);
            double total = Double.parseDouble(totalField.getText());
            String dataEmisio = dataEmisioField.getText();
            String metodePagament = metodePagamentComboBox.getValue();

            // Validem que tots els camps estiguin omplerts
            if (dataEmisio.isEmpty()) {
                mostrarMissatge("Tots els camps són obligatoris.");
                return;
            }

            // Inserir a la taula factures
            String sql = "INSERT INTO factures (id_reserva, data_emisio, metode_pagament, base_imposable, iva, total) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idReserva);
                stmt.setString(2, dataEmisio);
                stmt.setString(3, metodePagament);
                stmt.setDouble(4, baseImposable);
                stmt.setDouble(5, iva);
                stmt.setDouble(6, total);

                stmt.executeUpdate();
                mostrarMissatge("✅ Factura generada correctament!");
                carregarFactures(); // Recarregar la llista de factures
            } catch (SQLException e) {
                mostrarMissatge("❌ Error en generar factura: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            mostrarMissatge("❌ Format incorrecte. Utilitza punts (.) per als decimals.");
        }
    }

    @FXML
    private void eliminarFactura() {
        String selectedFactura = facturesListView.getSelectionModel().getSelectedItem();
        if (selectedFactura == null) {
            mostrarMissatge("Selecciona una factura per eliminar.");
            return;
        }

        // Obtenir l'ID de la factura seleccionada
        String[] parts = selectedFactura.split(" - ");
        int idFactura = Integer.parseInt(parts[0]);

        // Eliminar la factura
        String sql = "DELETE FROM factures WHERE id_factura = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFactura);
            stmt.executeUpdate();

            mostrarMissatge("✅ Factura eliminada correctament!");
            carregarFactures(); // Recarregar la llista de factures
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en eliminar factura: " + e.getMessage());
        }
    }

    private void calcularTotal() {
        try {
            // Substituir comes per punts en els camps numèrics
            String baseImposableText = baseImposableField.getText().replace(",", ".");
            String ivaText = ivaField.getText().replace(",", ".");

            double baseImposable = Double.parseDouble(baseImposableText);
            double iva = Double.parseDouble(ivaText);
            double total = baseImposable * (1 + iva / 100);

            // Forçar el format amb punt decimal
            totalField.setText(String.format(Locale.US, "%.2f", total));
        } catch (NumberFormatException e) {
            totalField.setText("");
        }
    }

    private void carregarFactures() {
        facturesList.clear();
        String sql = "SELECT f.id_factura, f.id_reserva, f.data_emisio, f.metode_pagament, f.base_imposable, f.iva, f.total, r.id_client " +
                     "FROM factures f " +
                     "JOIN reserves r ON f.id_reserva = r.id_reserva " +
                     "ORDER BY f.data_emisio";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Forçar el format amb punt decimal
                String totalFormatat = String.format(Locale.US, "%.2f", rs.getDouble("total"));
                String factura = rs.getInt("id_factura") + " - Reserva: " + rs.getInt("id_reserva") + " - Client: " + rs.getInt("id_client") + " - " +
                                 rs.getString("data_emisio") + " - " + rs.getString("metode_pagament") + " - " +
                                 rs.getDouble("base_imposable") + "€ (IVA: " + rs.getDouble("iva") + "%) - Total: " + totalFormatat + "€";
                facturesList.add(factura);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar factures: " + e.getMessage());
        }
    }

    private void mostrarMissatge(String missatge) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informació");
        alert.setHeaderText(null);
        alert.setContentText(missatge);
        alert.showAndWait();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}