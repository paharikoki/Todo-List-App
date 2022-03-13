package com.example.todo.Adapter.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;

public class HolderData extends RecyclerView.ViewHolder {
    public TextView tvid,tvnama,tvjam;

    public HolderData(@NonNull View itemView) {
        super(itemView);
        tvid = itemView.findViewById(R.id.tv_id);
        tvnama = itemView.findViewById(R.id.tv_nama);
        tvjam = itemView.findViewById(R.id.tv_jam);
    }
}
