package com.example.tushar_app;

import android.provider.BaseColumns;

public class names {
    private names() {}
    public static final class Entries implements BaseColumns{
        public static final String TABLE_NAME="entrytbl";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_DATE ="date";
        public static final String COLUMN_TIME ="time";
    }
}
