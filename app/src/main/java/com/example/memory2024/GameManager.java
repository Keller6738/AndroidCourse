package com.example.memory2024;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private Random rnd = new Random();

    private int[][] board;

    private GameActivity gameActivity;

    public GameManager(GameActivity activity) {
        this.board = new int[4][5];
        this.gameActivity = activity;
    }

    private void restartBoard() {
        ArrayList<Integer> boardShuffler = new ArrayList<>();
        int x;

        for (int i = 0; i < 10; i++) {
            boardShuffler.add(i);
            boardShuffler.add(i);
        }

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                x = rnd.nextInt(boardShuffler.size());
                this.board[i][j] = boardShuffler.get(x);
                boardShuffler.remove(x);
            }
        }
    }

    public int getPickNum(int i, int j) {
        return board[i][j];
    }
}
