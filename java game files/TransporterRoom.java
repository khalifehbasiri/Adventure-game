import java.util.Random;

/**
 * Whenever the player leaves this room by typing the "go" command, he or 
 * she is randomly transported into one of the other rooms in the game.
 *
 * @author khalifeh basiri 101195770
 * @version March 18, 2022
 */
public class TransporterRoom extends Room
{
    private Random randomPicker;
    
    /**
     * Constructor for objects of class TransporterRoom
     */
    public TransporterRoom()
    {
        super("in the TransporterRoom");
        randomPicker = new Random();
    }

    /**
     * Choose a random room.
     *
     * @return The room we end up in upon leaving this one.
     */
    private Room findRandomRoom()
    {
        return roomLst().get(randomPicker.nextInt(roomLst().size()));
    }
    
    /**
     * Returns a random room, independent of the direction parameter.
     *
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    @Override
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }
}
