/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Habitacio {
    private int idHabitacio;
    private int numeroHabitacio;
    private String tipus;
    private int capacitat;
    private double preuNitAD;
    private double preuNitMP;
    private String estat;
    private String descripcio;

    public Habitacio(int idHabitacio, int numeroHabitacio, String tipus, int capacitat, double preuNitAD, double preuNitMP, String estat, String descripcio) {
        this.idHabitacio = idHabitacio;
        this.numeroHabitacio = numeroHabitacio;
        this.tipus = tipus;
        this.capacitat = capacitat;
        this.preuNitAD = preuNitAD;
        this.preuNitMP = preuNitMP;
        this.estat = estat;
        this.descripcio = descripcio;
    }

    // Getters i Setters

    public int getIdHabitacio() {
        return idHabitacio;
    }

    public void setIdHabitacio(int idHabitacio) {
        this.idHabitacio = idHabitacio;
    }

    public int getNumeroHabitacio() {
        return numeroHabitacio;
    }

    public void setNumeroHabitacio(int numeroHabitacio) {
        this.numeroHabitacio = numeroHabitacio;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public int getCapacitat() {
        return capacitat;
    }

    public void setCapacitat(int capacitat) {
        this.capacitat = capacitat;
    }

    public double getPreuNitAD() {
        return preuNitAD;
    }

    public void setPreuNitAD(double preuNitAD) {
        this.preuNitAD = preuNitAD;
    }

    public double getPreuNitMP() {
        return preuNitMP;
    }

    public void setPreuNitMP(double preuNitMP) {
        this.preuNitMP = preuNitMP;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
    
}

