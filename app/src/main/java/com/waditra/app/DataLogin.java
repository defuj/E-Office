package com.waditra.app;

/**
 * Created by defuj on 11/08/2016.
 */
public class DataLogin {
    public static final String URL = "http://csmd-shop.16mb.com/webservice/android/index.php";
    //public static final String URL = "http://192.168.173.1/webservice/android/index.php";
    public static final String URL_PHOTOS_USERS = "http://csmd-shop.16mb.com/webservice/android/foto_pengguna/";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";

    // Jika respon server adalah sukses login
    public static final String LOGIN_SUCCESS = "success";

    public static final String KEY_ADA = "ada";
    public static final String KEY_FAILED = "failed";

    //Kunci untuk Sharedpreferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //Ini digunakan untuk store username jika User telah Login
    public static final String USERNAME_SHARED_PREF = "username";
    public static final String PASSWORD_SHARED_PREF = "password";
    public static final String CT_SHARED_PREF = "created_time";
    public static final String NAME_SHARED_PREF = "name";
    public static final String ALAMAT_SHARED_PREF = "alamat";
    public static final String TELPON_SHARED_PREF = "telpon";
    public static final String FACEBOOK_SHARED_PREF = "facebook";
    public static final String TWITTER_SHARED_PREF = "twitter";
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String STATUS_SHARED_PREF = "status";
    public static final String FOTO_USER = null;
    public static final String FOTO_ADDRESS = "foto";

    // Ini digunakan untuk store sharedpreference untuk cek user melakukan login atau tidak
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //untuk data karyawan
    public static final String KEY_EMP_ID = "id_karyawan";
    public static final String KEY_EMP_NAMA = "nama_karyawan";
    public static final String KEY_EMP_JABATAN = "jabatan";
    public static final String KEY_EMP_FOTO = "foto_karyawan";

    //Tag Format JSON
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JABATAN = "jabatan";
    public static final String TAG_TEMPAT_LAHIR = "tempatLahir";
    public static final String TAG_TANGGAL_LAHIR = "tglLahir";
    public static final String TAG_CREATED_TIME = "masuk";
    public static final String TAG_FOTO = "foto";

    //Tag untuk list user
    public static final String TAG_NAMA_USER = "name";
    public static final String TAG_USERNAME_USER = "username";
    public static final String TAG_CT_USER = "created_time";
    public static final String TAG_STATUS_USER = "status";
    public static final String TAG_ALAMAT_USER = "alamat";
    public static final String TAG_TELPON_USER = "telpon";
    public static final String TAG_FACEBOOK_USER = "facebook";
    public static final String TAG_TWITTER_USER = "twitter";
    public static final String TAG_EMAIL_USER = "email";
    public static final String TAG_HASIL_HAPUS = "hasil";

    public static final String TAG_ID_SPJ = "id_spj";
    public static final String TAG_NAMA_SPJ = "nama_spj";
    public static final String TAG_TUJUAN = "tujuan";
    public static final String TAG_TGL_MULAI = "tgl_mulai";
    public static final String TAG_TGL_SELESAI = "tgl_selesai";
    public static final String TAG_NAMA_KARYAWAN = "nama_karyawan";
    public static final String TAG_KEGIATAN_PEKERJAAN = "kegiatan";

    //REGISTER
    public static final String TAG_HASIL_REEGISTER = "hasil";

    public static final String EMP_ID = "emp_id";

    //Tag untuk produk
    public static final String TAG_ID_PRODUK = "id_produk";
    public static final String TAG_NAMA_PRODUK = "nama_produk";
    public static final String TAG_JENIS_PRODUK = "jenis_produk";
    public static final String TAG_DETAIL_PRODUK = "detail_produk";
    public static final String TAG_CREATE_PRODUK = "created_time";
    public static final String TAG_URL_FOTO_PRODUK = "url_produk";

    public static final String TAG_ID_PROYEK = "id";
    public static final String TAG_NAMA_PROYEK = "nama";
    public static final String TAG_TAHUN_PROYEK = "tahun";
    public static final String TAG_NAMA_INSTANSI = "nama_instansi";
    public static final String TAG_NILAI_PROYEK = "nilai";
    public static final String TAG_DETAIL_PROYEK = "detail";

    public static final String TAG_JUDUL_NOTIF = "judul";
    public static final String TAG_ISI_NOTIF = "isi";
    public static final String TAG_WAKTU_NOTIF = "waktu";
    public static final String TAG_JUMLAH_NOTIF = "jumlah";

    public static final String TAG_NAMA_PROJEK = "nama";
    public static final String TAG_JENIS_PROJEK = "jenis";
    public static final String TAG_WAKTU_PROJEK = "waktu";
    public static final String TAG_PROGRESS_PROJEK = "progress";
}
