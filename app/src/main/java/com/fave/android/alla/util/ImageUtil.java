package com.fave.android.alla.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by seungyeop on 2017-11-14.
 */

public class ImageUtil {

    //이미지 경로 한번 더 확인
    public static String getPath(Context context, Uri uri) {
        String imagePath = "";
        //제대로 된 경로가 아닐 때//어떻게??
        if (!(uri.getLastPathSegment().contains("."))) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //Log.v("test", "filePathColumn:" + filePathColumn);
            Cursor cursorImage = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursorImage.moveToFirst();
            imagePath = cursorImage.getString(cursorImage.getColumnIndex(filePathColumn[0]));
            cursorImage.close();
        } else {
            //맞으면 그대로 return
            imagePath = uri.getPath();
        }
        //Log.v("test", "imagePath:" + imagePath);
        return imagePath;
    }

    //비트맵 크기를 알려줌
    private static BitmapFactory.Options getBitmapSize(File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        return options;
    }
}
