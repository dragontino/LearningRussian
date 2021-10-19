package com.learning.ruslan;

public class Suffix extends Task {
    private String alternative;

    public Suffix(int id, String word, int position, String alternative_letter) {
        super(id, word, position);
        setAlternative(alternative_letter);
    }

    public Suffix(String word, int position, String alternative_letter) {
        this(0, word, position, alternative_letter);
    }

//    public void set(String word, int position, String alternative_letter) {
//        setWord(word);
//        setPosition(position);
//        setAlternative(alternative_letter);
//    }



    public String getAlternative() {
        return alternative;
    }

    public void setAlternative(String alternativeLetter) {
        this.alternative = alternativeLetter;
    }
}
