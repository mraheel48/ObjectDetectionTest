package com.example.objectdetectiontest;

import android.content.Context;
import android.graphics.Bitmap;

public interface DeeplabInterface {
    int getInputSize();

    void initialize(Context context);

    boolean isInitialized();

    Bitmap segment(Bitmap bitmap);
}
