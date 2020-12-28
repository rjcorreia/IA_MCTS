import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToe implements IBoardGame, Cloneable {

    private static final int dim = 3;
    private Board board;
    private char currentPlayer;
    private char openingPiece;

    /**
     * Constructor where a bidimensional char array is passed as an argument, creating a TicTacToe object.
     */
    public TicTacToe(char[][] board) throws BoardException {
        this.board = new Board(board);
        setCurrentPlayer();
    }


    /**
     * Constructor without any argument. It creates the board array as an empty array.
     */
    public TicTacToe() {
        try {
            this.board = new Board(dim, '_');
        } catch (BoardException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
    }


    /**
     * Constructor with the opening piece as arguments. It creates the board array as an empty array.
     *
     * @param openingPiece Piece selected to start the game
     * @throws TicTacToeException in case the give opening piece is not valid for the tic tac toe game.
     */
    public TicTacToe(char openingPiece) throws TicTacToeException, BoardException {
        this.board = new Board(dim, '_');
        setOpeningPiece(openingPiece);
    }


    /**
     * TESTS-ONLY
     * Create a new board given the rows of the game
     *
     * @param row1 First row of tic tac toe game
     * @param row2 First row of tic tac toe game
     * @param row3 First row of tic tac toe game
     * @throws IllegalStateException in case the Java application is not in an appropriate state for the requested
     *                               operation.
     */
    public TicTacToe(String row1, String row2, String row3) throws IllegalStateException, TicTacToeException {

        //Split each argument into characters
        String[] row1Splitted = row1.split("");
        String[] row2Splitted = row2.split("");
        String[] row3Splitted = row3.split("");

        //Throw exception in case the board is incomplete
        if (row1Splitted.length != dim || row2Splitted.length != dim || row3Splitted.length != dim) {
            throw new TicTacToeException("Invalid arg in TicTacToe constructor");
        }

        try {
            this.board = new Board(dim, row1Splitted, row2Splitted, row3Splitted);
        } catch (BoardException e) {
            System.out.println(e.toString());
            System.exit(0);
        }

        //Set current player
        setCurrentPlayer();
    }


    public static int getDim() {
        return dim;
    }

    public Board getBoard() {
        return this.board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }


    public char getOpeningPiece() {
        return openingPiece;
    }


    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Determines who's the next player to make a move, according to the current TicTacToe, and sets the object variable.
     */
    //Melhorar um bcd esta
    private void setCurrentPlayer() {
        int playerXmoves = 0;
        int player0moves = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.board.getBoard()[i][j] == 'X')
                    playerXmoves++;
                else if (this.board.getBoard()[i][j] == '0')
                    player0moves++;
            }
        }
        if (playerXmoves == player0moves) {
            this.currentPlayer = openingPiece;
        } else if (playerXmoves > player0moves) {
            this.currentPlayer = '0';
        } else {
            this.currentPlayer = 'X';
        }
    }

    public void setOpeningPiece(char openingPiece) throws TicTacToeException {
        if (openingPiece == 'X' || openingPiece == '0') {
            this.openingPiece = openingPiece;
            setCurrentPlayer();
        } else {
            throw new TicTacToeException("Wrong piece selected, select either 0 or X.");

        }
    }


    /**
     * Missing optimization
     * Computes a List containing the boards with all the possible moves for the current player.
     *
     * @return List of children of this board.
     * @throws CloneNotSupportedException in case it cannot create a new board to hold the new positions.
     */
    @Override
    public List<IBoardGame> children() throws CloneNotSupportedException {
        List<IBoardGame> children = new ArrayList<>();
        char nextPiece;

        if (this.currentPlayer == 'X') nextPiece = 'X';
        else nextPiece = '0';

        List<Point> availablePositions = this.getEmptyPositions();
        for (Point c : availablePositions) {
            TicTacToe b = (TicTacToe) this.clone();
            try {
                b.placeMove(c, nextPiece);
            } catch (TicTacToeException e) {
                System.out.println(e.toString());
                System.exit(0);
            }
            children.add(b);
        }
        return children;
    }

    @Override
    public List<Point> getEmptyPositions() {
        return this.board.getElementPositions('_');
    }


    @Override
    public String getStatus() {
        String result = "in progress";
        if (checkWin('0')) {
            result = "0";
        } else if (checkWin('X')) {
            result = "X";
        } else if (checkDraw()) {
            result = "draw";
        }
        return result;
    }

    /***
     *
     * @return
     */
    private boolean checkDraw() {
        boolean result = false;
        if (this.getEmptyPositions().size() == 0)
            result = true;
        return result;
    }

    /***
     *
     * @param piece
     * @return
     */
    private boolean checkWin(char piece) {
        boolean result = false;
        if (checkRows(piece) || checkDiagonals(piece) || checkColumns(piece))
            result = true;
        return result;
    }

    /***
     *
     * @param piece
     * @return
     */
    public boolean checkRows(char piece) {
        boolean result = false;
        for (int i = 0; i < dim; i++) {
            int j = 0;
            int counter = 0;
            while (j < dim && this.board.getBoard()[i][j] == piece) {
                counter++;
                j++;
                if (counter == dim)
                    result = true;
            }
        }
        return result;
    }

    /***
     *
     * @param piece
     * @return
     */
    public boolean checkColumns(char piece) {
        boolean result = false;
        for (int j = 0; j < dim; j++) {
            int i = 0;
            int counter = 0;
            while (i < dim && this.board.getBoard()[i][j] == piece) {
                counter++;
                i++;
                if (counter == dim)
                    result = true;
            }
        }
        return result;
    }

    /***
     *
     * @param piece
     * @return
     */
    public boolean checkDiagonals(char piece) {
        boolean result = false;
        int j = 0;
        int counter = 0;

        //check \ diagonal
        while (j < dim && this.board.getBoard()[j][j] == piece) {
            counter++;
            j++;
            if (counter == dim)
                result = true;
        }
        j = 0;
        int i = dim - 1;

        counter = 0;
        //check / diagonal
        while (j < dim && i >= 0 && this.board.getBoard()[i][j] == piece) {
            counter++;
            j++;
            i--;
            if (counter == dim)
                result = true;
        }

        return result;
    }


    /***
     *
     * @param c
     * @param piece
     * @throws TicTacToeException
     */
    public void placeMove(Point c, char piece) throws TicTacToeException {
        try {
            if (this.getBoard().isPositionAvailable(c, '_')) {
                this.getBoard().placeElement(c, piece);
                this.setCurrentPlayer();
            } else {
                throw new TicTacToeException("The selected position is already occupied.");
            }
        } catch (BoardException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
    }


    /**
     * Creates a 'deep' copy of TicTacToe object.
     *
     * @return deep copy of the object
     * @throws CloneNotSupportedException if
     */
    protected Object clone() throws CloneNotSupportedException {

        TicTacToe ticTacToeCopy = (TicTacToe) super.clone();

        ticTacToeCopy.board = (Board) this.board.clone();

//        System.out.println("Original is"+ this);
//        System.out.println("Clone is" + ticTacToeCopy);
//        ticTacToeCopy.getBoard().getBoard()[0][2] = '0';
//        System.out.println("Original is"+ this);
//        System.exit(0);
        ticTacToeCopy.setCurrentPlayer();
        try {
            ticTacToeCopy.setOpeningPiece(this.getOpeningPiece());
        } catch (TicTacToeException e) {
            System.out.println(e.toString());
        }
        return ticTacToeCopy;
    }

    /**
     * Verifies if an object is a TicTacToe and if so, then verifies if two TicTacToe objects are equal,
     * that's if all their positions hold the same values.
     *
     * @param o other object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicTacToe board = (TicTacToe) o;
        if (!this.getBoard().equals(board.getBoard()))
            return false;
        return this.getCurrentPlayer() == board.getCurrentPlayer() && this.getOpeningPiece() == board.getOpeningPiece();
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(board.getBoard());
    }

    /**
     * String representation of the tic tac toe board. Where "_" indicates a blank space.
     *
     * @return String
     */
    public String toString() {
        return board.toString();
    }

    /*************************************
     THIS BELONGS TO THE HEURISTICS
     /*************************************/

    private int checkOpenDiag(char piece) {
        int result = 0;
        int j = 0;
        int counter = 0;
        while (j < dim && (this.board.getBoard()[j][j] == piece || this.board.getBoard()[j][j] == '_')) {
            counter++;
            j++;
            if (counter == 3)
                result++;
        }
        j = 0;
        int i = dim - 1;
        counter = 0;
        while (j < dim && i >= 0 && (this.board.getBoard()[i][j] == piece || this.board.getBoard()[i][j] == '_')) {
            counter++;
            j++;
            i--;
            if (counter == 3)
                result++;
        }

        return result;
    }


    public int checkOpenColumns(char piece) {
        int result = 0;
        for (int j = 0; j < dim; j++) {
            int i = 0;
            int counter = 0;
            while (i < dim && (this.board.getBoard()[i][j] == piece || this.board.getBoard()[i][j] == '_')) {
                counter++;
                i++;
                if (counter == 3)
                    result++;
            }
        }
        return result;
    }

    public int checkOpenRows(char piece) {
        int result = 0;
        for (int i = 0; i < dim; i++) {
            int j = 0;
            int counter = 0;
            while (j < dim && (this.board.getBoard()[i][j] == piece || this.board.getBoard()[i][j] == '_')) {
                counter++;
                j++;
                if (counter == 3)
                    result++;
            }
        }
        return result;
    }

    private int checkOpenCircles() {
        int result = 0;
        char piece = '0';
        result += checkOpenRows(piece);
        result += checkOpenColumns(piece);
        result += checkOpenDiag(piece);
        return result;
    }

    private int checkOpenCrosses() {
        int result = 0;
        char piece = 'X';
        result += checkOpenRows(piece);
        result += checkOpenColumns(piece);
        result += checkOpenDiag(piece);
        return result;
    }

    @Override
    public int getH() {
        int result = 0;

        char lastPlayer = getCurrentPlayer() == 'X' ? 'Y' : 'X';
        if (lastPlayer == 'X') result = checkOpenCrosses() - checkOpenCircles();
        else result = checkOpenCircles() - checkOpenCrosses();

        return result;
    }

}