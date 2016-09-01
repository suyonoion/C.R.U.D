package com.blogspot.mykodetutorial.crud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Suyono on 8/22/2016.
 */
public class TambahData extends AppCompatActivity implements View.OnClickListener{
    //Mendefinisikan View Edit Text
    private EditText editTextName;
    private EditText editTextDesg;
    private EditText editTextSal;
    // Mendefinisikan View Button
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data);
        //Men Inisialisasi View Text dan Button
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDesg = (EditText) findViewById(R.id.editTextDesg);
        editTextSal = (EditText) findViewById(R.id.editTextSalary);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        //Berikan event listeners Klik ke Button
        buttonAdd.setOnClickListener(this);
    }

    //Adding an employee
    private void TambahDataMahasiswa(){
        // Ubah setiap View EditText ke tipe Data String
        final String npm = editTextName.getText().toString().trim();
        final String nama = editTextDesg.getText().toString().trim();
        final String jurusan = editTextSal.getText().toString().trim();
        // Pembuatan Class AsyncTask yang berfungsi untuk koneksi ke Database Server

        class TambahDataMahasiswa extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TambahData.this,"Proses Kirim Data...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TambahData.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                // Sesuaikan bagian ini dengan field di tabel Mahasiswa
                params.put(UrlList.TABLE_NPM,npm);
                params.put(UrlList.TABLE_NAMA,nama);
                params.put(UrlList.TABLE_JURUSAN,jurusan);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(UrlList.URL_ADD, params);
            }
        }
        // Jadikan Class TambahData Sabagai Object Baru
        TambahDataMahasiswa ae = new TambahDataMahasiswa();
        ae.execute();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAdd){
            TambahDataMahasiswa();
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(TambahData.this, MainActivity.class));
        super.onBackPressed();
    }
}
