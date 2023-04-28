package com.example.controlededespesas.Listar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controlededespesas.Adapter.CusteioReceitaRelatorioAdapter;
import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaDAO;
import com.example.controlededespesas.R;
import com.example.controlededespesas.conexao.Conexao;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListarCusteioReceitaRelatorioActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textPeriodo;
    List<CusteioReceitaBEAN> relatorio = new ArrayList<>();

    private TextView somaDespesa,somaReceita,saldo;
    private DecimalFormat dinheiro = new DecimalFormat("R$ ###,##0.00");

    private Conexao conexao;
    private SQLiteDatabase custeio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_custeio_receita_relatorio);

        conexao = new Conexao(this);
        custeio = conexao.getWritableDatabase();

        Intent intent = getIntent();
        String parametro = (String) intent.getSerializableExtra("periodomes");

        listView = findViewById(R.id.listaCuteioReceitaRelatorio);
        textPeriodo = findViewById(R.id.textPeriodo);
        textPeriodo.setText(parametro);

        somaDespesa = findViewById(R.id.textTotalDespesa4);
        somaReceita = findViewById(R.id.textTotalReceita4);
        saldo       = findViewById(R.id.textSaldo4);

        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarRepetir();
            }
        });
        relatorioCategoria(parametro);
    }

    private void relatorioCategoria(String parametro) {
           float Despesa = 0,Receita = 0,Saldototal = 0;
            String sql_grafico = "SELECT custeioreceita.id,custeioreceita.tipo," +
                    "custeioreceita.categoria,sum(custeioreceita.valor) as" +
                    " valortotal FROM custeioreceita WHERE periodo ="+"'"+parametro+"'"+
                    "GROUP BY categoria ORDER BY valortotal DESC";
            Cursor cursor = custeio.rawQuery(sql_grafico,null);
            while(cursor.moveToNext()){
                    CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
                    custeioReceitaBEAN.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    custeioReceitaBEAN.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                    custeioReceitaBEAN.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
                    custeioReceitaBEAN.setValor(Float.valueOf(cursor.getString(cursor.getColumnIndex("valortotal"))));
                if(custeioReceitaBEAN.getTipo().equals("Despesa")) {
                    relatorio.add(custeioReceitaBEAN);
                    Despesa = custeioReceitaBEAN.getValor() + Despesa;
                }else {
                    Receita = custeioReceitaBEAN.getValor() + Receita;
                }
            }
            CusteioReceitaRelatorioAdapter adaptador = new CusteioReceitaRelatorioAdapter(this, relatorio);
            listView.setAdapter(adaptador);
            Saldototal = Receita-Despesa;
            somaReceita.setText(dinheiro.format(Receita));
            somaDespesa.setText(dinheiro.format(Despesa));
            saldo.setText(dinheiro.format(Saldototal));
            registerForContextMenu(listView);
            custeio.close();
            cursor.close();
    }

    public void voltarRepetir(){
        Intent intent = new Intent(ListarCusteioReceitaRelatorioActivity.this, ListarCusteioReceitaActivity2.class);
        intent.putExtra("periodomes", textPeriodo.getText());
        startActivity(intent);
    }
}