/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Persona {
    private int idPersona;
    private String nom;
    private String cognom;
    private String adreca;
    private String documentIdentitat;
    private String dataNaixement;
    private String telefon;
    private String email;

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public String getDocumentIdentitat() {
        return documentIdentitat;
    }

    public void setDocumentIdentitat(String documentIdentitat) {
        this.documentIdentitat = documentIdentitat;
    }

    public String getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(String dataNaixement) {
        this.dataNaixement = dataNaixement;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Persona(int idPersona, String nom, String cognom, String adreca, String documentIdentitat, String dataNaixement, String telefon, String email) {
        this.idPersona = idPersona;
        this.nom = nom;
        this.cognom = cognom;
        this.adreca = adreca;
        this.documentIdentitat = documentIdentitat;
        this.dataNaixement = dataNaixement;
        this.telefon = telefon;
        this.email = email;
    }

    // Getters i Setters
}
