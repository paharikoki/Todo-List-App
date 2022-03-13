package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.todo.Adapter.Adapter;
import com.example.todo.Api.ApiRequest;
import com.example.todo.Api.RetroServer;
import com.example.todo.Model.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabadd;
    RecyclerView rvData;
    Adapter adData;
    List<Todo>todoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabadd = findViewById(R.id.fab_add);
        rvData = findViewById(R.id.rv_data);


        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });

        getDataTodo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataTodo();
    }

    public void getDataTodo(){
        ApiRequest ardData = RetroServer.konekRetrofit().create(ApiRequest.class);
        Call<List<Todo>> listCall =ardData.getRetrieveDataTodo();
        listCall.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                Log.d("Success","Success Connect Server");
                todoList = response.body();
                adData = new Adapter(MainActivity.this,todoList);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Berhasil Menampilkan Data, Jumlah Data : "+response.body().size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}