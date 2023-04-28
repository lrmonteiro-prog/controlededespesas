// Autor: Luiz Ricardo Monteiro

package com.example.controlededespesas.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.controlededespesas.R;


public class DialogGraficoLinha extends AppCompatDialogFragment {
    private EditText editTextAno;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_grafico_linha, null);



        builder.setView(view)
                .setTitle("Digite o ANO")
                .setNegativeButton("CANCELA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ano = editTextAno.getText().toString();
                        listener.applyTextsLinha(ano);
                    }
                });

        editTextAno = view.findViewById(R.id.edit_ano_linha);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "deve implementar DialogListener");
        }
    }

    public interface DialogListener {
        void applyTextsLinha(String ano);
    }

}
