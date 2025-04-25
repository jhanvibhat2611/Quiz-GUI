// 
package com.quiz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class HomePage {
    private JFrame frame;

    public HomePage(String username) {
        // Create frame
        frame = new JFrame("Quiz Application - Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);

        // Background image
        JLabel background = new JLabel(new ImageIcon("quiz-application-main\\OnlineQuiz\\icons\\homepage.jpg"));
        background.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.setContentPane(background);
        frame.setLayout(null);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!\n We're thrilled to have you here.",
                JLabel.CENTER);
        welcomeLabel.setFont(new Font("Poppins", Font.BOLD, 48));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(0, 80, frame.getWidth(), 80);
        frame.add(welcomeLabel);

        // New Label for "Select a subject to test your knowledge"
        JLabel startLabel = new JLabel("<html>Select a subject to test your knowledge.</html>");
        startLabel.setFont(new Font("Montserrat", Font.BOLD, 22));
        startLabel.setForeground(Color.WHITE);
        startLabel.setBounds(250, 220, 700, 60); // Shifted right for alignment
        frame.add(startLabel);

        // Panel for Subject Buttons (Moved Down & Smaller Square Buttons)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 50, 30)); // 2 rows, 3 columns, more space between rows
        buttonPanel.setBounds(100, 300, 650, 350); // Moved down
        buttonPanel.setOpaque(false);

        // Array of Subjects
        String[] subjects = { "Maths", "Python", "Java", "Mechanics", "C++", "EVS" };

        // Array of image paths for each subject
        String[] imagePaths = {
                "quiz-application-main\\OnlineQuiz\\icons\\mathsfinal.jpg",
                "quiz-application-main\\OnlineQuiz\\icons\\pythonfinal1.jpg",
                "quiz-application-main\\OnlineQuiz\\icons\\java.jpg",
                "quiz-application-main\\OnlineQuiz\\icons\\mehcanics.jpeg",
                "quiz-application-main\\OnlineQuiz\\icons\\c++.jpeg",
                "quiz-application-main\\OnlineQuiz\\icons\\evst.jpg"
        };

        // Custom JButton Class with Rounded Corners and Transparent Background
        for (int i = 0; i < subjects.length; i++) {
            final int index = i; // Make i final or effectively final
            JButton subjectButton = new JButton();

            // Set background image for each button
            ImageIcon icon = new ImageIcon(imagePaths[i]);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Scaling the image to fit the
                                                                                   // button
            subjectButton.setIcon(new ImageIcon(scaledImg));

            // Button Styling
            subjectButton.setFont(new Font("Montserrat", Font.BOLD, 16));
            subjectButton.setFocusPainted(false);
            subjectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Set rounded corners for the button with transparent background
            subjectButton.setBackground(null); // Set background to transparent
            subjectButton.setBorder(new RoundedBorder(30)); // Apply rounded corners
            subjectButton.setLayout(null); // No layout manager to place the text manually

            // Create the text JLabel to place over the image
            JLabel textLabel = new JLabel(subjects[i], JLabel.CENTER);
            textLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
            textLabel.setForeground(Color.WHITE);

            // Adjust padding and shift text to the left by modifying the x position
            int paddingX = 20; // Increased padding to shift text left
            int paddingY = 50; // Vertical padding
            textLabel.setBounds(paddingX, paddingY, 200 - 2 * paddingX, 50); // Adjust width to accommodate padding
            subjectButton.add(textLabel);

            // Hover effect: Scale and shift button when hovered
            subjectButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    // Apply a scale transformation to "pop-up" the button
                    subjectButton.setSize(220, 220); // Increase size (popping effect)
                    subjectButton.setLocation(subjectButton.getX() - 10, subjectButton.getY() - 10); // Shift upwards

                    // Optional: Change button border color or effect during hover (like a shadow)
                    subjectButton.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(70, 130, 180), 3), // Light blue outer border
                            BorderFactory.createLineBorder(new Color(0, 153, 204), 1) // Inner darker blue border
                    ));
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    // Reset size and position back to normal when mouse exits
                    subjectButton.setSize(200, 200);
                    subjectButton.setLocation(subjectButton.getX() + 10, subjectButton.getY() + 10); // Reset position

                    // Reset the border after hover
                    subjectButton.setBorder(new RoundedBorder(50)); // Reset to the original rounded border
                }
            });

            // Add Action Listener
            subjectButton.addActionListener((ActionEvent e) -> {
                frame.dispose();
                new QuizInfoPage(username, subjects[index]); // Use the final variable index
            });

            buttonPanel.add(subjectButton);
        }

        frame.add(buttonPanel);

        // Resources Section (Shifted left a bit)
        JPanel resourcesPanel = new JPanel();
        resourcesPanel.setLayout(new GridLayout(7, 1, 5, 5));
        resourcesPanel.setBounds(900, 280, 400, 350); // Slightly moved left
        resourcesPanel.setBackground(new Color(255, 255, 255, 150)); // Semi-transparent black
        resourcesPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); // White border around panel

        JLabel titleLabel = new JLabel("Study Resources", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 0, resourcesPanel.getWidth(), 30); // Set bounds to align title
        resourcesPanel.add(titleLabel);

        // Add Resource Links
        String[][] resources = {
                { "Mathematics", "https://www.khanacademy.org/math" },
                { "Python", "https://www.learnpython.org/" },
                { "Java", "https://www.javatpoint.com/java-tutorial" },
                { "Mechanics", "https://www.edx.org/learn/mechanics" },
                { "C++", "https://www.learncpp.com/" },
                { "EVS", "https://eepmoefcc.nic.in/" }
        };

        for (String[] resource : resources) {
            JLabel resourceLabel = new JLabel("<html><u>" + resource[0] + "</u></html>", JLabel.CENTER);
            resourceLabel.setFont(new Font("Roboto", Font.PLAIN, 22));
            resourceLabel.setForeground(new Color(51, 153, 255));
            resourceLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            resourceLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            // Hover effects without color change to orange
            resourceLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new java.net.URI(resource[1]));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Unable to open the link.");
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Do nothing on hover, no color change
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Do nothing on hover exit
                }
            });

            resourcesPanel.add(resourceLabel);
        }

        frame.add(resourcesPanel);

        // Resize Components Dynamically on Window Resize
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                welcomeLabel.setBounds(0, 80, frame.getContentPane().getWidth(), 80);
                startLabel.setBounds(250, 220, 700, 60); // Adjusted start label position and width
                resourcesPanel.setBounds(900, 280, 400, 350); // Ensure it remains properly positioned
            }
        });

        // Finalize Frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage("Admin"));
    }
}

// Custom Rounded Border Class for JButton
class RoundedBorder extends AbstractBorder {

    public RoundedBorder(int radius) {
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // No border drawing
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5); // Adjust border insets as needed
    }
}
