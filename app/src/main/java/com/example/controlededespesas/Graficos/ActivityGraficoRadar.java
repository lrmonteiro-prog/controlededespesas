package com.example.controlededespesas.Graficos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.Listar.ListarCusteioReceitaActivity2;
import com.example.controlededespesas.R;
import com.example.controlededespesas.conexao.Conexao;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ActivityGraficoRadar extends AppCompatActivity {
    private Conexao conexao;
    private SQLiteDatabase custeio;
    String[] cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_radar);

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
        String sql_grafico = "SELECT custeioreceita.categoria,sum(custeioreceita.valor) as valortotal FROM custeioreceita WHERE periodo ="+"'"+periodoGrafico+"'"+"GROUP BY categoria";
        Cursor cursor = custeio.rawQuery(sql_grafico,null);
        List<CusteioReceitaBEAN> graficoCategoria = new ArrayList<>();

        RadarChart radarChart = findViewById(R.id.radarChart);
        ArrayList<RadarEntry> visitante = new ArrayList<>();

        while(cursor.moveToNext()){
            //if(periodoGrafico.equals(cursor.getString(cursor.getColumnIndex("periodo"))));}
            CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
            custeioReceitaBEAN.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
            custeioReceitaBEAN.setValor(Float.valueOf(cursor.getString(cursor.getColumnIndex("valortotal"))));
            graficoCategoria.add(custeioReceitaBEAN);

            visitante.add(new RadarEntry(custeioReceitaBEAN.getValor()));
            cat = new String[]{custeioReceitaBEAN.getCategoria()};
        }
        RadarDataSet radarDataSet = new RadarDataSet(visitante,periodoGrafico);
        radarDataSet.setColor(Color.RED);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.RED);
        radarDataSet.setValueTextSize(14f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);

        String[] labels = {"2104","2015","2016","2017","2018","2019","2020"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(cat));

        radarChart.getDescription().setText(periodoGrafico);
        radarChart.setData(radarData);

        custeio.close();
    }

    public void voltarInclusao(String voltar){
        Intent intent = new Intent(ActivityGraficoRadar.this, ListarCusteioReceitaActivity2.class);
        intent.putExtra("periodomes",voltar);
        startActivity(intent);
    }

}