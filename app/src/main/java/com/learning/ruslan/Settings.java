package com.learning.ruslan;

public class Settings {

    private int id;
    private boolean isChecked;
    private int pause, questions;
    private String theme;
    private String language;

    public Settings() {
        this.id = 0;
        this.isChecked = true;
        this.pause = 1600;
        this.theme = Support.THEME_LIGHT;
        this.questions = 10;
        this.language = Support.RUSSIAN;
    }

    public void set(boolean isChecked, int pause, String theme, int questions, String language) {
        setChecked(isChecked);
        setPause(pause);
        setTheme(theme);
        setQuestions(questions);
        setLanguage(language);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
