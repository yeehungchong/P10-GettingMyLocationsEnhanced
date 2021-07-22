package com.yeehungchong.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    Button btnRefresh, btnFavorites;
    TextView tvNumberOfRecords;
    ListView lv;
    ArrayList<String> alCoord;
    ArrayAdapter<String> aaCoord;
    private String folderLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnFavorites = findViewById(R.id.btnFavorites);
        tvNumberOfRecords = findViewById(R.id.tvNumberOfRecords);
        lv = findViewById(R.id.lv);

        getRefresh();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRefresh();
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavorites();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = alCoord.get(i);
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity2.this);
                myBuilder.setMessage("Add this location in your favorite list?");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        folderLocation = getFilesDir().getAbsolutePath() + "/Folder";
                        File targetFile = new File(folderLocation, "favorites.txt");
                        File folder = new File(folderLocation);
                        if (folder.exists() == false) {
                            boolean result = folder.mkdir();
                            if (result == true) {
                                Log.d("File Read/Write", "Folder created");
                            }
                        }
                        try {
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(selectedItem + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity2.this, "Added to your favorite list", Toast.LENGTH_LONG).show();
                    }
                });
                myBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity2.this, "Will not add to your favorite list", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });
    }

    private void getRefresh() {
        alCoord = new ArrayList<String>();
        String folderLocation = getFilesDir().getAbsolutePath() + "/Folder";
        File targetFile = new File(folderLocation, "data.txt");
        if (targetFile.exists() == true) {
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    alCoord.add(line);
                    line = br.readLine();
                }
                br.close();
                reader.close();
                aaCoord = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alCoord);
                lv.setAdapter(aaCoord);
                lv.setEnabled(true);
                tvNumberOfRecords.setText("Number of records: " + alCoord.size());
            } catch (Exception e) {
                Toast.makeText(MainActivity2.this, "Failed to read!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity2.this, "No record found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFavorites() {
        alCoord = new ArrayList<String>();
        String folderLocation = getFilesDir().getAbsolutePath() + "/Folder";
        File targetFile = new File(folderLocation, "favorites.txt");
        if (targetFile.exists() == true) {
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    alCoord.add(line);
                    line = br.readLine();
                }
                br.close();
                reader.close();
                aaCoord = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alCoord);
                lv.setAdapter(aaCoord);
                lv.setEnabled(false);
                tvNumberOfRecords.setText("Number of records: " + alCoord.size());
            } catch (Exception e) {
                Toast.makeText(MainActivity2.this, "Failed to read!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity2.this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }
}