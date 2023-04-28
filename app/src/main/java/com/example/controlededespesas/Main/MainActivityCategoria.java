package com.example.controlededespesas.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlededespesas.BEAN.CusteioReceitaCategoriaBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaCategoriaDAO;
import com.example.controlededespesas.R;

public class MainActivityCategoria extends AppCompatActivity {

    public EditText item;
    private CusteioReceitaCategoriaDAO dao;
    private Button btCadastrarCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categoria);

        item = findViewById(R.id.editCategoria);
        dao = new CusteioReceitaCategoriaDAO(this);

        btCadastrarCategoria = findViewById(R.id.btCadastrarCategoria);

        btCadastrarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarItemCategoria();
            }
        });

    }

    public void salvarItemCategoria () {
        CusteioReceitaCategoriaBEAN custeioReceitaCategoriaBEAN = new CusteioReceitaCategoriaBEAN();
        custeioReceitaCategoriaBEAN.setItem(item.getText().toString());
            long idmes = dao.inserir(custeioReceitaCategoriaBEAN);
            if(idmes>0) {
                Toast.makeText(this, "Dado inserido com id: " + idmes, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Não INCLUÍDO, Dado existente!!!" , Toast.LENGTH_LONG).show();
            }
    }

}