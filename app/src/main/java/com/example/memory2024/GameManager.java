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
        ArrayList<Integer> nums = new ArrayList<>();
        int x;

        for (int i = 0; i < 10; i++) {
            nums.add(i);
            nums.add(i);
        }

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                x = rnd.nextInt(nums.size());
                this.board[i][j] = nums.get(x);
                nums.remove(x);
            }
        }
    }

    public int getPickNum(int i, int j) {
        return board[i][j];
    }
}
