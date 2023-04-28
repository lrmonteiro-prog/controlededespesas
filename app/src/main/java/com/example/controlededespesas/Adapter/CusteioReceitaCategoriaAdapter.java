package com.example.controlededespesas.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.controlededespesas.BEAN.CusteioReceitaCategoriaBEAN;
import com.example.controlededespesas.R;

import java.util.List;

public class CusteioReceitaCategoriaAdapter extends BaseAdapter {

    private List<CusteioReceitaCategoriaBEAN> custeioReceitasCategoria;
    private Activity activity;
    private float cor = 0;

    public CusteioReceitaCategoriaAdapter(Activity activity, List<CusteioReceitaCategoriaBEAN> custeioReceitasCategoria){
        this.activity = activity;
        this.custeioReceitasCategoria = custeioReceitasCategoria;
    }

    @Override
    public int getCount(){
        return custeioReceitasCategoria.size();
    }

    @Override
    public Object getItem(int i) {
        return custeioReceitasCategoria.get(i);
    }

    @Override
    public long getItemId(int i) {
        return custeioReceitasCategoria.get(i).getIditem();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(R.layout.item_categoria, parent,false);
        TextView item = v.findViewById(R.id.textItemCategoria);
        CusteioReceitaCategoriaBEAN a = custeioReceitasCategoria.get(i);
        if (cor == 0) {
            item.setBackgroundResource(R.color.azulclaro4);
            item.setText(a.getItem());
            cor = 1;
        }else{
            item.setBackgroundResource(R.color.laranja1);
            item.setText(a.getItem());
            cor = 0;
        }
        return v;

    }
}
