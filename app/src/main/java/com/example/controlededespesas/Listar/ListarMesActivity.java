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

import com.example.controlededespesas.Dialog.DialogGraficoBarra;
import com.example.controlededespesas.Dialog.DialogGraficoLinha;
import com.example.controlededespesas.Graficos.ActivityGraficoBarra;
import com.example.controlededespesas.Graficos.ActivityGraficoLinha;
import com.example.controlededespesas.Graficos.ActivityGraficoPizza;
import com.example.controlededespesas.Main.Activity_alterar_dados;
import com.example.controlededespesas.Main.Activity_repetir_mes;
import com.example.controlededespesas.Adapter.CusteioReceitaMesAdapter;
import com.example.controlededespesas.BEAN.CusteioReceitaMesBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaMesDAO;
import com.example.controlededespesas.Main.MainActivityMes;
import com.example.controlededespesas.R;

import java.util.ArrayList;
import java.util.List;

public class ListarMesActivity extends AppCompatActivity implements DialogGraficoBarra.DialogListener,DialogGraficoLinha.DialogListener{

    private ListView listView;
    private CusteioReceitaMesDAO daomes;
    private List<CusteioReceitaMesBEAN> custeioReceitasMes;
    private List<CusteioReceitaMesBEAN> custeioReceitaFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_mes);

        listView = findViewById(R.id.listaCuteioReceitaCategoria);
        daomes = new CusteioReceitaMesDAO(this);
        custeioReceitasMes = daomes.obterTodosMes();
        custeioReceitaFiltrados.addAll(custeioReceitasMes);

        //   ArrayAdapter<CusteioReceita> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, custeioReceitaFistrados);
        CusteioReceitaMesAdapter adaptador = new CusteioReceitaMesAdapter(this, custeioReceitaFiltrados);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListarMesActivity.this, ListarCusteioReceitaActivity2.class);
                intent.putExtra("periodomes",custeioReceitasMes.get(position).getPeriodomes());
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal_mes,menu);

        // inicio: procura registro
        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraCusteioReceitaMes(s);
                return false;
            }
        });
        return true;
    }



    public void procuraCusteioReceitaMes(String nome){
        custeioReceitaFiltrados.clear();
        for(CusteioReceitaMesBEAN a : custeioReceitasMes){
            if(a.getPeriodomes().toLowerCase().contains((nome.toLowerCase()))){
                custeioReceitaFiltrados.add(a);
            }
        }
        listView.invalidateViews();
    }
    // fim: procura registro


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto_mes,menu);
    }


    public void cadastrar (MenuItem item){
        Intent it = new Intent(this, MainActivityMes.class);
        startActivity(it);
    }

    public void excluir(MenuItem item){

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaMesBEAN custeioReceitaExcluir = custeioReceitaFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.validacao)
                .setMessage("Realmente deseja excluir esse dado:")
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        custeioReceitaFiltrados.remove(custeioReceitaExcluir);
                        custeioReceitasMes.remove(custeioReceitaExcluir);
                        daomes.excluir(custeioReceitaExcluir);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void atualizarMes(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaMesBEAN custeioReceitaAtualizar = custeioReceitaFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, Activity_alterar_dados.class);
        it.putExtra("custeioReceitames",custeioReceitaAtualizar);
        startActivity(it);
    }

    public void salvarRepetirMes(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaMesBEAN custeioReceitaRepetir = custeioReceitaFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, Activity_repetir_mes.class);
        it.putExtra("custeioReceitames",custeioReceitaRepetir);
        startActivity(it);
    }

    public void configurarItem (MenuItem item) {
        Intent it = new Intent(ListarMesActivity.this, ListarCategoriaActivity.class);
        startActivity(it);
    }

    public void graficoBarra (MenuItem item) {
        DialogGraficoBarra dialogGraficoBarra = new DialogGraficoBarra();
        dialogGraficoBarra.show(getSupportFragmentManager(), "example dialog");
    }

    public void graficoLinha (MenuItem item) {
        DialogGraficoLinha dialogGraficoLinha = new DialogGraficoLinha();
        dialogGraficoLinha.show(getSupportFragmentManager(), "example dialog");
    }


    @Override
    public void applyTexts(String ano,String tipo) {
        Intent intent = new Intent(ListarMesActivity.this, ActivityGraficoBarra.class);
        intent.putExtra("anografico",ano);
        intent.putExtra("tipografico",tipo);
        startActivity(intent);
    }

    @Override
    public void applyTextsLinha(String ano) {
        Intent intent = new Intent(ListarMesActivity.this, ActivityGraficoLinha.class);
        intent.putExtra("anografico",ano);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        custeioReceitasMes = daomes.obterTodosMes();
        custeioReceitaFiltrados.clear();
        custeioReceitaFiltrados.addAll(custeioReceitasMes);
        listView.invalidateViews();
    }
}