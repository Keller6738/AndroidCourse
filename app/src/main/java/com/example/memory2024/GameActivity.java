package com.example.memory2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName1, tvName2;
    private ImageButton btnBack;
    private Button btnRestart, btnShowCards;

    private ImageButton[][] cards;
    private int[] pictures = new int[]{
            R.drawable.car0,
            R.drawable.car1,
            R.drawable.car2,
            R.drawable.car3,
            R.drawable.car4,
            R.drawable.car5,
            R.drawable.car6,
            R.drawable.car7,
            R.drawable.car8,
            R.drawable.car9,
    };

    private GameManager gameManager;

    private Player[] players;
    int turn = 0;

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

        tvName1 = findViewById(R.id.tvName1);
        tvName2 = findViewById(R.id.tvName2);

        btnBack = findViewById(R.id.imgBack);
        btnBack.setOnClickListener(this);

        btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(this);
        btnShowCards = findViewById(R.id.btnShowCards);
        btnShowCards.setOnClickListener(this);

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

        gameManager = new GameManager(this);
        Toast.makeText(getApplicationContext(), gameManager.printMat(), Toast.LENGTH_LONG).show();

        players = new Player[2];
        players[0] = new Player(player1);
        players[1] = new Player(player2);

        cards = new ImageButton[4][5];

        String str;
        int resId;

        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                str = "img" + i + j;
                resId = getResources().getIdentifier(str, "id", getPackageName());
                cards[i][j] = findViewById(resId);
                cards[i][j].setOnClickListener(this);
            }
        }
    }

    private void restartBoardView() {
        int resId;

        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                cards[i][j].setImageResource(R.drawable.spongebob);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == btnBack.getId()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (view.getId() == btnRestart.getId()) {
            restartBoardView();
            gameManager.restartBoard();
        } else if (view.getId() == btnShowCards.getId()) {
            Toast.makeText(getApplicationContext(), gameManager.printMat(), Toast.LENGTH_LONG).show();
        }

        boolean found = false;
        for (int i = 0; i < cards.length && !found; i++) {
            for (int j = 0; j < cards[i].length && !found; j++) {
                if (view.getId() == cards[i][j].getId()) {
                    found = true;
                    cards[i][j].setImageResource(pictures[gameManager.getPickedNum(i, j)]);
                }
            }
        }
    }
}
