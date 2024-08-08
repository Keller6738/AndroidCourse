package com.example.memory2024.memory_game;

import java.util.ArrayList;

public class Player {
    private final String m_name;
    private int m_score;
    private final ArrayList<Integer> m_cards;

    public Player(String name) {
        this.m_name = name;
        this.m_score = 0;
        this.m_cards = new ArrayList<>();
    }

    public int getScore() {
        return m_score;
    }

    public void setScore() {
        this.m_score++;
    }

    public String getName() {
        return m_name;
    }

    public int getCard(int i) {
        return m_cards.get(i);
    }

    public int getCardsNumber() {
        return m_cards.size();
    }

    public void zeroScore() {
        m_score = 0;
    }

    public void addCouple(int card) {
        m_cards.add(card);
    }

    @Override
    public String toString() {
        return "name: " + m_name + "\n" +
                "score: " + m_score + "\n" +
                m_cards.toString();
    }
}
