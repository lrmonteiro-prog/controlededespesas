package com.example.controlededespesas.Graficos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaDAO;
import com.example.controlededespesas.R;
import com.example.controlededespesas.conexao.Conexao;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityGraficoLinha extends AppCompatActivity {

    private Conexao conexao;
    private SQLiteDatabase custeio;
    private String anografico;
    private String sql_grafico;

    XYPlot plot;
    List <Number>listValoresDespesa= new ArrayList<>();
    List <Number>listValoresReceita= new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_linha);
        plot = findViewById(R.id.plot);

        Intent intent = getIntent();
        anografico = (String) intent.getSerializableExtra("anografico");

        conexao = new Conexao(this);
        custeio = conexao.getWritableDatabase();

        sql_grafico = "SELECT custeioreceita.tipo,custeioreceita.periodo,sum(custeioreceita.valor) as" +
                " valortotal FROM custeioreceita  GROUP BY tipo,periodo";

        Cursor cursor = custeio.rawQuery(sql_grafico, null);
        List<CusteioReceitaBEAN> graficoCategoria = new ArrayList<>();

        while (cursor.moveToNext()) {
            CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
            custeioReceitaBEAN.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
            custeioReceitaBEAN.setPeriodo(cursor.getString(cursor.getColumnIndex("periodo")));
            custeioReceitaBEAN.setValor(Float.valueOf(cursor.getString(cursor.getColumnIndex("valortotal"))));
            graficoCategoria.add(custeioReceitaBEAN);
            if (custeioReceitaBEAN.getPeriodo().substring(3, 7).equals(anografico)) {
                if (custeioReceitaBEAN.getTipo().equals("Despesa")) {
                    listValoresDespesa.add(custeioReceitaBEAN.getValor());
                }else{
                    listValoresReceita.add(custeioReceitaBEAN.getValor());
                }
            }
        }

        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 11, 12};
        Number[] arrayDespesa = listValoresDespesa.toArray(new Number[0]);
        Number[] arrayReceita = listValoresReceita.toArray(new Number[0]);

        XYSeries series1 = new SimpleXYSeries(Arrays.asList(arrayDespesa),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Despesa");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(arrayReceita),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Receita");


        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.BLACK,
                null,null);
        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.BLUE, Color.BLACK,
                null, null);

        series1Format.setInterpolationParams(new CatmullRomInterpolator.Params(10,
                CatmullRomInterpolator.Type.Centripetal));
        series2Format.setInterpolationParams(new CatmullRomInterpolator.Params(10,
                CatmullRomInterpolator.Type.Centripetal));

        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        PanZoom.attach(plot);
    }
}