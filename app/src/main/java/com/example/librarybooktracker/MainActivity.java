package com.example.librarybooktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText bookCode, daysBorrowed;
    Button borrow;
    TextView title, author, price;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookCode = findViewById(R.id.bookCodeInputEditText);
        daysBorrowed = findViewById(R.id.daysInputEditText);
        borrow = findViewById(R.id.borrowButton);
        title = findViewById(R.id.textViewTitle);
        author = findViewById(R.id.textViewAuthor);
        price = findViewById(R.id.textViewPrice);

        db =FirebaseFirestore.getInstance();

        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                String code = bookCode.getText().toString();
                String day = daysBorrowed.getText().toString();
                int days = Integer.parseInt(daysBorrowed.getText().toString());

                if (!code.isEmpty() && !day.isEmpty()) {
                    if (code.length() == 6) {
                        if (code.substring(0,2).equals("PB")) {
                            //Toast.makeText(MainActivity.this, "Premium", Toast.LENGTH_SHORT).show();
                            performSearch();

                            // Perform desired operations with the book data
                            Premium premium = new Premium(days);
                            premium.computePayment();
                            price.setText(""+premium.returnPayment());

                            return;
                        }
                        else if (code.substring(0,2).equals("RB")) {
                            //Toast.makeText(MainActivity.this, "Regular", Toast.LENGTH_SHORT).show();
                            performSearch();

                            //Perform desired operations with the book data
                            Regular regular = new Regular(days);
                            regular.computePayment();
                            price.setText(""+regular.returnPayment());

                            return;
                        }
                        else {
                            bookCode.setError("Invalid Code!");
                        }
                    }
                    else {
                        bookCode.setError("Book Code is at least 6 characters and in 2 letter Uppercase followed by a 4 digit number 'PB0000'");
                    }

                }
                else{
                    bookCode.setError("Empty Fields!");
                }
            }
        });
    }

    private void performSearch() {
        String code = bookCode.getText().toString();

        db.collection("books")
                .whereEqualTo("bookCode", code)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (!querySnapshot.isEmpty()) {
                            // Books with the provided code exist in the database
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Boolean isBorrowed = document.getBoolean("isBorrowed");

                                if (isBorrowed != null && !isBorrowed) {
                                    String codes = document.getString("bookCode");
                                    String titles = document.getString("bookTitle");
                                    String authors = document.getString("bookAuthor");
                                    Log.d("Firestore", "Code: " + codes + ", Title: " + titles + ", Author: " + authors);

                                    // Set the book title and author to the respective TextViews
                                    title.setText(titles);
                                    author.setText(authors);

                                } else {
                                    // Book is already borrowed
                                    Toast.makeText(MainActivity.this, "Book is already borrowed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            // No book with the provided code found
                            Toast.makeText(MainActivity.this, "No book found with the provided code", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Error reading data from Firestore: " + e.getMessage());
                    }
                });
    }
}