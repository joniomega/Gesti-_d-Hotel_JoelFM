/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Empleat extends Persona {
    private int idEmpleat;
    private String llocFeina;
    private String dataContratacio;
    private double salariBrut;
    private String estatLaboral;

    public Empleat(int idPersona, String nom, String cognom, String adreca, String documentIdentitat, String dataNaixement, String telefon, String email, 
                   int idEmpleat, String llocFeina, String dataContratacio, double salariBrut, String estatLaboral) {
        super(idPersona, nom, cognom, adreca, documentIdentitat, dataNaixement, telefon, email);
        this.idEmpleat = idEmpleat;
        this.llocFeina = llocFeina;
        this.dataContratacio = dataContratacio;
        this.salariBrut = salariBrut;
        this.estatLaboral = estatLaboral;
    }

    // Getters i Setters

    public int getIdEmpleat() {
        return idEmpleat;
    }

    public void setIdEmpleat(int idEmpleat) {
        this.idEmpleat = idEmpleat;
    }

    public String getLlocFeina() {
        return llocFeina;
    }

    public void setLlocFeina(String llocFeina) {
        this.llocFeina = llocFeina;
    }

    public String getDataContratacio() {
        return dataContratacio;
    }

    public void setDataContratacio(String dataContratacio) {
        this.dataContratacio = dataContratacio;
    }

    public double getSalariBrut() {
        return salariBrut;
    }

    public void setSalariBrut(double salariBrut) {
        this.salariBrut = salariBrut;
    }

    public String getEstatLaboral() {
        return estatLaboral;
    }

    public void setEstatLaboral(String estatLaboral) {
        this.estatLaboral = estatLaboral;
    }
    
}

