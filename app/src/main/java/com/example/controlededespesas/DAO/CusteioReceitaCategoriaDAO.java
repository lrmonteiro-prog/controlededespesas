// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlededespesas.BEAN.CusteioReceitaCategoriaBEAN;
import com.example.controlededespesas.conexao.Conexao;

import java.util.ArrayList;
import java.util.List;

public class CusteioReceitaCategoriaDAO {

    private Conexao conexao;
    private SQLiteDatabase custeio;

    public CusteioReceitaCategoriaDAO(Context context){
        conexao = new Conexao(context);
        custeio = conexao.getWritableDatabase();
    }

    public long inserir (CusteioReceitaCategoriaBEAN custeioReceitaCategoriaBEAN){
        ContentValues values = new ContentValues();
        values.put("item", custeioReceitaCategoriaBEAN.getItem());
        return custeio.insert("custeioreceitacategoria",null, values);
    }

    public List<CusteioReceitaCategoriaBEAN> obterTodosCategoria(){
        String sql ="select * from custeioreceitacategoria order by item asc;";
        List<CusteioReceitaCategoriaBEAN> custeioReceitasCategoria = new ArrayList<>();
        Cursor cursor = custeio.rawQuery(sql,null);
        while (cursor.moveToNext()){
            CusteioReceitaCategoriaBEAN c = new CusteioReceitaCategoriaBEAN();
            c.setIditem(cursor.getInt(0));
            c.setItem(cursor.getString(1));
            custeioReceitasCategoria.add(c);
        }
        return custeioReceitasCategoria;
    }

    public void excluir(CusteioReceitaCategoriaBEAN custeioReceitaCategoriaBEAN){
        custeio.delete("custeioreceitacategoria","iditem=?",new String[]{custeioReceitaCategoriaBEAN.getIditem().toString()});
    }

    public void atualizar(CusteioReceitaCategoriaBEAN custeioReceitaCategoriaBEAN){
        ContentValues values = new ContentValues();
        values.put("item", custeioReceitaCategoriaBEAN.getItem());
        custeio.update("custeioreceitacategoria",values,"iditem=?",new String[]{custeioReceitaCategoriaBEAN.getIditem().toString()});
    }

}
