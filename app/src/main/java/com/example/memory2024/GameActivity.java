package com.example.memory2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName1, tvName2;
    private ImageButton btnBack;

    private ImageView[][] cards;

    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gameManager = new GameManager(this);

        tvName1 = findViewById(R.id.tvName1);
        tvName2 = findViewById(R.id.tvName2);

        btnBack = findViewById(R.id.imgBack);
        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extras;
        String player1 = "!!!!", player2 = "$$$$";

        if (intent != null && intent.getExtras() != null) {
            extras = intent.getExtras();
            player1 = extras.getString("NAME1");
            player2 = extras.getString("NAME2");
        }

        tvName1.setText(player1);
        tvName2.setText(player2);

        this.cards = new ImageView[4][5];
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == btnBack.getId()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
