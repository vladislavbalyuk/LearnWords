package com.status.learnwords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SharedPreferences settings;
    EditText editTextPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editTextPass = (EditText) findViewById(R.id.Pass);
        settings = getSharedPreferences("LearnWords", MODE_PRIVATE);
        editTextPass.setText(settings.getString("pass",""));
    }

    public void onOpenFileClick(View view) {
        OpenFileDialog fileDialog = new OpenFileDialog(this)
                .setFilter(".*\\.txt")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        EditText pass = (EditText) findViewById(R.id.Pass);
                        pass.setText(fileName);
                    }
                });
        fileDialog.show();    }

    public void onStartClick(View view) throws IOException {

        EditText editTextPass = (EditText) findViewById(R.id.Pass);
        String pass = (editTextPass.getText()).toString();

        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString("pass", pass);
        prefEditor.apply();

        Intent intent = new Intent(this, DictionaryActivity.class);
        intent.putExtra("pass", pass);
        startActivity(intent);
    }
}
