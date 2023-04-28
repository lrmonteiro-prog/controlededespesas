package com.example.controlededespesas.BEAN;

import java.io.Serializable;

public class CusteioReceitaBEAN implements Serializable {

    public Integer id;               // índice
    public String  tipo;             // despesa ou receita
    public String  datapagamento;    // data pagamento
    public String  datavencimento;   // data vencimento
    public String  descricao;        // descrição da despesa ou receita
    public String  categoria;        // categorização da despesa ou receita
    public Float   valor;            // valor da despesa ou receita
    public String  periodo;          // período (mês/ano) da despesa ou receita

    public String getPeriodo() {return periodo;}

    public void setPeriodo(String periodo) {this.periodo = periodo;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDatapagamento() {
        return datapagamento;
    }

    public void setDatapagamento(String datapagamento) {
        this.datapagamento = datapagamento;
    }

    public String getDatavencimento() {
        return datavencimento;
    }

    public void setDatavencimento(String datavencimento) {
        this.datavencimento = datavencimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    @Override
    public String toString(){
        return periodo;
    }
}
