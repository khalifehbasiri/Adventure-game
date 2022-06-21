Project: Adventure game 
Authors: khalifeh Basiri

Description
------------ 
This project is a simple adventure game. In this version, 
it has a few rooms and the ability for a player to walk between these rooms. 
While in these rooms, the player can pick up or drop objects that have 
names, weights, and descriptions.


Installation
-------------
To install this project, you must download and place the following files in the same folder.
classes Main, Game, Command, 
Room, Parser, Item, CommandWords, Beamer, transporter Room, 
and their corresponding .ctxt and .class files.


Operating system requiremnts
-----------------------------
To run this project on your personal computer, you must have java installed on your computer 
and have an IDE that can run java.



Usage
------
Interactive ui usage:

- Typing “help” will give the user a list of commands
- Typing “go” + "a direction" will move the user to a room in the specified direction
(If the player is in a TransporterRoom, typing go will transport the player to a random room)
- Typing “quit” will quit the program
- Typing “look” will return a description of the current room
- Typing “eat” will eat food if the player has a food item in hand
- Typing “back” will move the player to the previous room if possible
- Typing “stackback” will move the player to the previous room if possible(is stackable)
- Typing “take” + "item" will pick up a specified item in the room
- Typing “drop” will drop the item the player is currently holding

In this game, there is an object called a beamer
- Typing “charge” will charge the beamer saving the current room
- Typing “fire” will transport the player to the room the beamer was charged in

