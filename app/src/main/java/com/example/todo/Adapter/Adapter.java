package com.example.todo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ViewHolder.HolderData;
import com.example.todo.Api.ApiRequest;
import com.example.todo.Api.RetroServer;
import com.example.todo.DetailActivity;
import com.example.todo.EditorActivity;
import com.example.todo.Model.Todo;
import com.example.todo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<HolderData> {
    private Context context;
    private List<Todo> todoList;
    private AlertDialog.Builder dialog,dialogDelete;

    public Adapter(Context context, List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;
    }


    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Todo dm =todoList.get(position);
        Log.d("Sucess Id","Id ====>"+dm.getId());
        holder.tvid.setText(String.valueOf(dm.getId()));
        holder.tvnama.setText(dm.getNama());
        //holder.tvjam.setText(dm.getWaktu());
        String format="yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(dm.getWaktu());
            //"2022-03-11T00:00:00"
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String format2="dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(format2, Locale.getDefault());
            final String dateformat = simpleDateFormat2.format(date);

            holder.tvjam.setText(dateformat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] dialogItems={"Tampil","Edit","Hapus"};
                dialog = new AlertDialog.Builder(view.getContext());
                dialog.setItems(dialogItems,(dialogInterface, i) -> {
                    switch (i){
                        case 0:
                            Intent intentDetails = new Intent(view.getContext(), DetailActivity.class);
                            intentDetails.putExtra("id",dm.getId());
                            view.getContext().startActivity(intentDetails);
                            break;
                        case 1:
                            Intent intentUpdate = new Intent(view.getContext(), EditorActivity.class);

                            intentUpdate.putExtra("id",dm.getId());
                            intentUpdate.putExtra("nama",dm.getNama());
                            intentUpdate.putExtra("jam",dm.getWaktu());
                            intentUpdate.putExtra("deskripsi",dm.getDeskripsi());
                            intentUpdate.putExtra("kategori",dm.getKategori());
                            view.getContext().startActivity(intentUpdate);
                            break;
                        case 2:
                            dialogDelete = new AlertDialog.Builder(view.getContext())
                                    .setTitle("Delete Data")
                                    .setMessage("Yakin Mau Hapus Data Id"+dm.getId())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ApiRequest ardData = RetroServer.konekRetrofit().create(ApiRequest.class);
                                    Call<Response<String >> listCall =ardData.deleteDataTodo(dm.getId());
                                    listCall.enqueue(new Callback<Response<String>>() {
                                        @Override
                                        public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                                            Toast.makeText(view.getContext(), "Berhasil Menghapus data id="+dm.getId(), Toast.LENGTH_SHORT).show();
                                            todoList.remove(holder.getAbsoluteAdapterPosition());
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Call<Response<String>> call, Throwable t) {
                                            Toast.makeText(view.getContext(), "Check Your Connection", Toast.LENGTH_SHORT).show();
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            ;
                            dialogDelete.create().show();

                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
