package com.learning.ruslan;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Paronym extends Task {

    private static final String split = "\\|";

    private String[] alternatives;
    private String[] variants;

    public Paronym(String word, String variants, String alternative_words) {
        super(word, -1);
        setAlternatives(alternative_words);
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
        return getVariant(new Random().nextInt(variants.length));
    }

    public String getVariant(int position) {
        return variants[position];
    }

    public void setAlternatives(String alternatives) {
        this.alternatives = alternatives.split(split);
    }

    public String[] getAlternatives() {
        return alternatives;
    }

    public String getStringAlternatives() {
        return toString(alternatives);
    }

    public List<String> getRandomAlternatives(int count) {
        List<String> list = Arrays.asList(alternatives);
        Collections.shuffle(list);

        if (list.size() <= count) return list;
        return list.subList(0, count);
    }

    public String getAlternative(int position) {
        return alternatives[position];
    }


    @NonNull
    private String toString(String[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String elem: arr)
            stringBuilder.append(elem).append("|");

        return stringBuilder.toString();
    }
}
