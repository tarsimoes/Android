package com.fiap.si.cepapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText txtCEP, txtLogradouro, txtComplemento, txtBairro, txtLocalidade, txtUF, txtIBGE, txtGIA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buscarCep(View v){

        txtCEP = (EditText) findViewById(R.id.txtCEP);
        txtLogradouro = (EditText) findViewById(R.id.txtLogradouro);
        txtComplemento = (EditText) findViewById(R.id.txtComplemento);
        txtBairro = (EditText) findViewById(R.id.txtBairro);
        txtLocalidade = (EditText) findViewById(R.id.txtLocalidade);
        txtUF = (EditText) findViewById(R.id.txtUF);
        txtIBGE = (EditText) findViewById(R.id.txtIBGE);
        txtGIA = (EditText) findViewById(R.id.txtGIA);

        String cep = txtCEP.getText().toString();
        Pattern pattern = Pattern.compile("^[0-9]{5}-[0-9]{3}$");
        Matcher matcher = pattern.matcher(cep);

        if(matcher.find()){

            BuscarCepTask task = new BuscarCepTask();
            task.execute(cep);

        }else{
            Toast.makeText(this, "Favor informar CEP válido", Toast.LENGTH_LONG).show();
        }


    }

    private class BuscarCepTask extends AsyncTask<String, Void, String>{

        //private ProgressDialog progress;

        protected void onPreExecute(){
           // progress = ProgressDialog.show(MainActivity.this, "Aguarde...", "Realizando a consulta");

        }

        @Override
        protected String doInBackground(String... params){
            StringBuilder resultado = new StringBuilder();

            try {
                URL url = new URL("http://viacep.com.br/ws/" + params[0]+"/json/");
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while((line = rd.readLine())!=null){

                    resultado.append(line);
                }

            }catch(MalformedURLException e){

                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            return resultado.toString();
        }

        protected void onPostExecute(String resultado){

            //progress.dismiss();

            try{
                JSONObject json = new JSONObject(resultado);

                txtLogradouro.setText(json.getString("logradouro"));
                txtComplemento.setText(json.getString("complemento"));
                txtBairro.setText(json.getString("bairro"));
                txtLocalidade.setText(json.getString("localidade"));
                txtUF.setText(json.getString("uf"));
                txtIBGE.setText(json.getString("ibge"));
                txtGIA.setText(json.getString("gia"));


            }catch(JSONException e){
                e.printStackTrace();

            }
        }
    }
}
