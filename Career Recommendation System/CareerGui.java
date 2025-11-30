import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CareerGui extends JFrame {

    // Deep Dark Palette
    private static final Color BG_COLOR = new Color(18, 18, 18);
    private static final Color CARD_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(170, 170, 170);
    private static final Color ACCENT_COLOR = new Color(29, 185, 84); // Spotify Green

    private JTextField nameField;
    private JTextField ageField;
    private JTextField searchField;
    private JPanel skillsPanel;
    private JPanel resultsPanel;
    private CareerEngine engine;
    private List<JCheckBox> skillCheckBoxes;
    private List<String> allSkills;
    private java.util.Set<String> selectedSkillsSet;

    public CareerGui() {
        engine = new CareerEngine();
        skillCheckBoxes = new ArrayList<>();
        selectedSkillsSet = new java.util.HashSet<>();

        setTitle("Career Recommendation System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(25, 40, 25, 40));

        JLabel title = new JLabel("Career Recommendation System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // History Button in Header (Absolute Top Right)
        JButton historyBtn = createSecondaryButton("View History");
        historyBtn.addActionListener(e -> showHistory());
        header.add(historyBtn, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // --- Content Grid ---
        JPanel contentGrid = new JPanel(new GridLayout(1, 2, 0, 0));
        contentGrid.setBackground(BG_COLOR);
        contentGrid.setBorder(new EmptyBorder(10, 40, 40, 40));

        // Left Card: Input
        JPanel inputCard = new JPanel();
        inputCard.setLayout(new BoxLayout(inputCard, BoxLayout.Y_AXIS));
        inputCard.setBackground(CARD_COLOR);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 60, 60)),
                new EmptyBorder(30, 30, 30, 30)));

        inputCard.add(createLabel("Your Profile", 18, TEXT_PRIMARY));
        inputCard.add(Box.createRigidArea(new Dimension(0, 25)));

        inputCard.add(createLabel("Full Name", 12, TEXT_SECONDARY));
        nameField = createBlendedField();
        inputCard.add(nameField);
        inputCard.add(Box.createRigidArea(new Dimension(0, 20)));

        inputCard.add(createLabel("Age", 12, TEXT_SECONDARY));
        ageField = createBlendedField();
        inputCard.add(ageField);
        inputCard.add(Box.createRigidArea(new Dimension(0, 25)));

        inputCard.add(createLabel("Select Skills", 12, TEXT_SECONDARY));
        inputCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Search Bar for Skills
        searchField = createBlendedField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterSkills(searchField.getText());
            }
        });

        JLabel searchLabel = createLabel("Search Skills", 11, TEXT_SECONDARY);
        inputCard.add(searchLabel);
        inputCard.add(Box.createRigidArea(new Dimension(0, 5)));
        inputCard.add(searchField);
        inputCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Skills List
        skillsPanel = new JPanel();
        skillsPanel.setLayout(new BoxLayout(skillsPanel, BoxLayout.Y_AXIS));
        skillsPanel.setBackground(CARD_COLOR);
        allSkills = engine.getAllSkills();
        refreshSkills();

        JScrollPane skillScroll = new JScrollPane(skillsPanel);
        skillScroll.setBorder(null);
        skillScroll.getViewport().setBackground(CARD_COLOR);
        skillScroll.getVerticalScrollBar().setUnitIncrement(16);
        skillScroll.setPreferredSize(new Dimension(0, 200));
        skillScroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        skillScroll.setAlignmentX(Component.LEFT_ALIGNMENT); // Ensure scroll pane aligns left

        inputCard.add(skillScroll);

        inputCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Deselect All button
        JButton deselectBtn = createSecondaryButton("Deselect All");
        deselectBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        deselectBtn.addActionListener(e -> deselectAllSkills());
        inputCard.add(deselectBtn);

        inputCard.add(Box.createRigidArea(new Dimension(0, 10)));
        // History button moved to header
        inputCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton findBtn = createPrimaryButton("Find Careers");
        findBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        findBtn.addActionListener(e -> getRecommendations());
        inputCard.add(findBtn);

        contentGrid.add(inputCard);

        // Right Card: Results
        JPanel resultCard = new JPanel();
        resultCard.setLayout(new BoxLayout(resultCard, BoxLayout.Y_AXIS));
        resultCard.setBackground(CARD_COLOR);
        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                new EmptyBorder(30, 30, 30, 30)));

        JLabel resHeader = createLabel("Recommended for You", 18, TEXT_PRIMARY);
        resultCard.add(resHeader);
        resultCard.add(Box.createRigidArea(new Dimension(0, 20)));

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(CARD_COLOR);
        resultsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder
        JLabel placeholder = createLabel("Enter your details to see matches.", 14, TEXT_SECONDARY);
        resultsPanel.add(placeholder);

        JScrollPane resScroll = new JScrollPane(resultsPanel);
        resScroll.setBorder(null);
        resScroll.getViewport().setBackground(CARD_COLOR);
        resScroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        resScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        resultCard.add(resScroll);

        contentGrid.add(resultCard);
        mainPanel.add(contentGrid, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void refreshSkills() {
        filterSkills("");
    }

    private void filterSkills(String searchText) {
        skillsPanel.removeAll();
        skillCheckBoxes.clear();
        String search = searchText.toLowerCase().trim();

        for (String skill : allSkills) {
            // Filter based on search text
            if (search.isEmpty() || skill.toLowerCase().contains(search)) {
                String display = skill.substring(0, 1).toUpperCase() + skill.substring(1);
                JCheckBox cb = new JCheckBox(display);
                cb.setBackground(CARD_COLOR);
                cb.setForeground(TEXT_PRIMARY);
                cb.setFont(new Font("Satoshi", Font.PLAIN, 14));
                cb.setFocusPainted(false);
                cb.setBorder(new EmptyBorder(5, 0, 5, 0));
                cb.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Custom Icon
                cb.setIcon(new DarkCheckBoxIcon(false));
                cb.setSelectedIcon(new DarkCheckBoxIcon(true));

                // Restore selection state
                if (selectedSkillsSet.contains(cb.getText())) {
                    cb.setSelected(true);
                }

                // Add listener to update set
                cb.addActionListener(e -> {
                    if (cb.isSelected()) {
                        selectedSkillsSet.add(cb.getText());
                    } else {
                        selectedSkillsSet.remove(cb.getText());
                    }
                });

                skillCheckBoxes.add(cb);
                skillsPanel.add(cb);
            }
        }

        skillsPanel.revalidate();
        skillsPanel.repaint();
    }

    private void deselectAllSkills() {
        for (JCheckBox cb : skillCheckBoxes) {
            cb.setSelected(false);
        }
        selectedSkillsSet.clear();
    }

    private JLabel createLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Satoshi", Font.BOLD, size));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createBlendedField() {
        JTextField field = new JTextField();
        field.setBackground(CARD_COLOR);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_COLOR);
        field.setFont(new Font("Satoshi", Font.PLAIN, 14));
        field.setBorder(new MatteBorder(0, 0, 2, 0, new Color(60, 60, 60)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(30, 215, 96));
                } else {
                    g2.setColor(ACCENT_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Satoshi", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(10, 30, 10, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void getRecommendations() {
        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name and age.", "Missing Info",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> selectedSkills = new ArrayList<>(selectedSkillsSet);

        if (selectedSkills.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one skill.", "No Skills",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String skillsStr = String.join(",", selectedSkills);
        Applicant applicant = new Applicant(name, age, skillsStr);

        try {
            List<String> recommendations = engine.recommend(applicant);

            // Save to history if we have a match
            if (!recommendations.isEmpty()) {
                engine.saveHistory(applicant, recommendations.get(0));
            }

            updateResults(recommendations, applicant);
        } catch (Exception e) {
            if (e instanceof NoCareerFoundException) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "No Matches", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateResults(List<String> recommendations, Applicant applicant) {
        resultsPanel.removeAll();
        if (recommendations.isEmpty()) {
            resultsPanel.add(createLabel("No matches found.", 14, TEXT_SECONDARY));
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                String career = recommendations.get(i);

                // Highlight the best match (first item)
                if (i == 0) {
                    // Best match label with neon glow effect
                    JLabel bestMatchLabel = new JLabel("BEST MATCH") {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            // Draw neon glow effect
                            g2.setColor(new Color(29, 185, 84, 40));
                            g2.fillRect(0, 0, getWidth(), getHeight());

                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    bestMatchLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
                    bestMatchLabel.setForeground(ACCENT_COLOR);
                    bestMatchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    bestMatchLabel.setBorder(new EmptyBorder(5, 5, 2, 5));
                    bestMatchLabel.setOpaque(false);
                    resultsPanel.add(bestMatchLabel);

                    // Best match career with neon glow panel
                    JPanel glowPanel = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            // Multi-layer glow effect
                            int glowSize = 20;
                            for (int j = glowSize; j > 0; j -= 2) {
                                int alpha = (int) (30 * (1.0 - (double) j / glowSize));
                                g2.setColor(new Color(29, 185, 84, alpha));
                                g2.fillRoundRect(j / 2, j / 2, getWidth() - j, getHeight() - j, 15, 15);
                            }

                            g2.dispose();
                        }
                    };
                    glowPanel.setLayout(new BorderLayout());
                    glowPanel.setBackground(CARD_COLOR);
                    glowPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                    // Fetch details for best match
                    String[] details = engine.getCareerDetails(career);
                    String description = details[2];

                    String labelText = "<html>- " + career;
                    if (description != null && !description.isEmpty()) {
                        if (description.length() > 80) {
                            description = description.substring(0, 77) + "...";
                        }
                        labelText += " <span style='color: #aaaaaa; font-size: 12px;'>(" + description + ")</span>";
                    }
                    labelText += "</html>";

                    JLabel label = new JLabel(labelText);
                    label.setFont(new Font("SansSerif", Font.BOLD, 18));
                    label.setForeground(ACCENT_COLOR);

                    glowPanel.add(label, BorderLayout.CENTER);
                    glowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    glowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                    resultsPanel.add(glowPanel);
                    resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                    // Detailed Explanation
                    String explanation = engine.generateExplanation(career, applicant);
                    JLabel explanationLabel = new JLabel(explanation);
                    explanationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    resultsPanel.add(explanationLabel);

                    resultsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                } else {
                    // Regular recommendations
                    String[] details = engine.getCareerDetails(career);
                    String description = details[2];
                    String labelText = "<html>- " + career;

                    if (description != null && !description.isEmpty()) {
                        // Truncate description if it's too long
                        if (description.length() > 80) {
                            description = description.substring(0, 77) + "...";
                        }
                        labelText += " <span style='color: #aaaaaa; font-size: 10px;'>(" + description + ")</span>";
                    }
                    labelText += "</html>";

                    JLabel label = new JLabel(labelText);
                    label.setFont(new Font("Satoshi", Font.PLAIN, 16));
                    label.setForeground(TEXT_PRIMARY);
                    label.setBorder(new EmptyBorder(5, 0, 5, 0));
                    resultsPanel.add(label);
                }
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private static class DarkCheckBoxIcon implements Icon {
        private boolean selected;

        public DarkCheckBoxIcon(boolean selected) {
            this.selected = selected;
        }

        public int getIconWidth() {
            return 16;
        }

        public int getIconHeight() {
            return 16;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(new Color(60, 60, 60)); // Dark Grey Box
            g2.fillRoundRect(x, y, 16, 16, 4, 4);

            // Checkmark
            if (selected) {
                g2.setColor(ACCENT_COLOR);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(x + 4, y + 8, x + 7, y + 12);
                g2.drawLine(x + 7, y + 12, x + 12, y + 4);
            }
            g2.dispose();
        }
    }

    // Minimal Dark Scrollbar UI
    static class DarkScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(60, 60, 60);
            this.trackColor = new Color(30, 30, 30);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }
    }

    private void showHistory() {
        List<String> history = engine.getHistory();

        JDialog historyDialog = new JDialog(this, "History", true);
        historyDialog.setSize(600, 500);
        historyDialog.setLocationRelativeTo(this);
        historyDialog.getContentPane().setBackground(BG_COLOR);
        historyDialog.setLayout(new BorderLayout());

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(BG_COLOR);
        historyPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (history.isEmpty()) {
            historyPanel.add(createLabel("No history found.", 14, TEXT_SECONDARY));
        } else {
            for (String entry : history) {
                String timestamp = "";
                String details = entry;
                String match = "";

                try {
                    int endTimestamp = entry.indexOf("] ");
                    if (endTimestamp != -1) {
                        timestamp = entry.substring(1, endTimestamp);
                        details = entry.substring(endTimestamp + 2);
                    }

                    int matchIndex = details.indexOf(" - Match: ");
                    if (matchIndex != -1) {
                        match = details.substring(matchIndex + 10);
                        details = details.substring(0, matchIndex);
                    }
                } catch (Exception e) {
                    // Fallback
                }

                JPanel card = new JPanel();
                card.setLayout(new BorderLayout());
                card.setBackground(CARD_COLOR);
                card.setBorder(new EmptyBorder(5, 10, 5, 10));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));

                final String finalTimestamp = timestamp;
                final String finalDetails = details;
                final String finalMatch = match;

                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        showHistoryDetails(finalTimestamp, finalDetails, "", finalMatch);
                    }

                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        card.setBackground(new Color(40, 40, 40));
                        card.repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        card.setBackground(CARD_COLOR);
                        card.repaint();
                    }
                });

                JPanel left = new JPanel();
                left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
                left.setBackground(CARD_COLOR);
                left.setOpaque(false);

                JLabel nameLbl = new JLabel(details);
                nameLbl.setFont(new Font("Satoshi", Font.BOLD, 13));
                nameLbl.setForeground(TEXT_PRIMARY);
                nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel timeLbl = new JLabel(timestamp);
                timeLbl.setFont(new Font("Satoshi", Font.PLAIN, 11));
                timeLbl.setForeground(TEXT_SECONDARY);
                timeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                left.add(nameLbl);
                left.add(Box.createRigidArea(new Dimension(0, 2)));
                left.add(timeLbl);

                JLabel matchLbl = new JLabel(match);
                matchLbl.setFont(new Font("Satoshi", Font.BOLD, 14));
                matchLbl.setForeground(ACCENT_COLOR);

                card.add(left, BorderLayout.CENTER);
                card.add(matchLbl, BorderLayout.EAST);

                JPanel container = new JPanel(new BorderLayout());
                container.setBackground(BG_COLOR);
                container.add(card, BorderLayout.CENTER);
                container.add(Box.createRigidArea(new Dimension(0, 2)), BorderLayout.SOUTH);

                historyPanel.add(container);
            }
        }

        JScrollPane scroll = new JScrollPane(historyPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton clearBtn = createSecondaryButton("Clear History");
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    historyDialog,
                    "Are you sure you want to clear all history?",
                    "Confirm Clear",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                engine.clearHistory();
                historyDialog.dispose();
                showHistory();
            }
        });
        bottomPanel.add(clearBtn);

        historyDialog.add(scroll, BorderLayout.CENTER);
        historyDialog.add(bottomPanel, BorderLayout.SOUTH);
        historyDialog.setVisible(true);
    }

    private void showHistoryDetails(String timestamp, String details, String skills, String match) {
        JDialog detailDialog = new JDialog(this, "Report Details", true);
        detailDialog.setSize(550, 500);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.getContentPane().setBackground(BG_COLOR);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLbl = createLabel("Previous Report", 20, ACCENT_COLOR);
        panel.add(titleLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel timeLbl = createLabel("Date: " + timestamp, 12, TEXT_SECONDARY);
        panel.add(timeLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel detailsLbl = createLabel("User: " + details, 14, TEXT_PRIMARY);
        panel.add(detailsLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Fetch career details from database
        String[] careerInfo = engine.getCareerDetails(match);
        String salary = careerInfo[0];
        String difficulty = careerInfo[1];
        String description = careerInfo[2];

        JLabel matchTitle = createLabel("Best Match:", 14, TEXT_SECONDARY);
        panel.add(matchTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel matchLbl = createLabel(match, 18, ACCENT_COLOR);
        panel.add(matchLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Determine difficulty color
        Color difficultyColor = Color.WHITE;
        if (difficulty != null) {
            String diffLower = difficulty.toLowerCase();
            if (diffLower.contains("low") || diffLower.contains("easy")) {
                difficultyColor = new Color(76, 175, 80);
            } else if (diffLower.contains("medium") || diffLower.contains("moderate")) {
                difficultyColor = new Color(255, 193, 7);
            } else if (diffLower.contains("very high")) {
                difficultyColor = new Color(244, 67, 54);
            } else if (diffLower.contains("high")) {
                difficultyColor = new Color(255, 87, 34);
            } else if (diffLower.contains("extreme")) {
                difficultyColor = new Color(156, 39, 176);
            }
        }

        // Career details card
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(new Color(40, 40, 40));
        detailsCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        detailsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel salaryLbl = new JLabel("Salary: " + salary);
        salaryLbl.setFont(new Font("Satoshi", Font.BOLD, 14));
        salaryLbl.setForeground(new Color(76, 175, 80));
        salaryLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(salaryLbl);

        detailsCard.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel diffLbl = new JLabel("Difficulty: " + difficulty);
        diffLbl.setFont(new Font("Satoshi", Font.BOLD, 14));
        diffLbl.setForeground(difficultyColor);
        diffLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(diffLbl);

        if (description != null && !description.isEmpty()) {
            detailsCard.add(Box.createRigidArea(new Dimension(0, 15)));
            JLabel descLbl = new JLabel("<html><div style='width: 450px;'>" + description + "</div></html>");
            descLbl.setFont(new Font("Satoshi", Font.PLAIN, 13));
            descLbl.setForeground(TEXT_SECONDARY);
            descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            detailsCard.add(descLbl);
        }

        panel.add(detailsCard);

        detailDialog.add(panel);
        detailDialog.setVisible(true);
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(60, 60, 60));
                } else {
                    g2.setColor(new Color(45, 45, 45));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_SECONDARY);
        btn.setFont(new Font("Satoshi", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
