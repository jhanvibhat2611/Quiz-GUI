package com.quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LeaderboardPage {
    public LeaderboardPage(String username) {
        JFrame frame = new JFrame("Quiz Leaderboard");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Light pink background panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 228, 240)); // light pink
        mainPanel.setLayout(new BorderLayout(20, 20));
        frame.setContentPane(mainPanel);

        // Welcome label
        JLabel nameLabel = new JLabel("Welcome, " + username);
        nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setForeground(new Color(139, 0, 139)); // dark magenta
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Table setup
        String[] columns = {"Rank", "Username", "Score"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing in all cells
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        int highlightRow = -1;
        int latestUserScore = -1;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:quiz.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, score FROM scores ORDER BY score DESC, id ASC")) {

            int rank = 1;
            for (int rowIndex = 0; rs.next(); rowIndex++) {
                String user = rs.getString("username");
                int score = rs.getInt("score");

                tableModel.addRow(new Object[]{rank, user, score});

                if (user.equals(username)) {
                    highlightRow = rowIndex;
                    latestUserScore = score;
                }
                rank++;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading leaderboard:\n" + e.getMessage());
        }

        final int finalHighlightRow = highlightRow;
        final String currentUser = username;
        final int currentUserScore = latestUserScore;

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String rowUser = table.getValueAt(row, 1).toString();
                int rowScore = Integer.parseInt(table.getValueAt(row, 2).toString());

                if (row == finalHighlightRow && rowUser.equals(currentUser) && rowScore == currentUserScore) {
                    c.setBackground(new Color(255, 182, 193)); // light pink highlight
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        if (finalHighlightRow != -1) {
            SwingUtilities.invokeLater(() -> table.scrollRectToVisible(table.getCellRect(finalHighlightRow, 0, true)));
        }

        // Styled Back Button
        JButton backButton = new JButton("← Back to Results");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(199, 21, 133)); // deep pink
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            frame.dispose();
            new ResultPage(currentUserScore, currentUserScore, username);
        });

        // Center panel (scrollable table)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 228, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 228, 240));
        buttonPanel.add(backButton);

        // Add to frame
        mainPanel.add(nameLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
