package com.mycompany.gestio_hotel_dam_joelfm;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservesController {

    @FXML
    private TextField idClientField, idHabitacioField, dataIniciField, dataFiField;
    
    @FXML
    private ListView<String> reservesListView;

    private final ObservableList<String> reservesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarReserves();
        reservesListView.setItems(reservesList);
    }

    @FXML
    private void ferReserva() {
        // Obtenim els valors dels camps de text
        int idClient = Integer.parseInt(idClientField.getText());
        int idHabitacio = Integer.parseInt(idHabitacioField.getText());
        String dataInici = dataIniciField.getText();
        String dataFi = dataFiField.getText();

        // Validem que tots els camps estiguin omplerts
        if (dataInici.isEmpty() || dataFi.isEmpty()) {
            mostrarMissatge("Tots els camps són obligatoris.");
            return;
        }

        // Inserir a la taula reserves
        String sql = "INSERT INTO reserves (id_client, id_habitacio, data_inici, data_fi) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            stmt.setInt(2, idHabitacio);
            stmt.setString(3, dataInici);
            stmt.setString(4, dataFi);

            stmt.executeUpdate();
            mostrarMissatge("✅ Reserva creada correctament!");
            carregarReserves(); // Recarregar la llista de reserves
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en fer reserva: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarReserva() {
        // Obtenim la reserva seleccionada
        String reservaSeleccionada = reservesListView.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarMissatge("⚠ Selecciona una reserva per eliminar.");
            return;
        }

        // Extreure l'ID de la reserva seleccionada
        int idReserva = Integer.parseInt(reservaSeleccionada.split(" - ")[0]);

        // Eliminar la reserva de la base de dades
        String sql = "DELETE FROM reserves WHERE id_reserva = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            stmt.executeUpdate();
            mostrarMissatge("✅ Reserva eliminada correctament!");
            carregarReserves(); // Recarregar la llista de reserves
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en eliminar reserva: " + e.getMessage());
        }
    }

    private void carregarReserves() {
        reservesList.clear();
        String sql = "SELECT r.id_reserva, r.id_client, r.id_habitacio, r.data_inici, r.data_fi, p.nom, p.cognom " +
                     "FROM reserves r " +
                     "JOIN clients c ON r.id_client = c.id_client " +
                     "JOIN persones p ON c.id_persona = p.id_persona " +
                     "ORDER BY r.data_inici";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String reserva = rs.getInt("id_reserva") + " - Client: " + rs.getString("nom") + " " + rs.getString("cognom") + 
                                 " - Habitació: " + rs.getInt("id_habitacio") + " - " + rs.getString("data_inici") + " a " + rs.getString("data_fi");
                reservesList.add(reserva);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar reserves: " + e.getMessage());
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