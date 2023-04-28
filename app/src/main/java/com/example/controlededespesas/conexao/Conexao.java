// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.conexao;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexao extends SQLiteOpenHelper {
    private static final String name = "custeio.bd";
    private static final int version = 8;
    public Conexao(@Nullable Context context) {
        super(context, name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas
        String sql_custeioreceitacategoria = "create table custeioreceitacategoria(iditem integer primary key autoincrement," +
                " item varchar(25)unique)";

        String sql_custeioreceitames = "create table custeioreceitames(idmes integer primary key autoincrement," +
                " periodomes varchar(7)unique)";

        String sql_custeioreceita = "create table custeioreceita(id integer primary key autoincrement," +
                " tipo varchar(7),datapagamento varchar(10),datavencimento varchar(10)," +
                " descricao varchar(150),categoria varchar(50), valor float, periodo varchar(7))";

        db.execSQL(sql_custeioreceitacategoria);
        db.execSQL(sql_custeioreceitames);
        db.execSQL(sql_custeioreceita);

        // inserir dados preliminares na tabela categoria
        String[] categoria = {"Sem categoria","Crédito","Aplicação Financeira","Combustível","Alimentação","Contas domésticas",
                "Educação","Financiamento","Internet/comunicação","Lazer","Mercado","Roupa/beleza",
                "Saúde","Transporte","Veículo-pessoal","Uber/Taxi","Doação","Crédito Mês anterior",
                "Débito Mês anterior"};
        for(int i=0; i<categoria.length;i++){
            ContentValues item = new ContentValues();
            item.put("item",categoria[i]);
            db.insert("custeioreceitacategoria",null,item);
        }
    }

    // desconectar
    public static void DesconnectDb() {
        Connection conn = null;
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {}
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
