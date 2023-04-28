package com.example.controlededespesas.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaDAO;
import com.example.controlededespesas.Listar.ListarCusteioReceitaActivity2;
import com.example.controlededespesas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_alterar_dados extends AppCompatActivity {

    private EditText descricao;
    private EditText tipo;
    private TextView datapagamento;
    private EditText categoria;
    private EditText valor;
    private EditText periodo;
    private EditText datavencimento;
    private CusteioReceitaDAO dao;
    private CusteioReceitaBEAN custeioReceitaBEAN = null;

    private EditText data;
    private Calendar calendar;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);

        descricao = findViewById(R.id.editDescricao);
        tipo = findViewById(R.id.editTipo);
        datapagamento = findViewById(R.id.textPagamento);
        categoria = findViewById(R.id.editCategoria);
        valor = findViewById(R.id.editValor);
        periodo = findViewById(R.id.editPeriodo);
        datavencimento = findViewById(R.id.editVencimento);
        dao = new CusteioReceitaDAO(this);

        data = findViewById(R.id.editTextData);
        calendar = Calendar.getInstance();

        Intent it = getIntent();
        if(it.hasExtra("custeioReceita")){
            custeioReceitaBEAN = (CusteioReceitaBEAN) it.getSerializableExtra("custeioReceita");
            tipo.setText(custeioReceitaBEAN.getTipo());
            datapagamento.setText(custeioReceitaBEAN.getDatapagamento());
            datavencimento.setText(custeioReceitaBEAN.getDatavencimento());
            descricao.setText(custeioReceitaBEAN.getDescricao());
            categoria.setText(custeioReceitaBEAN.getCategoria());
            valor.setText(custeioReceitaBEAN.getValor().toString());
            periodo.setText(custeioReceitaBEAN.getPeriodo());
        }
        // início Calendário de Data de Vencimento
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month,dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                datapagamento.setText(format.format(dataSelecionada.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        datapagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        // fim Calendário de Data de Vencimento

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarRepetir();
            }
        });

    }

    public void salvarAtualizacao(View view) {
        custeioReceitaBEAN.setDescricao(descricao.getText().toString());
        custeioReceitaBEAN.setTipo(tipo.getText().toString());
        custeioReceitaBEAN.setDatavencimento(datavencimento.getText().toString());
        custeioReceitaBEAN.setDatapagamento(datapagamento.getText().toString());
        custeioReceitaBEAN.setCategoria(categoria.getText().toString());
        custeioReceitaBEAN.setValor((Float.valueOf(valor.getText().toString())));
        custeioReceitaBEAN.setPeriodo(periodo.getText().toString());
        dao.atualizar(custeioReceitaBEAN);
        Toast.makeText(this, "Dado foi atualizado ", Toast.LENGTH_SHORT).show();
    }

    public void voltarRepetir(){
        Intent intent = new Intent(Activity_alterar_dados.this, ListarCusteioReceitaActivity2.class);
        intent.putExtra("periodomes", custeioReceitaBEAN.getPeriodo());
        startActivity(intent);
    }

}