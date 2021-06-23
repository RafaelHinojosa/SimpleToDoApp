package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    // Class for MainActivity (Register, Edit, Remove)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        // Load Items from the .txt
        loadItems();

        // Removes an element when long clicked
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
          @Override
          public void onItemLongClicked(int position) {
              // Delete item from the model (list)
              items.remove(position);
              // Notify the adapter
              itemsAdapter.notifyItemRemoved(position);
              Toast.makeText(getApplicationContext(), "Item was deleted", Toast.LENGTH_SHORT).show();
              saveItems();
          }
        };

        // Edit: Communicates with Edit Activity by making an Intent when Item is clicked
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);

                // Intent: Request to Android System. It will open the new view
                // Create new activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                // Pass the data to intent
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);
                //Display the activity
                startActivityForResult(intent, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Add Item to the List
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write an item
                String todoItem = etItem.getText().toString();

                // Check if there is something written
                if(todoItem.length() > 0) {
                    // Add the item to the list
                    items.add(todoItem);
                    // Notify the adapter in the last position
                    itemsAdapter.notifyItemInserted(items.size()-1);
                    etItem.setText("");
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
                // If not, notify the user and do nothing
                else {
                    Toast.makeText(getApplicationContext(), "Write something to add", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Updates a specific element after clicking Save in the Edit view (after edit activity->set onclick listener)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // Returns a new File with the saved data from a txt
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Saves each line of the .txt in an ArrayList
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            // Check if any item has no text
            for(String item : items) {
                if(item.length() < 1) {
                    items.remove(item);
                }
            }
        }
        catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // Saves each element of items in the .txt
    private void saveItems() {
        try{
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}



































