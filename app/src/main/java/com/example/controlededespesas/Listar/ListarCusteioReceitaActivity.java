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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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

public class ListarCusteioReceitaActivity extends AppCompatActivity {

    private ListView listView;
    private CusteioReceitaDAO dao;
    private List<CusteioReceitaBEAN> custeioReceitas;
    private List<CusteioReceitaBEAN> custeioReceitaFiltrados = new ArrayList<>();

    private TextView somaDespesa,somaReceita,saldo;
    private float sDespesa,tDespesa,sReceita,tReceita,tSaldo = 0;

    private DecimalFormat dinheiro = new DecimalFormat("R$ ###,##0.00");

    private TextView periodomes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_custeio_receita);

        somaDespesa = findViewById(R.id.textTotalDespesa);
        somaReceita = findViewById(R.id.textTotalReceita);
        saldo       = findViewById(R.id.textSaldo);

        listView = findViewById(R.id.listaCuteioReceita);
        dao = new CusteioReceitaDAO(this);
        custeioReceitas = dao.obterTodos();
        custeioReceitaFiltrados.addAll(custeioReceitas);
        CusteioReceitaAdapter adaptador = new CusteioReceitaAdapter(this, custeioReceitaFiltrados);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);

        // inicios: totalisando os custos
        for(CusteioReceitaBEAN somaCusteio:custeioReceitaFiltrados){
            if(somaCusteio.getTipo().equals("Despesa")){
                sDespesa = somaCusteio.getValor();
                tDespesa = sDespesa+tDespesa;
            }else{
                sReceita = somaCusteio.getValor();
                tReceita = sReceita+tReceita;
            }
        }
        tSaldo = tReceita-tDespesa;
        String totalDespesa = String.valueOf(dinheiro.format(tDespesa));
        String totalReceita = String.valueOf(dinheiro.format(tReceita));
        String totalSaldo   = String.valueOf(dinheiro.format(tSaldo));
        somaDespesa.setText(totalDespesa);
        somaReceita.setText(totalReceita);
        saldo.setText(totalSaldo);
        // fim: totalisando os custos
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal,menu);

        // inicio: procura registro
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
                // inicios: totalisando os custos
                custeioReceitaFiltrados.add(a);
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
        listView.invalidateViews();
    }
    // fim: procura registro


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto,menu);
    }


    public void cadastrar (MenuItem item){
        Intent it = new Intent(this, MainActivity.class);
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
                        listView.invalidateViews();
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

    @Override
    public void onResume() {
        super.onResume();
        custeioReceitas = dao.obterTodos();
        custeioReceitaFiltrados.clear();
        custeioReceitaFiltrados.addAll(custeioReceitas);
        listView.invalidateViews();
    }
}