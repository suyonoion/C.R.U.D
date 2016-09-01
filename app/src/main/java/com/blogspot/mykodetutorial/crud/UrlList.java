package com.blogspot.mykodetutorial.crud;

/**
 * Created by Suyono on 8/14/2016.
 */
public class UrlList {
    // Link untuk INSERT Data
    public static final String URL_ADD="http://mykode.id/crud/create.php";
    // Link Untuk Tampil Data
    public static final String URL_GET_ALL = "http://mykode.id/crud/read.php";
    // Link ini untuk mengambil data berdasarkan ID
    public static final String URL_GET_ID = "http://mykode.id/crud/get_data.php?id=";
    // Link untuk Update data
    public static final String URL_UPDATE_EMP="http://mykode.id/crud/update.php";
    // Link Untuk Hapus Data
    public static final String URL_DELETE_EMP="http://mykode.id/crud/delete.php?id=";

    // Field yang digunakan untuk dikirimkan ke Database, sesuaikan saja dengan Field di Tabel Mahasiswa
    public static final String TABLE_ID = "id";
    public static final String TABLE_NPM = "npm";
    public static final String TABLE_NAMA = "nama";
    public static final String TABLE_JURUSAN = "jurusan";

    // Tags Format JSON
    public static final String JSON_ARRAY_RESULT="result";
    public static final String JSON_ID = "id";
    public static final String JSON_NPM = "npm";
    public static final String JSON_NAME = "nama";
    public static final String JSON_JURUSAN = "jurusan";

    // id to pass with intent
    public static final String GET_ID = "get_id";
}
