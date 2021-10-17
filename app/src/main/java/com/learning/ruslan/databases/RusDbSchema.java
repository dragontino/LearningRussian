package com.learning.ruslan.databases;

public class RusDbSchema {
    public static final class AssentTable {
        public static final String NAME = "assents";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String WORD = "word";
            public static final String POSITION = "position_assent";
            public static final String CHECKED = "isChecked";
        }
    }

    public static final class SupportTable {
        public static final String NAME = "support";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String CHECKED = "isChecked";
            public static final String PAUSE = "pause";
            public static final String THEME = "theme";
            public static final String QUESTIONS = "questions";
            public static final String LANGUAGE = "language";
        }
    }

    public static final class SuffixTable {
        public static final String NAME = "suffixes";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String WORD = "word";
            public static final String POSITION = "position_suffix";
            public static final String ALTERNATIVE = "alternative_later";
        }
    }

    public static final class ParonymTable {
        public static final String NAME = "paronyms";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String WORD = "word";
            public static final String VARIANTS = "variants";
            public static final String ALTERNATIVE = "alternative_words";
        }
    }
}
