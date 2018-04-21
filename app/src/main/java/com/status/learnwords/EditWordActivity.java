package com.status.learnwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditWordActivity extends AppCompatActivity {

    private EditText textWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            textWord = (EditText) findViewById(R.id.TextWord);
            textWord.setText(extras.getString(DictionaryFragment.WORD_KEY));
        }
    }

    public void onOK(View view){
        Intent data = new Intent();
        data.putExtra(DictionaryFragment.WORD_KEY, textWord.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
