/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
public class Factura {
    private int idFactura;
    private String dataEmisio;
    private String metodePagament;
    private double baseImposable;
    private double iva;
    private double total;

    public Factura(int idFactura, String dataEmisio, String metodePagament, double baseImposable, double iva, double total) {
        this.idFactura = idFactura;
        this.dataEmisio = dataEmisio;
        this.metodePagament = metodePagament;
        this.baseImposable = baseImposable;
        this.iva = iva;
        this.total = total;
    }

    // Getters i Setters

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getDataEmisio() {
        return dataEmisio;
    }

    public void setDataEmisio(String dataEmisio) {
        this.dataEmisio = dataEmisio;
    }

    public String getMetodePagament() {
        return metodePagament;
    }

    public void setMetodePagament(String metodePagament) {
        this.metodePagament = metodePagament;
    }

    public double getBaseImposable() {
        return baseImposable;
    }

    public void setBaseImposable(double baseImposable) {
        this.baseImposable = baseImposable;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
}

