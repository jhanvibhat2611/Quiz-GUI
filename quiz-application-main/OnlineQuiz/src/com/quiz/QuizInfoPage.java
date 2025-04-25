package com.quiz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class QuizInfoPage {
    public QuizInfoPage(String username, String subject) {
        // Create the JFrame for the Quiz Info Page
        JFrame frame = new JFrame("Quiz Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("quiz-application-main\\OnlineQuiz\\icons\\quizinfo1.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null); // Set to absolute layout
        frame.setContentPane(backgroundPanel);
        // Title label
        JLabel shadowLabel = new JLabel("Quiz Details", JLabel.CENTER);
        shadowLabel.setFont(new Font("Poppins", Font.BOLD, 70));
        shadowLabel.setForeground(new Color(64, 64, 64)); // Gray for shadow
        shadowLabel.setBounds(360, 10, 800, 100);
        JLabel titleLabel = new JLabel("Quiz Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 60));
        titleLabel.setForeground(new Color(255, 223, 0)); // Gold color
        titleLabel.setBounds(350, 0, 800, 100);
        backgroundPanel.add(titleLabel);
        // Details Panel with Border
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        detailsPanel.setBackground(new Color(255, 255, 255, 180)); // Semi-transparent white
        detailsPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        detailsPanel.setBounds(100, 150, 600, 200);

        // Quiz Info Labels
        JLabel numQuestionsLabel = new JLabel("Number of Questions: 10", JLabel.LEFT);
        numQuestionsLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        numQuestionsLabel.setForeground(new Color(255, 255, 255));
        numQuestionsLabel.setBounds(250, 130, 400, 30);
        backgroundPanel.add(numQuestionsLabel);

        JLabel maxMarksLabel = new JLabel("Maximum Marks: 100", JLabel.LEFT);
        maxMarksLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        maxMarksLabel.setForeground(new Color(255, 255, 255));
        maxMarksLabel.setBounds(250, 170, 400, 30);
        backgroundPanel.add(maxMarksLabel);

        JLabel timeLabel = new JLabel("Time Given: 8 Minutes", JLabel.LEFT);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        timeLabel.setForeground(new Color(255, 255, 255));
        timeLabel.setBounds(250, 210, 400, 30);
        backgroundPanel.add(timeLabel);

        JPanel instructionsPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 180)); // Semi-transparent white
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40)); // Rounded corners
            }
        };
        instructionsPanel.setOpaque(false); // Transparent panel
        instructionsPanel.setBounds(230, 260, 1000, 340); // Panel size
        // Heading for instructions
        JLabel instructionsHeading = new JLabel("Instructions", JLabel.CENTER);
        instructionsHeading.setFont(new Font("Arial", Font.BOLD, 32));
        instructionsHeading.setForeground(new Color(0, 51, 102)); // Dark blue color
        instructionsHeading.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding
        instructionsPanel.add(instructionsHeading, BorderLayout.NORTH);

        // Instructions text area
        JTextArea instructionsArea = new JTextArea(
                "1. You will be presented with multiple-choice questions. Choose the correct answer from the given options..\n\n"
                        +
                        "2. The quiz consists of 10 questions, each carrying equal marks.There will be no negative marks for incorrect answers.\n\n"
                        +
                        "3. You have a total of 8 minutes to complete the quiz. Keep an eye on the countdown timer.\n\n"
                        +
                        "4. Once you have answered all questions, click the \"Submit\" button to view your results.\n\n"
                        +
                        "5. Press the \"Start the Quiz\" button when you're ready to begin. Good luck!");
        instructionsArea.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionsArea.setForeground(new Color(0, 0, 0)); // Black text
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setOpaque(false); // Transparent background for the text area
        instructionsArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        instructionsArea.setFocusable(false); // Prevents focus and standing pointer
        instructionsPanel.add(instructionsArea, BorderLayout.CENTER);

        // Add the instructions panel to the background
        backgroundPanel.add(instructionsPanel);

        // Start Quiz Button
        JButton startQuizButton = new JButton("Start the Quiz") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50); // Rounded corners
                super.paintComponent(g2);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // No default background fill
            }
        };
        startQuizButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        startQuizButton.setForeground(Color.WHITE);
        startQuizButton.setBackground(new Color(255, 0, 255)); // Magenta color
        startQuizButton.setBounds(620, 620, 200, 50);
        startQuizButton.setFocusPainted(false);
        startQuizButton.setBorder(BorderFactory.createEmptyBorder()); // Modern button look
        startQuizButton.setContentAreaFilled(false);
        backgroundPanel.add(startQuizButton);

        startQuizButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startQuizButton.setBackground(new Color(138, 43, 226)); // Violet on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startQuizButton.setBackground(new Color(255, 0, 255)); // Magenta when not hovering
            }
        });

        // ActionListener for Start Quiz Button
        startQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window
                frame.dispose();
                // Start the quiz by opening the QuizPage (pass the username)
                new QuizPage(username, subject); // Assuming you have a QuizPage class that starts the quiz
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Test the Quiz Info Page with a dummy username
        SwingUtilities.invokeLater(() -> new QuizInfoPage("User123", "Math"));
    }
}
