package com.example.customlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    private Random random = new Random();

    private ItemsDataAdapter adapter;

    private List<Drawable> images = new ArrayList<>();


    FileWriter dataWriter = null;

    FileReader dataReader = null;

    File dataFile;

    List<ItemData> startItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (isExternalStorageWritable()) {
            init();
        } else {
            Toast.makeText(MainActivity.this, "Файл для записи недоступен", Toast.LENGTH_SHORT).show();
        }
    }


    private void init() {
        FloatingActionButton fab = findViewById(R.id.fab);
        ListView listView = findViewById(R.id.listView);

        fillImages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomItemData();
            }
        });

        prepareContent();

        adapter = new ItemsDataAdapter(this, startItemList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showItemData(position);
                return true;
            }
        });
    }


    private void prepareContent() {
        dataFile = new File(getApplicationContext().getExternalFilesDir(null), "data.txt");
        try {
            dataReader = new FileReader(dataFile);
            Scanner scanner = new Scanner(dataReader);
            String longLine = scanner.nextLine();
            if (!longLine.trim().equals("")) {
                String[] strings = longLine.split(";");

                startItemList = new ArrayList<>();

                for (String string : strings) {
                    startItemList.add(new ItemData(images.get(random.nextInt(images.size())),
                            string,
                            "It\'s me"));
                }
            } else {
                startItemList = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                dataReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void fillImages() {
        images.add(getDrawable(R.drawable.ic_attach_money));
        images.add(getDrawable(R.drawable.ic_blur_circular));
        images.add(getDrawable(R.drawable.ic_color_lens));
        images.add(getDrawable(R.drawable.ic_healing));
        images.add(getDrawable(R.drawable.ic_insert_emoticon));
    }


    private void generateRandomItemData() {

        ItemData newItem = new ItemData(images.get(random.nextInt(images.size())),
                "Hello" + adapter.getCount(),
                "It\'s me");

        dataFile = new File(getApplicationContext().getExternalFilesDir(null), "data.txt");
        try {
            dataWriter = new FileWriter(dataFile, true);
            dataWriter.append(newItem.getTitle() + ";");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        adapter.addItem(newItem);
    }


    private void showItemData(int position) {
        ItemData itemData = adapter.getItem(position);
        Toast.makeText(MainActivity.this,
                "Title: " + itemData.getTitle() + "\n" +
                        "Subtitle: " + itemData.getSubtitle(),
                Toast.LENGTH_SHORT).show();
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}


