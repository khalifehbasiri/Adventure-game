import java.util.Stack;
import java.util.ArrayList;
import java.util.Random;

/**
 *  Used sample solution for assignment 4.
 * 
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version A3 Solution
 * 
 * @author khalifeh basiri 101195770
 * @version March 18, 2022
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private Item playerItem;
    private Beamer beamerItem;
    private int itemCount;
    private Room beamerRoom;
    private TransporterRoom transfRoom;
    
    private static final int itemSpace = 5;
        
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        playerItem = null;
        itemCount = 0;
        beamerItem = null;
        beamerRoom = null;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        Item chair1, cookie1, chair2, chair3, bar, computer1, computer2, cookie2, tree1, tree2;
        Beamer beamer1, beamer2;
        TransporterRoom transporter;
        
        // create some items
        chair1 = new Item("chair","a wooden chair",5);
        cookie1 = new Item("cookie","a chocolate chip cookie",1);
        chair2 = new Item("chair","a wooden chair",5);
        chair3 = new Item("chair","a wooden chair",5);
        beamer1 = new Beamer();
        beamer2 = new Beamer();
        bar = new Item("stool","a long bar with stools",95.67);
        computer1 = new Item("PC","a PC",10);
        computer2 = new Item("Mac","a Mac",5);
        cookie2 = new Item("cookie","a chocolate chip cookie",1);
        tree1 = new Item("tree", "a fir tree",500.5);
        tree2 = new Item("tree", "a fir tree",500.5);
       
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporter = new TransporterRoom();
        transfRoom = transporter;
        
        // put items in the rooms
        outside.addItem(tree1);
        outside.addItem(tree2);
        theatre.addItem(chair1);
        pub.addItem(bar);
        pub.addItem(beamer2);
        lab.addItem(cookie1);
        lab.addItem(computer1);
        lab.addItem(chair2);
        lab.addItem(computer2);
        office.addItem(chair3);
        office.addItem(cookie2);
        office.addItem(beamer1);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("west", transporter);

        office.setExit("west", lab);
        
        transporter.setExit("east", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println(carryDescription());
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord() && currentRoom == transfRoom) {
            currentRoom = transfRoom.getExit(command.getSecondWord());
            System.out.println(currentRoom.getLongDescription());
            System.out.println(carryDescription());
            return;
        }else if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            System.out.println(carryDescription());
        }
    }
    
    /**
     * indicate whether or not you are carrying an item.
     *  
     * @return String - Returns a String indicating whether or not you are carrying an item.
     */
    private String carryDescription()
    {
        if(playerItem == null){
            return "You are not carrying anything.";
        }else{
            return "You are carrying a " + playerItem.getName() + ".";
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(currentRoom.getLongDescription());
            System.out.println(carryDescription());
        }
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
        
        if (playerItem == null){
            System.out.println("You do not have any food.");
        }
        else if (playerItem.getName().equals("cookie")){
            System.out.println("You have eaten a cookie.");
            itemCount = itemSpace;
            playerItem = null;
        }
        else {
            System.out.println("You do not have any food.");
        }
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription());
                System.out.println(carryDescription());
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription());
                System.out.println(carryDescription());
            }
        }
    }
    
    /** 
     * take an item in the room based on input.
     * 
     * @param command The command to be processed
     */
    private void take(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("take what?");
            return;
        }

        String item = command.getSecondWord();
        Item crrItem = currentRoom.getItem(item);
        
        if (playerItem != null){
            System.out.println("Cant hold more than one item");
        }else if(itemCount == 0 && currentRoom.contain(crrItem) 
                            && crrItem.getName().equals("cookie")){
            playerItem = crrItem;
            currentRoom.removeItem(crrItem);
            System.out.println("You picked up a " + crrItem.getName() + ".");
        }else if(itemCount == 0){
            System.out.println("Need to eat.");
        }else if(crrItem.getName().equals("beamer")){
            playerItem = crrItem;
            currentRoom.removeItem(crrItem);
            beamerItem = new Beamer();
            System.out.println("You picked up a beamer.");
        }else if (currentRoom.contain(crrItem) && itemCount > 0){
            itemCount -= 1;
            playerItem = crrItem;
            currentRoom.removeItem(crrItem);
            System.out.println("You picked up a " + crrItem.getName() + ".");
        }else{
            System.out.println("That item is not in the room.");
        }
    }
    
    /** 
     * drop item player is holding in current room.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("drop what?");
            return;
        }
        
        if(playerItem == null){
            System.out.println("You have nothing to drop.");
        }else{
            currentRoom.addItem(playerItem);
            System.out.println("you have droped a " + playerItem.getName() + 
                                ".");
            playerItem = null;
            beamerItem = null;
        }
    }
    
    /** 
     * sets beamer to charge mode.
     * 
     * @param command The command to be processed
     */
    private void charge(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("charge what?");
            return;
        }
        
        if(playerItem == null){
            System.out.println("You do not have a beamer.");
        }else if(playerItem.getName().equals("beamer") && beamerItem.getState().equals("fire")){
            beamerItem.charge();
            beamerRoom = currentRoom;
            System.out.println("The beamer is charged.");
        }else{
            System.out.println("Can only charge a beamer.");
        }
    }
    
    /** 
     * sets beamer to fire mode.
     * 
     * @param command The command to be processed
     */
    private void fire(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("fire what?");
            return;
        }
        
        if(playerItem == null){
            System.out.println("You do not have a beamer.");
        }else if(playerItem.getName().equals("beamer") && beamerItem.getState().equals("charge")){
            beamerItem.fire();
            currentRoom = beamerRoom;
            System.out.println("The beamer is fired.");
        }else{
            System.out.println("Can only fire a beamer.");
        }
    }
}