import java.util.List;

public interface Ilayout {


    /**
     * @return the children of the receiver.
     */
    List<Ilayout> children() throws CloneNotSupportedException;


    /***
     *
     * @return the empty positions
     */
    List<Coordinate> getEmptyPositions();

    /***
     *
     * @return the game state (in progress, circles win, crosses win, draw)
     */
    String getStatus();

    /***
     *
     * @return the player that will make the next move
     */
    char getCurrentPlayer();

    boolean placeMove(Coordinate c,char move);


}

