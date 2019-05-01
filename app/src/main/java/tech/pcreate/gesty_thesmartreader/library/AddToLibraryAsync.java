/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.library;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static tech.pcreate.gesty_thesmartreader.utils.AppConstants.PATH;

public class AddToLibraryAsync extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {

        String srcDir = strings[0];
        String dstDir = PATH + File.separator;
        String name = srcDir.substring(srcDir.lastIndexOf('/')+1);

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir);
//            Log.e("dst = ", dst.getAbsolutePath());
//            Log.e("Src = ", src.getAbsolutePath());

            /*Copyright (c) @ 2019 Yash Prakash
             */
            copy(src, dst, name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static void copy(File src, File dst, String name) throws IOException {
        if (!dst.exists()) {
            if (!dst.mkdir()) {
            }
        }/*Copyright (c) @ 2019 Yash Prakash
         */

        File expFile = new File(dst.getPath() + File.separator  + name);
        if (!expFile.exists()) {

            FileChannel inChannel = null;
            FileChannel outChannel = null;

            try {
                inChannel = new FileInputStream(src).getChannel();
                outChannel = new FileOutputStream(expFile).getChannel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        }


    }
}