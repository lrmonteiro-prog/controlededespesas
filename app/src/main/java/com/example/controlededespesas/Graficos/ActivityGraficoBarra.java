package com.example.controlededespesas.Graficos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.R;
import com.example.controlededespesas.conexao.Conexao;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ActivityGraficoBarra extends AppCompatActivity {
    private Conexao conexao;
    private SQLiteDatabase custeio;
    private String anografico;
    private String tipografico;
    private String sql_grafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_barra);

        Intent intent = getIntent();
        anografico = (String) intent.getSerializableExtra("anografico");
        tipografico = (String) intent.getSerializableExtra("tipografico");

//        Toast.makeText(this, "Ano: "+anografico, Toast.LENGTH_LONG).show();
//        Toast.makeText(this, "Tipo: "+tipografico, Toast.LENGTH_LONG).show();

        conexao = new Conexao(this);
        custeio = conexao.getWritableDatabase();
        grafico();
    }

        public void grafico(){
            if(tipografico.equals("Despesa")) {
                sql_grafico = "SELECT custeioreceita.tipo,custeioreceita.periodo,sum(custeioreceita.valor) as" +
                        " valortotal FROM custeioreceita WHERE tipo =" + "'" + "Despesa" + "'" +
                        "GROUP BY periodo";
            }else{
                sql_grafico = "SELECT custeioreceita.tipo,custeioreceita.periodo,sum(custeioreceita.valor) as" +
                        " valortotal FROM custeioreceita WHERE tipo =" + "'" + "Receita" + "'" +
                        "GROUP BY periodo";
            }
            Cursor cursor = custeio.rawQuery(sql_grafico,null);
            List<CusteioReceitaBEAN> graficoCategoria = new ArrayList<>();
            BarChart barChart = findViewById(R.id.barChart);
            ArrayList<BarEntry> visitante = new ArrayList<>();

            while(cursor.moveToNext()){
                CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
                custeioReceitaBEAN.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                custeioReceitaBEAN.setPeriodo(cursor.getString(cursor.getColumnIndex("periodo")));
                custeioReceitaBEAN.setValor(Float.valueOf(cursor.getString(cursor.getColumnIndex("valortotal"))));
                graficoCategoria.add(custeioReceitaBEAN);
                Integer p = Integer.valueOf(custeioReceitaBEAN.getPeriodo().substring(0,2));
                if(custeioReceitaBEAN.getPeriodo().substring(3,7).equals(anografico)) {
                    visitante.add(new BarEntry(p, custeioReceitaBEAN.getValor()));
                }
            }
            BarDataSet barDataSet = new BarDataSet(visitante, "MES");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            BarData barData = new BarData(barDataSet);

            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setText("Despesa Anual");
            barChart.animateY(2000);

            custeio.close();
            cursor.close();
        }
}