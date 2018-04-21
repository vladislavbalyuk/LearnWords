package com.status.learnwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    public static String pass;
    private List<Word> listWord = new ArrayList<Word>();
    private int marked;
    private Word currentWord;
    static final String WORD_KEY = "WORD";

    private int count;
    private boolean transleted, replaced, onlyMark, moved;

    private float xStart, yStart, x, y;

    private TextView textWord1, textWord2, textProgress;

    private ImageButton btnMark, btnOnlyMark;
    private ProgressBar indicator;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        transleted = false;
        replaced = false;
        onlyMark = false;
        count = 0;
        marked = 0;

        xStart = 0;
        yStart = 0;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) {
            if (view.getParent() != null){
                ((ViewGroup)view.getParent()).removeView(view);
            }
        }
        else{
            view = inflater.inflate(R.layout.fragment_dictionary,container,false);
            indicator = (ProgressBar) view.findViewById(R.id.indicator);
            textWord1 = (TextView) view.findViewById(R.id.TextWord1);
            textWord2 = (TextView) view.findViewById(R.id.TextWord2);
            textProgress = (TextView) view.findViewById(R.id.textProgress);

            btnMark = (ImageButton) view.findViewById(R.id.btnMark);
            btnOnlyMark = (ImageButton) view.findViewById(R.id.btnOnlyMark);
            btnMark.setOnClickListener(this);
            btnOnlyMark.setOnClickListener(this);
            ((ImageButton) view.findViewById(R.id.btnBack)).setOnClickListener(this);
            ((ImageButton) view.findViewById(R.id.btnForward)).setOnClickListener(this);
            ((ImageButton) view.findViewById(R.id.btnReplace)).setOnClickListener(this);
            ((ImageButton) view.findViewById(R.id.btnReverse)).setOnClickListener(this);
            ((ImageButton) view.findViewById(R.id.btnMix)).setOnClickListener(this);
            ((ImageButton) view.findViewById(R.id.btnSave)).setOnClickListener(this);


            textWord1.setOnClickListener(this);
            textWord2.setOnClickListener(this);


            View l1 = (View) view.findViewById(R.id.l1);
            l1.setOnTouchListener(this);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int id = v.getId();

        switch (id){
            case R.id.TextWord1:
                intent = new Intent(getActivity(), EditWordActivity.class);
                intent.putExtra(WORD_KEY, ((TextView)v).getText().toString());
                getActivity().startActivityForResult(intent, 1);
                break;
            case R.id.TextWord2:
                if(!((TextView)v).getText().toString().replace(" ","").replace(" ","").isEmpty()){
                    intent = new Intent(getActivity(), EditWordActivity.class);
                    intent.putExtra(WORD_KEY, ((TextView)v).getText().toString());
                    getActivity().startActivityForResult(intent, 2);
                }
                break;
            case R.id.btnBack:
                goBack(true);
                break;
            case R.id.btnForward:
                goForward(true);
                break;
            case R.id.btnReplace:
                replace();
                break;
            case R.id.btnReverse:
                reverse();
                break;
            case R.id.btnMark:
                currentWord.setMark(!currentWord.getMark());
                marked += (currentWord.getMark()? 1 : -1);
                transleted = false;
                showWord();
                break;
            case R.id.btnOnlyMark:
                onlymark();
                break;
            case R.id.btnMix:
                mix();
                break;
            case R.id.btnSave:
                saveFile();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN:
                xStart = x;
                yStart = y;
                moved = false;
                break;
            case  MotionEvent.ACTION_MOVE:
                if(Math.abs(y - yStart) > Math.abs(x - xStart)) {
                    if ((y - yStart) > 200 && !moved && count < (listWord.size() - 1)) {
                        goForward(false);
                        moved = true;
                    }
                    if ((yStart - y) > 200 && !moved && count > 0) {
                        goBack(false);
                        moved = true;
                    }
                }
                if(Math.abs(x - xStart) > Math.abs(y - yStart) &&
                        Math.abs(xStart - x) > 100 && !moved){
                    showTranslate();
                    moved = true;
                }

                break;
        }

        return true;
    }

    public void readFile() throws IOException {
        int i, c = 0;
        boolean mark;

        listWord.clear();
        marked = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pass));
            String line;
            while ((line = reader.readLine()) != null) {
                mark = false;
                i = line.indexOf("\t");
                if (!(i == -1)) {
                    String word1 = line.substring(0, i);
                    while (line.substring(i, i + 1).equals("\t") || line.substring(i, i + 1).equals(" ")) {
                        i++;
                    }
                    String word2 = line.substring(i, line.length());
                    i = word2.indexOf("*");
                    if (!(i == -1)) {
                        word2 = word2.substring(0, i);
                        mark = true;
                        marked++;
                    }
                    listWord.add(new Word(word1, word2, ++c, mark));
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        showWord();
    }

    public Word getCurrentWord() {
        return currentWord;
    }


    public  void showWord(){
        int c;
        c = count;
        while (onlyMark && !listWord.get(count).getMark() && count < (listWord.size() - 1))
            count++;
        if(onlyMark && !listWord.get(count).getMark())
            count = c;

        currentWord = listWord.get(count);
        textWord1.setText(currentWord.getLanguage1());
        if (transleted)
            textWord2.setText(currentWord.getLanguage2());
        else
            textWord2.setText(" ");
        indicator.setMax(listWord.size());
        indicator.setProgress(count);
        textProgress.setText(String.valueOf(count+1) + "/" + String.valueOf(listWord.size()) + "    marked: " + marked);
        if (currentWord.getMark())
            btnMark.setImageResource(R.drawable.star1);
        else
            btnMark.setImageResource(R.drawable.star2);
        if (onlyMark)
            btnOnlyMark.setImageResource(R.drawable.onlymark2);
        else
            btnOnlyMark.setImageResource(R.drawable.onlymark1);

    }

    public  void showTranslate(){
        transleted = true;
        showWord();
    }


    public  void replace(){
        String temp;
        for(Word word: listWord){
            temp = word.getLanguage1();
            word.setLanguage1(word.getLanguage2());
            word.setLanguage2(temp);
        }
        count = 0;
        replaced = !replaced;
        transleted = false;
        showWord();
    }

    public  void reverse(){
        Collections.reverse(listWord);
        count = 0;
        transleted = false;
        showWord();
    }

    public  void mix(){
        Collections.shuffle(listWord);
        count = 0;
        transleted = false;
        showWord();
    }

    public  void onlymark(){
        onlyMark = !onlyMark;
        count = 0;
        transleted = false;
        showWord();
        if (onlyMark)
            btnOnlyMark.setImageResource(R.drawable.onlymark2);
        else
            btnOnlyMark.setImageResource(R.drawable.onlymark1);
    }

    public  void saveFile() {
        if(replaced){
            replace();
        }
        Collections.sort(listWord);


        try{BufferedWriter writer = new BufferedWriter(new FileWriter(pass,false));
            for(Word word:listWord){
                writer.write(word.getLanguage1() + "\t\t\t" + word.getLanguage2() + (word.getMark() ? "*" : "") + "\r\n");
            }
            writer.close();
            Toast.makeText(getActivity(),"Dictionary is sucsesfully saved",Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){

            e.printStackTrace();
        }
    }

    public void goForward(boolean jump){
        int c;
        if(jump) {
            if (!onlyMark) {
                count = (count + 50) < listWord.size() ? (count + 50) : listWord.size() - 1;
            }
        }
        else {
            c = count;
            do
                count++;
            while (onlyMark && !listWord.get(count).getMark() && count < (listWord.size() - 1));

            if (onlyMark && !listWord.get(count).getMark())
                count = c;
        }
        transleted = false;
        showWord();
    }

    public void goBack(boolean jump){
        int c;
        if(jump){
            if (!onlyMark) {
                count = (count - 50) >= 0 ? (count - 50) : 0;
            }
        }
        else {
            c = count;
            do
                count--;
            while (onlyMark && !listWord.get(count).getMark() && count > 0);

            if (onlyMark && !listWord.get(count).getMark())
                count = c;
        }
        transleted = false;
        showWord();
    }

    public void setPass(String pass){
        this.pass = pass;
    }

}
