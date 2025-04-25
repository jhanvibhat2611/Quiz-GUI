package com.quiz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class QuizUI {
    private JFrame frame;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;
    private JButton nextButton;
    private QuizApp quizApp;

    public QuizUI() {
        if (frame != null && frame.isVisible()) {
            return; // If window is already open, do nothing
        }
        createUI(); // If not open, create the window
    }

    private void createUI() {
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel(new GridLayout(6, 1));
        questionLabel = new JLabel("Question");
        questionPanel.add(questionLabel);

        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            optionGroup.add(options[i]);
            questionPanel.add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(new NextButtonListener());
        questionPanel.add(nextButton);

        frame.add(questionPanel, BorderLayout.CENTER);
        loadNextQuestion();
        frame.setVisible(true);
    }

    private void loadNextQuestion() {
        Question question = quizApp.getNextQuestion();
        if (question != null) {
            questionLabel.setText(question.getQuestion());
            String[] questionOptions = question.getOptions();
            for (int i = 0; i < 4; i++) {
                options[i].setText(questionOptions[i]);
            }
        } else {
            showResult();
        }
    }

    private void showResult() {
        JOptionPane.showMessageDialog(frame,
                "Quiz Over! Your score is: " + quizApp.getScore() + "/" + quizApp.getTotalQuestions(),
                "Result", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    public QuizApp getQuizApp() {
        return quizApp;
    }

    public void setQuizApp(QuizApp quizApp) {
        this.quizApp = quizApp;
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].isSelected()) {
                    quizApp.checkAnswer(i);
                    break;
                }
            }
            optionGroup.clearSelection();
            loadNextQuestion();
        }
    }

    public static void main(String[] args) {
        new QuizUI();
    }
}
