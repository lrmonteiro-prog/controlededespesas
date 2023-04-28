// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.controlededespesas.BEAN.CusteioReceitaMesBEAN;
import com.example.controlededespesas.R;
import java.util.List;

public class CusteioReceitaMesAdapter extends BaseAdapter {

    private List<CusteioReceitaMesBEAN> custeioReceitasMes;
    private Activity activity;
    private float cor = 0;

    public CusteioReceitaMesAdapter(Activity activity, List<CusteioReceitaMesBEAN> custeioReceitasMes){
        this.activity = activity;
        this.custeioReceitasMes = custeioReceitasMes;
    }

    @Override
    public int getCount(){
        return custeioReceitasMes.size();
    }

    @Override
    public Object getItem(int i) {
        return custeioReceitasMes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return custeioReceitasMes.get(i).getIdmes();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(R.layout.item_mes, parent,false);
        TextView periodomes = v.findViewById(R.id.textItemCategoria);
        CusteioReceitaMesBEAN a = custeioReceitasMes.get(i);
        if (cor == 0) {
            periodomes.setBackgroundResource(R.color.azulclaro2);
            periodomes.setText(a.getPeriodomes());
            cor = 1;
        }else{
            periodomes.setBackgroundResource(R.color.azulclaro3);
            periodomes.setText(a.getPeriodomes());
            cor = 0;
        }
        return v;

    }
}
