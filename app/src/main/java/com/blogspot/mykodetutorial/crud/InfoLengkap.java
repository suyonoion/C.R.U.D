package com.blogspot.mykodetutorial.crud;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Suyono on 8/14/2016.
 */
public class InfoLengkap extends AppCompatActivity implements View.OnClickListener {
    // Inisialiasasi View
    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextDesg;
    private EditText editTextSalary;
    // Inisialisasi Button
    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_lengkap);
        Intent intent = getIntent();
        // ID
        id = intent.getStringExtra(UrlList.GET_ID);
        // Inisialisasi View dan Button
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDesg = (EditText) findViewById(R.id.editTextDesg);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        // Event click Button
        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        // Tampilka ID pada EditText ID
        editTextId.setText(id);
// Panggil Methood GetData
        getData();
    }

    // Buat Method GetData untuk ambil data diserver
    private void getData(){
        class getData extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoLengkap.this,"Proses Data...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                // Method Show Data
                ShowData(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UrlList.URL_GET_ID,id);
                return s;
            }
        }
        getData ge = new getData();
        ge.execute();
    }
    // Method ShowData untuk tampilkan data pada setiap EditText
    private void ShowData(String json){
        try {
            // Jadikan sebagai JSON object
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(UrlList.JSON_ARRAY_RESULT);
            JSONObject c = result.getJSONObject(0);
            // Data berdasarkan di Tabel Database
            String npm = c.getString(UrlList.JSON_NPM);
            String nama = c.getString(UrlList.JSON_NAME);
            String jurusan = c.getString(UrlList.JSON_JURUSAN);
// Tampilkan setiap data JSON format kedalam setiap EditText
            editTextName.setText(npm);
            editTextDesg.setText(nama);
            editTextSalary.setText(jurusan);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method Untuk Update Data
    private void updateData(){
        final String npm = editTextName.getText().toString().trim();
        final String nama = editTextDesg.getText().toString().trim();
        final String jurusan = editTextSalary.getText().toString().trim();

        class updateData extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoLengkap.this,"Update Data...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(InfoLengkap.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(UrlList.TABLE_ID,id);
                hashMap.put(UrlList.TABLE_NPM,npm);
                hashMap.put(UrlList.TABLE_NAMA,nama);
                hashMap.put(UrlList.TABLE_JURUSAN,jurusan);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(UrlList.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        updateData ue = new updateData();
        ue.execute();
    }

    // Method Untuk Confirmasi Hapus Data

    private void confirmDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apa Kamu Yakin Untuk Menghapus Data ini?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Buatkan method hapus data kemudian dipanggil disini
                        deleteData();
                        startActivity(new Intent(InfoLengkap.this, MainActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Method Hapus Data
    private void deleteData(){
        class DeleteData extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoLengkap.this, "Update Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(InfoLengkap.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(UrlList.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteData de = new DeleteData();
        de.execute();
    }


    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateData();
        }

        if(v == buttonDelete){
            confirmDelete();
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(InfoLengkap.this, MainActivity.class));
        super.onBackPressed();
    }
}
