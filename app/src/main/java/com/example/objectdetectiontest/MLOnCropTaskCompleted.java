package com.example.objectdetectiontest;

import android.graphics.Bitmap;

public interface MLOnCropTaskCompleted {
    void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int i, int i2);
}
