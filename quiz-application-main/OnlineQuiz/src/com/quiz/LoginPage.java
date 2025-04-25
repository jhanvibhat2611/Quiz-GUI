package com.quiz;

import java.sql.*;  
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPage {
    private JFrame frame;

    private float opacity = 0.0f; // Initial opacity for fade effect

    // Top-level method to initialize the database
public static void initDatabase() {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {
        if (conn != null) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "username TEXT NOT NULL UNIQUE, " +
                         "password TEXT NOT NULL)";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public LoginPage() {
        frame = new JFrame("Quiz Application - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setSize(1920, 1080); // Setting explicit frame size for clarity

        // Background Panel with Gradient (Dark Purple to Blue)
        JPanel backgroundPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("quiz-application-main\\OnlineQuiz\\icons\\login.jpg"); // Ensure the correct path
                Image img = background.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(backgroundPanel);

        // Modern Title Label with Gradient and Shadow
        JLabel titleLabel = new JLabel("LOGIN PAGE", JLabel.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 50, frame.getWidth(), 80);

        // Add a gradient effect to the title
        titleLabel.setText(
                "<html><span style='font-family:Poppins; font-weight:bold; font-size:60px; text-shadow: 3px 3px 8px rgba(0, 0, 0, 0.6);'>LOGIN PAGE</span></html>");
        backgroundPanel.add(titleLabel);

        // Funny Paragraph on the Left Side with Lighter Violet Color and Fade Effect
        JTextArea funnyText = new JTextArea("Welcome to the ultimate quiz challenge! \n\n\n"
                + "Are you ready to test your brain cells? Our quizzes will make you think, laugh, and maybe even cry! \n\n"
                + "Let's get started and see if you can beat the quiz master!\n\nHope you're ready for some fun!");
        funnyText.setFont(new Font("Quicksand", Font.BOLD + Font.ITALIC, 30));
        funnyText.setForeground(new Color(230, 200, 255)); // Lighter Violet Color closer to white
        funnyText.setOpaque(false);
        funnyText.setWrapStyleWord(true);
        funnyText.setLineWrap(true);
        funnyText.setEditable(false);
        funnyText.setBounds(150, 360, 620, 800); // Adjusted position of the paragraph
        backgroundPanel.add(funnyText);

        // Timer for Fade-in Effect
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.05f;
                    funnyText.setForeground(new Color(230, 200, 255, (int) (opacity * 255)));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        fadeInTimer.start();

        // Login Panel with Rounded Corners and Semi-Transparent White Background
        JPanel loginPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 180)); // Semi-transparent white color
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50); // Rounded corners
            }
        };
        loginPanel.setBounds(frame.getWidth() - 500, frame.getHeight() / 4 + 50, 400, 350);
        backgroundPanel.add(loginPanel);

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Roboto", Font.BOLD, 22));
        usernameLabel.setBounds(30, 30, 200, 30);
        loginPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Roboto", Font.PLAIN, 20));
        usernameField.setBounds(30, 70, 340, 40);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        usernameField.setMargin(new Insets(0, 10, 0, 0)); // Added space before text starts
        loginPanel.add(usernameField);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Roboto", Font.BOLD, 22));
        passwordLabel.setBounds(30, 130, 200, 30);
        loginPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Roboto", Font.PLAIN, 20));
        passwordField.setBounds(30, 170, 340, 40);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        loginPanel.add(passwordField);

        // Buttons Panel with GridLayout
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBounds(30, 230, 340, 150);
        loginPanel.add(buttonsPanel);

        // Create styled buttons
        JButton loginButton = createStyledButton("Login", new Color(34, 177, 76)); // Green
        JButton createAccountButton = createStyledButton("Create Account", new Color(255, 223, 0)); // Yellow

        buttonsPanel.add(loginButton);
        buttonsPanel.add(createAccountButton);

        // Login Button Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
        
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {
                // Check if the username exists
                String userCheckSql = "SELECT * FROM users WHERE username = ?";
                PreparedStatement userCheckStmt = conn.prepareStatement(userCheckSql);
                userCheckStmt.setString(1, username);
                ResultSet userRs = userCheckStmt.executeQuery();
        
                if (!userRs.next()) {
                    // Username not found
                    JOptionPane.showMessageDialog(frame, "User not found. Please create a new account.", "Account Not Found", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Username exists, check password
                    String storedPassword = userRs.getString("password");
                    if (storedPassword.equals(password)) {
                        // Login successful
                        JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose(); // Close login window
                        new HomePage(username); // Pass to home page
                    } else {
                        // Wrong password
                        JOptionPane.showMessageDialog(frame, "Invalid user credentials. Please try again.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        

        // Dynamic UI resizing
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = frame.getWidth();
                int height = frame.getHeight();

                backgroundPanel.setBounds(0, 0, width, height);
                titleLabel.setBounds(20, 30, width, 80);
                funnyText.setBounds(120, 200, 620, 800); // Adjusted position of the paragraph
                loginPanel.setBounds(width - 500, height / 4 + 50, 400, 350);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }
        });

        // Create Account Button Action (optional, you can implement this further)
        createAccountButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
        
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {
                String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
        
                JOptionPane.showMessageDialog(frame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                if (ex.getMessage().contains("UNIQUE")) {
                    JOptionPane.showMessageDialog(frame, "Username already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        // Make frame visible
        frame.setVisible(true);
    }

    // Create a styled button with rounded corners, hover effect, and modern look
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2)); // Modern border effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        initDatabase();
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
