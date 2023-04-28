package com.example.controlededespesas.Graficos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.Listar.ListarCusteioReceitaActivity2;
import com.example.controlededespesas.R;
import com.example.controlededespesas.conexao.Conexao;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ActivityGraficoPizza extends AppCompatActivity {
    private Conexao conexao;
    private SQLiteDatabase custeio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_pizza);

        conexao = new Conexao(this);
        custeio = conexao.getWritableDatabase();

        Intent intent = getIntent();
        String parametro = (String) intent.getSerializableExtra("periodomes");
        grafico(parametro);

        Button btVoltarGrafico = findViewById(R.id.btVoltarGrafico);
        btVoltarGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarInclusao(parametro);
            }
        });

    }

    public void grafico (String periodoGrafico){
        String sql_grafico = "SELECT custeioreceita.tipo,custeioreceita.categoria,sum(custeioreceita.valor) as" +
                             " valortotal FROM custeioreceita WHERE periodo ="+"'"+periodoGrafico+"'"+
                             "GROUP BY categoria";
        Cursor cursor = custeio.rawQuery(sql_grafico,null);
        List<CusteioReceitaBEAN> graficoCategoria = new ArrayList<>();

        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> visitante = new ArrayList<>();

        while(cursor.moveToNext()){
            CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
            custeioReceitaBEAN.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
            custeioReceitaBEAN.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
            custeioReceitaBEAN.setValor(Float.valueOf(cursor.getString(cursor.getColumnIndex("valortotal"))));
            graficoCategoria.add(custeioReceitaBEAN);
            if(custeioReceitaBEAN.getTipo().equals("Despesa")) {
                visitante.add(new PieEntry(custeioReceitaBEAN.getValor(), custeioReceitaBEAN.getCategoria()));
            }
        }
        PieDataSet pieDataSet = new PieDataSet(visitante,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(periodoGrafico);
        pieChart.animate();

        custeio.close();
        cursor.close();
    }

    public void voltarInclusao(String voltar){
        Intent intent = new Intent(ActivityGraficoPizza.this, ListarCusteioReceitaActivity2.class);
        intent.putExtra("periodomes",voltar);
        startActivity(intent);
    }

}