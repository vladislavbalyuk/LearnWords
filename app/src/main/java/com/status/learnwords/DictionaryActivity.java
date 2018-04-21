package com.status.learnwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class DictionaryActivity extends AppCompatActivity {

    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (savedInstanceState == null) {
            DictionaryFragment fragment = (DictionaryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_dictionary);

            Intent intent = getIntent();
            fragment.setPass(intent.getStringExtra("pass"));
            try {
                fragment.readFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DictionaryFragment fragment = (DictionaryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_dictionary);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                fragment.getCurrentWord().setLanguage1(data.getStringExtra(DictionaryFragment.WORD_KEY));
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                fragment.getCurrentWord().setLanguage2(data.getStringExtra(DictionaryFragment.WORD_KEY));
            }
        }
        fragment.showWord();
    }

}
