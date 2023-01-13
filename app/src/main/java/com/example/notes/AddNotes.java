package com.example.notes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNotes extends AppCompatActivity {
    EditText editText;
    Button addbutton;
    FirebaseFirestore firestore;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        setTitle("Add Notes");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editText = findViewById(R.id.editext);
        addbutton = findViewById(R.id.addbutton);
        firestore = FirebaseFirestore.getInstance();

        addbutton.setOnClickListener(view ->
        {
            String date = getdate();

            if (editText.getText().toString().equalsIgnoreCase("")) {
                editText.setError("Enter the notes");

            } else {
                Map<String, Object> user = new HashMap<>();
                user.put("notes", editText.getText().toString());
                user.put("date", date);
                firestore.collection("Notes")
                        .document(date)
                        .set(user)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(AddNotes.this, "Added", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                            finish();
                            startActivity(new Intent(AddNotes.this, MainActivity.class));
                        })
                        .addOnFailureListener(e -> Toast.makeText(AddNotes.this, "" + e, Toast.LENGTH_SHORT).show());
            }


        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getdate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, dd MMM yyyy hh:mm:ss a");
        return myDateObj.format(myFormatObj);
    }
}