package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //define model for Activity content
    List<String> items;

    //declare view member vars
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define member vars with objects passed in earlier
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        //instantiate list to contents of data.txt
        loadItems();

        //definer ItemsAdapter instance on OnClickListener
        ItemsAdapter.OnLongClickListener onLongClickListener =  new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //step 1: delete item from model
                items.remove(position);
                //step 2: notify adapter of items deleted
                itemsAdapter.notifyItemRemoved(position);
                //inform user they removed an item -> Toast
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        //construct adapter and pass in list "items"
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        //formats UI vertically
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //everytime user taps ADD button complete the following action
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getText() returns an editable that toString() converts to a string
                String todoItem = etItem.getText().toString();
                //step 1:add new item to model
                items.add(todoItem);
                //step 2: notify adapter item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                //step 3: clear edit text once submitted
                etItem.setText("");
                //embellishment: notify user they added item to list successfully -> Toast
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    //add methods to Main Activity for persistence
    //persistence = consistency of updates to model between opening and closing app
    //method 1: return file in which to store items
    private File getDataFile() {
        //pass in dir of app and name of file
        return new File(getFilesDir(), "data.txt");
    }
    //method 2: load items by reading every line of data file
    private void loadItems() {
        //try to read lines out of data file and populate into arrayList then handle exception
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e( "MainActivity", "Error reading items", e);
            //creates empty list
            items = new ArrayList<>();
        }
    }
    //method 3: saves items by writing them into data file
    //called whenever change made to todoList
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}