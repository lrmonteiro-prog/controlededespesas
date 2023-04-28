package com.example.controlededespesas.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlededespesas.BEAN.CusteioReceitaMesBEAN;
import com.example.controlededespesas.conexao.Conexao;

import java.util.ArrayList;
import java.util.List;

public class CusteioReceitaMesDAO {

    private Conexao conexao;
    private SQLiteDatabase custeio;

    public CusteioReceitaMesDAO (Context context){
        conexao = new Conexao(context);
        custeio = conexao.getWritableDatabase();
    }

    public long inserir (CusteioReceitaMesBEAN custeioReceitaMesBEAN){
        ContentValues values = new ContentValues();
        values.put("periodomes", custeioReceitaMesBEAN.getPeriodomes());
        return custeio.insert("custeioreceitames",null, values);
    }

    public List<CusteioReceitaMesBEAN> obterTodosMes(){
        String sql ="select * from custeioreceitames order by periodomes desc;";
        List<CusteioReceitaMesBEAN> custeioReceitasMes = new ArrayList<>();
        Cursor cursor = custeio.rawQuery(sql,null);
        while (cursor.moveToNext()){
            CusteioReceitaMesBEAN c = new CusteioReceitaMesBEAN();
            c.setIdmes(cursor.getInt(0));
            c.setPeriodomes(cursor.getString(1));
            custeioReceitasMes.add(c);
        }
        return custeioReceitasMes;
    }

    public void excluir(CusteioReceitaMesBEAN custeioReceitaMesBEAN){
        custeio.delete("custeioreceitames","idmes=?",new String[]{custeioReceitaMesBEAN.getIdmes().toString()});
    }

    public void atualizar(CusteioReceitaMesBEAN custeioReceitaMesBEAN){
        ContentValues values = new ContentValues();
        values.put("periodomes", custeioReceitaMesBEAN.getPeriodomes());
        custeio.update("custeioreceitames",values,"idmes=?",new String[]{custeioReceitaMesBEAN.getIdmes().toString()});
    }

}
