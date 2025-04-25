package com.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizApp {
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;

    public QuizApp() {
        questions = new ArrayList<>();
        loadQuestions();
        currentQuestionIndex = 0;
        score = 0;
    }

    private void loadQuestions() {
    }

    public Question getNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex++);
        }
        return null;
    }

    public void checkAnswer(int selectedOption) {
        if (selectedOption == questions.get(currentQuestionIndex - 1).getCorrectOption()) {
            score++;
        }
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
