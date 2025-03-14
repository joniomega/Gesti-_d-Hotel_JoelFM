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

public class EmpleatsController {

    @FXML
    private TextField nomField, cognomField, adrecaField, documentIdentitatField, dataNaixementField, telefonField, emailField, llocFeinaField, dataContratacioField, salariBrutField, estatLaboralField;
    
    @FXML
    private ListView<String> empleatsListView;

    private final ObservableList<String> empleatsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarEmpleats();
        empleatsListView.setItems(empleatsList);
    }

    @FXML
    private void afegirEmpleat() {
        // Obtenim els valors dels camps de text
        String nom = nomField.getText();
        String cognom = cognomField.getText();
        String adreca = adrecaField.getText();
        String documentIdentitat = documentIdentitatField.getText();
        String dataNaixement = dataNaixementField.getText();
        String telefon = telefonField.getText();
        String email = emailField.getText();
        String llocFeina = llocFeinaField.getText();
        String dataContratacio = dataContratacioField.getText();
        double salariBrut = Double.parseDouble(salariBrutField.getText());
        String estatLaboral = estatLaboralField.getText();

        // Validem que tots els camps estiguin omplerts
        if (nom.isEmpty() || cognom.isEmpty() || adreca.isEmpty() || documentIdentitat.isEmpty() || dataNaixement.isEmpty() || telefon.isEmpty() || email.isEmpty() || llocFeina.isEmpty() || dataContratacio.isEmpty() || estatLaboral.isEmpty()) {
            mostrarMissatge("Tots els camps són obligatoris.");
            return;
        }

        // Inserir a la taula persones
        String sqlPersona = "INSERT INTO persones (nom, cognom, adreça, document_identitat, data_naixement, telèfon, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlEmpleat = "INSERT INTO empleats (id_persona, llocFeina, data_contratacio, salariBrut, estat_laboral) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtEmpleat = conn.prepareStatement(sqlEmpleat)) {

            // Inserir a persones
            stmtPersona.setString(1, nom);
            stmtPersona.setString(2, cognom);
            stmtPersona.setString(3, adreca);
            stmtPersona.setString(4, documentIdentitat);
            stmtPersona.setString(5, dataNaixement);
            stmtPersona.setString(6, telefon);
            stmtPersona.setString(7, email);
            stmtPersona.executeUpdate();

            // Obtenir l'id_persona generat
            ResultSet generatedKeys = stmtPersona.getGeneratedKeys();
            int idPersona = -1;
            if (generatedKeys.next()) {
                idPersona = generatedKeys.getInt(1);
            }

            // Inserir a empleats
            stmtEmpleat.setInt(1, idPersona);
            stmtEmpleat.setString(2, llocFeina);
            stmtEmpleat.setString(3, dataContratacio);
            stmtEmpleat.setDouble(4, salariBrut);
            stmtEmpleat.setString(5, estatLaboral);
            stmtEmpleat.executeUpdate();

            mostrarMissatge("✅ Empleat afegit correctament!");
            carregarEmpleats(); // Recarregar la llista d'empleats
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en afegir empleat: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarEmpleat() {
        String selectedEmpleat = empleatsListView.getSelectionModel().getSelectedItem();
        if (selectedEmpleat == null) {
            mostrarMissatge("Selecciona un empleat per eliminar.");
            return;
        }

        // Obtenir l'email de l'empleat seleccionat
        String[] parts = selectedEmpleat.split(" - ");
        String email = parts[2]; // Suposant que l'email és l'últim camp a la llista

        // Eliminar l'empleat (i la persona associada)
        String sqlDeleteEmpleat = "DELETE FROM empleats WHERE id_persona = (SELECT id_persona FROM persones WHERE email = ?)";
        String sqlDeletePersona = "DELETE FROM persones WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtEmpleat = conn.prepareStatement(sqlDeleteEmpleat);
             PreparedStatement stmtPersona = conn.prepareStatement(sqlDeletePersona)) {

            // Eliminar de empleats
            stmtEmpleat.setString(1, email);
            stmtEmpleat.executeUpdate();

            // Eliminar de persones
            stmtPersona.setString(1, email);
            stmtPersona.executeUpdate();

            mostrarMissatge("✅ Empleat eliminat correctament!");
            carregarEmpleats(); // Recarregar la llista d'empleats
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en eliminar empleat: " + e.getMessage());
        }
    }

    private void carregarEmpleats() {
        empleatsList.clear();
        String sql = "SELECT p.nom, p.cognom, p.email, e.llocFeina " +
                     "FROM empleats e " +
                     "JOIN persones p ON e.id_persona = p.id_persona";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String empleat = rs.getString("nom") + " " + rs.getString("cognom") + " - " + rs.getString("email") + " - " + rs.getString("llocFeina");
                empleatsList.add(empleat);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar empleats: " + e.getMessage());
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