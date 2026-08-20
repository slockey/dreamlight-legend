package com.slockey.dreamlightlegend.engine;

/*
 * Sample Java file by Huw Collingbourne
 * 
 * This code (and other sample code) accompanies the book 
 *    "The Little Book of Adventure Game Programming In Java"
 * Source code can be downloaded from:
 *     http://www.bitwisebooks.com
 */
public class WordAndType {

    private String word;
    private WordType wordtype;

    public WordAndType(String wd, WordType wt) {
        word = wd;
        wordtype = wt;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public WordType getWordtype() {
        return wordtype;
    }

    public void setWordtype(WordType wordtype) {
        this.wordtype = wordtype;
    }

}
