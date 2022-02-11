/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.sql.Date;

/**
 *
 * @author Fabian
 */
public class EmpleadoDTO {

    private String cod_empleado; //--------
    private String doc_identidad; //-------
    private double ultimo_sueldo; //-------
    private String centro_de_costo;//----------------
    private String cuenta_de_pago; //---------------
    private String nom_em;
    private String correo;

    public EmpleadoDTO() {
    }

    public String getCod_empleado() {
        return cod_empleado;
    }

    public void setCod_empleado(String cod_empleado) {
        this.cod_empleado = cod_empleado;
    }

    public String getDoc_identidad() {
        return doc_identidad;
    }

    public void setDoc_identidad(String doc_identidad) {
        this.doc_identidad = doc_identidad;
    }

    public double getUltimo_sueldo() {
        return ultimo_sueldo;
    }

    public void setUltimo_sueldo(double ultimo_sueldo) {
        this.ultimo_sueldo = ultimo_sueldo;
    }

    public String getCentro_de_costo() {
        return centro_de_costo;
    }

    public void setCentro_de_costo(String centro_de_costo) {
        this.centro_de_costo = centro_de_costo;
    }

    public String getCuenta_de_pago() {
        return cuenta_de_pago;
    }

    public void setCuenta_de_pago(String cuenta_de_pago) {
        this.cuenta_de_pago = cuenta_de_pago;
    }

    public String getNom_em() {
        return nom_em;
    }

    public void setNom_em(String nom_em) {
        this.nom_em = nom_em;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "EmpleadoDTO{" + "cod_empleado=" + cod_empleado + ", doc_identidad=" + doc_identidad + ", ultimo_sueldo=" + ultimo_sueldo + ", centro_de_costo=" + centro_de_costo + ", cuenta_de_pago=" + cuenta_de_pago + ", nom_em=" + nom_em + ", correo=" + correo + '}';
    }

}
