package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItemEdit);
        btnSave = findViewById(R.id.btnSave);

        // Title of the view
        getSupportActionBar().setTitle("Edit item");

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // Save changes by clicking the button Save... Writes data into the intent to communicate back with MainActivity and go to the OnClickListener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent with edited text
                Intent intent = new Intent();

                String itemText = etItem.getText().toString();
                // If there is text
                if(itemText.length() > 0) {
                    // Pass data to the intent
                    intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                    intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                    // Set results for intent
                    setResult(RESULT_OK, intent);

                    // Finish and go back to main menu
                    finish();
                }
                // If not, notify the user and do nothing
                else {
                    Toast.makeText(getApplicationContext(), "Write something to update the item!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}