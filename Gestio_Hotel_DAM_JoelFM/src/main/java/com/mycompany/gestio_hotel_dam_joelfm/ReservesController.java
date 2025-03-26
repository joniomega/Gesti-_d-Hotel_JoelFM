package com.mycompany.gestio_hotel_dam_joelfm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReservesController {

    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private ComboBox<String> habitacioComboBox;
    @FXML
    private DatePicker dataIniciPicker;
    @FXML
    private DatePicker dataFiPicker;
    @FXML
    private ListView<String> reservesListView;

    private final ObservableList<String> reservesList = FXCollections.observableArrayList();
    private final Map<String, Integer> dniToClientIdMap = new HashMap<>();
    private final Map<String, Integer> habitacioToIdMap = new HashMap<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        carregarClients();
        carregarHabitacions();
        carregarReserves();
        reservesListView.setItems(reservesList);
        
        // Configurar los DatePickers para que no permitan fechas pasadas
        dataIniciPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        dataFiPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = dataIniciPicker.getValue();
                setDisable(empty || date.isBefore(LocalDate.now()) || 
                          (startDate != null && date.isBefore(startDate)));
            }
        });
    }

    private void carregarClients() {
        String sql = "SELECT c.id_client, p.document_identitat, p.nom, p.cognom " +
                     "FROM clients c " +
                     "JOIN persones p ON c.id_persona = p.id_persona " +
                     "ORDER BY p.nom, p.cognom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String dni = rs.getString("document_identitat");
                String nomComplet = dni + " - " + rs.getString("nom") + " " + rs.getString("cognom");
                int idClient = rs.getInt("id_client");
                
                clientComboBox.getItems().add(nomComplet);
                dniToClientIdMap.put(nomComplet, idClient);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar clients: " + e.getMessage());
        }
    }

    private void carregarHabitacions() {
        String sql = "SELECT id_habitacio, numero_habitacio FROM habitacions ORDER BY numero_habitacio";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String habitacio = "Habitació " + rs.getString("numero_habitacio");
                int idHabitacio = rs.getInt("id_habitacio");
                
                habitacioComboBox.getItems().add(habitacio);
                habitacioToIdMap.put(habitacio, idHabitacio);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar habitacions: " + e.getMessage());
        }
    }

    @FXML
    private void ferReserva() {
        // Validar selección de cliente
        String clientSeleccionat = clientComboBox.getSelectionModel().getSelectedItem();
        if (clientSeleccionat == null) {
            mostrarMissatge("Selecciona un client.");
            return;
        }
        
        // Validar selección de habitación
        String habitacioSeleccionada = habitacioComboBox.getSelectionModel().getSelectedItem();
        if (habitacioSeleccionada == null) {
            mostrarMissatge("Selecciona una habitació.");
            return;
        }
        
        // Validar fechas
        LocalDate dataInici = dataIniciPicker.getValue();
        LocalDate dataFi = dataFiPicker.getValue();
        
        if (dataInici == null || dataFi == null) {
            mostrarMissatge("Selecciona les dates d'inici i fi.");
            return;
        }
        
        if (dataFi.isBefore(dataInici)) {
            mostrarMissatge("La data de fi ha de ser posterior a la data d'inici.");
            return;
        }
        
        // Obtener IDs
        int idClient = dniToClientIdMap.get(clientSeleccionat);
        int idHabitacio = habitacioToIdMap.get(habitacioSeleccionada);
        
        // Verificar disponibilidad
        if (!habitacioDisponible(idHabitacio, dataInici, dataFi)) {
            mostrarMissatge("❌ L'habitació no està disponible en les dates seleccionades.");
            return;
        }

        // Insertar reserva
        String sql = "INSERT INTO reserves (id_client, id_habitacio, data_inici, data_fi) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            stmt.setInt(2, idHabitacio);
            stmt.setString(3, dataInici.format(dateFormatter));
            stmt.setString(4, dataFi.format(dateFormatter));

            stmt.executeUpdate();
            mostrarMissatge("✅ Reserva creada correctament!");
            carregarReserves();
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en fer reserva: " + e.getMessage());
        }
    }

    private boolean habitacioDisponible(int idHabitacio, LocalDate novaDataInici, LocalDate novaDataFi) {
        String sql = "SELECT COUNT(*) FROM reserves " +
                     "WHERE id_habitacio = ? AND " +
                     "((data_inici <= ? AND data_fi >= ?) OR " +
                     "(data_inici >= ? AND data_inici <= ?) OR " +
                     "(data_fi >= ? AND data_fi <= ?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idHabitacio);
            stmt.setString(2, novaDataFi.format(dateFormatter));
            stmt.setString(3, novaDataInici.format(dateFormatter));
            stmt.setString(4, novaDataInici.format(dateFormatter));
            stmt.setString(5, novaDataFi.format(dateFormatter));
            stmt.setString(6, novaDataInici.format(dateFormatter));
            stmt.setString(7, novaDataFi.format(dateFormatter));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en verificar disponibilitat: " + e.getMessage());
        }
        return false;
    }

    @FXML
    private void eliminarReserva() {
        String reservaSeleccionada = reservesListView.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarMissatge("⚠ Selecciona una reserva per eliminar.");
            return;
        }

        int idReserva = Integer.parseInt(reservaSeleccionada.split(" - ")[0]);
        String sql = "DELETE FROM reserves WHERE id_reserva = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            stmt.executeUpdate();
            mostrarMissatge("✅ Reserva eliminada correctament!");
            carregarReserves();
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en eliminar reserva: " + e.getMessage());
        }
    }

    private void carregarReserves() {
        reservesList.clear();
        String sql = "SELECT r.id_reserva, r.id_client, r.id_habitacio, r.data_inici, r.data_fi, " +
                     "p.nom, p.cognom, p.document_identitat, h.numero_habitacio " +
                     "FROM reserves r " +
                     "JOIN clients c ON r.id_client = c.id_client " +
                     "JOIN persones p ON c.id_persona = p.id_persona " +
                     "JOIN habitacions h ON r.id_habitacio = h.id_habitacio " +
                     "ORDER BY r.data_inici";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String reserva = rs.getInt("id_reserva") + " - Client: " + 
                               rs.getString("document_identitat") + " (" + 
                               rs.getString("nom") + " " + rs.getString("cognom") + 
                               ") - Habitació: " + rs.getString("numero_habitacio") + 
                               " - " + rs.getString("data_inici") + " a " + 
                               rs.getString("data_fi");
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