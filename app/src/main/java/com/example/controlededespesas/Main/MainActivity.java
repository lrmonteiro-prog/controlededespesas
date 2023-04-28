package com.example.controlededespesas.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.controlededespesas.BEAN.CusteioReceitaBEAN;
import com.example.controlededespesas.BEAN.CusteioReceitaCategoriaBEAN;
import com.example.controlededespesas.DAO.CusteioReceitaCategoriaDAO;
import com.example.controlededespesas.DAO.CusteioReceitaDAO;
import com.example.controlededespesas.Listar.ListarCusteioReceitaActivity2;
import com.example.controlededespesas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Float.valueOf;

public class MainActivity extends AppCompatActivity {

    public Spinner menu;
    public TextView textoSpinner;
    private CusteioReceitaCategoriaDAO daoCategoria;

    public Switch opcaopgto;
    public TextView datapgto;

    public RadioGroup grupo;
    public TextView   textoRagioGroup;

    private TextView tipo;
    private TextView datapagamento;
    private EditText datavencimento;
    private EditText descricao;
    private TextView categoria;
    private EditText valor;
    private TextView periodo;
    private CusteioReceitaDAO dao;
    private CusteioReceitaBEAN custeioReceitaBEAN = null;

    private EditText data;
    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialogPgto;

    private String parametro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // traz por parâmetro o mês em que se trabalha
        Intent i = getIntent();
        parametro = (String) i.getSerializableExtra("periodomes");

        // início Spinner Categoria
        textoSpinner = findViewById(R.id.textViewCategoria);
        menu = findViewById(R.id.spinner);
        daoCategoria = new CusteioReceitaCategoriaDAO(this);

        ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,daoCategoria.obterTodosCategoria());
        menu.setAdapter(adapter);
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //textoSpinner.setText(adapter[position]);
                CusteioReceitaCategoriaBEAN cat = (CusteioReceitaCategoriaBEAN) adapter.getItem(position);
                textoSpinner.setText(cat.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // fim Spinner Categoria

        // início do RadioGroup
        grupo = findViewById(R.id.radioGroup1);
        textoRagioGroup = findViewById(R.id.textViewTipo);
        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton opcao = findViewById(grupo.getCheckedRadioButtonId());
                textoRagioGroup.setText(opcao.getText());
            }
        });
        // fim do RadioGroup

        tipo = findViewById(R.id.textViewTipo);
        datapagamento = findViewById(R.id.textDataPagamento);
        datavencimento = findViewById(R.id.editTextData);
        descricao = findViewById(R.id.editTextDescricao);
        categoria = findViewById(R.id.textViewCategoria);
        valor = findViewById(R.id.editTextValor);
        periodo = findViewById(R.id.textViewPeriodo);
        dao = new CusteioReceitaDAO(this);

        Intent it = getIntent();
        if(it.hasExtra("custeioReceita")){
            custeioReceitaBEAN = (CusteioReceitaBEAN) it.getSerializableExtra("custeioReceita");
            tipo.setText(custeioReceitaBEAN.getTipo());
            datapagamento.setText(custeioReceitaBEAN.getDatapagamento());
            datavencimento.setText(custeioReceitaBEAN.getDatavencimento());
            descricao.setText(custeioReceitaBEAN.getDescricao());
            categoria.setText(custeioReceitaBEAN.getCategoria());
            valor.setText(custeioReceitaBEAN.getValor().toString());
            periodo.setText(custeioReceitaBEAN.getPeriodo());
        }

        data = findViewById(R.id.editTextData);
        calendar = Calendar.getInstance();


        opcaopgto = findViewById(R.id.switch1);
        datapgto = findViewById(R.id.textDataPagamento);


        // início Calendário de Data de Vencimento
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month,dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formato = new SimpleDateFormat("MM/yyyy");
                data.setText(format.format(dataSelecionada.getTime()));
                periodo.setText(formato.format(dataSelecionada.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        // fim Calendário de Data de Vencimento

        // início Calendário de Data de Pagamento
        opcaopgto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                datapgto.setText(converterParaMetodo(isChecked));
            }
        });
       // fim Calendário de Data de Pagamento

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarInclusao();
            }
        });
    }

    private String converterParaMetodo(boolean isChecked) {
        String textoExibir = "";
        if(isChecked){
            textoExibir = "Pagando";
            datePickerDialogPgto = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar dataSelecionada = Calendar.getInstance();
                    dataSelecionada.set(year, month,dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    datapgto.setText(format.format(dataSelecionada.getTime()));
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

            opcaopgto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialogPgto.show();
                }
            });
        }else{
            textoExibir = "Não foi pago!";
        }
        return textoExibir;
    }

    public void salvar (View view) {
        boolean res = false;
        String descricoes = descricao.getText().toString();

        CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
        custeioReceitaBEAN.setTipo(tipo.getText().toString());
        custeioReceitaBEAN.setDatapagamento(datapagamento.getText().toString());
        custeioReceitaBEAN.setDatavencimento(datavencimento.getText().toString());
        custeioReceitaBEAN.setDescricao(descricao.getText().toString());
        custeioReceitaBEAN.setCategoria(categoria.getText().toString());
        custeioReceitaBEAN.setValor(Float.valueOf(valor.getText().toString()));
        custeioReceitaBEAN.setPeriodo(periodo.getText().toString());

        if(res = isCampoVazio(descricoes)){
            descricao.requestFocus();
        }else{
            long id = dao.inserir(custeioReceitaBEAN);
            Toast.makeText(this, "Dado inserido com id: " + id, Toast.LENGTH_SHORT).show();
        }
        if(res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("O campo Descrição está em branco!");
            dlg.setNeutralButton("Ok",null);
            dlg.show();
        }
    }
    public void limpar(View view){
        CusteioReceitaBEAN custeioReceitaBEAN = new CusteioReceitaBEAN();
        custeioReceitaBEAN.setTipo(null);
        custeioReceitaBEAN.setDatapagamento(null);
        custeioReceitaBEAN.setDatavencimento(null);
        custeioReceitaBEAN.setDescricao(null);
        custeioReceitaBEAN.setCategoria(null);
        custeioReceitaBEAN.setValor(null);
        custeioReceitaBEAN.setPeriodo(null);

        tipo.setText("Despesa");
        datapagamento.setText("Não está pago!");
        datavencimento.setText("");
        descricao.setText("");
        categoria.setText("");
        valor.setText("");
        periodo.setText("");

        descricao.requestFocus();
    }

    public void voltarInclusao(){
        Intent intent = new Intent(MainActivity.this, ListarCusteioReceitaActivity2.class);
        intent.putExtra("periodomes",parametro);
        startActivity(intent);
    }

    public void validaCampos(){
        boolean res = false;
        String vencimento = datavencimento.getText().toString();
        String descricoes = descricao.getText().toString();
        Float  valores     = valueOf(valor.getText().toString());
        if(res = isCampoVazio(descricoes)){
                descricao.requestFocus();
        }else
            if(res = isCampoVazio(vencimento)){
                datavencimento.requestFocus();
            }else
                if(res = isCampoVazio(String.valueOf(valores))){
                    valor.requestFocus();
                }
        if(res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campos inválidos ou em branco!");
            dlg.setNeutralButton("Ok",null);
            dlg.show();
        }
    }

    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private boolean isEmailValido( String email){
        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

}