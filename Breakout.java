/*
 * File: Breakout.java
 * -------------------
 * by Innocent Mabuza 
 * 
 * This file implements the Game Breakout based on Stanford intro to computer science with Java starter code and specifications
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JLabel;

public class Breakout extends GraphicsProgram{

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
//	private static final int NTURNS = 3;

	// Walls
	  public static double RightWall = WIDTH - BALL_RADIUS;
	  public static double BottomWall = HEIGHT - BALL_RADIUS;
	  public static	double LeftWall = 0;
	  public static	double TopWall = 0;

	public void init() {
		setSize(WIDTH, 650); //Initialize the window size;
		setBackground(Color.BLACK);
		createBricks();
		createPaddle();
		addMouseListeners();
		createBall();
		
		// Improvement with GUI
		
		createJLabels();
		
		
		
		
	}
	
	public void run(){
		waitForClick();
		animateBall();
			
	}
	
	//GUI
	
	// Creates the points and turns Labels
	
	public void createJLabels(){
		turns = new JLabel("Turns: " + life);
		add(turns, NORTH);
		
		score = new JLabel("Points: " + points);
		add(score, NORTH);
		
	}

	// Create Paddle
	public void createPaddle(){
		paddle = new GRect((getWidth() - PADDLE_WIDTH) / 2, HEIGHT - PADDLE_Y_OFFSET,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLUE);
		add(paddle);
		
	}
	
	// Move paddle with mouse
	public void mouseMoved(MouseEvent e){
		double nowX = e.getX() - paddle.getX(); //sets where the mouse is
		if(e.getX() > 340){
			paddle.move(340 - paddle.getX(), 0);
		} else {
			paddle.move(nowX, 0);
		}
		
	}

	// Create Ball
	public void createBall(){
		ball = new GOval((getWidth() - BALL_RADIUS)/2, (getHeight() - BALL_RADIUS)/2, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.RED);
		add(ball);
		
	}
// Next Level Code
//	public void levelTxt(){
//		GLabel winner = new GLabel("Level 1 Complete", 100, 35);
//					winner.setColor(Color.WHITE);
//					winner.setFont("Helvetica-24");
//					add(winner);
//	}

	// Animate The Ball
	public void animateBall(){
		
		RandomGenerator rgen = new RandomGenerator();
		vx = rgen.nextDouble(1.0, 3.0); //Randomise xVelocity
		vy = 3.0;
		
		if(rgen.nextBoolean(0.5)) vx = -vx;

		life = 3;
		
		
		while(life > 0){
			ball.move(vx, vy);
			pause(1000/100);
			
			GObject collider = getCollidingObject();
			
			if(collider == paddle){
				vy = -vy;
			}else if(collider != null){
				vy = -vy;
				remove(collider);
				points++;
				score.setText("Points: " + points);
				
//				if(points == 100){
//					remove(ball);
//					levelTxt();
//					waitForClick();
//					createBall();
//					createBricks();
//					
//					
//				}
			}
			
			if(ball.getX() >= RightWall){
				vx = -vx;	
			} else if(ball.getY() >= BottomWall){
				life--;
				
				if(life > 0){
					remove(ball);
					GLabel msg = new GLabel("You have " + life + " lives left!!", 100, 50);
					msg.setFont("Helvetica-24");
					msg.setColor(Color.WHITE);
					add(msg);
					waitForClick();
					remove(msg);
					createBall();
					pause(1000);
					turns.setText("Turns: " + life);
				}else{
					GLabel msg = new GLabel("GAME OVER!!", 100, 35);
					msg.setFont("Helvetica-24");
					msg.setColor(Color.WHITE);
					add(msg);
					remove(ball);
				}
				
			} else if(ball.getX() <= LeftWall){
				vx = -vx;
			} else if(ball.getY() <= TopWall){
				vy = -vy;
			}
		}
	}
	
	private GObject getCollidingObject(){
		
		if(getElementAt(ball.getX() + BALL_RADIUS, ball.getY()) != null){
			//Top Right
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
		} else if(getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS) != null){
			//Bottom Right
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
		} else if(getElementAt(ball.getX() , ball.getY()+ BALL_RADIUS) != null){
			//Bottom Left
			return getElementAt(ball.getX() , ball.getY()+ BALL_RADIUS);
		}
		
		return getElementAt(ball.getX(), ball.getY()); //Top Left
		
	}
	
	public void createBricks(){
		
		double allBricksWidth = (BRICK_WIDTH + 4) * 10;
		    double y = BRICK_Y_OFFSET;
			double x = (getWidth()- allBricksWidth)/2;
			
			if(points < 100){
				for(int i = 0; i < NBRICKS_PER_ROW; i++){
			y = BRICK_Y_OFFSET;
			for(int j = 0; j < NBRICK_ROWS; j++){
				Brick = new GRect(x,y,BRICK_WIDTH, BRICK_HEIGHT);
				Brick.setFilled(true);
				Brick.setFillColor(Color.RED);
				if(j == 2 || j == 3){
					Brick.setFillColor(Color.ORANGE);
				} else if(j == 4 || j == 5){
					Brick.setFillColor(Color.YELLOW);
				} else if(j == 6 || j == 7){
					Brick.setFillColor(Color.GREEN);
				}else if(j == 8 || j == 9){
					Brick.setFillColor(Color.CYAN);
				}
				add(Brick);
				y += BRICK_HEIGHT + BRICK_SEP;
			}
			
			x+= BRICK_WIDTH + BRICK_SEP;
			
		}
			}
		
	}
	
	
	private GRect paddle;
	private GOval ball;
	private GRect Brick;
	private double vx, vy;
	private int points = 0;
//	private GLabel pointsTxt;
//	private GLabel lives;
	private int life = 3;
	
	// GUI
	
	private JLabel turns;
	private JLabel score;
	
}
