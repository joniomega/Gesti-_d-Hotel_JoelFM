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

public class ClientsController {

    @FXML
    private TextField nomField, cognomField, adrecaField, documentIdentitatField, dataNaixementField, telefonField, emailField, dataRegistreField, tipusClientField, targetaCreditField;
    
    @FXML
    private ListView<String> clientsListView;

    private final ObservableList<String> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarClients();
        clientsListView.setItems(clientsList);
    }

    @FXML
    private void afegirClient() {
        // Obtenim els valors dels camps de text
        String nom = nomField.getText();
        String cognom = cognomField.getText();
        String adreca = adrecaField.getText();
        String documentIdentitat = documentIdentitatField.getText();
        String dataNaixement = dataNaixementField.getText();
        String telefon = telefonField.getText();
        String email = emailField.getText();
        String dataRegistre = dataRegistreField.getText();
        String tipusClient = tipusClientField.getText();
        String targetaCredit = targetaCreditField.getText();

        // Validem que tots els camps estiguin omplerts
        if (nom.isEmpty() || cognom.isEmpty() || adreca.isEmpty() || documentIdentitat.isEmpty() || dataNaixement.isEmpty() || telefon.isEmpty() || email.isEmpty() || dataRegistre.isEmpty() || tipusClient.isEmpty() || targetaCredit.isEmpty()) {
            mostrarMissatge("Tots els camps són obligatoris.");
            return;
        }

        // Inserir a la taula persones
        String sqlPersona = "INSERT INTO persones (nom, cognom, adreça, document_identitat, data_naixement, telèfon, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlClient = "INSERT INTO clients (id_persona, data_registre, tipus_client, targeta_credit) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtClient = conn.prepareStatement(sqlClient)) {

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

            // Inserir a clients
            stmtClient.setInt(1, idPersona);
            stmtClient.setString(2, dataRegistre);
            stmtClient.setString(3, tipusClient);
            stmtClient.setString(4, targetaCredit);
            stmtClient.executeUpdate();

            mostrarMissatge("✅ Client afegit correctament!");
            carregarClients(); // Recarregar la llista de clients
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en afegir client: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarClient() {
        String selectedClient = clientsListView.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            mostrarMissatge("Selecciona un client per eliminar.");
            return;
        }

        // Obtenir l'email del client seleccionat
        String[] parts = selectedClient.split(" - ");
        String email = parts[2]; // Suposant que l'email és l'últim camp a la llista

        // Eliminar el client (i la persona associada)
        String sqlDeleteClient = "DELETE FROM clients WHERE id_persona = (SELECT id_persona FROM persones WHERE email = ?)";
        String sqlDeletePersona = "DELETE FROM persones WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtClient = conn.prepareStatement(sqlDeleteClient);
             PreparedStatement stmtPersona = conn.prepareStatement(sqlDeletePersona)) {

            // Eliminar de clients
            stmtClient.setString(1, email);
            stmtClient.executeUpdate();

            // Eliminar de persones
            stmtPersona.setString(1, email);
            stmtPersona.executeUpdate();

            mostrarMissatge("✅ Client eliminat correctament!");
            carregarClients(); // Recarregar la llista de clients
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en eliminar client: " + e.getMessage());
        }
    }

    private void carregarClients() {
        clientsList.clear();
        String sql = "SELECT p.nom, p.cognom, p.email " +
                     "FROM clients c " +
                     "JOIN persones p ON c.id_persona = p.id_persona";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String client = rs.getString("nom") + " " + rs.getString("cognom") + " - " + rs.getString("email");
                clientsList.add(client);
            }
        } catch (SQLException e) {
            mostrarMissatge("❌ Error en carregar clients: " + e.getMessage());
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