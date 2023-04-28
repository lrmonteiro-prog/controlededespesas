// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.R;
import java.text.DecimalFormat;
import java.util.List;

public class CusteioReceitaRelatorioAdapter extends BaseAdapter {

    private List<CusteioReceitaBEAN> custeioReceitas;
    private Activity activity;
    private DecimalFormat dinheiro = new DecimalFormat("R$ ###,##0.00");

    public CusteioReceitaRelatorioAdapter(Activity activity, List<CusteioReceitaBEAN> custeioReceitas){
        this.activity = activity;
        this.custeioReceitas = custeioReceitas;
    }

    @Override
    public int getCount() {
        return custeioReceitas.size();
    }

    @Override
    public Object getItem(int i) {
        return custeioReceitas.get(i);
    }

    @Override
    public long getItemId(int i) {
      return custeioReceitas.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(R.layout.item_relatorio, parent,false);
        TextView categoria = v.findViewById(R.id.txtCategoria);
        TextView valor = v.findViewById(R.id.txtValor);

        CusteioReceitaBEAN a = custeioReceitas.get(i);
        categoria.setText(a.getCategoria());
        valor.setText(dinheiro.format(a.getValor()));
        return v;
    }
}
