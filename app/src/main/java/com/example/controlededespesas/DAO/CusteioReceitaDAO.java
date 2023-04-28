// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.conexao.Conexao;
import java.util.ArrayList;
import java.util.List;

public class CusteioReceitaDAO {

    private Conexao conexao;
    private SQLiteDatabase custeio;

    public CusteioReceitaDAO (Context context){
        conexao = new Conexao(context);
        custeio = conexao.getWritableDatabase();
    }

    public long inserir (CusteioReceitaBEAN custeioReceitaBEAN){
        ContentValues values = new ContentValues();
        values.put("tipo", custeioReceitaBEAN.getTipo());
        values.put("datapagamento", custeioReceitaBEAN.getDatapagamento());
        values.put("datavencimento", custeioReceitaBEAN.getDatavencimento());
        values.put("descricao", custeioReceitaBEAN.getDescricao());
        values.put("categoria", custeioReceitaBEAN.getCategoria());
        values.put("valor", custeioReceitaBEAN.getValor());
        values.put("periodo", custeioReceitaBEAN.getPeriodo());
        return custeio.insert("custeioreceita",null, values);
    }

    public List<CusteioReceitaBEAN> obterTodos(){
        float somar,somatorio = 0;
        List<CusteioReceitaBEAN> custeioReceitas = new ArrayList<>();
        Cursor cursor = custeio.query("custeioreceita", new String[]{"id","tipo",
                        "datapagamento","datavencimento","descricao","categoria",
                        "valor","periodo"},null,null,null,
                        null,null);
        while (cursor.moveToNext()){
            CusteioReceitaBEAN c = new CusteioReceitaBEAN();
            c.setId(cursor.getInt(0));
            c.setTipo(cursor.getString(1));
            c.setDatapagamento(cursor.getString(2));
            c.setDatavencimento(cursor.getString(3));
            c.setDescricao(cursor.getString(4));
            c.setCategoria(cursor.getString(5));
            c.setValor(cursor.getFloat(6));
            c.setPeriodo(cursor.getString(7));
            custeioReceitas.add(c);
            somar = c.getValor();
            somatorio = somar+somatorio;
        }
        return custeioReceitas;
    }

    public void excluir(CusteioReceitaBEAN custeioReceitaBEAN){
        custeio.delete("custeioreceita","id=?",new String[]{custeioReceitaBEAN.getId().toString()});
    }

    public void atualizar(CusteioReceitaBEAN custeioReceitaBEAN){
        ContentValues values = new ContentValues();
        values.put("tipo", custeioReceitaBEAN.getTipo());
        values.put("datapagamento", custeioReceitaBEAN.getDatapagamento());
        values.put("datavencimento", custeioReceitaBEAN.getDatavencimento());
        values.put("descricao", custeioReceitaBEAN.getDescricao());
        values.put("categoria", custeioReceitaBEAN.getCategoria());
        values.put("valor", custeioReceitaBEAN.getValor());
        values.put("periodo", custeioReceitaBEAN.getPeriodo());
        custeio.update("custeioreceita",values,"id=?",new String[]{custeioReceitaBEAN.getId().toString()});
    }

}
