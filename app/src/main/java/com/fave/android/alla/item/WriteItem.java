package com.fave.android.alla.item;

import java.io.File;

/**
 * Created by seungyeop on 2017-11-15.
 */

public class WriteItem {
    private File imageFile;
    private String content;

    public WriteItem(File imageFile, String content){
        this.imageFile = imageFile;
        this.content = content;
    }

    public WriteItem(File imageFile){
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
