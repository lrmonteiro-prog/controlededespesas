package com.example.controlededespesas.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlededespesas.BEAN.CusteioReceitaCategoriaBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaCategoriaDAO;
import com.example.controlededespesas.R;

public class Activity_alterar_categoria extends AppCompatActivity {

    private EditText categoria;
    private CusteioReceitaCategoriaDAO dao;
    private CusteioReceitaCategoriaBEAN custeioReceitaCategoriaBEAN = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);

        categoria = findViewById(R.id.editCategoria);
        dao = new CusteioReceitaCategoriaDAO(this);

        Intent it = getIntent();
        if(it.hasExtra("custeioReceitaCategoria")){
            custeioReceitaCategoriaBEAN = (CusteioReceitaCategoriaBEAN) it.getSerializableExtra("custeioReceitaCategoria");
            categoria.setText(custeioReceitaCategoriaBEAN.getItem());
        }
    }

    public void salvarAtualizacao(View view) {
        custeioReceitaCategoriaBEAN.setItem(categoria.getText().toString());
        dao.atualizar(custeioReceitaCategoriaBEAN);
        Toast.makeText(this, "Dado foi atualizado ", Toast.LENGTH_SHORT).show();
    }
}