package com.example.controlededespesas.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlededespesas.BEAN.CusteioReceitaMesBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaMesDAO;
import com.example.controlededespesas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivityMes extends AppCompatActivity {

    public TextView periodomes;
    private CusteioReceitaMesDAO dao;

    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialogOutra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mes);

        periodomes = findViewById(R.id.txtCategoria);
        dao = new CusteioReceitaMesDAO(this);

        calendar = Calendar.getInstance();

        // início Calendário
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month,dayOfMonth);
                SimpleDateFormat formatPagamento = new SimpleDateFormat("MM/yyyy");
                periodomes.setText(formatPagamento.format(dataSelecionada.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        periodomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });
        // fim Calendário
    }

    public void salvarPeriodoMes (View view) {
        CusteioReceitaMesBEAN custeioReceitaMesBEAN = new CusteioReceitaMesBEAN();
        custeioReceitaMesBEAN.setPeriodomes(periodomes.getText().toString());
            long idmes = dao.inserir(custeioReceitaMesBEAN);
            if(idmes>0) {
                Toast.makeText(this, "Dado inserido com id: " + idmes, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Não INCLUÍDO, Dado existente!!!" , Toast.LENGTH_LONG).show();
            }
    }

}