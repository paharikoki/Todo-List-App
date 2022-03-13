package com.example.todo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.Api.ApiRequest;
import com.example.todo.Api.RetroServer;
import com.example.todo.Model.Todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText etNama,etdeskripsi,etkategori;
    private TextView tvjam;
    private int id;
    private String nama,deskripsi,kategori,waktu,status;
    private DatePickerDialog datePickerDialog;
    private Spinner spkategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        etNama = findViewById(R.id.et_nama);
        etdeskripsi = findViewById(R.id.et_deskripsi);
        spkategori = findViewById(R.id.sp_kategori);
        tvjam = findViewById(R.id.tv_jampicker);
        btnSave= findViewById(R.id.btn_save);

        id = getIntent().getIntExtra("id",0);
        nama = getIntent().getStringExtra("nama");
        waktu = getIntent().getStringExtra("jam");
        kategori = getIntent().getStringExtra("kategori");
        waktu = getIntent().getStringExtra("jam");
        deskripsi = getIntent().getStringExtra("deskripsi");



        Log.d("Success id","Id===>"+id);
        if (id == 0){
            setTitle("Tambah Mobil Rental");
        }else {
            setTitle("Edit Mobil Rental");
            etNama.setText(nama);
            etdeskripsi.setText(deskripsi);
            tvjam.setText(waktu);


            if (kategori.equals("belajar")){
                spkategori.setSelection(0);
            }else if (kategori.equals("android")){
                spkategori.setSelection(1);
            }else if (kategori.equals("sekolah")){
                spkategori.setSelection(2);
            }else if (kategori.equals("rumah")){
                spkategori.setSelection(3);
            }else {
                spkategori.setSelection(0);
            }
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (id==0){
                        insertData();
                    }else {
                        updateData();
                    }
                }catch (Exception e){
                    Log.d("Saving",e.getMessage());
                }
            }
        });
        tvjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDate();
            }
        });
    }

    private void insertData(){
        waktu = tvjam.getText().toString();
        Todo todoList = new Todo();
        todoList.setNama(etNama.getText().toString());
        todoList.setDeskripsi(etdeskripsi.getText().toString());
        todoList.setKategori(spkategori.getSelectedItem().toString());
        todoList.setWaktu(waktu);
        todoList.setStatus("belum");

        ApiRequest ardData = RetroServer.konekRetrofit().create(ApiRequest.class);
        Call<Todo> listCall =ardData.insertDataTodo(todoList);
        listCall.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                Toast.makeText(EditorActivity.this, "Berhasil Menambahkan", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Toast.makeText(EditorActivity.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showDialogDate(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i,i1,i2);
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                String format="yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
                tvjam.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void updateData(){
        waktu = tvjam.getText().toString();
        Todo todoList = new Todo();
        todoList.setId(id);
        todoList.setNama(etNama.getText().toString());
        todoList.setDeskripsi(etdeskripsi.getText().toString());
        todoList.setKategori(spkategori.getSelectedItem().toString());
        todoList.setWaktu(waktu);
        todoList.setStatus("belum");

        ApiRequest ardData = RetroServer.konekRetrofit().create(ApiRequest.class);
        Call<Response<String >> listCall =ardData.updataDataTodo(id,todoList);
        listCall.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                Toast.makeText(EditorActivity.this, "Berhasil Mengedit data id="+id, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                Toast.makeText(EditorActivity.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }
}