package com.example.controlededespesas.BEAN;

import java.io.Serializable;

public class CusteioReceitaMesBEAN implements Serializable {

    public Integer idmes;
    public String periodomes;

    public Integer getIdmes() {
        return idmes;
    }

    public void setIdmes(int idmes) {
        this.idmes = idmes;
    }

    public String getPeriodomes() {
        return periodomes;
    }

    public void setPeriodomes(String periodomes) {
        this.periodomes = periodomes;
    }
}
