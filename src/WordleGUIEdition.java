import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

public class WordleGUIEdition extends JFrame {
    private String[] wordleWords = new String[2310];
    private String current;
    private int strikes = 0;
    private int MAX_STRIKES = 10;
    private JPanel gridPanel;
    private JTextField inputField;
    private JTextArea binaryLog;
    private Color BG_COLOR = new Color(18, 18, 19);
    private Color ACCENT_COLOR = new Color(58, 58, 60);
    private Color TEXT_COLOR = new Color(255, 255, 255);
    private Color BINARY_GOLD = new Color(215, 185, 54);

    public WordleGUIEdition() {
        setupWindow();
        initializeData();
        createUI();
        pickWord();
        setVisible(true);
    }

    private void setupWindow() {
        setTitle("Bruce's Binary Wordle");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout(15, 15));
    }

    private void createUI() {
        JLabel header = new JLabel("WORDLE [BINARY]", SwingConstants.CENTER);
        header.setFont(new Font("Monospaced", Font.BOLD, 28));
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(header, BorderLayout.NORTH);
        JPanel centerArea = new JPanel(new GridLayout(1, 2, 10, 0));
        centerArea.setBackground(BG_COLOR);
        centerArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gridPanel = new JPanel(new GridLayout(MAX_STRIKES, 1, 5, 5));
        gridPanel.setBackground(BG_COLOR);
        for (int i = 0; i < MAX_STRIKES; i++) {
            JLabel label = new JLabel("_ _ _ _ _", SwingConstants.CENTER);
            label.setFont(new Font("Monospaced", Font.BOLD, 20));
            label.setForeground(ACCENT_COLOR);
            gridPanel.add(label);
        }
        binaryLog = new JTextArea();
        binaryLog.setEditable(false);
        binaryLog.setBackground(new Color(25, 25, 26));
        binaryLog.setForeground(BINARY_GOLD);
        binaryLog.setFont(new Font("Monospaced", Font.BOLD, 22));
        binaryLog.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(ACCENT_COLOR), "FEEDBACK (1=HIT)", 0, 0, null, BINARY_GOLD));
        centerArea.add(gridPanel);
        centerArea.add(new JScrollPane(binaryLog));
        add(centerArea, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 40, 50));

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.BOLD, 18));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.addActionListener(e -> handleGuess());

        JButton submitBtn = new JButton("GUESS");
        submitBtn.addActionListener(e -> handleGuess());

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(submitBtn, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleGuess() {
        String guess = inputField.getText().toUpperCase();
        inputField.setText("");


        if (!guess.matches("^[a-z A-Z]{5}$")) {
            JOptionPane.showMessageDialog(this, "Invalid entry! 5 letters only.");
            return;
        }
        JLabel rowLabel = (JLabel) gridPanel.getComponent(strikes);
        rowLabel.setText(guess.replace("", " ").trim());
        rowLabel.setForeground(TEXT_COLOR);
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == current.toUpperCase().charAt(i)) {
                binary.append("1");
            } else {
                binary.append("0");
            }
        }
        binaryLog.append(" " + (strikes + 1) + ". [" + binary + "]\n");

        if (guess.equalsIgnoreCase(current)) {
            endGame("You Won");
        } else {
            strikes++;
            if (strikes >= MAX_STRIKES) {
                endGame(" You lose.  The word was: " + current.toUpperCase());
            }
        }
    }

    private void endGame(String msg) {
        int res = JOptionPane.showConfirmDialog(this, msg + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) resetGame(); else System.exit(0);
    }

    private void resetGame() {
        strikes = 0;
        pickWord();
        binaryLog.setText("");
        for (Component c : gridPanel.getComponents()) {
            ((JLabel) c).setText("_ _ _ _ _");
        }
    }

    private void initializeData() {
        try (Scanner s = new Scanner(new File("Wordle.txt"))) {
            int i = 0;
            while (s.hasNext() && i < 2310) wordleWords[i++] = s.next();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "File Wordle.txt not found!");
        }
    }

    private void pickWord() {
        current = wordleWords[(int) (Math.random() * 2310)];

    }
}
