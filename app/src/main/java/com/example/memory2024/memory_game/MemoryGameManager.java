package com.example.memory2024.memory_game;

import java.util.ArrayList;
import java.util.Random;

public class MemoryGameManager {
    private Random rnd = new Random();

    private int[][] m_board;


    public MemoryGameManager() {
        this.m_board = new int[4][5];
        restartBoard();
    }

    public void restartBoard() {
        ArrayList<Integer> boardShuffler = new ArrayList<>();
        int x;

        for (int i = 0; i < 10; i++) {
            boardShuffler.add(i);
            boardShuffler.add(i);
        }

        for (int i = 0; i < this.m_board.length; i++) {
            for (int j = 0; j < this.m_board[i].length; j++) {
                x = rnd.nextInt(boardShuffler.size());
                this.m_board[i][j] = boardShuffler.get(x);
                boardShuffler.remove(x);
            }
        }
    }

    public String printMat() {
        String str = "";
        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                str += m_board[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    }

    public int getPickedNum(int i, int j) {
        return m_board[i][j];
    }

    public boolean isCouple(int i1, int j1,
                            int i2, int j2) {
        if (i1 == -1 || i2 == -1)
            return false;
        return getPickedNum(i1, j1) == getPickedNum(i2, j2);
    }
}
