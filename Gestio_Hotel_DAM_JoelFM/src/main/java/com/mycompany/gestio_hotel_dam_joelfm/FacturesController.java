package com.mycompany.gestio_hotel_dam_joelfm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import java.util.HashMap;
import java.util.Map;

public class FacturesController {

    private static final double PREU_X_DIA = 10.0; // Preu per dia de l'habitació

    @FXML
    private ComboBox<String> reservaComboBox;
    @FXML
    private TextField baseImposableField, ivaField, totalField;
    @FXML
    private DatePicker dataEmisioPicker;
    @FXML
    private ComboBox<String> metodePagamentComboBox;
    @FXML
    private ListView<String> facturesListView;

    private final ObservableList<String> facturesList = FXCollections.observableArrayList();
    private final Map<String, Integer> reservaToIdMap = new HashMap<>();
    private final Map<String, LocalDate[]> reservaDatesMap = new HashMap<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        // Inicialitzar el ComboBox amb les opcions de pagament
        metodePagamentComboBox.getItems().addAll("Targeta", "Efectiu", "Transferència");
        metodePagamentComboBox.setValue("Targeta"); // Valor per defecte

        // Configurar DatePicker per a data d'emissió
        dataEmisioPicker.setValue(LocalDate.now());
        dataEmisioPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Carregar les reserves disponibles
        carregarReserves();

        // Configurar el càlcul automàtic del total
        ivaField.textProperty().addListener((observable, oldValue, newValue) -> calcularTotal());

        // Validar que només s'introdueixin números i punts en el camp IVA
        ivaField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));

        // Configurar listener per al ComboBox de reserves
        reservaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                calcularBaseImposable();
            }
        });

        carregarFactures();
        facturesListView.setItems(facturesList);
    }

    private void carregarReserves() {
        reservaComboBox.getItems().clear();
        reservaToIdMap.clear();
        reservaDatesMap.clear();

        String sql = "SELECT r.id_reserva, r.data_inici, r.data_fi, p.nom, p.cognom, p.document_identitat " +
                     "FROM reserves r " +
                     "JOIN clients c ON r.id_client = c.id_client " +
                     "JOIN persones p ON c.id_persona = p.id_persona " +
                     "WHERE r.id_reserva NOT IN (SELECT id_reserva FROM factures) " +
                     "ORDER BY r.data_inici";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LocalDate dataInici = LocalDate.parse(rs.getString("data_inici"));
                LocalDate dataFi = LocalDate.parse(rs.getString("data_fi"));
                
                String reservaInfo = "Reserva " + rs.getInt("id_reserva") + " - " +
                                    rs.getString("document_identitat") + " (" + 
                                    rs.getString("nom") + " " + rs.getString("cognom") + ") - " +
                                    rs.getString("data_inici") + " a " + rs.getString("data_fi");
                
                reservaComboBox.getItems().add(reservaInfo);
                reservaToIdMap.put(reservaInfo, rs.getInt("id_reserva"));
                reservaDatesMap.put(reservaInfo, new LocalDate[]{dataInici, dataFi});
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar reserves: " + e.getMessage());
        }
    }

    private void calcularBaseImposable() {
        String selectedReserva = reservaComboBox.getSelectionModel().getSelectedItem();
        if (selectedReserva != null) {
            LocalDate[] dates = reservaDatesMap.get(selectedReserva);
            long dies = ChronoUnit.DAYS.between(dates[0], dates[1]);
            double baseImposable = dies * PREU_X_DIA;
            baseImposableField.setText(String.format(Locale.US, "%.2f", baseImposable));
            calcularTotal();
        }
    }

    private void calcularTotal() {
        try {
            String ivaText = ivaField.getText().replace(",", ".");
            double baseImposable = Double.parseDouble(baseImposableField.getText());
            double iva = Double.parseDouble(ivaText);
            double total = baseImposable * (1 + iva / 100);

            totalField.setText(String.format(Locale.US, "%.2f", total));
        } catch (NumberFormatException e) {
            totalField.setText("");
        }
    }

    @FXML
    private void generarFactura() {
        try {
            // Obtenim la reserva seleccionada
            String selectedReserva = reservaComboBox.getSelectionModel().getSelectedItem();
            if (selectedReserva == null) {
                mostrarMissatge("Selecciona una reserva.");
                return;
            }
            
            int idReserva = reservaToIdMap.get(selectedReserva);
            
            String ivaText = ivaField.getText().replace(",", ".");
            double baseImposable = Double.parseDouble(baseImposableField.getText());
            double iva = Double.parseDouble(ivaText);
            double total = Double.parseDouble(totalField.getText());
            LocalDate dataEmisio = dataEmisioPicker.getValue();
            String metodePagament = metodePagamentComboBox.getValue();

            // Validem que tots els camps estiguin omplerts
            if (dataEmisio == null) {
                mostrarMissatge("Tots els camps són obligatoris.");
                return;
            }

            // Inserir a la taula factures
            String sql = "INSERT INTO factures (id_reserva, data_emisio, metode_pagament, base_imposable, iva, total) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idReserva);
                stmt.setString(2, dataEmisio.format(dateFormatter));
                stmt.setString(3, metodePagament);
                stmt.setDouble(4, baseImposable);
                stmt.setDouble(5, iva);
                stmt.setDouble(6, total);

                stmt.executeUpdate();
                mostrarMissatge("✅ Factura generada correctament!");
                carregarReserves(); // Recarregar les reserves (per treure la que ja té factura)
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
            carregarReserves(); // Recarregar les reserves (potser ara hi ha una nova disponible)
            carregarFactures(); // Recarregar la llista de factures
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en eliminar factura: " + e.getMessage());
        }
    }

    private void carregarFactures() {
        facturesList.clear();
        String sql = "SELECT f.id_factura, f.id_reserva, f.data_emisio, f.metode_pagament, f.base_imposable, f.iva, f.total, " +
                     "p.document_identitat, p.nom, p.cognom " +
                     "FROM factures f " +
                     "JOIN reserves r ON f.id_reserva = r.id_reserva " +
                     "JOIN clients c ON r.id_client = c.id_client " +
                     "JOIN persones p ON c.id_persona = p.id_persona " +
                     "ORDER BY f.data_emisio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String totalFormatat = String.format(Locale.US, "%.2f", rs.getDouble("total"));
                String factura = rs.getInt("id_factura") + " - Reserva: " + rs.getInt("id_reserva") + 
                               " - Client: " + rs.getString("document_identitat") + " (" + 
                               rs.getString("nom") + " " + rs.getString("cognom") + ") - " +
                               rs.getString("data_emisio") + " - " + rs.getString("metode_pagament") + 
                               " - " + rs.getDouble("base_imposable") + "€ (IVA: " + 
                               rs.getDouble("iva") + "%) - Total: " + totalFormatat + "€";
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