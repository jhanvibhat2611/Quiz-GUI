package com.quiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPage {
    private JFrame frame;
    private JLabel welcomeLabel;
    private JLabel welcomeLabel2;
    private JButton startButton;
    private ImageIcon backgroundImage;
    private JLabel backgroundLabel;

    public MainPage() {
        // Set up the frame for full screen
        frame = new JFrame("Quiz Application - Main Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Remove window borders and set full screen
        frame.setUndecorated(true); // Optional: Remove borders
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen mode
        frame.setLocationRelativeTo(null); // Center the frame

        // Set up the background image and add the component listener to scale the image
        backgroundImage = new ImageIcon("quiz-application-main\\OnlineQuiz\\icons\\home.jpg"); // Correct
                                                            // image
                                                            // path
        backgroundLabel = new JLabel();
        frame.setContentPane(backgroundLabel);
        frame.setLayout(new BorderLayout());

        // Create a panel with GridBagLayout for better control over positioning
        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.setOpaque(false); // Make panel background transparent

        // Create GridBagConstraints for the welcome labels
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0; // Column index (center)
        gbcLabel.gridy = 0; // Row index for first label (Welcome to QuizWhiz)
        gbcLabel.anchor = GridBagConstraints.CENTER; // Align content to the center
        gbcLabel.insets = new Insets(150, 0, 20, 0); // Add space at the top (adjust the '100' to move the labels down)

        welcomeLabel = new JLabel("Welcome to QuizWhiz!!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 80));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setOpaque(false); // Make sure text is not covered by background

        labelPanel.add(welcomeLabel, gbcLabel); // Add first welcome label to label panel

        // Move the second label just below the first
        gbcLabel.gridy = 1; // Set the second label's row position
        gbcLabel.insets = new Insets(20, 0, 20, 0); // Space between the labels (adjust the '20' if needed)
        welcomeLabel2 = new JLabel("Your Ultimate Quiz Platform - Explore and Learn!", JLabel.CENTER);
        welcomeLabel2.setFont(new Font("Serif", Font.ITALIC, 40));
        welcomeLabel2.setForeground(Color.WHITE);
        welcomeLabel2.setOpaque(false);

        labelPanel.add(welcomeLabel2, gbcLabel); // Add second welcome label to label panel

        // Add label panel to the center of the frame
        frame.add(labelPanel, BorderLayout.CENTER);

        // Create a rounded "Get Started" button to start the quiz
        startButton = new JButton("Get Started") {
            // Override the paintComponent to create rounded corners
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(new Color(238, 130, 238)); // Light violet color when pressed
                } else {
                    g.setColor(getBackground()); // Use the current button background color
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners (30px radius)
                super.paintComponent(g);
            }
        };

        // Set the initial button color to dark purple
        startButton.setBackground(new Color(75, 0, 130)); // Dark purple color
        startButton.setFont(new Font("Arial", Font.BOLD, 30));
        startButton.setForeground(Color.WHITE); // White text color
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        startButton.setPreferredSize(new Dimension(250, 60));
        startButton.setContentAreaFilled(false); // Transparent button background

        // Add hover effect to button (change color on hover and "lift" effect)
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(238, 130, 238)); // Light violet when hovered
                startButton.setBorder(BorderFactory.createLineBorder(new Color(238, 130, 238), 2)); // "Lift" effect
                                                                                                    // with border color
                                                                                                    // change
                startButton.setPreferredSize(new Dimension(260, 70)); // Slightly increase size to simulate lift effect
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(75, 0, 130)); // Reset to dark purple color
                startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Reset border color
                startButton.setPreferredSize(new Dimension(250, 60)); // Reset size to original
            }
        });

        // Button action to go to login page
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the main page
                new LoginPage(); // Open the login page
            }
        });

        // Create a panel for the button to avoid white background
        // Create a panel with GridBagLayout for better control over positioning
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make panel background transparent

        // Create GridBagConstraints for button positioning
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0; // Column index (center)
        gbcButton.gridy = 0; // Row index (center)
        gbcButton.anchor = GridBagConstraints.CENTER; // Align content to the center
        gbcButton.insets = new Insets(0, 0, 100, 0); // Space below the button (adjust to shift upwards)

        // Add the button to the panel using GridBagConstraints
        buttonPanel.add(startButton, gbcButton);

        // Add the button panel to the bottom part of the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add a component listener to resize the background image
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Scale the image to the current frame size
                Image backgroundImg = backgroundImage.getImage().getScaledInstance(frame.getWidth(), frame.getHeight(),
                        Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(backgroundImg)); // Update background image
            }
        });

        // Show the frame
        frame.setVisible(true);

        // Add a slide-in effect for the labels (faster)
        slideInText(welcomeLabel);
        fadeInText(welcomeLabel2);
    }

    private void fadeInText(JLabel label) {
        // Thread to handle the fading-in effect
        Thread fadeThread = new Thread(() -> {
            try {
                // Gradually increase the opacity of the label
                for (int i = 0; i <= 255; i += 25) { // Alpha values from 0 to 255
                    Thread.sleep(150); // Control the speed of the fade-in effect
                    Color bgColor = new Color(255, 255, 255, i); // Gradually increase alpha
                    label.setForeground(bgColor); // Set text transparency (foreground)
                    label.repaint(); // Ensure the label is redrawn with the new transparency
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        fadeThread.start();

    }

    private void slideInText(JLabel label) {
        Thread slideThread = new Thread(() -> {
            try {
                // Start the label off-screen to the left
                int initialX = -label.getWidth();
                label.setLocation(initialX, label.getY());
                for (int i = initialX; i < (frame.getWidth() - label.getWidth()) / 2; i++) {
                    label.setLocation(i, label.getY());
                    Thread.sleep(3); // Speed of the sliding effect
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        slideThread.start();
    }

    public static void main(String[] args) {
        new MainPage(); // Start the main page
    }
}