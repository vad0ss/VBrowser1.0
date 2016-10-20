package ru.geekbrains.vbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import ru.geekbrains.vbrowser.adapters.ListRecyclerAdapter;

public class VBrowserFavoritesActivity extends AppCompatActivity implements View.OnClickListener {

    private String FAVORITES_FILE_NAME;
    private RecyclerView recyclerViewFavorites;
    private Resources res;

    private Button clearFavoritesBtn, backBrowserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vbrowser_favourites);
        initViews();
        initVars();
        setListeners();
        inflateFavoritesView();
    }

    private void initViews(){
        clearFavoritesBtn = (Button)findViewById(R.id.clearFavoritesBtn);
        backBrowserBtn = (Button)findViewById(R.id.backFBrowserBtn);
    }

    private void initVars(){
        res = getResources();
        FAVORITES_FILE_NAME = res.getString(R.string.favorites_file_name);
    }

    private void setListeners(){
        clearFavoritesBtn.setOnClickListener(this);
        backBrowserBtn.setOnClickListener(this);
    }

    private void inflateFavoritesView(){
        ArrayList<String> favorites = openFavoritesFile();
        recyclerViewFavorites = (RecyclerView)findViewById(R.id.rvf);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        ListRecyclerAdapter favoritesAdapter = new ListRecyclerAdapter(favorites,this);

        recyclerViewFavorites.setLayoutManager(layoutManager);
        recyclerViewFavorites.setAdapter(favoritesAdapter);
    }

    private ArrayList<String> openFavoritesFile(){
        ArrayList<String> favoritesStringArray = new ArrayList<String>();
        try {
            InputStream inputStream = openFileInput(FAVORITES_FILE_NAME);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;

                while ((line = reader.readLine()) != null) {
                    favoritesStringArray.add(line);
                }

                inputStream.close();
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    res.getText(R.string.exception) + t.toString(), Toast.LENGTH_LONG).show();
        }
        return favoritesStringArray;
    }

    private void clearURLFavorites(){
        try {
            OutputStream outputStream = openFileOutput(FAVORITES_FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    res.getText(R.string.exception) + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.clearFavoritesBtn:
                clearURLFavorites();
                inflateFavoritesView();
                break;
            case R.id.backFBrowserBtn:
                Intent intent = new Intent(VBrowserFavoritesActivity.this,VBrowserActivity.class);
                startActivity(intent);
                break;
        }
    }
}
