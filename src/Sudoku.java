import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Sudoku extends JFrame {
    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int[][] board;
    private int[][] solution;
    private  boolean[][] locked;
    private  int level = 1;
    private JComboBox<String> difficultyComboBox;
    private JPanel gameBoard;
    private static int secondsPassed = 0;
    private JLabel timerLabel;

    private Timer timer;

    public static void main(String[] args) {
        new Sudoku();
    }

    public Sudoku() {
        gameBoard = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        generateBoard();
        populateGameBoard(gameBoard, board);

        JButton checkButton = new JButton("Check Solution");
        checkButton.addActionListener(e -> {


            if (!checkSolution()) {
                JOptionPane.showMessageDialog(null,
                        "Your solution is NOT correct!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Your solution is correct! Your score is ");

                gameBoard.removeAll();
                generateBoard();
                populateGameBoard(gameBoard, board);
                gameBoard.revalidate();
                secondsPassed = 0;

            }

        });

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficultyLevels);
        difficultyComboBox.addActionListener(e -> {
            String value = difficultyComboBox.getSelectedItem().toString();
            if (value == "Easy") {
                level = 1;
            } else if (value == "Medium") {
                level = 2;
            } else {
                level = 3;
            }
            gameBoard.removeAll();
            generateBoard();

            populateGameBoard(gameBoard, board);
            gameBoard.revalidate();
            gameBoard.repaint();
            secondsPassed = 0;
        });

        JButton showButton = new JButton("Show Solution");
        showButton.addActionListener(e -> {
            gameBoard.removeAll();
            populateGameBoard(gameBoard, solution);
            gameBoard.revalidate();
            gameBoard.repaint();
        });


        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                int minutes = secondsPassed / 60;
                int seconds = secondsPassed % 60;
                String timeString = String.format("%02d:%02d", minutes, seconds);
                timerLabel.setText(timeString);
            }
        });

        JPanel controls = new JPanel(new FlowLayout());

        controls.add(new JLabel("Difficulty:"));
        controls.add(difficultyComboBox);
        controls.add(checkButton);
        controls.add(showButton);
        controls.add(new JLabel("Time:"));
        controls.add(timerLabel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(gameBoard, BorderLayout.CENTER);
        getContentPane().add(controls, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setVisible(true);
        timer.start();
    }

    private void generateBoard() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        solution = new int[BOARD_SIZE][BOARD_SIZE];
        locked = new boolean[BOARD_SIZE][BOARD_SIZE];
        Random rand = new Random();


        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }

        for (int i = 0; i < BOARD_SIZE; i += SUBGRID_SIZE) {
            for (int j = 0; j < BOARD_SIZE; j += SUBGRID_SIZE) {
                int val = rand.nextInt(9) + 1;
                if (isValid(i, j, val)) {
                    board[i][j] = val;
                }
            }
        }

        if (!solve(0, 0)) {
            generateBoard();
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = board[i][j];
            }
        }

        int numLocked = 0;
        switch (level) {
            case 1:
                numLocked = 30;
                break;
            case 2:
                numLocked = 45;
                break;
            case 3:
                numLocked = 60;
                break;
        }

        while (numLocked > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (!locked[row][col]) {
                locked[row][col] = true;
                numLocked--;
                board[row][col] = 0;
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println(" ______________________ ");
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution.length; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println("");
        }

    }
    private boolean isValid(int row, int col, int val) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[row][i] == val || board[i][col] == val) {
                return false;
            }
        }

        int subgridRow = row - row % SUBGRID_SIZE;
        int subgridCol = col - col % SUBGRID_SIZE;
        for (int i = subgridRow; i < subgridRow + SUBGRID_SIZE; i++) {
            for (int j = subgridCol; j < subgridCol + SUBGRID_SIZE; j++) {
                if (board[i][j] == val) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean solve(int row, int col) {
        if (row == BOARD_SIZE) {
            row = 0;
            if (++col == BOARD_SIZE) {
                return true;
            }
        }

        if (board[row][col] != 0) {
            return solve(row + 1, col);
        }

        for (int val = 1; val <= 9; ++val) {
            if (isValid(row, col, val)) {
                board[row][col] = val;
                if (solve(row + 1, col)) {
                    return true;
                }
            }
        }



        board[row][col] = 0;
        return false;
    }

    private void populateGameBoard(JPanel gameBoard, int[][] boardToPopulate) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                JTextField textField = new JTextField();
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font("Arial", Font.BOLD, 20));
                textField.setEditable(true);
                if (boardToPopulate[row][col] != 0) {
                    textField.setText(String.valueOf(boardToPopulate[row][col]));
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                }

                gameBoard.add(textField);
            }
        }
    }

    private boolean checkSolution() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Component component = gameBoard.getComponent(row * BOARD_SIZE + col);
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    String value = textField.getText();
                    if (!value.equals(String.valueOf(solution[row][col]))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

