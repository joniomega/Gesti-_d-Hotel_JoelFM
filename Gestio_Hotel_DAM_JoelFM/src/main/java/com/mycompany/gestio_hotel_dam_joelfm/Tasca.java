/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Tasca {
    private int idTasca;
    private String descripcio;
    private String dataCreacio;
    private String dataExecucio;
    private String estat;

    public Tasca(int idTasca, String descripcio, String dataCreacio, String dataExecucio, String estat) {
        this.idTasca = idTasca;
        this.descripcio = descripcio;
        this.dataCreacio = dataCreacio;
        this.dataExecucio = dataExecucio;
        this.estat = estat;
    }

    // Getters i Setters

    public int getIdTasca() {
        return idTasca;
    }

    public void setIdTasca(int idTasca) {
        this.idTasca = idTasca;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(String dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public String getDataExecucio() {
        return dataExecucio;
    }

    public void setDataExecucio(String dataExecucio) {
        this.dataExecucio = dataExecucio;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }
    
}

