package com.learning.ruslan;

public class Assent extends Task {
    private boolean isChecked;
//    private int id;

//    public Assent() {
//        this("", -1, false, 0);
//    }

    public Assent(String word, int positionOfRightLetter, boolean isChecked) {
        this(word, positionOfRightLetter, isChecked, 0);
    }

    public Assent(String word, int position, boolean isChecked, int id) {
        super(id, word, position);
        setChecked(isChecked);
    }

    public void set(String word, int positionOfRightLetter, boolean isChecked) {
        setWord(word);
        setPosition(positionOfRightLetter);
        setChecked(isChecked);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
