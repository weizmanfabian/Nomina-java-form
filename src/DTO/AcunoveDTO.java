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
public class AcunoveDTO {

    private String cod_nov;
    private String nom_nov;
    private String cant_nov;
    private double valor_unit;
    private double valor_tot;
    private Date fec_mov;
    private String cc_em;
    private String nom_em;

    public AcunoveDTO() {
    }

    public AcunoveDTO(String cod_nov, String nom_nov, String cant_nov, double valor_unit, double valor_tot, Date fec_mov, String cc_em, String nom_em) {
        this.cod_nov = cod_nov;
        this.nom_nov = nom_nov;
        this.cant_nov = cant_nov;
        this.valor_unit = valor_unit;
        this.valor_tot = valor_tot;
        this.fec_mov = fec_mov;
        this.cc_em = cc_em;
        this.nom_em = nom_em;
    }

    public String getCod_nov() {
        return cod_nov;
    }

    public void setCod_nov(String cod_nov) {
        this.cod_nov = cod_nov;
    }

    public String getNom_nov() {
        return nom_nov;
    }

    public void setNom_nov(String nom_nov) {
        this.nom_nov = nom_nov;
    }

    public String getCant_nov() {
        return cant_nov;
    }

    public void setCant_nov(String cant_nov) {
        this.cant_nov = cant_nov;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public double getValor_tot() {
        return valor_tot;
    }

    public void setValor_tot(double valor_tot) {
        this.valor_tot = valor_tot;
    }

    public Date getFec_mov() {
        return fec_mov;
    }

    public void setFec_mov(Date fec_mov) {
        this.fec_mov = fec_mov;
    }

    public String getCc_em() {
        return cc_em;
    }

    public void setCc_em(String cc_em) {
        this.cc_em = cc_em;
    }

    public String getNom_em() {
        return nom_em;
    }

    public void setNom_em(String nom_em) {
        this.nom_em = nom_em;
    }

    @Override
    public String toString() {
        return "AcunoveDTO{" + "cod_nov=" + cod_nov + ", nom_nov=" + nom_nov + ", cant_nov=" + cant_nov + ", valor_unit=" + valor_unit + ", valor_tot=" + valor_tot + ", fec_mov=" + fec_mov + ", cc_em=" + cc_em + ", nom_em=" + nom_em + '}';
    }

}
