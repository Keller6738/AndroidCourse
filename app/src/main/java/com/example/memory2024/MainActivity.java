package com.example.memory2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnMemory, btnTtt, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnMemory = findViewById(R.id.btnMemory);
        btnMemory.setOnClickListener(this);
        btnTtt = findViewById(R.id.btnTtt);
        btnTtt.setOnClickListener(this);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == btnMemory.getId()) {
            intent = new Intent(this, OpenActivity.class);
            intent.putExtra("game", true);
            startActivity(intent);
        }
        if (view.getId() == btnTtt.getId()) {
            intent = new Intent(this, OpenActivity.class);
            intent.putExtra("game", false);
            startActivity(intent);
        }
        if (view.getId() == btnExit.getId()) {
            System.exit(0);
        }
    }
}