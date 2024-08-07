package com.example.memory2024.tic_tac_toe;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.rgb;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class XOGameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNameX, tvNameO;
    private Button btnRestart;
    private ImageButton btnBack;

    private ImageButton[][] m_board;

    private XOGameManager m_gameManager = new XOGameManager();
    private int m_turn = 0;
    private int[][] m_straight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xogame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNameX = findViewById(R.id.tvNameX);
        tvNameO = findViewById(R.id.tvNameO);

        Intent intent = getIntent();
        Bundle extras;
        String player1 = "!!!!", player2 = "$$$$";

        if (intent != null && intent.getExtras() != null) {
            extras = intent.getExtras();
            player1 = extras.getString("NAME1");
            player2 = extras.getString("NAME2");
        }

        tvNameX.setText(player1);
        tvNameO.setText(player2);

        btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        m_board = new ImageButton[3][3];

        String str;
        int resId;
        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                str = "img" + i + j;
                resId = getResources().getIdentifier(str, "id", getPackageName());
                m_board[i][j] = findViewById(resId);
                m_board[i][j].setOnClickListener(this);
            }
        }

        tvNameX.setBackgroundColor(GREEN);
    }

    private void restartBoardView() {
        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                m_board[i][j].setImageResource(R.drawable.white);
            }
        }
    }

    private void changeTurn() {
        switch (m_turn) {
            case 0:
                tvNameX.setBackgroundColor(rgb(3, 169, 244));
                break;
            case 1:
                tvNameO.setBackgroundColor(rgb(3, 169, 244));
        }
        m_turn = 1 - m_turn;
        switch (m_turn) {
            case 0:
                tvNameX.setBackgroundColor(GREEN);
                break;
            case 1:
                tvNameO.setBackgroundColor(GREEN);
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
            m_gameManager.restartBoard();
            if (m_turn == 1) {
                changeTurn();
            }
        }

        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                if (view.getId() == m_board[i][j].getId()) {
                    if (m_gameManager.getValue(i, j) == -1) {
                        if (m_turn == 0) {
                            m_board[i][j].setImageResource(R.drawable.x);
                        } else {
                            m_board[i][j].setImageResource(R.drawable.o);
                        }
                        m_gameManager.changeValue(i, j, m_turn);
                        if (m_gameManager.checkWin(m_turn)) {
                            new AlertDialog.Builder(this)
                                    .setTitle("WIN!!!")
                                    .setMessage(m_turn == 0 ? "X won!!" : "O won!!")
                                    .setNeutralButton("ok", (dialogInterface, i1) -> {
                                    })
                                    .setIcon(R.drawable.trophy)
                                    .show();
                            m_straight = m_gameManager.getStraight(m_turn);
                            for (int k = 0; k < 3; k++) {
                                m_board[m_straight[0][k]][m_straight[1][k]].setImageResource(
                                        m_turn == 0 ? R.drawable.green_x : R.drawable.green_o
                                );
                            }
                        } else if (m_gameManager.checkDraw() && !m_gameManager.checkWin(m_turn)) {
                            new AlertDialog.Builder(this)
                                    .setTitle("DRAW!!!")
                                    .setMessage("you're boring")
                                    .setNeutralButton("ok", (dialogInterface, i1) -> {
                                    })
                                    .setIcon(R.drawable.meh)
                                    .show();
                        }
                        changeTurn();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("ERROR!!")
                                .setMessage("There is already character in this place.")
                                .setNeutralButton("ok", (dialogInterface, i1) -> {
                                })
                                .setIcon(R.drawable.error)
                                .show();
                    }
                }
            }
        }
    }
}