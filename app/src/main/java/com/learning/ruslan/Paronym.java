package com.learning.ruslan;

import androidx.annotation.NonNull;

import java.util.Random;

public class Paronym extends Task {

    private static final String split = "\\|";

    private String[] alternative;
    private String[] variants;

    public Paronym(String word, String variants, String alternative_words) {
        super(word, -1);
        setAlternative(alternative_words);
        setVariants(variants);
    }

    public Paronym(String[] params) {
        this(params[0], params[1], params[2]);
    }

    public void setVariants(String variants) {
        this.variants = variants.split(split);
    }

    public String[] getVariants() {
        return variants;
    }

    public String getStringVariants() {
        return toString(variants);
    }

    public String getRandomVariant() {
        int index = (new Random()).nextInt(variants.length);
        return variants[index];
    }

    public void setAlternative(String alternative) {
        this.alternative = alternative.split(split);
    }

    public String getAlternative() {
        return toString(alternative);
    }


    @NonNull
    private String toString(String[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String elem: arr)
            stringBuilder.append(elem).append("|");

        return stringBuilder.toString();
    }
}
