package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateNotes extends AppCompatActivity {

    EditText editText;
    Button addbutton, deletebutton;
    FirebaseFirestore firestore;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);
        setTitle("Update Notes");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        editText = findViewById(R.id.editext);
        addbutton = findViewById(R.id.addbutton);
        deletebutton = findViewById(R.id.deletebutton);
        firestore = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);

        String Notes = sharedPreferences.getString("name", "");

        editText.setText(Notes);


        firestore.collection("Notes")
                .whereEqualTo("notes", Notes)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    id = "" + queryDocumentSnapshots.getDocuments().get(0);
                    System.out.println("Document id is " + id.substring(27, 55));
                }).addOnFailureListener(e -> Toast.makeText(UpdateNotes.this, "" + e, Toast.LENGTH_SHORT).show());


        addbutton.setOnClickListener(view ->
        {

            if (editText.getText().toString().equalsIgnoreCase("")) {
                editText.setError("Enter the notes");

            } else {
                Map<String, Object> user = new HashMap<>();
                user.put("notes", editText.getText().toString());
                user.put("date", id.substring(27, 55));

                firestore.collection("Notes")
                        .document(id.substring(27, 55))
                        .set(user)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(UpdateNotes.this, "Updated", Toast.LENGTH_SHORT).show();
                            //editText.setText("");
                            finish();
                            startActivity(new Intent(UpdateNotes.this, MainActivity.class));
                        })
                        .addOnFailureListener(e -> Toast.makeText(UpdateNotes.this, "" + e, Toast.LENGTH_SHORT).show());

            }


        });

        deletebutton.setOnClickListener(view -> firestore.collection("Notes")
                .document(id.substring(27, 55))
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(UpdateNotes.this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(UpdateNotes.this, MainActivity.class));
                }));


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
