/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Reserva {
    private int idReserva;
    private String dataReserva;
    private String dataInici;
    private String dataFi;
    private String tipusReserva;
    private double tipusIVA;
    private double preuTotalReserva;

    public Reserva(int idReserva, String dataReserva, String dataInici, String dataFi, String tipusReserva, double tipusIVA, double preuTotalReserva) {
        this.idReserva = idReserva;
        this.dataReserva = dataReserva;
        this.dataInici = dataInici;
        this.dataFi = dataFi;
        this.tipusReserva = tipusReserva;
        this.tipusIVA = tipusIVA;
        this.preuTotalReserva = preuTotalReserva;
    }

    // Getters i Setters

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getDataInici() {
        return dataInici;
    }

    public void setDataInici(String dataInici) {
        this.dataInici = dataInici;
    }

    public String getDataFi() {
        return dataFi;
    }

    public void setDataFi(String dataFi) {
        this.dataFi = dataFi;
    }

    public String getTipusReserva() {
        return tipusReserva;
    }

    public void setTipusReserva(String tipusReserva) {
        this.tipusReserva = tipusReserva;
    }

    public double getTipusIVA() {
        return tipusIVA;
    }

    public void setTipusIVA(double tipusIVA) {
        this.tipusIVA = tipusIVA;
    }

    public double getPreuTotalReserva() {
        return preuTotalReserva;
    }

    public void setPreuTotalReserva(double preuTotalReserva) {
        this.preuTotalReserva = preuTotalReserva;
    }
    
}

