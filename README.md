# Pachinko Machine
# Andrew Meservy
==========================================================================================================
What is it?

This is a console-based real-time pachinko machine. It has point jackpots, ball return jackpots, and lazers
that destroy your pachinko balls. As you gain points, your pachinko balls upgrade to different characters.

I built this in Java.

Essentially how the updates work is this:
The main method contains a loop. Inside this loop, the code executes game logic,
then prints several new line characters to clear the screen, then waits a specific amount of time.
The console piece links (using a different thread) to a JavaFX/Swing window that lets you input info to the game dynamically.
