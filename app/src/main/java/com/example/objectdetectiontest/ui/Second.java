package com.example.objectdetectiontest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.objectdetectiontest.R;
import com.example.objectdetectiontest.databinding.ActivitySecondBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Second extends AppCompatActivity {

    private ActivitySecondBinding mainBinding;

    private ExecutorService workerThread = Executors.newCachedThreadPool();
    private Handler workerHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivitySecondBinding.inflate(getLayoutInflater());

        setContentView(mainBinding.getRoot());

        mainBinding.processorBtn.setOnClickListener(v -> {

        });
    }
}