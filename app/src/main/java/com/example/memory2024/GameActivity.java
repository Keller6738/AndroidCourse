package com.example.memory2024;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.rgb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private TextView tvName1, tvScore1, tvName2, tvScore2;
    private ImageButton btnBack;
    private Button btnRestart, btnShowCards;

    private ImageButton[][] m_cards;
    private final int[] pictures = new int[]{
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

    private GameManager m_gameManager;

    private Player[] m_players;
    int m_turn = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private int clicks = 0;
    private int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

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

        tvScore1 = findViewById(R.id.tvScore1);
        tvScore2 = findViewById(R.id.tvScore2);

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

        m_gameManager = new GameManager(this);
        Toast.makeText(getApplicationContext(), m_gameManager.printMat(), Toast.LENGTH_LONG).show();

        m_players = new Player[2];
        m_players[0] = new Player(player1);
        m_players[1] = new Player(player2);

        m_cards = new ImageButton[4][5];

        String str;
        int resId;

        for (int i = 0; i < m_cards.length; i++) {
            for (int j = 0; j < m_cards[i].length; j++) {
                str = "img" + i + j;
                resId = getResources().getIdentifier(str, "id", getPackageName());
                m_cards[i][j] = findViewById(resId);
                m_cards[i][j].setOnClickListener(this);
            }
        }

        tvName1.setBackgroundColor(GREEN);
    }

    private void restartBoardView() {
        for (int i = 0; i < m_cards.length; i++) {
            for (int j = 0; j < m_cards[i].length; j++) {
                m_cards[i][j].setImageResource(R.drawable.spongebob);
            }
        }
    }

    private void changeTurn() {
        switch (m_turn) {
            case 0:
                tvName1.setBackgroundColor(rgb(3, 169, 244));
                break;
            case 1:
                tvName2.setBackgroundColor(rgb(3, 169, 244));
        }
        m_turn = 1 - m_turn;
        switch (m_turn) {
            case 0:
                tvName1.setBackgroundColor(GREEN);
                break;
            case 1:
                tvName2.setBackgroundColor(GREEN);
        }
    }

    private void couple(int card) {
        m_players[m_turn].addCouple(card);
        m_players[m_turn].setScore();
        switch (m_turn) {
            case 0:
                tvScore1.setText("score: " + m_players[m_turn].getScore());
                break;
            case 1:
                tvScore2.setText("score: " + m_players[m_turn].getScore());
                break;
        }
        clicks = 0;
    }

    private void notCouple() {
        if (x1 != -1 && x2 != -1) {
            m_cards[x1][y1].setImageResource(R.drawable.spongebob);
            m_cards[x2][y2].setImageResource(R.drawable.spongebob);
            m_cards[x1][y1].setClickable(true);
            m_cards[x2][y2].setClickable(true);
            changeTurn();
        }
        clicks = 0;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == btnBack.getId()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (view.getId() == btnRestart.getId()) {
            restartBoardView();
            m_gameManager.restartBoard();
            m_players[0].zeroScore();
            m_players[1].zeroScore();
            tvScore1.setText("score: " + m_players[0].getScore());
            tvScore2.setText("score: " + m_players[1].getScore());
            if (m_turn == 1) {
                changeTurn();
            }
        } else if (view.getId() == btnShowCards.getId()) {
            Toast.makeText(getApplicationContext(), m_gameManager.printMat(), Toast.LENGTH_LONG).show();
        }


        boolean found = false;

        if (clicks == 0 || clicks == 1) {
            for (int i = 0; i < m_cards.length && !found; i++) {
                for (int j = 0; j < m_cards[i].length && !found; j++) {
                    if (view.getId() == m_cards[i][j].getId()) {
                        if (clicks == 0) {
                            found = true;
                            m_cards[i][j].setImageResource(pictures[m_gameManager.getPickedNum(i, j)]);
                            m_cards[i][j].setClickable(false);
                            x1 = i;
                            y1 = j;
                            clicks++;
                        } else if (clicks == 1) {
                            found = true;
                            m_cards[i][j].setImageResource(pictures[m_gameManager.getPickedNum(i, j)]);
                            m_cards[i][j].setClickable(false);
                            x2 = i;
                            y2 = j;
                            clicks++;
                            if (m_gameManager.isCouple(x1, y1, x2, y2)) {
                                couple(m_gameManager.getPickedNum(x1, y1));
                            } else {
                                handler.postDelayed(
                                        this::notCouple,
                                        2000
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}
