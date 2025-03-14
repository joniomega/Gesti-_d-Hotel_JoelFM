/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Gestio_Hotel_JoelFM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcio;

        do {
            System.out.println("\n=== MENÚ GESTIÓ HOTEL ===");
            System.out.println("1. Afegir Client");
            System.out.println("2. Afegir Empleat");
            System.out.println("3. Fer Reserva");
            System.out.println("4. Generar Factura");
            System.out.println("5. Gestionar Tasques");
            System.out.println("0. Sortir");
            System.out.print("Tria una opció: ");
            opcio = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcio) {
                case 1:
                    afegirClient(scanner);
                    break;
                case 2:
                    afegirEmpleat(scanner);
                    break;
                case 3:
                    ferReserva(scanner);
                    break;
                case 4:
                    generarFactura(scanner);
                    break;
                case 5:
                    gestionarTasques(scanner);
                    break;
                case 0:
                    System.out.println("Sortint del programa...");
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna-ho a intentar.");
            }
        } while (opcio != 0);

        scanner.close();
    }

    private static void afegirClient(Scanner scanner) {
        System.out.println("\n[📌 Afegir Client]");

        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Cognom: ");
        String cognom = scanner.nextLine();
        System.out.print("Adreça: ");
        String adreca = scanner.nextLine();
        System.out.print("Document Identitat: ");
        String document = scanner.nextLine();
        System.out.print("Data de Naixement (YYYY-MM-DD): ");
        String dataNaixement = scanner.nextLine();
        System.out.print("Telèfon: ");
        String telefon = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Data de Registre (YYYY-MM-DD): ");
        String dataRegistre = scanner.nextLine();
        System.out.print("Tipus Client (Regular/VIP): ");
        String tipusClient = scanner.nextLine();
        System.out.print("Targeta de Crèdit: ");
        String targetaCredit = scanner.nextLine();

        String sql = "INSERT INTO clients (nom, cognom, adreça, document_identitat, data_naixement, telefon, email, data_registre, tipus_client, targeta_credit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, cognom);
            stmt.setString(3, adreca);
            stmt.setString(4, document);
            stmt.setString(5, dataNaixement);
            stmt.setString(6, telefon);
            stmt.setString(7, email);
            stmt.setString(8, dataRegistre);
            stmt.setString(9, tipusClient);
            stmt.setString(10, targetaCredit);

            stmt.executeUpdate();
            System.out.println("✅ Client afegit correctament!");
        } catch (SQLException e) {
            System.out.println("❌ Error en afegir client: " + e.getMessage());
        }
    }

    private static void afegirEmpleat(Scanner scanner) {
        System.out.println("\n[📌 Afegir Empleat]");

        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Cognom: ");
        String cognom = scanner.nextLine();
        System.out.print("Lloc de Treball: ");
        String llocFeina = scanner.nextLine();
        System.out.print("Data de Contractació (YYYY-MM-DD): ");
        String dataContractacio = scanner.nextLine();
        System.out.print("Salari Brut: ");
        double salari = scanner.nextDouble();
        scanner.nextLine(); // Consumir salt de línia
        System.out.print("Estat Laboral (Actiu/Baixa/Permís): ");
        String estatLaboral = scanner.nextLine();

        String sql = "INSERT INTO empleats (nom, cognom, llocFeina, data_contratacio, salariBrut, estat_laboral) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, cognom);
            stmt.setString(3, llocFeina);
            stmt.setString(4, dataContractacio);
            stmt.setDouble(5, salari);
            stmt.setString(6, estatLaboral);

            stmt.executeUpdate();
            System.out.println("✅ Empleat afegit correctament!");
        } catch (SQLException e) {
            System.out.println("❌ Error en afegir empleat: " + e.getMessage());
        }
    }

    private static void ferReserva(Scanner scanner) {
        System.out.println("\n[📌 Fer Reserva]");
        System.out.print("ID del Client: ");
        int idClient = scanner.nextInt();
        System.out.print("ID de l'Habitació: ");
        int idHabitacio = scanner.nextInt();
        scanner.nextLine(); // Consumir salt de línia
        System.out.print("Data Inici (YYYY-MM-DD): ");
        String dataInici = scanner.nextLine();
        System.out.print("Data Fi (YYYY-MM-DD): ");
        String dataFi = scanner.nextLine();

        String sql = "INSERT INTO reserves (id_client, id_habitacio, data_inici, data_fi) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setInt(2, idHabitacio);
            stmt.setString(3, dataInici);
            stmt.setString(4, dataFi);

            stmt.executeUpdate();
            System.out.println("✅ Reserva creada correctament!");
        } catch (SQLException e) {
            System.out.println("❌ Error en fer reserva: " + e.getMessage());
        }
    }

private static void generarFactura(Scanner scanner) {
    System.out.println("\n[📌 Generar Factura]");

    System.out.print("ID de la Reserva: ");
    int idReserva = scanner.nextInt();
    scanner.nextLine(); // Consumir salt de línia

    System.out.print("Import Total (€): ");
    double importTotal = scanner.nextDouble();
    scanner.nextLine(); // Consumir salt de línia

    System.out.print("Data de Pagament (YYYY-MM-DD): ");
    String dataPagament = scanner.nextLine();

    System.out.print("Mètode de Pagament (Targeta/Efectiu/Transferència): ");
    String metodePagament = scanner.nextLine();

    String sql = "INSERT INTO factures (id_reserva, import_total, data_pagament, metode_pagament) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idReserva);
        stmt.setDouble(2, importTotal);
        stmt.setString(3, dataPagament);
        stmt.setString(4, metodePagament);

        stmt.executeUpdate();
        System.out.println("✅ Factura generada correctament!");
    } catch (SQLException e) {
        System.out.println("❌ Error en generar factura: " + e.getMessage());
    }
}
private static void gestionarTasques(Scanner scanner) {
    System.out.println("\n[📌 Gestionar Tasques]");

    System.out.print("ID de l'Empleat: ");
    int idEmpleat = scanner.nextInt();
    scanner.nextLine(); // Consumir salt de línia

    System.out.print("Descripció de la Tasca: ");
    String descripcio = scanner.nextLine();

    System.out.print("Data d'Inici (YYYY-MM-DD): ");
    String dataInici = scanner.nextLine();

    System.out.print("Data de Finalització (YYYY-MM-DD): ");
    String dataFi = scanner.nextLine();

    System.out.print("Estat de la Tasca (Pendent/En curs/Completada): ");
    String estat = scanner.nextLine();

    String sql = "INSERT INTO tasques (id_empleat, descripcio, data_inici, data_fi, estat) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idEmpleat);
        stmt.setString(2, descripcio);
        stmt.setString(3, dataInici);
        stmt.setString(4, dataFi);
        stmt.setString(5, estat);

        stmt.executeUpdate();
        System.out.println("✅ Tasca assignada correctament!");
    } catch (SQLException e) {
        System.out.println("❌ Error en gestionar tasques: " + e.getMessage());
    }
}

}

