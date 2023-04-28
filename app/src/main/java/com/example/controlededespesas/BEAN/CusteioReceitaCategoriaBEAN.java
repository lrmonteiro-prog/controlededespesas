// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.BEAN;

import java.io.Serializable;

public class CusteioReceitaCategoriaBEAN implements Serializable {

    private Integer iditem;
    private String item;

    public Integer getIditem() { return iditem; }

    public void setIditem(Integer iditem) { this.iditem = iditem; }

    public String getItem() { return item; }

    public void setItem(String item) { this.item = item; }

    @Override
    public String toString(){
        return item;
    }
}
