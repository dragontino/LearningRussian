package com.learning.ruslan.databases;

import com.learning.ruslan.Suffix;

public class Paronym extends Suffix {
    private String[] variants;

    public Paronym(String word, String alternative_word, String[] variants) {
        super(word, 0, alternative_word);
        setVariants(variants);
    }

    public void setVariants(String[] variants) {
        this.variants = variants;
    }

    public String getVariants() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String elem: variants)
            stringBuilder.append(elem).append(" ");

        return stringBuilder.toString();
    }
}
