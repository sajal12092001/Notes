package com.example.notes;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add;
    RecyclerView recyclerView;
    ArrayList<ModelNotes> datalist;
    FirebaseFirestore db;
    AdapterNotes adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        add = findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        datalist = new ArrayList<>();


        db = FirebaseFirestore.getInstance();
        db.collection("Notes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();


                    for (DocumentSnapshot d : list) {
                        ModelNotes obj = d.toObject(ModelNotes.class);
                        datalist.add(obj);
                    }
                    adapter = new AdapterNotes(datalist);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_SHORT).show());

        add.setOnClickListener(view ->
                startActivity(new Intent(this, AddNotes.class)));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            db.collection("Notes")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        datalist.clear();
                        for (DocumentSnapshot d : list) {
                            ModelNotes obj = d.toObject(ModelNotes.class);
                            datalist.add(obj);
                        }
                        adapter = new AdapterNotes(datalist);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                    })
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_SHORT).show());

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}