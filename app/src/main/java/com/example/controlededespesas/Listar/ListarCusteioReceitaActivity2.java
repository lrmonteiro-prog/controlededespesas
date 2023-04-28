package com.example.controlededespesas.Listar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controlededespesas.Graficos.ActivityGraficoRadar;
import com.example.controlededespesas.Graficos.ActivityGraficoPizza;
import com.example.controlededespesas.Main.Activity_alterar_dados;
import com.example.controlededespesas.Main.Activity_repetir_mes;
import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.Adapter.CusteioReceitaAdapter;
import com.example.controlededespesas.DAO.CusteioReceitaDAO;
import com.example.controlededespesas.Main.MainActivity;
import com.example.controlededespesas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListarCusteioReceitaActivity2 extends AppCompatActivity {

    private ListView listViewMes;
    private CusteioReceitaDAO dao;
    private List<CusteioReceitaBEAN> custeioReceitas;
    private List<CusteioReceitaBEAN> custeioReceitaFiltrados = new ArrayList<>();

    private TextView somaDespesa,somaReceita,saldo;

    private DecimalFormat dinheiro = new DecimalFormat("R$ ###,##0.00");

    private TextView txtFiltro;

    public Spinner menu;
    public TextView textoSpinner;

    public String parametro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_custeio_receita2);

        somaDespesa = findViewById(R.id.textTotalDespesa2);
        somaReceita = findViewById(R.id.textTotalReceita2);
        saldo       = findViewById(R.id.textSaldo2);
        dao = new CusteioReceitaDAO(this);

        listViewMes = findViewById(R.id.listaCuteioReceitaRelatorio);
        custeioReceitas = dao.obterTodos();
        custeioReceitaFiltrados.addAll(custeioReceitas);

        CusteioReceitaAdapter adaptador = new CusteioReceitaAdapter(this, custeioReceitaFiltrados);
        listViewMes.setAdapter(adaptador);
        registerForContextMenu(listViewMes);

        // inicio FILTRO
        txtFiltro = findViewById(R.id.txtFiltro);
        Intent intent = getIntent();
        String parametro = (String) intent.getSerializableExtra("periodomes");
        txtFiltro.setText(parametro);

        procuraCusteioReceita(parametro);

        // início Spinner Categoria
        textoSpinner = findViewById(R.id.textViewCategoria);
        menu = findViewById(R.id.spinner2);
        ArrayAdapter adapter2 = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,dao.obterTodos());
        menu.setAdapter(adapter2);
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                procuraCusteioReceita(parametro);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // fim Spinner Categoria

        Button button9 = findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarMes();
            }
        });
    }


    // inicio: procura registro
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal,menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                procuraCusteioReceita(s);
                return false;
            }
        });
        return true;
    }

    public void procuraCusteioReceita(String periodo){
        custeioReceitaFiltrados.clear();
        float ssDespesa,ttDespesa = 0,ssReceita,ttReceita = 0,ttSaldo;
        for(CusteioReceitaBEAN a : custeioReceitas){
            if(a.getPeriodo().toLowerCase().contains((periodo.toLowerCase()))){
                custeioReceitaFiltrados.add(a);
                // inicios: totalisando os custos
                if(a.getTipo().equals("Despesa")){
                    ssDespesa = a.getValor();
                    ttDespesa = ssDespesa+ttDespesa;
                }else{
                    ssReceita = a.getValor();
                    ttReceita = ssReceita+ttReceita;
                }
            }
        }
        ttSaldo = ttReceita-ttDespesa;
        String totalDespesa = String.valueOf(dinheiro.format(ttDespesa));
        String totalReceita = String.valueOf(dinheiro.format(ttReceita));
        String totalSaldo   = String.valueOf(dinheiro.format(ttSaldo));
        somaDespesa.setText(totalDespesa);
        somaReceita.setText(totalReceita);
        saldo.setText(totalSaldo);
        // fim: totalisando os custos
        listViewMes.invalidateViews();
    }
    // fim: procura registro

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto,menu);
    }

    public void cadastrar(MenuItem item){
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("periodomes",txtFiltro.getText());
        startActivity(it);
    }

    public void excluir(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final CusteioReceitaBEAN custeioReceitaExcluir = custeioReceitaFiltrados.get(menuInfo.position);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.validacao)
                .setMessage("Realmente deseja excluir esse dado:")
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        custeioReceitaFiltrados.remove(custeioReceitaExcluir);
                        custeioReceitas.remove(custeioReceitaExcluir);
                        dao.excluir(custeioReceitaExcluir);
                        listViewMes.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void atualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaBEAN custeioReceitaAtualizar = custeioReceitaFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, Activity_alterar_dados.class);
        it.putExtra("custeioReceita",custeioReceitaAtualizar);
        startActivity(it);
    }

    public void salvarRepetirMes(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaBEAN custeioReceitaRepetir = custeioReceitaFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, Activity_repetir_mes.class);
        it.putExtra("custeioReceita",custeioReceitaRepetir);
        startActivity(it);
    }

    public void voltarMes(){
        Intent intent = new Intent(this, ListarMesActivity.class);
        startActivity(intent);
    }

    public void graficoPizza (MenuItem item) {
        Intent intent = new Intent(ListarCusteioReceitaActivity2.this, ActivityGraficoPizza.class);
        intent.putExtra("periodomes",txtFiltro.getText());
        startActivity(intent);
    }

    public void relatorioCategoria (MenuItem item) {
        Intent intent = new Intent(ListarCusteioReceitaActivity2.this, ListarCusteioReceitaRelatorioActivity.class);
        intent.putExtra("periodomes",txtFiltro.getText());
        startActivity(intent);
    }



    public void graficoBarra (MenuItem item) {
        Intent intent = new Intent(ListarCusteioReceitaActivity2.this, ActivityGraficoRadar.class);
        intent.putExtra("periodomes",txtFiltro.getText());
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        custeioReceitas = dao.obterTodos();
        custeioReceitaFiltrados.clear();
        custeioReceitaFiltrados.addAll(custeioReceitas);
        listViewMes.invalidateViews();
    }
}