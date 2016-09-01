package com.blogspot.mykodetutorial.crud;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Suyono on 8/11/2016.
 */
public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener{
    // Definisikan ListView
    private ListView listView;
    private TableRow judulTableRow;
    TextView responkoneksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        // Inisialiasi ListView
        listView = (ListView) findViewById(R.id.listView);
        judulTableRow = (TableRow) findViewById(R.id.tabelrow);
        responkoneksi = (TextView) findViewById(R.id.respon_koneksi);
        listView.setOnItemClickListener(this);
        if (isNetworkAvailable()) {
            try {
                GetJSON gj = new GetJSON();
                gj.execute();
            } catch (Exception e){
                responkoneksi.setText("MBOH AH");
            }
        } else {
            responkoneksi.setText("Tidak ada koneksi internet. Periksa koneksi internet Anda.");
            responkoneksi.setVisibility(View.VISIBLE);
        }
    }

    //PROSES MULAI
    class GetJSON extends AsyncTask<Void,Void,String> {
        ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading ProgressDialog
            loading = new ProgressDialog(MainActivity.this);
            loading.setTitle("Pengambilan Data");
            loading.setMessage("Mohon tunggu sejenak... klik sembarang untuk batal");
            loading.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();

            judulTableRow.setVisibility(View.VISIBLE);
            // AMbil dari server PHP
            //mulai
            // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
            JSONObject jsonObject;
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray(UrlList.JSON_ARRAY_RESULT);
                // FOR untuk ambil data
                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    // JSON_ID dan JSON_NAME adalah variabel yang ada di Class UrlList.java,
                    String id = jo.getString(UrlList.JSON_ID);
                    String npm = jo.getString(UrlList.JSON_NPM);
                    String nama = jo.getString(UrlList.JSON_NAME);
                    String jurusan = jo.getString(UrlList.JSON_JURUSAN);

                    HashMap<String,String> mahasiswa = new HashMap<>();
                    mahasiswa.put(UrlList.JSON_ID,id);
                    mahasiswa.put(UrlList.JSON_NPM,npm);
                    mahasiswa.put(UrlList.JSON_NAME,nama);
                    mahasiswa.put(UrlList.JSON_JURUSAN,jurusan);
                    list.add(mahasiswa);
                }

                // Tampilkan datanya dalam Layout Lihat Data
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this, list, R.layout.tampilkan_read,
                        new String[]{
                                UrlList.JSON_ID,
                                UrlList.JSON_NPM,
                                UrlList.JSON_NAME,
                                UrlList.JSON_JURUSAN},
                        new int[]{
                                R.id.id,
                                R.id.npm,
                                R.id.name,
                                R.id.jurusan});
                // Tampilkan dalam bentuk ListView
                listView.setAdapter(adapter);
                //selesai

            } catch (JSONException e) {
                judulTableRow.setVisibility(View.GONE);
                responkoneksi.setText("Terjadi kesalahan saat Pengambilan Data! Cek koneksi internet anda dan Coba lagi");
                responkoneksi.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                // Proses nya sesuai alamat URL letak script PHP yang kita set di Class UrlList.java
            return rh.sendGetRequest(UrlList.URL_GET_ALL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tindakan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tambah_data) {
            startActivity(new Intent(getBaseContext(),TambahData.class));
            return true;
        }

        if (id == R.id.refresh){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        if (id == R.id.keluar){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // menggunakan Intent untuk pemmanggilan class ViewData
        Intent intent = new Intent(this, InfoLengkap.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        // Kita akan ambil data berdasarkan ID, JSON_ID ini berada di class UrlList.java
        String getID = map.get(UrlList.JSON_ID);
        intent.putExtra(UrlList.GET_ID,getID);
        // Tampilkan class ViewData
        startActivity(intent);
    }

    // Cek ketersediaan jaringan internet
    private Boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo !=null && netInfo.isConnectedOrConnecting();
    }

}
