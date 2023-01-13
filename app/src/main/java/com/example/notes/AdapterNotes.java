package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.ViewHolder> {

    ArrayList<ModelNotes> datalist;

    public AdapterNotes(ArrayList<ModelNotes> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.t1.setText(datalist.get(position).getNotes());
        holder.t2.setText(datalist.get(position).getDate());

        holder.itemView.setOnClickListener(view -> {

            Intent intent=new Intent(holder.t1.getContext(), UpdateNotes.class);
            holder.t1.getContext().startActivity(intent);

            SharedPreferences sharedPreferences=holder.t1.getContext().getSharedPreferences("Notes", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();

            editor.putString("name", datalist.get(position).getNotes());
            editor.apply();

        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.t1);
            t2=itemView.findViewById(R.id.t2);
        }
    }
}
