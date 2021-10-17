package com.learning.ruslan;

public class Task {
    private int id;
    private String word;
    private int position;

    public Task(String word) {
        this(0, word, 0);
    }

    public Task(String word, int position) {
        this(0, word, position);
    }

    public Task(int id, String word, int position) {
        setId(id);
        setWord(word);
        setPosition(position);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
