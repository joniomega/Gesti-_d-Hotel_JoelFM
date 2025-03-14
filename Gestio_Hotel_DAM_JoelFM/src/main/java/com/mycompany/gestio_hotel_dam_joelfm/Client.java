/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Client extends Persona {
    private int idClient;
    private String dataRegistre;
    private String tipusClient;
    private String targetaCredit;

    public Client(int idPersona, String nom, String cognom, String adreca, String documentIdentitat, String dataNaixement, String telefon, String email, 
                  int idClient, String dataRegistre, String tipusClient, String targetaCredit) {
        super(idPersona, nom, cognom, adreca, documentIdentitat, dataNaixement, telefon, email);
        this.idClient = idClient;
        this.dataRegistre = dataRegistre;
        this.tipusClient = tipusClient;
        this.targetaCredit = targetaCredit;
    }

    // Getters i Setters

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getDataRegistre() {
        return dataRegistre;
    }

    public void setDataRegistre(String dataRegistre) {
        this.dataRegistre = dataRegistre;
    }

    public String getTipusClient() {
        return tipusClient;
    }

    public void setTipusClient(String tipusClient) {
        this.tipusClient = tipusClient;
    }

    public String getTargetaCredit() {
        return targetaCredit;
    }

    public void setTargetaCredit(String targetaCredit) {
        this.targetaCredit = targetaCredit;
    }
    
}
