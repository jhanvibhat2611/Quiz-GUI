package com.quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.Statement;


import java.awt.*;
import javax.swing.*;

public class ResultPage {
    public ResultPage(int score, int totalQuestions, String username) {
        JFrame frame = new JFrame("Quiz Application - Result");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Background Panel with background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon("quiz-application-main\\OnlineQuiz\\icons\\resultpage.jpg").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        frame.setContentPane(backgroundPanel);

        // Spacer
        backgroundPanel.add(Box.createVerticalStrut(100));

        // Result label
        JLabel resultLabel = new JLabel("You scored " + score + " out of " + totalQuestions);
        resultLabel.setFont(new Font("Serif", Font.BOLD, 55));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0)); // top, left, bottom, right

        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(resultLabel);

        // Message label
        JLabel messageLabel = new JLabel(getMessage(score, totalQuestions));
        messageLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
        messageLabel.setForeground(new Color(255, 223, 51));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBounds(300,300,600,50);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(messageLabel);

        // Spacer before buttons
        backgroundPanel.add(Box.createVerticalStrut(70));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton exitButton = createStyledButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        JButton backButton = createStyledButton("Go Back to Subject Selection");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            frame.dispose();
            new HomePage(username);
        });

        JButton leaderboardButton = createStyledButton("Leaderboard");
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> {
            frame.dispose();
            new LeaderboardPage(username);
        });

        // Add buttons
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(leaderboardButton);

        backgroundPanel.add(buttonPanel);

        // Save score
        saveScoreToDatabase(username, score);

        // Show frame
        frame.setVisible(true);
    }

    private void saveScoreToDatabase(String username, int score) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {
            Class.forName("org.sqlite.JDBC");
    
            String createTable = "CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, score INTEGER NOT NULL)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTable);
            }
    
            // Delete existing scores for this user
            String deleteOldScores = "DELETE FROM scores WHERE username = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteOldScores)) {
                deleteStmt.setString(1, username);
                deleteStmt.executeUpdate();
            }
    
            // Insert new score
            String insertScore = "INSERT INTO scores(username, score) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertScore)) {
                pstmt.setString(1, username);
                pstmt.setInt(2, score);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(72, 61, 139));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(138, 43, 226));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(72, 61, 139));
            }
        });

        return button;
    }

    private String getMessage(int score, int totalQuestions) {
        double percentage = ((double) score / totalQuestions) * 100;
        if (percentage == 100) return "Excellent! Perfect Score!";
        else if (percentage >= 75) return "Great Job! Keep it up!";
        else if (percentage >= 50) return "Good Effort! You can do better!";
        else return "Don't give up! Try again!";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResultPage(8, 10, "User123"));
    }
}

