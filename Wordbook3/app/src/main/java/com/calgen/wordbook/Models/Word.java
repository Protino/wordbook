package com.calgen.wordbook.Models;


/**
 * Created by Gurupad on 02-Jul-15.
 */
public class Word {
    public String word;
    public String description;
    public byte type;
    public String etymo;
    public String example;

    public Word(String word, String description,byte type,String etymo,String example) {
        this.word = word;
        this.description = description;
        this.type = type;
        this.etymo = etymo;
        this.example = example;
    }
}