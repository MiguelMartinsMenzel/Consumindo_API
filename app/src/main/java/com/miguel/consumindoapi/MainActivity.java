package com.miguel.consumindoapi;

// Importações do sistema Android e layouts
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

// Importações do Retrofit (que vão parar de dar erro após o Passo 1)
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define a cor da barra de status e da barra de ação do topo
        getWindow().setStatusBarColor(Color.parseColor("#FF018786"));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF018786")));
        }

        // Mapeia os elementos do XML usando os IDs correspondentes
        TextInputLayout editCep = findViewById(R.id.edit_cep);
        TextInputLayout editLogradouro = findViewById(R.id.edit_logradouro);
        TextInputLayout editBairro = findViewById(R.id.edit_bairro);
        TextInputLayout editCidade = findViewById(R.id.edit_cidade);
        TextInputLayout editEstado = findViewById(R.id.edit_estado);
        Button btnBuscar = findViewById(R.id.btn_buscar);

        // Configura o clique do botão de busca
        btnBuscar.setOnClickListener(v -> {
            String cepDigitado = editCep.getEditText() != null ? editCep.getEditText().getText().toString().trim() : "";

            if (cepDigitado.length() == 8) {
                // Chama a requisição do Retrofit para buscar o CEP
                RetrofitConfig.getViaCepService().buscarCep(cepDigitado).enqueue(new Callback<Cep>() {
                    @Override
                    public void onResponse(Call<Cep> call, Response<Cep> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Cep dados = response.body();
                            // Preenche os campos do formulário na tela
                            if (editLogradouro.getEditText() != null) editLogradouro.getEditText().setText(dados.getLogradouro());
                            if (editBairro.getEditText() != null) editBairro.getEditText().setText(dados.getBairro());
                            if (editCidade.getEditText() != null) editCidade.getEditText().setText(dados.getLocalidade());
                            if (editEstado.getEditText() != null) editEstado.getEditText().setText(dados.getUf());
                        } else {
                            Toast.makeText(MainActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cep> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                editCep.setError("Digite um CEP válido com 8 dígitos");
            }
        });
    }
}