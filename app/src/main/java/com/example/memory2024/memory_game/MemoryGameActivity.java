package com.example.memory2024.memory_game;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.rgb;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memory2024.MainActivity;
import com.example.memory2024.R;

public class MemoryGameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName1, tvScore1, tvName2, tvScore2;
    private ImageButton btnBack;
    private Button btnRestart, btnShowCards;

    private Dialog dialog;
    private TextView tvDname;
    private Button btnDback;
    private ImageButton[] m_selfButtons;

    private ImageButton[][] m_cards;
    private final int[] m_pictures = new int[]{
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

    private MemoryGameManager m_gameManager;

    private Player[] m_players;
    int m_turn = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private int clicks = 0;
    private int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memory_game);
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

        m_gameManager = new MemoryGameManager();
        Log.d("board", m_gameManager.printMat());

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

        m_selfButtons = new ImageButton[10];

        tvName1.setBackgroundColor(GREEN);
    }

    private void restartBoardView() {
        for (int i = 0; i < m_cards.length; i++) {
            for (int j = 0; j < m_cards[i].length; j++) {
                m_cards[i][j].setImageResource(R.drawable.spongebob);
                m_cards[i][j].setVisibility(VISIBLE);
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
        m_cards[x1][y1].setVisibility(INVISIBLE);
        m_cards[x2][y2].setVisibility(INVISIBLE);
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

    public void playersHand() {
        Log.d("player", m_players[this.m_turn].toString());
        for (int i = 0; i < m_players[this.m_turn].getCardsNumber(); i++)
            m_selfButtons[i].setImageResource(m_pictures[m_players[this.m_turn].getCard(i)]);
        for (int i = m_players[this.m_turn].getCardsNumber(); i < 10; i++)
            m_selfButtons[i].setVisibility(View.INVISIBLE);
    }

    public void createDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_cards);
        dialog.setTitle("Choose a card");
        dialog.setCancelable(true);

        tvDname = dialog.findViewById(R.id.tvDName);
        tvDname.setText(m_players[this.m_turn].getName());
        btnDback = dialog.findViewById(R.id.btnBackToGame);
        btnDback.setOnClickListener(view -> dialog.cancel());

        String str = "";
        int resId;
        for (int i = 0; i < 10; i++) {
            str = "imgCard" + i;
            resId = getResources().getIdentifier(str, "id", getPackageName());
            m_selfButtons[i] = dialog.findViewById(resId);
            m_selfButtons[i].setVisibility(VISIBLE);
        }

        playersHand();

        dialog.show();
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
            createDialog();
        }

        boolean found = false;

        if (clicks == 0 || clicks == 1) {
            for (int i = 0; i < m_cards.length && !found; i++) {
                for (int j = 0; j < m_cards[i].length && !found; j++) {
                    if (view.getId() == m_cards[i][j].getId()) {
                        if (clicks == 0) {
                            found = true;
                            m_cards[i][j].setImageResource(m_pictures[m_gameManager.getPickedNum(i, j)]);
                            m_cards[i][j].setClickable(false);
                            x1 = i;
                            y1 = j;
                            clicks++;
                        } else if (clicks == 1) {
                            found = true;
                            m_cards[i][j].setImageResource(m_pictures[m_gameManager.getPickedNum(i, j)]);
                            m_cards[i][j].setClickable(false);
                            x2 = i;
                            y2 = j;
                            clicks++;
                            if (m_gameManager.isCouple(x1, y1, x2, y2)) {
                                handler.postDelayed(
                                        () -> {
                                            couple(m_gameManager.getPickedNum(x1, y1));
                                            if (dialog.isShowing()) {
                                                dialog.cancel();
                                            }
                                        },
                                        1000
                                );
                            } else {
                                handler.postDelayed(
                                        () -> {
                                            notCouple();
                                            if (dialog.isShowing()) {
                                                dialog.cancel();
                                            }
                                        },
                                        1000
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}
