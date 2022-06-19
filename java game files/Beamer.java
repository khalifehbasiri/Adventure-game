
/**
 * A beamer is a device that can be charged and fired. When you charge
 * the beamer, it memorizes the current room. When you fire the beamer, it transports you
 * immediately back to the room it was charged in.
 *
 * @author khalifeh basiri 101195770
 * @version March 18, 2022
 */
public class Beamer extends Item
{
    private String state;
    
    /**
     * Create a new beamer item that can be charged and fired.
     */
    public Beamer()
    {
        super("beamer", "a beamer that can be charged and fired", 5);
        state = "fire";
    }
    
    /**
     * sets beamer to charge mode.
     */
    public void charge()
    {
        state = "charge";
    }
    
    /**
     * sets beamer to fire mode.
     */
    public void fire()
    {
        state = "fire";
    }
    
    /**
     * Returns the state of the beamer.
     * 
     * @return String - Returns the state of the beamer
     */
    public String getState()
    {
        return state;
    }
}
