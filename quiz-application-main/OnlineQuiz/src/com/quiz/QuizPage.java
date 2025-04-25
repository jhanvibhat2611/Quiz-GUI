package com.quiz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class QuizPage {
    private String username; 
    private JFrame frame;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;

    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeLeftInSeconds = 8 * 60;

    public QuizPage(String username, String subject) {
        this.username = username;  
        frame = new JFrame("Quiz Application - " + subject);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen mode

        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage = Toolkit.getDefaultToolkit()
                    .getImage("quiz-application-main\\OnlineQuiz\\icons\\quiz.jpg");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setOpaque(false); // Ensure transparency
        backgroundPanel.setLayout(new BorderLayout());
        frame.add(backgroundPanel);

        // Load questions for the quiz
        questions = loadQuestions(subject);

        // Timer label
        timerLabel = new JLabel("Time Left: 08:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Robonto Mono", Font.BOLD, 24));
        timerLabel.setForeground(new Color(33, 150, 243));
        timerLabel.setBorder(BorderFactory.createEmptyBorder(80, 140, 0, 0));

        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        backgroundPanel.add(timerPanel, BorderLayout.NORTH);

        // Main quiz panel with question and options
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbcQuestion = new GridBagConstraints();
        gbcQuestion.gridy = 0;
        gbcQuestion.weighty = 0.4; // Space allocation for question
        gbcQuestion.insets = new Insets(60, 300, 10, 350);
        gbcQuestion.anchor = GridBagConstraints.NORTHWEST;

        GridBagConstraints gbcOptions = new GridBagConstraints();
        gbcOptions.gridy = 1;
        gbcOptions.weighty = 0.3; // Space allocation for options
        gbcOptions.insets = new Insets(5, 50, 10, 50);
        gbcOptions.anchor = GridBagConstraints.NORTHWEST;

        // Question Panel
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setOpaque(false);

        questionLabel = new JLabel("", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Robonto", Font.BOLD, 22));
        questionLabel.setForeground(Color.BLACK);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        questionLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Set preferred width to wrap text
        questionLabel.setPreferredSize(new Dimension(700, 100));
        questionLabel.setMaximumSize(new Dimension(700, 200));

        questionPanel.add(questionLabel);

        // Options Panel
        JPanel optionsContainer = new JPanel(new GridBagLayout());
        optionsContainer.setOpaque(false);

        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Open Sans", Font.PLAIN, 24));
            options[i].setForeground(Color.BLACK);
            options[i].setOpaque(false);
            options[i].setHorizontalAlignment(SwingConstants.LEFT);
            options[i].setFocusPainted(false);
            options[i].setBorderPainted(false);
            optionGroup.add(options[i]);

            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = i + 1;
            gbcOption.insets = new Insets(30, 350, 10, 20);
            gbcOption.anchor = GridBagConstraints.WEST;
            optionsContainer.add(options[i], gbcOption);

        }
        mainPanel.add(questionPanel, gbcQuestion);
        mainPanel.add(optionsContainer, gbcOptions);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 90, 70, 0));

        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        styleButton(previousButton);
        styleButton(nextButton);

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Sidebar Panel
        JPanel sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            }
        };
        sidebarPanel.setOpaque(true); // Ensures no opaque background
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Question Status Boxes
        JPanel questionStatusPanel = new JPanel(new GridLayout(4, 3, 5, 5)); // 4 rows, 3 columns
        questionStatusPanel.setOpaque(false); // Transparent background

        // Array to track question statuses (green = attempted, red = unattempted)
        JLabel[] questionStatusBoxes = new JLabel[10];
        for (int i = 0; i < 10; i++) {
            questionStatusBoxes[i] = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            questionStatusBoxes[i].setOpaque(true);
            questionStatusBoxes[i].setBackground(new Color(102, 187, 106));
            questionStatusBoxes[i].setBackground(new Color(255, 112, 67)); // Default: unattempted (red)
            questionStatusBoxes[i].setPreferredSize(new Dimension(20, 18));
            questionStatusBoxes[i].setForeground(Color.WHITE);
            questionStatusBoxes[i].setFont(new Font("Arial", Font.BOLD, 14));
            questionStatusPanel.add(questionStatusBoxes[i]);
        }

        // Add Question Status Panel to Sidebar
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(questionStatusPanel);

        // Legend for Status Colors
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false); // Transparent background

        // Add legend explanation
        JLabel greenLabel = new JLabel("Green: Attempted");
        greenLabel.setFont(new Font("Arial", Font.BOLD, 14));
        greenLabel.setForeground(Color.GREEN);
        greenLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 20));

        JLabel redLabel = new JLabel("Orange: Unattempted");
        redLabel.setFont(new Font("Arial", Font.BOLD, 14));
        redLabel.setForeground(Color.RED);
        redLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 150, 20));
        legendPanel.add(Box.createVerticalStrut(50));
        legendPanel.add(greenLabel);
        legendPanel.add(Box.createVerticalStrut(0));
        legendPanel.add(redLabel);
        sidebarPanel.add(legendPanel);
        sidebarPanel.add(Box.createVerticalGlue()); //

        // Add sidebar to the main frame
        frame.getContentPane().add(sidebarPanel, BorderLayout.EAST);

        nextButton.addActionListener((ActionEvent e) -> {
            int selectedOption = -1;

            // Check which option is selected
            for (int i = 0; i < 4; i++) {
                if (options[i].isSelected()) {
                    selectedOption = i;
                    break;
                }
            }

            // Update the question status box color
            if (selectedOption != -1) {
                questionStatusBoxes[currentQuestionIndex].setBackground(Color.GREEN); // Mark as attempted
                if (selectedOption == questions.get(currentQuestionIndex).getCorrectOption()) {
                    score++; // Increment score for correct answers
                }
            } else {
                questionStatusBoxes[currentQuestionIndex].setBackground(Color.RED); // Mark as unattempted
            }

            // Move to the next question or finish the quiz
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                loadNextQuestion();
            } else {
                countdownTimer.stop();
                frame.dispose();
                new ResultPage(score, questions.size(), username); // Call method to display result
            }

            // Clear selection for the next question
            optionGroup.clearSelection();
        });

        previousButton.addActionListener((ActionEvent e) -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--; // Move to the previous question
                loadNextQuestion(); // Reload the previous question
            } else {
                // Show a warning message if it's the first question
                JOptionPane.showMessageDialog(frame, "You are already on the first question!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        backgroundPanel.add(mainPanel, BorderLayout.WEST);

        frame.setResizable(false);
        frame.add(backgroundPanel);
        loadNextQuestion();
        frame.setVisible(true);

        startTimer();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Source Sans Pro", Font.BOLD, 24));
        button.setBackground(new Color(0, 140, 140));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 3, true)); // Rounded corners
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 50)); // Adjust button size for better appearance

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(34, 193, 195));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 140, 140));
            }
        });
    }

    private List<Question> loadQuestions(String subject) {
        List<Question> questions = new ArrayList<>();
        String fileName = "quiz-application-main\\OnlineQuiz\\resources/" + subject.toLowerCase() + "_questions.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    String questionText = parts[0];
                    String[] options = { parts[1], parts[2], parts[3], parts[4] };
                    int correctOption = Integer.parseInt(parts[5]);
                    questions.add(new Question(questionText, options, correctOption));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(questions);
        return questions.size() > 10 ? questions.subList(0, 10) : questions;
    }

    private void loadNextQuestion() {
        Question question = questions.get(currentQuestionIndex);
        String questionText = "Question " + (currentQuestionIndex + 1) + ":  " + question.getQuestion();
        questionLabel.setText(questionText);
        String[] optionsText = question.getOptions();
        for (int i = 0; i < 4; i++) {
            options[i].setText(optionsText[i]);
        }
    }

    private void startTimer() {
        countdownTimer = new Timer(1000, (ActionEvent e) -> {
            if (timeLeftInSeconds > 0) {
                timeLeftInSeconds--;
                updateTimerDisplay();
                if (timeLeftInSeconds % 120 == 0 && timeLeftInSeconds != 8 * 60) {
                    JOptionPane.showMessageDialog(frame,
                            "Warning: Only " + (timeLeftInSeconds / 60) + " minutes left!",
                            "Time Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(frame, "Time's up! The quiz has ended.");
                frame.dispose();
                new ResultPage(score, questions.size(), username);

            }
        });
        countdownTimer.start();
    }

    private void updateTimerDisplay() {
        int minutes = timeLeftInSeconds / 60;
        int seconds = timeLeftInSeconds % 60;
        String timeLeft = String.format("Time Left: %02d:%02d", minutes, seconds);
        timerLabel.setText(timeLeft);
    }
    
}