package com.status.learnwords;

public class Word implements Comparable{
    private String language1;
    private String language2;
    final private int range;
    private boolean mark;


    public Word(String l1, String l2, int r, boolean m){
        setLanguage1(l1);
        setLanguage2(l2);
        range = r;
        setMark(m);
    }

    public void setLanguage1(String l){
        language1 = l;
    }

    public void setLanguage2(String l){
        language2 = l;
    }

    public void setMark(boolean m){
        mark = m;
    }

    public boolean getMark(){
        return mark;
    }

    public String getLanguage1(){
        return language1;
    }

    public String getLanguage2(){
        return language2;
    }

    public int getRange(){

        return range;
    }

    @Override
    public int compareTo(Object obj){
        return ((Integer)(this.range)).compareTo((Integer)((Word)obj).range);
    }

}
