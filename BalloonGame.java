// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP-102-112 - 2021T1, Assignment 8
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;

import java.util.*;
import java.awt.Color;

/**
 * Program for a simple game in which the player has to blow up balloons
 * on the screen.
 * The game starts with a collection of randomly placed small balloons
 * (coloured circles) on the graphics pane.
 * The player then clicks on balloons to blow them up by a small amount
 * (randomly increases the radius between 4 and 10 pixels).
 * If an expanded balloon touches another balloon, then they both "burst" and go grey.
 * The goal is to get the largest score. The score is the total of the
 * sizes (areas) of all the active balloons, minus the total size of all
 * the burst balloons.
 * At each step, the current score is recalculated and displayed,
 * along with the highest score that the player has achieved so far.
 * At any time, the player may choose to stop and "lock in" their score.
 * <p>
 * The BalloonGame class has a field containing an Arraylist of Balloon objects
 * to represent the current set of Balloons on the screen.
 * It has a field to hold the highest score.
 * <p>
 * The New Game button should start a new game.
 * The Lock Score button should finish the current game, updating the highest score
 * <p>
 * Clicking (ie, releasing) the mouse on the graphics pane is the main "action"
 * of the game. The action should do the following
 * Find out if the mouse was clicked on top of any balloon.
 * If so,
 * Make the balloon a bit larger and redraw it.
 * Check whether the balloon is touching any other balloon.
 * If so
 * burst the two balloons (which will make them go grey)
 * redraw the burst Balloons
 * Recalculate and redisplay the score
 * If all the balloons are gone, the game is over.
 * <p>
 * To start a game, the program should
 * Clear the graphics pane
 * Initialise the score information
 * Make a new list of Balloons at random positions
 * Print a message
 * <p>
 * If the game is over, the program should
 * Update the highest score if the current score is better,
 * Print a message reporting the scores,
 * <p>
 * There are lots of ways of designing the program. It is not a good idea
 * to try to put everything into one big method.
 * <p>
 * Note that the Balloon class is written for you. Make sure that you know
 * all its methods - no marks for redoing code that is given to you.
 */
public class BalloonGame {
    public int MAX_BALLOONS = 10;

    private ArrayList<Balloon> balloons = new ArrayList<Balloon>(); // The list of balloons
    // (initially empty)

    // Fields
    Integer score = 0;
    Integer highScore = 0;
    /*# YOUR CODE HERE */

    public void setupGUI() {
        UI.setWindowSize(600, 600);
        /*# YOUR CODE HERE */
        UI.addButton("New Game", this::restartGame);
        UI.addButton("Lock Score", this::endGame);
        UI.addSlider("Balloon Amount", 2, 22, 10, this::balloonCreate);
        UI.setMouseListener(this::doMouse);
        UI.setDivider(0.0);
        restartGame();
    }

    /**
     * Start the game:
     * Clear the graphics pane
     * Initialise the score information
     * Make a new set of Balloons at random positions
     */
    public void restartGame() {
        /*# YOUR CODE HERE */
        UI.clearGraphics();
        score = 0;
        balloons.clear();
        UI.drawString("Score: " + score.toString(), 10, 10);
        UI.drawString("High Score: " + highScore.toString(), 100, 10);

        for (int i = 0; i < MAX_BALLOONS; i++) {
            balloons.add(new Balloon(Math.random() * 600, Math.random() * 600));
        }
        for (Balloon eF : balloons) {
            eF.draw();
        }
        calcScore();


    }

    /**
     * Main game action:
     * Find the balloon at (x,y) if any,
     * Expand it
     * Check whether it is touching another balloon,
     * If so, burst both balloons.
     * Redraw the balloon (and the other balloon if it was touching)
     * Calculate and Report the score. (Hint: use UI.printMessage(...) to report)
     * If there are no active balloons left, end the game.
     */
    public void doMouse(String action, double x, double y) {
        /*# YOUR CODE HERE */
        if (action.equals("pressed")) {
            onBalloon(x, y);
            gameOver();
            calcScore();
            UI.setColor(Color.white);
            UI.fillRect(0, 0, 200, 10);
            UI.setColor(Color.black);
            UI.drawString("Score: " + score.toString(), 10, 10);
            UI.drawString("High Score: " + highScore.toString(), 100, 10);
        }
    }

    // Possible additional helper methods:
    //    public Balloon findBalloonOn(double x, double y){...
    //       for finding the (active) balloon that the point (x,y) is on, if any

    //    public Balloon findTouching(Balloon balloon){...
    //       for finding another active balloon touching the given one.

    //    public int calculateScore(){...
    //       for calculating the current score

    //    public boolean allBalloonsBurst(){...
    //       to find out if all the balloons have been burst.

    //    public void endGame(){...
    //        to update the highestScore and print a message

    /*# YOUR CODE HERE */
    public void endGame() {
        UI.clearGraphics();
        highScore = score;
        UI.drawString("High Score: " + highScore.toString(), 100, 10);
        UI.drawString("Score: " + score.toString(), 10, 10);
        restartGame();
    }

    public void onBalloon(double x, double y) {
        for (Balloon eF : balloons) {
            if (eF.on(x, y)) {
                eF.expand();
                balloonTouch(eF);
            }
            eF.draw();
        }
    }

    public void balloonTouch(Balloon other) {
        for (Balloon iF : balloons) {
            if (iF.isTouching(other) && (iF != other) && (iF.isActive() && other.isActive())) {
                iF.burst();
                other.burst();
            }
        }
    }

    public void gameOver() {

        int count = MAX_BALLOONS;
        for (Balloon iF : balloons) {
            if (!iF.isActive()) {
                count--;
            }
        }
        if (count == 0) {
            UI.println("Game Over");
            UI.println("Score: " + score);
        }


    }

    public void balloonCreate(double v) {
        if (v % 2 == 0) {
            MAX_BALLOONS = (int) v;
        } else {
            MAX_BALLOONS = (int) v + 1;
        }
    }

    public void calcScore() {
        score = 0;
        for (Balloon iF : balloons) {
            if (iF.isActive()) {
                score = score + iF.size();
            } else {
                score = score - iF.size();
            }
        }

    }

    public static void main(String[] arguments) {
        BalloonGame bg = new BalloonGame();
        bg.setupGUI();
    }

}
