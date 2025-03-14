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

public class TasquesController {

    @FXML
    private TextField idEmpleatField, descripcioField, dataCreacioField, dataExecucioField;
    @FXML
    private ComboBox<String> estatComboBox;
    
    @FXML
    private ListView<String> tasquesListView;

    private final ObservableList<String> tasquesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inicialitzar el ComboBox amb les opcions d'estat
        estatComboBox.getItems().addAll("Pendent", "Completada");
        estatComboBox.setValue("Pendent"); // Valor per defecte

        carregarTasques();
        tasquesListView.setItems(tasquesList);
    }

    @FXML
    private void afegirTasca() {
        // Obtenim els valors dels camps de text
        int idEmpleat = Integer.parseInt(idEmpleatField.getText());
        String descripcio = descripcioField.getText();
        String dataCreacio = dataCreacioField.getText();
        String dataExecucio = dataExecucioField.getText();
        String estat = estatComboBox.getValue();

        // Validem que tots els camps estiguin omplerts
        if (descripcio.isEmpty() || dataCreacio.isEmpty() || dataExecucio.isEmpty()) {
            mostrarMissatge("Tots els camps són obligatoris.");
            return;
        }

        // Inserir a la taula tasques
        String sqlTasca = "INSERT INTO tasques (descripcio, data_creacio, data_ejecucio, estat) VALUES (?, ?, ?, ?)";
        String sqlEmpleatTasca = "INSERT INTO empleat_tasca (id_empleat, id_tasca, estat) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtTasca = conn.prepareStatement(sqlTasca, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtEmpleatTasca = conn.prepareStatement(sqlEmpleatTasca)) {

            // Inserir a tasques
            stmtTasca.setString(1, descripcio);
            stmtTasca.setString(2, dataCreacio);
            stmtTasca.setString(3, dataExecucio);
            stmtTasca.setString(4, estat);
            stmtTasca.executeUpdate();

            // Obtenir l'id_tasca generat
            ResultSet generatedKeys = stmtTasca.getGeneratedKeys();
            int idTasca = -1;
            if (generatedKeys.next()) {
                idTasca = generatedKeys.getInt(1);
            }

            // Inserir a empleat_tasca
            stmtEmpleatTasca.setInt(1, idEmpleat);
            stmtEmpleatTasca.setInt(2, idTasca);
            stmtEmpleatTasca.setString(3, estat); // Estat inicial
            stmtEmpleatTasca.executeUpdate();

            mostrarMissatge("✅ Tasca afegida correctament!");
            carregarTasques(); // Recarregar la llista de tasques
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en afegir tasca: " + e.getMessage());
        }
    }

    @FXML
    private void modificarEstatTasca() {
        String selectedTasca = tasquesListView.getSelectionModel().getSelectedItem();
        if (selectedTasca == null) {
            mostrarMissatge("Selecciona una tasca per modificar l'estat.");
            return;
        }

        // Obtenir l'ID de la tasca seleccionada
        String[] parts = selectedTasca.split(" - ");
        int idTasca = Integer.parseInt(parts[0]);

        // Demanar el nou estat
        String nouEstat = estatComboBox.getValue();
        if (nouEstat.isEmpty()) {
            mostrarMissatge("Introdueix el nou estat de la tasca.");
            return;
        }

        // Actualitzar l'estat de la tasca a la taula tasques
        String sqlTasca = "UPDATE tasques SET estat = ? WHERE id_tasca = ?";
        // Actualitzar l'estat de la tasca a la taula empleat_tasca
        String sqlEmpleatTasca = "UPDATE empleat_tasca SET estat = ? WHERE id_tasca = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtTasca = conn.prepareStatement(sqlTasca);
             PreparedStatement stmtEmpleatTasca = conn.prepareStatement(sqlEmpleatTasca)) {

            // Actualitzar a tasques
            stmtTasca.setString(1, nouEstat);
            stmtTasca.setInt(2, idTasca);
            stmtTasca.executeUpdate();

            // Actualitzar a empleat_tasca
            stmtEmpleatTasca.setString(1, nouEstat);
            stmtEmpleatTasca.setInt(2, idTasca);
            stmtEmpleatTasca.executeUpdate();

            mostrarMissatge("✅ Estat de la tasca actualitzat correctament!");
            carregarTasques(); // Recarregar la llista de tasques
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en modificar l'estat de la tasca: " + e.getMessage());
        }
    }

    private void carregarTasques() {
        tasquesList.clear();
        String sql = "SELECT t.id_tasca, t.descripcio, t.data_creacio, t.data_ejecucio, t.estat, p.nom, p.cognom " +
                     "FROM tasques t " +
                     "JOIN empleat_tasca et ON t.id_tasca = et.id_tasca " +
                     "JOIN empleats e ON et.id_empleat = e.id_empleat " +
                     "JOIN persones p ON e.id_persona = p.id_persona " +
                     "ORDER BY t.data_creacio";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tasca = rs.getInt("id_tasca") + " - " + rs.getString("descripcio") + " - " +
                               rs.getString("data_creacio") + " a " + rs.getString("data_ejecucio") + " - " +
                               rs.getString("estat") + " - Assignada a: " + rs.getString("nom") + " " + rs.getString("cognom");
                tasquesList.add(tasca);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar tasques: " + e.getMessage());
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