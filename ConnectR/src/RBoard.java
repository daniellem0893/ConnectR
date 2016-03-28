import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RBoard {
	static boolean gameWin = false;
	static boolean firstplayer;
	static int rows;
	static int columns;
	static int r;
	static int depth;
	static int board[][];
	static JLabel boardimgs[][];
	static ImageIcon blank = new ImageIcon("D:/Pics/Misc/blank.jpg"); 
	static ImageIcon black = new ImageIcon("D:/Pics/Misc/black.jpg");
	static ImageIcon red = new ImageIcon("D:/Pics/Misc/red.jpg");
	static 	Random rand = new Random(System.currentTimeMillis());
	
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		boolean firstmove = true;
		System.out.println("Play First? (y/n):");
		String result = input.next();
		System.out.println(result);
		result.toLowerCase();
		if(result.equals("y"))
			firstplayer = true;
		else
			firstplayer = false;
		System.out.println("Enter number of rows:");
		rows = input.nextInt();
		System.out.println("Enter number of cols:");
		columns = input.nextInt();
		System.out.println("Enter R:");
		r = input.nextInt();
		System.out.println("Enter depth to search:");
		depth = input.nextInt()+1;
		
		JFrame f = new JFrame();
		f.setTitle("R Board");
	    f.setSize(800, 600);
	    f.setLocationRelativeTo(null);
	     
	    final JPanel panel = new JPanel(new GridLayout(rows+1,columns));
	    JButton button[] = new JButton[columns];
	    
	    board = new int[rows][columns];
	    boardimgs = new JLabel[rows][columns];
	    
	    for(int i = 0; i < columns; i++)
	    {
	    	button[i] = new JButton();
	    	button[i].setText("Drop" + i);
	    	panel.add(button[i]);
	    	final int col = i;
	    	button[i].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	RBoard thisboard = new RBoard();
	            	if(gameWin == false){
	            		thisboard.updateBoard(col, firstplayer);
	            		
	            		if(gameWin == false){
		            		int move = thisboard.buildTree(firstplayer);
		            		thisboard.updateBoard(move, firstplayer);
	            		}
	            	}
	            	else
	            		System.out.println("Game Over. Please Exit.");
	            }
	        });
	    }
	    
	    for(int r = rows-1; r >= 0; r--)
	    {
	    	for(int c = 0; c < columns; c++)
	    	{
		    	boardimgs[r][c] = new JLabel(blank);
		    	boardimgs[r][c].setText("" + r + "," + c);
		    	board[r][c] = 0;
		    	panel.add(boardimgs[r][c]);
	    	}
	    }
	    
	    if(firstmove == true && firstplayer == false)
		{
	    	firstplayer = true;
			RBoard thisboard = new RBoard();
			int move = thisboard.buildTree(firstplayer);
	//		System.out.println("Entering move: " + move);
    		thisboard.updateBoard(move, firstplayer);
    		firstmove = false;
		}
	    
	    f.add(panel);
	    f.pack();
	    f.setVisible(true);
	}
	
	public void updateBoard(int col, boolean player){
//		System.out.println(player);
//		System.out.println(col);
    	for(int ro = 0; ro < rows; ro++){
    //		System.out.println(board[ro][col]);
    		if(board[ro][col] == 0){
    			RBoard thisb = new RBoard();
    			
    			if(player == true){
    	//			System.out.println("Updating black...");
    				board[ro][col] = 1;
    				boardimgs[ro][col].setIcon(black);
    				thisb.checkWin(player, ro, col, r);
    				firstplayer = false;
    			}
    			else{
    	//			System.out.println("Updating red...");
    				board[ro][col] = 2;
    				boardimgs[ro][col].setIcon(red);
    				thisb.checkWin(player, ro, col, r);
    				firstplayer = true;
    			}
    			break;
    		}
    	}
    }
	
	public void checkWin (boolean player, int row, int col, int r){
		int color;
		boolean win = false;
		int numHoriz= 1;
		int numVert = 1;
		int numDiagBlUr = 1;
		int numDiagUlBr = 1;
		
		if(player == true)
			color = 1; //black
		else 
			color = 2; // red
			
		//check horizontal
		for(int i = col-1; i >= 0; i--){
			if(board[row][i] == color){
				numHoriz++;
				if(numHoriz == r){
					win = true;
					gameWin = true;
					
					System.out.println("Game Won!");
				}
			}
			else
				break;
		}
		for(int i = col+1; i < columns; i++){
			if(board[row][i] == color){
				numHoriz++;
				if(numHoriz == r){
					win = true;
					gameWin = true;
					System.out.println("Game Won!");
				}
			}
			else
				break;
		}

		//check vertical
		for(int i = row-1; i >= 0; i--){
			if(board[i][col] == color){
				numVert++;
				if(numVert == r){
					win = true;
					gameWin = true;
					System.out.println("Game Won!");
				}
			}
			else
				break;
		}
		for(int i = row+1; i < rows; i++){
			if(board[i][col] == color){
				numVert++;
				if(numVert == r){
					win = true;
					gameWin = true;
					System.out.println("Game Won!");
				}
			}
			else
				break;
		}
		//check diagonal bl -> ur
		int diagRow = row;
		int diagCol = col;
		
		while(diagRow >= 0 && diagCol >= 0){
			if(diagRow == 0 || diagCol == 0 || diagRow >= rows || diagCol >= columns)
				break;
			else{
				diagRow--;
				diagCol--;
				if(board[diagRow][diagCol] == color){
					numDiagBlUr++;
					if(numDiagBlUr == r){
						win = true;
						gameWin = true;
						System.out.println("Game Won!");
					}
				}
				else
					break;
			}
		}
		diagRow = row;
		diagCol = col;
		while(diagRow < rows && diagCol < columns){
			diagRow++;
			diagCol++;
			if(diagRow == 0 || diagCol == 0 || diagRow >= rows || diagCol >= columns)
				break;
			else{
				
				if(board[diagRow][diagCol] == color){
					numDiagBlUr++;
					if(numDiagBlUr == r){
						win = true;
						gameWin = true;
						System.out.println("Game Won!");
					}
				}
				else
					break;
			}
		}
		//check diagonal ul -> br
		diagRow = row;
		diagCol = col;
		
		while(diagRow < rows && diagCol >= 0){

			diagRow++;
			diagCol--;
			if(diagCol < 0 || diagRow >= rows)
				break;
			else{
				if(board[diagRow][diagCol] == color){
					numDiagUlBr++;
					if(numDiagUlBr == r){
						win = true;
						gameWin = true;
						System.out.println("Game Won!");
					}
				}
				else
					break;
			}
		}
		diagRow = row;
		diagCol = col;
		
		while(diagRow >= 0 && diagCol < columns){
			diagRow--;
			diagCol++;
			if(diagRow < 0 || diagCol >= columns)
				break;
			else{
				if(board[diagRow][diagCol] == color){
					numDiagUlBr++;
					if(numDiagUlBr == r){
						win = true;
						gameWin = true;
						System.out.println("Game Won!");
					}
				}
				else
					break;
			}
			
		}
	}
	
	public int buildTree(boolean player){
		
		int tempBoard[][] = new int[rows][columns];
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < columns; c++)
			{
				int val = board[r][c];
				tempBoard[r][c] = val;
			}
		}
		boolean terminalWarning = false;

		RBoard thisb = new RBoard();
		Random rand = new Random();
		int[] selectionArray = new int[columns];
		int d = 1;
		
		int placement = 0;
		boolean placed = false;
		
		//first check for automatic win scenarios or blocking opponent winning scenario
		
		if(terminalWarning == false){
			for(int i=0; i<columns; i++)
			{
				for(int ro = 0; ro < rows; ro++){
		    		if(tempBoard[ro][i] == 0 && placed == false){
		    			if(player == true){
		    				tempBoard[ro][i] = 1;
		    				player = false;
		    				selectionArray[i] = thisb.buildNode(tempBoard, d, i, player);

//		    				System.out.println("Depth " + d + " " +selectionArray[i]);
		    				placed = true;
		    			}
		    			else if(player == false){
		    				tempBoard[ro][i] = 2;
		    				player = true;
		    				selectionArray[i] = thisb.buildNode(tempBoard, d, i, player);

//		    				System.out.println("Depth " + d + " " +selectionArray[i]);
		    				placed = true;
		    			}
		    			else
		    				break;
		    		}
		    	}
				placed = false;
			}

//			System.out.println("Calculating max for depth: " + d);
			int max = 0;

//			System.out.println("Final array: ");
			for(int i=0; i<columns; i++){
//				System.out.println(selectionArray[i]);
				if(selectionArray[i] > max){
					max = selectionArray[i];
					placement = i;
//					System.out.println("New max: " + max + " at " + i);
				}
			}
		}
//		System.out.println("Final choice: " + placement);
		return placement;
	}
	
	public int buildNode(int[][] b, int d, int col, boolean player)
	{
		//System.out.println("passed depth = " + d);
		RBoard thisb = new RBoard();
		int selection = 0;

		int[] selectionArray = new int[columns];
		int[][] tBoard = new int[rows][columns];
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < columns; c++)
			{
				int val = b[r][c];
				tBoard[r][c] = val;
			}
		}
		for(int ro = 0; ro < rows; ro++){
    		if(tBoard[ro][col] == 0){
    			if(player == true){
    				tBoard[ro][col] = 1;
    				player = false;
    			}
    			else{
    				tBoard[ro][col] = 2;
    				player = true;
    			}
    			break;
    		}
    	}
		
		if(d < depth-1)
		{
			d++;
		//	System.out.println("Calculated depth for < d-1 " + d);
			
			for(int i=0; i<columns; i++)
			{
			//	System.out.println("Printing heuristics for depth: " + d);
				selectionArray[i] = thisb.buildNode(tBoard, d, i, player);
			//	System.out.println(selectionArray[i]);
			}
			if(d%2 == 0) // min
			{
				int min = 1000000;

			//	System.out.println("Calculating min for depth: " + d);
				for(int i=0; i<columns; i++){
					if(selectionArray[i] < min){
						min = selectionArray[i];
						selection = min;
				//		System.out.println("New min: " + selection);
					}
				}
			}
			else // max
			{
				int max = 0;

		//		System.out.println("Calculating max for depth: " + d);
				for(int i=0; i<columns; i++){
					if(selectionArray[i] > max){
						max = selectionArray[i];
						selection = max;
			//			System.out.println("New max: " + selection);
					}
				}
			}
		}
		else if(d == depth-1){
			d++;
		//	System.out.println("Calculated depth for == d-1 " + d);
		//	System.out.println("Printing heuristics for depth: " + d);
			for(int i=0; i<columns; i++){
				selectionArray[i] = thisb.getHeuristic(tBoard, i, player);
		//		System.out.println(selectionArray[i]);
				
			}
			int min = 100000;
		//	System.out.println("Calculating min for depth: " + d);
			for(int i=0; i<columns; i++){
				if(selectionArray[i] < min){
					min = selectionArray[i];
					selection = min;
		//			System.out.println("New min: " + selection);
				}
			}
	//		System.out.println("");
		}
	//	System.out.println("Final selection: " + selection + "\n");
		return selection;
	}
	
	public int getHeuristic(int[][] b, int col, boolean player)
	{
		int  n = rand.nextInt(500) + 1;
		//System.out.println(n);
		return n;
	}
}
