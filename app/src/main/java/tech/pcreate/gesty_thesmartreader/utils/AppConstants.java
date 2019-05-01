/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.utils;

import android.os.Environment;

public class AppConstants {
    public static final int EPUB_RQCODE = 101;
    public static final int NEXT = 1;
    public static final int PREVIOUS = 2;

    public static final int SLOW = 97;
    public static final int FAST = 67;
    public static final int PRETTY_FAST = 49;

    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Gesty Library/");

}
