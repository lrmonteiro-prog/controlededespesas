package com.example.controlededespesas.Listar;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlededespesas.Main.Activity_alterar_categoria;
import com.example.controlededespesas.BEAN.CusteioReceitaCategoriaBEAN;
import com.example.controlededespesas.Adapter.CusteioReceitaCategoriaAdapter;
import com.example.controlededespesas.DAO.CusteioReceitaCategoriaDAO;
import com.example.controlededespesas.Main.MainActivityCategoria;
import com.example.controlededespesas.R;

import java.util.ArrayList;
import java.util.List;

public class ListarCategoriaActivity extends AppCompatActivity {

    private ListView listView;
    private CusteioReceitaCategoriaDAO dao;
    private List<CusteioReceitaCategoriaBEAN> custeioReceitasCategoria;
    private List<CusteioReceitaCategoriaBEAN> custeioReceitaFiltrados = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categoria);

        listView = findViewById(R.id.listaCuteioReceitaCategoria);
        dao = new CusteioReceitaCategoriaDAO(this);
        custeioReceitasCategoria = dao.obterTodosCategoria();
        custeioReceitaFiltrados.addAll(custeioReceitasCategoria);

        CusteioReceitaCategoriaAdapter adaptador = new CusteioReceitaCategoriaAdapter(this, custeioReceitaFiltrados);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal_categoria,menu);

        // inicio: procura registro
        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraCusteioReceitaCategoria(s);
                return false;
            }
        });
        return true;
    }

    public void procuraCusteioReceitaCategoria(String nome){
        custeioReceitaFiltrados.clear();
        for(CusteioReceitaCategoriaBEAN a : custeioReceitasCategoria){
            if(a.getItem().toLowerCase().contains((nome.toLowerCase()))){
                custeioReceitaFiltrados.add(a);
            }
        }
        listView.invalidateViews();
    }
    // fim: procura registro


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto_categoria,menu);
    }


    public void cadastrarCategoria (MenuItem item){
        Intent it = new Intent(this, MainActivityCategoria.class);
        startActivity(it);
    }

    public void excluir(MenuItem item){

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaCategoriaBEAN custeioReceitaExcluir = custeioReceitaFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.validacao)
                .setMessage("Realmente deseja excluir esse dado:")
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        custeioReceitaFiltrados.remove(custeioReceitaExcluir);
                        custeioReceitasCategoria.remove(custeioReceitaExcluir);
                        dao.excluir(custeioReceitaExcluir);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void atualizarCategoria(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CusteioReceitaCategoriaBEAN custeioReceitaAtualizar = custeioReceitaFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, Activity_alterar_categoria.class);
        it.putExtra("custeioReceitaCategoria",custeioReceitaAtualizar);
        startActivity(it);
    }

    public void cadastrar(MenuItem item){
        Intent it = new Intent(this,MainActivityCategoria.class);
        startActivity(it);
    }

    @Override
    public void onResume() {
        super.onResume();
        custeioReceitasCategoria = dao.obterTodosCategoria();
        custeioReceitaFiltrados.clear();
        custeioReceitaFiltrados.addAll(custeioReceitasCategoria);
        listView.invalidateViews();
    }
}