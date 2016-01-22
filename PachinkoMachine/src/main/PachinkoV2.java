package main;

import java.util.Scanner;
import java.util.ArrayList;

public class PachinkoV2
{
	static ArrayList<PachinkoBall> AllBalls = new ArrayList<PachinkoBall>();
	final static int MAX_POSITION_X = 55;//starting at 0
	final static int MAX_POSITION_Y = 25;//starting at 0 not including point slot, starting from top! includes dropping slots
	final static int NUM_OF_SLOTS = 26;
	final static int STARTING_AMOUNT = 10;
	static int score;
	static int ballCount = 10;
	static String ballChar = "O";
	
	static int shootingIndex = 0;
	static MessageBox myMessageBox = new MessageBox();
	
	static ArrayList<Vector2> returnBallPos = new ArrayList<Vector2>();
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		StartWithBalls(STARTING_AMOUNT);
		
		InitializeMessageBox();
		
		try
		{
			RunV2(input);
		}
		catch (java.lang.Exception e)
		{
			System.err.println("There was an error.");
		}
		
		input.close();
	}
	
	public static ArrayList<PachinkoBall> GetBallsInPlay(ArrayList<PachinkoBall> balls)
	{
		ArrayList<PachinkoBall> retBalls = new ArrayList<PachinkoBall>();
		
		for (int i = 0; i < balls.size(); i++)
		{
			if (balls.get(i).isInPlay)
			{
				retBalls.add(balls.get(i));
			}
		}
		
		return retBalls;
	}
	
	public static void StartWithBalls(int amount)
	{
		for (int i = 0; i < amount; i++)
		{
			AddNewBall();
		}
	}
	
	public static void AddNewBall()
	{
		AllBalls.add(new PachinkoBall());
	}
	
	public static void InitializeShootNextBall()
	{
		if (!IsAllInPlay() && ballCount > 0)
		{
			if (!(shootingIndex >= 0 && shootingIndex < AllBalls.size()))
			{
				shootingIndex = 0;
			}
			
			AllBalls.get(shootingIndex).isInPlay = true;
			shootingIndex = (shootingIndex >= AllBalls.size()) ? 0 : shootingIndex + 1;
			ballCount -= 1;
		}
	}
	
	private static boolean IsAllInPlay()
	{
		for (int i = 0; i < AllBalls.size(); i++)
		{
			if (!AllBalls.get(i).isInPlay)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static int GetDirection(PachinkoBall ball)
	{
		//0 for left, 1 for right
		int move = (int)((Math.random() * 100) % 2);
		int temp = (int)((Math.random() * 100) % 2);
		
		//get direction
		if (ball.pos.x > MAX_POSITION_X / 2 && move == 1 && temp == 1)
		{
			move = (int)((Math.random() * 100) % 2);
		}
		else if (ball.pos.x <= MAX_POSITION_X / 2 && move == 0 && temp == 1)
		{
			move = (int)((Math.random() * 100) % 2);
		}
		
		return move;
	}
	
	private static void SetBallPosition(PachinkoBall ball)
	{
		int move = GetDirection(ball);
		
		if (move == 0)
		{
			if (ball.pos.x >= 1)
			{
				ball.pos.x -= 1;
			}
			else
			{
				ball.pos.x += 1;
			}
		}
		else if (move == 1)
		{
			if (ball.pos.x < MAX_POSITION_X - 3)
			{
				ball.pos.x += 1;
			}
			else
			{
				ball.pos.x -= 1;
			}
		}
		
		ball.pos.y = (ball.pos.y < MAX_POSITION_Y) ? ball.pos.y + 1 : ball.pos.y;
	}
	
	public static void SetAllBallPos(ArrayList<PachinkoBall> balls, int slotNum)
	{
		for (int i = 0; i < balls.size(); i++)
		{
			if (balls.get(i).isInPlay)
			{
				if (balls.get(i).pos.x == -1 && balls.get(i).pos.y == -1)
				{
					InitializeBallPositionFromSlotNumber(balls.get(i), slotNum);
				}
				else
				{
					SetBallPosition(balls.get(i));
				}
			}
		}
	}
	
	private static void InitializeBallPositionFromSlotNumber(PachinkoBall ball, int slotNum)
	{
		ball.pos.x = slotNum * 2;
		ball.pos.y = 0;
	}
	
	public static int ShotPressed()
	{
		if (myMessageBox.isShot)
		{
			InitializeShootNextBall();
			myMessageBox.isShot = false;
		}
		
		return myMessageBox.slot;
	}
	
	public static void InitializeMessageBox()
	{
		Thread messageThread = new Thread(myMessageBox);
		messageThread.start();
	}
	
	public static boolean IsBallAtPosX(ArrayList<PachinkoBall> balls, int x)
	{
		for (int i = 0; i < balls.size(); i++)
		{
			if (balls.get(i).pos.x == x)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean IsBallAtPos(ArrayList<PachinkoBall> balls, int x, int y)
	{
		for (int i = 0; i < balls.size(); i++)
		{
			if (balls.get(i).pos.x == x && balls.get(i).pos.y == y)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean IsBallAtPosY(ArrayList<PachinkoBall> balls, int y)
	{
		for (int i = 0; i < balls.size(); i++)
		{
			if (balls.get(i).pos.y == y)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void ChangeBallChar()
	{
		if (score >= 600)
		{
			ballChar = "A";
		}
		else if (score >= 500)
		{
			ballChar = "&";
		}
		else if (score >= 400)
		{
			ballChar = "%";
		}
		else if (score >= 300)
		{
			ballChar = "#";
		}
		else if (score >= 200)
		{
			ballChar = "@";
		}
		else if (score >= 100)
		{
			ballChar = "o";
		}
	}
	
	public static void RunV2(Scanner input)
	{
		int slot;
		ArrayList<PachinkoBall> ballsInPlay =  new ArrayList<PachinkoBall>();
		
		boolean cont = true;
		while (cont)
		{	
			slot = ShotPressed();
			
			//InitializeShootNextBall();
			ballsInPlay = GetBallsInPlay(AllBalls);
			
			SetAllBallPos(ballsInPlay, slot);
			
			InitializeMachine(ballsInPlay);
			
			BallReachEndOfMachine(ballsInPlay);
			ChangeBallChar();
			
			try
			{
				Thread.sleep(350);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void BallReachEndOfMachine(ArrayList<PachinkoBall> ballsInPlay)
	{
		for (int x = 0; x < MAX_POSITION_X; x++)
		{
			if (IsBallAtPos(ballsInPlay, x, MAX_POSITION_Y))
			{
				//ball is at bottom of machine
				PachinkoBall bottomBall = GetBallByPos(ballsInPlay, x, MAX_POSITION_Y);
				
				if (x >= 3 && x <= 12 || x >= 16 && x <= 20 || x >= 24 && x <= 30 || x >= 34 && x <= 38 || x >= 42 && x <= 51)// for balls not returned
				{
					bottomBall.isInPlay = false;
					bottomBall.pos.x = -1;
					bottomBall.pos.y = -1;
					
					AllBalls.remove(bottomBall);
				}
				else//for balls returned
				{
					//returnBallPos.add(new Vector2(x, 0));
					
					boolean cont = true;
					int counter = x;
					while (cont)
					{
						if (!IsReturnBallAtPosX(counter))
						{
							returnBallPos.add(new Vector2(counter, 0));
							cont = false;
						}
						else
						{
							counter--;
						}
					}
					
					
					bottomBall.isInPlay = false;
					bottomBall.pos.x = -1;
					bottomBall.pos.y = -1;
					
					AllBalls.remove(bottomBall);
				}
			}
		}
	}
	
	public static PachinkoBall GetBallByPos(ArrayList<PachinkoBall> balls, int x, int y)
	{
		for (int i = 0; i < balls.size(); i++)
		{
			if (balls.get(i).pos.x == x && balls.get(i).pos.y == y)
			{
				return balls.get(i);
			}
		}
		
		return null;
	}
	
	public static void InitializeSlots(ArrayList<PachinkoBall> balls)
	{
		String slots = (ballCount >= 27) ? "| | | | | | | | | | | | | | | | | | | | | | | | | | | |" + ballChar : "| | | | | | | | | | | | | | | | | | | | | | | | | | | | ";
		
		if (IsBallAtPosY(balls, 0))
		{
			for (int x = 0; x < MAX_POSITION_X; x++)
			{
				if (IsBallAtPos(balls, x - 1, 0))
				{
					System.out.print(ballChar);
				}
				else
				{
					System.out.print(slots.charAt(x));
				}
			}
			
			System.out.print(slots.charAt(MAX_POSITION_X) + "|\n");
		}
		else
		{
			System.out.println(slots + "|");
		}
	}
	
	private static void CountEmUp(int y)
	{
		int compNum = MAX_POSITION_Y;
		
		for (int i = 0; i < MAX_POSITION_Y; i++)
		{
			if (y > i + 2)//(y > i + 2)//row 1 > 
			{
				compNum--;
			}
		}
		
		if (y == 1)
		{
			compNum = 26;
		}
		else if (y == 2)
		{
			compNum = 25;
		}
		
		if (ballCount >= compNum)
		{
			System.out.print(ballChar + "|\n");
		}
		else
		{
			System.out.print(" |\n");
		}
	}
	
	private static boolean IsJackPot(int[] pos, int ballPos)
	{
		for (int i = 0; i < pos.length; i++)
		{
			if (ballPos == pos[i])
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static void JackPotRow(ArrayList<PachinkoBall> ballsInPlay, int y, String s, int[] pos, int numOfBalls)
	{
		System.out.print("|");
		for (int x = 0; x < MAX_POSITION_X - 1; x++)
		{
			if (IsBallAtPos(ballsInPlay, x, y))
			{
				if (IsJackPot(pos, x))//lands in basket
				{
					PachinkoBall myBall = GetBallByPos(ballsInPlay, x, y);

					for (int i = numOfBalls; i > 0; i--)
					{
						if (!IsReturnBallAtPosX(i - numOfBalls))
						{
							returnBallPos.add(new Vector2(i - numOfBalls, 0));
						}
						else
						{
							boolean cont = true;
							int counter = i - numOfBalls;
							while (cont)
							{
								if (!IsReturnBallAtPosX(counter))
								{
									returnBallPos.add(new Vector2(counter, 0));
									cont = false;
								}
								else
								{
									counter--;
								}
							}
						}
					}
					
					myBall.isInPlay = false;
					myBall.pos.x = -1;
					myBall.pos.y = -1;
					
					AllBalls.remove(myBall);
				}
				
				System.out.print(ballChar);
			}
			else
			{
				System.out.print(s.charAt(x));
			}
		}
		
		CountEmUp(y);
	}
	
	private static void JackPotPointRow(ArrayList<PachinkoBall> ballsInPlay, int y, String s, int[] pos, int numOfPoints)
	{
		System.out.print("|");
		for (int x = 0; x < MAX_POSITION_X - 1; x++)
		{
			if (IsBallAtPos(ballsInPlay, x, y))
			{
				if (IsJackPot(pos, x))//lands in basket
				{
					score += numOfPoints;
					
					
					PachinkoBall myBall = GetBallByPos(ballsInPlay, x, y);
					
					myBall.isInPlay = false;
					myBall.pos.x = -1;
					myBall.pos.y = -1;
					
					AllBalls.remove(myBall);
				}
				
				if (s.charAt(x) == '=')//lasers
				{
					System.out.print(s.charAt(x));
				}
				else
				{
					System.out.print(ballChar);
				}
			}
			else
			{
				System.out.print(s.charAt(x));
			}
		}
		
		CountEmUp(y);
	}
	
	public static void InitializeMiddleOfMachine(ArrayList<PachinkoBall> ballsInPlay)
	{
		for (int y = 0; y < MAX_POSITION_Y; y++)
		{
			if (y == 0)
			{
				InitializeSlots(ballsInPlay);
			}
			else if (y == 5)
			{
				String s = "* * * * === * * * * * * * * * * * * * * * === * * * *|";
				int[] pos = new int[] {9, 43};
				JackPotPointRow(ballsInPlay, y, s, pos, 0);
			}
			else if (y == 9)
			{
				String s = "* * * * |_| * * * * * * * * * * * * * * * |_| * * * *|";
				int[] pos = new int[] {9, 43};
				JackPotRow(ballsInPlay, y, s, pos, 4);
			}
			else if (y == 13)
			{
				String s = "* * * * * * * * * * * * |___| * * * * * * * * * * * *|";
				int[] pos = new int[] {25, 26, 27};
				JackPotRow(ballsInPlay, y, s, pos, 2);
			}
			else if (y == 14)
			{
				String s = " * * * * * * * * * * === * * === * * * * * * * * * * |";
				int[] pos = new int[] {22, 30};
				JackPotPointRow(ballsInPlay, y, s, pos, 0);
			}
			else if (y == 15)
			{
				String s = "* * * * * * * * * * * * ===== * * * * * * * * * * * *|";
				int[] pos = new int[] {25, 26, 27};
				JackPotPointRow(ballsInPlay, y, s, pos, 0);
			}
			else if (y == 16)
			{
				String s = " * * * * * * * * * * * |$| |$| * * * * * * * * * * * |";
				int[] pos = new int[] {24, 28};
				JackPotPointRow(ballsInPlay, y, s, pos, 50);
			}
			else if (y == 21)
			{
				String s = "* * * * * * |$| * * * * * * * * * * * |$| * * * * * *|";
				int[] pos = new int[] {13, 39};
				JackPotPointRow(ballsInPlay, y, s, pos, 10);
			}
			else
			{
				System.out.print("|");
				if (y % 2 == 1)//odd
				{
					for (int x = 0; x < MAX_POSITION_X - 2; x++)
					{
						if (x % 2 == 1)
						{
							if (IsBallAtPos(ballsInPlay, x, y))
							{
								System.out.print(ballChar);
							}
							else
							{
								System.out.print(" ");
							}
						}
						else
						{
							System.out.print("*");
						}
					}
				}
				else//even
				{
					for (int x = 0; x < MAX_POSITION_X - 2; x++)
					{
						if (x % 2 == 1)
						{
							System.out.print("*");
						}
						else
						{
							if (IsBallAtPos(ballsInPlay, x, y))
							{
								System.out.print(ballChar);
							}
							else
							{
								System.out.print(" ");
							}
						}
					}
				}
				System.out.print("|");
				
				
				CountEmUp(y);
			}
		}
	}
	
	public static void InitializeMachine(ArrayList<PachinkoBall> ballsInPlay)
	{
		Scroll();
		InitializeTopOfMachine();
		InitializeMiddleOfMachine(ballsInPlay);
		InitializeBottomOfMachine(ballsInPlay);
	}
	
	public static void InitializeTopOfMachine()
	{
		System.out.println("Score: " + score + " 	Balls: " + ballCount);
		System.out.println("_______________________________________________________\n"
						 + "|                      PACHINKO!                      |_");
		
		String s = "|_____________________________________________________|";
		
		for (int i = 0; i < s.length(); i++)
		{
			if (i == myMessageBox.slot * 2 + 1)
			{
				System.out.print("v");
			}
			else
			{
				System.out.print(s.charAt(i));
			}
		}
		
		if (ballCount >= 28)
		{
			System.out.print(ballChar + "|");
		}
		else
		{
			System.out.print(" |");
		}
		
		System.out.print("\n");
	}

	public static void InitializeBottomOfMachine(ArrayList<PachinkoBall> ballsInPlay)
	{
		//String s1 = "|  |________|   |___|   |_____|   |___|   |________|  |";
		String s1 = "|  ==========   =====   =======   =====   ==========  |";
		String s2 = "|                                                      ";
		String s3 = "--------------------------------------------------------|\n\n";
		
		if (IsBallAtPosY(ballsInPlay, MAX_POSITION_Y))
		{
			for (int x = 0; x < MAX_POSITION_X; x++)
			{
				if (IsBallAtPos(ballsInPlay, x, MAX_POSITION_Y))
				{
					if (s1.charAt(x) == ' ')//don't print out ball on ====
					{
						System.out.print(ballChar);
					}
					else
					{
						System.out.print(s1.charAt(x));
					}
				}
				else
				{
					System.out.print(s1.charAt(x));
				}
			}
			
			if (ballCount >= 2)
			{
				System.out.println(ballChar + "|");
			}
			else
			{
				System.out.println(" |");
			}
		}
		else
		{
			System.out.print(s1);
			
			if (ballCount >= 2)
			{
				System.out.println(ballChar + "|");
			}
			else
			{
				System.out.println(" |");
			}
		}
		
		
		
		ArrayList<Vector2> tempRetBalls = new ArrayList<Vector2>();
		
		for (int i = 0; i < s2.length(); i++)
		{
			if (i != s2.length() - 1 && i != s2.length() - 2 && IsReturnBallAtPosX(i))
			{
				System.out.print(ballChar);
				tempRetBalls.add(returnBallPos.get(GetReturnBallAtPosX(i)));
			}
			else
			{
				System.out.print(s2.charAt(i));
			}
			
			if (i == s2.length() - 2 && IsReturnBallAtPosX(i))
			{
				ballCount += 1;
				AddNewBall();
			}
		}
		
		for (int i = 0; i < returnBallPos.size(); i++)
		{
			if (returnBallPos.get(i).x < 0)
			{
				tempRetBalls.add(returnBallPos.get(i));
			}
		}
		
		returnBallPos.clear();
		
		for (int i = 0; i < tempRetBalls.size(); i++)
		{
			tempRetBalls.get(i).x += 1;
			returnBallPos.add(tempRetBalls.get(i));
		}
		
		
		if (ballCount >= 1)
		{
			System.out.print(ballChar + "|");
		}
		else
		{
			System.out.print(" |");
		}
		
		
		System.out.println("\n" + s3);
	}
	
	public static void Scroll()
	{
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
	public static boolean IsReturnBallAtPosX(int x)
	{
		for (int r = 0; r < returnBallPos.size(); r++)
		{
			if (returnBallPos.get(r).x == x)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static int GetReturnBallAtPosX(int x)
	{
		for (int r = 0; r < returnBallPos.size(); r++)
		{
			if (returnBallPos.get(r).x == x)
			{
				return r;
			}
		}
		
		return -1;
	}
}

