import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Sudoku extends JFrame {
    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int[][] board;
    private int[][] solution;
    private  boolean[][] locked;

    private JPanel gameBoard;

    public static void main(String[] args) {
        new Sudoku();
    }

    public Sudoku() {
        gameBoard = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        generateBoard();
        populateGameBoard(gameBoard, board);

        JButton checkButton = new JButton("Check Solution");
        JButton showButton = new JButton("Show Solution");
        JPanel controls = new JPanel(new FlowLayout());

        controls.add(checkButton);
        controls.add(showButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(gameBoard, BorderLayout.CENTER);
        getContentPane().add(controls, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);
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

        int numLocked = 30;

        while (numLocked > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (!locked[row][col]) {
                locked[row][col] = true;
                numLocked--;
                board[row][col] = 0;
            }
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
}

