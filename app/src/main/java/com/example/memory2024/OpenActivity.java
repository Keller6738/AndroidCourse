package com.example.memory2024;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memory2024.memory_game.MemoryGameActivity;
import com.example.memory2024.tic_tac_toe.XOGameActivity;

import java.util.Locale;

public class OpenActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPlay;
    private ImageButton btnBack;
    private EditText edName1, edName2;
    private TextView tvTitle;

    private boolean memoryGame;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPlay = findViewById(R.id.btmStart);
        btnPlay.setOnClickListener(this);

        btnBack = findViewById(R.id.imgBack);
        btnBack.setOnClickListener(this);

        edName1 = findViewById(R.id.etName1);
        edName2 = findViewById(R.id.etName2);

        tvTitle = findViewById(R.id.tvTitle);

        Intent intent = getIntent();
        Bundle extras;

        if (intent != null && intent.getExtras() != null) {
            extras = intent.getExtras();
            memoryGame = extras.getBoolean("game");
        }

        if (!memoryGame) {
            tvTitle.setText("Tic Tac Toe");
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i == TextToSpeech.SUCCESS) {
                int lang = textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        String name1 = edName1.getText().toString().trim();
        String name2 = edName2.getText().toString().trim();

        if (view.getId() == btnPlay.getId()) {
            if (name1.isEmpty() ||
                    name2.isEmpty() ||
                    name1.equals(name2)) {
                new AlertDialog.Builder(this)
                        .setTitle("ERROR!!")
                        .setMessage("User names are wrong!")
                        .setNeutralButton("ok", (dialogInterface, i) -> {
                        })
                        .setIcon(R.drawable.error)
                        .show();
            } else {
                textToSpeech.speak("Good Luck, " + name1 + "and" + name2,
                        TextToSpeech.QUEUE_FLUSH, null);

                intent = new Intent(
                        this,
                        memoryGame?
                        MemoryGameActivity.class : XOGameActivity.class
                );
                intent.putExtra("NAME1", name1);
                intent.putExtra("NAME2", name2);
                startActivity(intent);
            }
        } else if (view.getId() == btnBack.getId()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
