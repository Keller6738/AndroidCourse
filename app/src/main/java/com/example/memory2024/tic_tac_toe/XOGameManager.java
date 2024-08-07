package com.example.memory2024.tic_tac_toe;

public class XOGameManager {
    private int[][] m_board;

    public XOGameManager() {
        m_board = new int[3][3];
        restartBoard();
    }

    public void restartBoard() {
        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                m_board[i][j] = -1;
            }
        }
    }

    public String printBoard() {
        String str = "";
        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                str += m_board[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    }

    public int getValue(int i, int j) {
        return m_board[i][j];
    }

    public void changeValue(int i, int j, int turn) {
        if (turn == 0) {
            m_board[i][j] = 0;
        } else {
            m_board[i][j] = 1;
        }
    }

    public boolean checkWin(int turn) {
        int count = 0;

        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                if (m_board[i][j] == turn) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
            count = 0;
        }

        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board.length; j++) {
                if (m_board[j][i] == turn) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
            count = 0;
        }

        for (int i = 0; i < m_board.length; i++) {
            if (m_board[i][i] == turn) {
                count++;
            }
        }
        if (count == 3) {
            return true;
        }

        count = 0;

        for (int i = 0; i < m_board.length; i++) {
            if (m_board[i][2 - i] == turn) {
                count++;
            }
        }
        if (count == 3) {
            return true;
        }

        return false;
    }

    public boolean checkDraw() {
        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                if (m_board[i][j] == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public int[][] getStraight(int winner) {
        int count = 0;
        int[][] straight;

        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board[i].length; j++) {
                if (m_board[i][j] == winner) {
                    count++;
                }
            }
            if (count == 3) {
                straight = new int[][]{
                        {i, i, i},
                        {0, 1, 2}
                };
                return straight;
            }

            count = 0;
        }

        for (int i = 0; i < m_board.length; i++) {
            for (int j = 0; j < m_board.length; j++) {
                if (m_board[j][i] == winner) {
                    count++;
                }
            }
            if (count == 3) {
                straight = new int[][]{
                        {0, 1, 2},
                        {i, i, i}
                };
                return straight;
            }

            count = 0;
        }

        for (int i = 0; i < m_board.length; i++) {
            if (m_board[i][i] == winner) {
                count++;
            }
        }
        if (count == 3) {
            straight = new int[][]{
                    {0, 1, 2},
                    {0, 1, 2}
            };
            return straight;
        }

        count = 0;

        for (int i = 0; i < m_board.length; i++) {
            if (m_board[i][2 - i] == winner) {
                count++;
            }
        }
        if (count == 3) {
            straight = new int[][]{
                    {0, 1, 2},
                    {2, 1, 0}
            };
            return straight;
        }

        return new int[0][0];
    }
}
