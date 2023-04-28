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

public class Activity_repetir_mes extends AppCompatActivity {

    private TextView vencimentoRepetir;
    private TextView periodoRepetir;
    private TextView pagamentoRepetir;
    private TextView tipoRepetir;
    private EditText descricaoRepetir;
    private EditText categoriaRepetir;
    private EditText valorRepetir;

    private CusteioReceitaDAO dao;
    private CusteioReceitaBEAN custeioReceitaBEAN;

    private EditText data;
    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialogOutra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetir_mes);

        vencimentoRepetir = findViewById(R.id.textVencimentoRepetir);
        periodoRepetir    = findViewById(R.id.textPeriodoRepetir);
        pagamentoRepetir  = findViewById(R.id.textPagamentoRepetir);
        tipoRepetir       = findViewById(R.id.editTipoRepetir);
        descricaoRepetir  = findViewById(R.id.editDescricaoRepetir);
        categoriaRepetir  = findViewById(R.id.editCategoriaRepetir);
        valorRepetir      = findViewById(R.id.editValorRepetir);

        dao = new CusteioReceitaDAO(this);
        calendar = Calendar.getInstance();

        Intent it = getIntent();
        if(it.hasExtra("custeioReceita")){
            custeioReceitaBEAN = (CusteioReceitaBEAN) it.getSerializableExtra("custeioReceita");
            tipoRepetir.setText(custeioReceitaBEAN.getTipo());
            pagamentoRepetir.setText(custeioReceitaBEAN.getDatapagamento());
            vencimentoRepetir.setText(custeioReceitaBEAN.getDatavencimento());
            descricaoRepetir.setText(custeioReceitaBEAN.getDescricao());
            categoriaRepetir.setText(custeioReceitaBEAN.getCategoria());
            valorRepetir.setText(custeioReceitaBEAN.getValor().toString());
            periodoRepetir.setText(custeioReceitaBEAN.getPeriodo());
        }

        // início Calendário de Data de Pagamento
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month,dayOfMonth);
                SimpleDateFormat formatPagamento = new SimpleDateFormat("dd/MM/yyyy");
                pagamentoRepetir.setText(formatPagamento.format(dataSelecionada.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        pagamentoRepetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        // fim Calendário de Data de Pagamento
        // início Calendário de Data de Vencimento
        datePickerDialogOutra = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month,dayOfMonth);
                SimpleDateFormat formatVencimento = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatPeriodo = new SimpleDateFormat("MM/yyyy");
                vencimentoRepetir.setText(formatVencimento.format(dataSelecionada.getTime()));
                periodoRepetir.setText(formatPeriodo.format(dataSelecionada.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        vencimentoRepetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogOutra.show();
            }
        });
        // fim Calendário de Data de Vencimento

        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarRepetir();
            }
        });

    }

    public void salvarRepetirMes(View view){
        CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
        custeioReceitaBEAN.setTipo(tipoRepetir.getText().toString());
        custeioReceitaBEAN.setDatapagamento(pagamentoRepetir.getText().toString());
        custeioReceitaBEAN.setDatavencimento(vencimentoRepetir.getText().toString());
        custeioReceitaBEAN.setDescricao(descricaoRepetir.getText().toString());
        custeioReceitaBEAN.setCategoria(categoriaRepetir.getText().toString());
        custeioReceitaBEAN.setValor(Float.valueOf(valorRepetir.getText().toString()));
        custeioReceitaBEAN.setPeriodo(periodoRepetir.getText().toString());
        long id = dao.inserir(custeioReceitaBEAN);
        Toast.makeText(this, "Dado inserido com id: " + id, Toast.LENGTH_SHORT).show();
    }

    public void voltarRepetir(){
        Intent intent = new Intent(Activity_repetir_mes.this, ListarCusteioReceitaActivity2.class);
        intent.putExtra("periodomes", custeioReceitaBEAN.getPeriodo());
        startActivity(intent);

//        Intent intent = new Intent(this, ListarMesActivity.class);
//        startActivity(intent);
    }

}