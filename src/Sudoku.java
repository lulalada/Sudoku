import javax.swing.*;
import java.awt.*;

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
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                JTextField textField = new JTextField();
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font("Arial", Font.BOLD, 20));
                textField.setEditable(true);
                textField.setText(row + " " + col);

                gameBoard.add(textField);
            }
        }
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
}
