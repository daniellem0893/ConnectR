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
	//file paths for the blank black and red connect R board images, please change accordingly
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
	    
	    for(int i = 0; i < columns; i++) {
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
	    
	    for(int r = rows-1; r >= 0; r--){
	    	for(int c = 0; c < columns; c++){
		    	boardimgs[r][c] = new JLabel(blank);
		    	boardimgs[r][c].setText("" + r + "," + c);
		    	board[r][c] = 0;
		    	panel.add(boardimgs[r][c]);
	    	}
	    }
	    
	    if(firstmove == true && firstplayer == false){
	    	firstplayer = true;
			RBoard thisboard = new RBoard();
		//	int move = thisboard.buildTree(firstplayer);
			int move = columns/2;
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
		RBoard thisb = new RBoard();
		boolean placed = false;
    	for(int ro = 0; ro < rows; ro++){
    //		System.out.println(board[ro][col]);
    		if(board[ro][col] == 0){
    			
    			if(player == true){
    				placed = true;
    				System.out.println("Updating black...");
    				board[ro][col] = 1;
    				boardimgs[ro][col].setIcon(black);
    				thisb.checkWin(player, ro, col, r);
    				firstplayer = false;
    			}
    			else{
    				placed = true;
    				System.out.println("Updating red...");
    				board[ro][col] = 2;
    				boardimgs[ro][col].setIcon(red);
    				thisb.checkWin(player, ro, col, r);
    				firstplayer = true;
    			}
    			break;
    		}
    	}
    	if(placed == false){
			int  n = rand.nextInt(columns);
			thisb.updateBoard(n, player);
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
		boolean fullBoard = true;
		for (int rrrr = 0; rrrr < rows; rrrr++)
		{
			for (int cccc = 0; cccc < columns; cccc++)
			{
				if (board[rrrr][cccc] == 0)
				{
					fullBoard = false;
				}
			}
		}
		if(fullBoard == true)
		{
			gameWin = true;
			System.out.println("Stalemate. Please Exit.");
		}
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
			if(diagRow >= rows || diagCol >= columns)
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
		int color;
		int opColor;
		int tempBoard[][] = new int[rows][columns];
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				int val = board[r][c];
				tempBoard[r][c] = val;
			}
		}
		boolean terminalWarning = false;
		boolean winState = false;
		RBoard thisb = new RBoard();
		int[] selectionArray = new int[columns];
		int d = 1;
		int placement = 0;
		boolean placed = false;
		
		//first check for automatic win scenarios
		if(player == true){
			color = 1; //black
			opColor = 2;
		}
		else {
			color = 2; // red
			opColor = 1;
		}
		for(int c = 0; c < columns; c++){
			boolean rowcheck = false;
			for(int ro = 0; ro < rows; ro++){
			  if(tempBoard[ro][c] == 0 && rowcheck == false){
				  rowcheck = true;
				  System.out.println("Checking at... " + ro + "," + c);
				int numHoriz= 1;
				int enemyHoriz = 0;
				int numVert = 1;
				int enemyVert = 0;
				int numDiagBlUr = 1;
				int enemyDiagBlUr = 0;
				int numDiagUlBr = 1;
				int enemyDiagUlBr = 0;
				boolean playerPiece = true;
				boolean opPiece = true;
					//check horizontal
					for(int i = c-1; i >= 0; i--){
						if(tempBoard[ro][i] == color && playerPiece == true){
							numHoriz++;
							opPiece = false;
							if(numHoriz == r){
							winState = true;
							placement = c;
							System.out.println("H Win, placement: " + placement);
							return placement;
							}
						}
						else if(tempBoard[ro][i] == opColor && opPiece == true){
							enemyHoriz++;
							playerPiece = false;
						}
						else 
							break;
					}
					playerPiece = true;
					opPiece = true;
					for(int i = c+1; i < columns; i++){
						if(tempBoard[ro][i] == color && playerPiece == true){
							opPiece = false;
							numHoriz++;
							if(numHoriz == r){
								winState = true;
								placement = c;
								System.out.println("H Win, placement: " + placement);
								return placement;
							}
						}
						else if(tempBoard[ro][i] == opColor && opPiece == true){
							playerPiece = false;
							enemyHoriz++;
						}
						else
							break;
					}
					System.out.println("numH at " + ro + "," + c + " " + numHoriz);
					playerPiece = true;
					opPiece = true;
					//check vertical
					for(int i = ro-1; i >= 0; i--){
						if(tempBoard[i][c] == color && playerPiece == true){
							opPiece = false;
							numVert++;
							if(numVert == r){
								winState = true;
								placement = c;
								System.out.println("V Win, placement: " + placement);
								return placement;
							}
						}
						else if(tempBoard[i][c] == opColor && opPiece == true){
							playerPiece = false;
							enemyVert++;
						}
						else
							break;
					}
					playerPiece = true;
					opPiece = true;
					for(int i = ro+1; i < rows; i++){
						if(tempBoard[i][c] == color && playerPiece == true){
							opPiece = false;
							numVert++;
							if(numVert == r){
								winState = true;
								placement = c;
								System.out.println("V Win, placement: " + placement);
								return placement;
							}
						}
						else if(tempBoard[i][c]== opColor && opPiece == true){
							playerPiece = false;
							enemyVert++;
						}
						else
							break;
					}
					System.out.println("numV at " + ro + "," + c + " " + numVert);
					playerPiece = true;
					opPiece = true;
					//check diagonal bl -> ur
						int diagRow = ro;
						int diagCol = c;
						while(diagRow >= 0 && diagCol >= 0){
							if(diagRow == 0 || diagCol == 0 || diagRow >= rows || diagCol >= columns)
								break;
							else{
								diagRow--;
								diagCol--;
								if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
									opPiece = false;
									numDiagBlUr++;
									if(numDiagBlUr == r){
										winState = true;
										placement = c;
										System.out.println("BlUr Win Placement: " + placement);
										return placement;
									}
								}
								else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
									playerPiece = false;
									enemyDiagBlUr++;
								}
								else
									break;
							}
						}
						playerPiece = true;
						opPiece = true;
						diagRow = ro;
						diagCol = c;
						while(diagRow < rows && diagCol < columns){
							if(diagRow == rows-1 || diagCol == columns-1)
								break;
							else{
								diagRow++;
								diagCol++;
								if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
									opPiece = false;
									numDiagBlUr++;
									if(numDiagBlUr == r){
										winState = true;
										placement = c;
										System.out.println("BlUr Win Placement: " + placement);
										return placement;
									}
								}
								else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
									playerPiece = false;
									enemyDiagBlUr++;
								}
								else
									break;
							}
						}
						System.out.println("numBlUr at " + ro + "," + c + " " + numDiagBlUr);
						//check diagonal ul -> br
						diagRow = ro;
						diagCol = c;
						playerPiece = true;
						opPiece = true;
						while(diagRow < rows && diagCol >= 0){
							if(diagCol == 0 || diagRow == rows-1)
								break;
							else{
								diagRow++;
								diagCol--;
								if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
									opPiece = false;
									numDiagUlBr++;
									if(numDiagUlBr == r){
										winState = true;
										placement = c;
										System.out.println("UlBr Win Placement: " + placement);
										return placement;
									}
								}
								else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
									playerPiece = false;
									enemyDiagUlBr++;
								}
								else
									break;
							}
						}
						diagRow = ro;
						diagCol = c;
						playerPiece = true;
						opPiece = true;
						while(diagRow >= 0 && diagCol < columns){
							if(diagRow == 0 || diagCol == columns-1)
								break;
							else{
								diagRow--;
								diagCol++;
								if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
									opPiece = false;
									numDiagUlBr++;
									if(numDiagUlBr == r){
										winState = true;
										placement = c;
										System.out.println("UlBr Win Placement: " + placement);
										return placement;
									}
								}
								else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
									playerPiece = false;
									enemyDiagUlBr++;
								}
								else
									break;
							}
							
						}
						System.out.println("numUlBr at " + ro + "," + c + " " + numDiagUlBr);
	    		}
			}
			rowcheck = false;
		}
		//check for block
		for(int c = 0; c < columns; c++){
			boolean rowcheck = false;
			for(int ro = 0; ro < rows; ro++){
			  if(tempBoard[ro][c] == 0 && rowcheck == false){
				  rowcheck = true;
				//  System.out.println("Checking at... " + ro + "," + c);
				int numHoriz= 1;
				int enemyHoriz = 0;
				int numVert = 1;
				int enemyVert = 0;
				int numDiagBlUr = 1;
				int enemyDiagBlUr = 0;
				int numDiagUlBr = 1;
				int enemyDiagUlBr = 0;
				boolean playerPiece = true;
				boolean opPiece = true;
					//check horizontal
					for(int i = c-1; i >= 0; i--){
						if(tempBoard[ro][i] == color && playerPiece == true){
							numHoriz++;
							opPiece = false;
						}
						else if(tempBoard[ro][i] == opColor && opPiece == true){
							enemyHoriz++;
							playerPiece = false;
							if(enemyHoriz == r-1){
								terminalWarning = true;
								placement = c;
								System.out.println("H Block, placement: " + placement);
								return placement;
							}
						}
						else 
							break;
					}
					playerPiece = true;
					opPiece = true;
					for(int i = c+1; i < columns; i++){
						if(tempBoard[ro][i] == color && playerPiece == true){
							opPiece = false;
							numHoriz++;
						}
						else if(tempBoard[ro][i] == opColor && opPiece == true){
							playerPiece = false;
							enemyHoriz++;
							if(enemyHoriz == r-1){
								terminalWarning = true;
								placement = c;
								System.out.println("H Block, placement: " + placement);
								return placement;
							}
						}
						else
							break;
					}
					playerPiece = true;
					opPiece = true;
					//check vertical
					for(int i = ro-1; i >= 0; i--){
						if(tempBoard[i][c] == color && playerPiece == true){
							opPiece = false;
							numVert++;
						}
						else if(tempBoard[i][c] == opColor && opPiece == true){
							playerPiece = false;
							enemyVert++;
							if(enemyVert == r-1)
							{
								terminalWarning = true;
								placement = c;
								System.out.println("V Block, placement: " + placement);
								return placement;
							}
						}
						else
							break;
					}
					playerPiece = true;
					opPiece = true;
					for(int i = ro+1; i < rows; i++){
						if(tempBoard[i][c] == color && playerPiece == true){
							opPiece = false;
							numVert++;
						}
						else if(tempBoard[i][c]== opColor && opPiece == true){
							playerPiece = false;
							enemyVert++;
							if(enemyVert == r-1){
								terminalWarning = true;
								placement = c;
								System.out.println("V Block, placement: " + placement);
								return placement;
							}
						}
						else
							break;
					}
					playerPiece = true;
					opPiece = true;
					//check diagonal bl -> ur
							int diagRow = ro;
							int diagCol = c;
							while(diagRow >= 0 && diagCol >= 0){
								if(diagRow == 0 || diagCol == 0 || diagRow >= rows || diagCol >= columns)
									break;
								else{
									diagRow--;
									diagCol--;
									if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
										opPiece = false;
										numDiagBlUr++;
									}
									else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
										playerPiece = false;
										enemyDiagBlUr++;
										if(enemyDiagBlUr == r-1){
											terminalWarning = true;
											placement = c;
											System.out.println("BlUr Block Placement: " + placement);
											return placement;
										}
									}
									else
										break;
								}
							}
							playerPiece = true;
							opPiece = true;
							diagRow = ro;
							diagCol = c;
							while(diagRow < rows && diagCol < columns){
								diagRow++;
								diagCol++;
								if(diagRow >= rows || diagCol >= columns)
									break;
								else{
									if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
										opPiece = false;
										numDiagBlUr++;
									}
									else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
										playerPiece = false;
										enemyDiagBlUr++;
										if(enemyDiagBlUr == r-1){
											terminalWarning = true;
											placement = c;
											System.out.println("BlUr Block Placement: " + placement);
											return placement;
										}
									}
									else
										break;
								}
							}
							//check diagonal ul -> br
							diagRow = ro;
							diagCol = c;
							playerPiece = true;
							opPiece = true;
							while(diagRow < rows && diagCol >= 0){
								diagRow++;
								diagCol--;
								if(diagCol < 0 || diagRow >= rows)
									break;
								else{
									if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
										opPiece = false;
										numDiagUlBr++;
									}
									else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
										playerPiece = false;
										enemyDiagUlBr++;
										if(enemyDiagUlBr == r-1){
											terminalWarning = true;
											placement = c;
											System.out.println("UlBr Block Placement: " + placement);
											return placement;
										}
									}
									else
										break;
								}
							}
							diagRow = ro;
							diagCol = c;
							playerPiece = true;
							opPiece = true;
							while(diagRow >= 0 && diagCol < columns){
								diagRow--;
								diagCol++;
								if(diagRow < 0 || diagCol >= columns)
									break;
								else{
									if(tempBoard[diagRow][diagCol] == color && playerPiece == true){
										opPiece = false;
										numDiagUlBr++;
									}
									else if(tempBoard[diagRow][diagCol] == opColor && opPiece == true){
										playerPiece = false;
										enemyDiagUlBr++;
										if(enemyDiagUlBr == r-1){
											terminalWarning = true;
											placement = c;
											System.out.println("UlBr Block Placement: " + placement);
											return placement;
										}
									}
									else
										break;
								}
								
							}
	    		}
			}
			rowcheck = false;
		}
		//now begin to build the tree
		if(terminalWarning == false && winState == false){
			for(int i=0; i<columns; i++)
			{
				for(int ro = 0; ro < rows; ro++){
		    		if(tempBoard[ro][i] == 0 && placed == false){
		    			if(player == true){
		    				tempBoard[ro][i] = 1;
		    				player = false;
		    				selectionArray[i] = thisb.buildNode(tempBoard, d, i, player);
		    				placed = true;
		    			}
		    			else if(player == false){
		    				tempBoard[ro][i] = 2;
		    				player = true;
		    				selectionArray[i] = thisb.buildNode(tempBoard, d, i, player);
		    				placed = true;
		    			}
		    			else
		    				break;
		    		}
		    	}
				placed = false;
			}
			int max = 0;
			for(int i=0; i<columns; i++){
				if(selectionArray[i] > max){
					max = selectionArray[i];
					placement = i;
				}
			}
		}
		System.out.println(winState);
		System.out.println(terminalWarning);
		System.out.println(placement);
		return placement;
	}
	
	public int buildNode(int[][] b, int d, int col, boolean player)
	{
		RBoard thisb = new RBoard();
		int selection = 0;
		int[] selectionArray = new int[columns];
		int[][] tBoard = new int[rows][columns];
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
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
		if(d < depth-1){
			d++;
			for(int i=0; i<columns; i++){
				selectionArray[i] = thisb.buildNode(tBoard, d, i, player);
			}
			if(d%2 == 0){
				int min = 1000000;
				for(int i=0; i<columns; i++){
					if(selectionArray[i] < min){
						min = selectionArray[i];
						selection = min;
					}
				}
			}
			else{
				int max = 0;
				for(int i=0; i<columns; i++){
					if(selectionArray[i] > max){
						max = selectionArray[i];
						selection = max;
					}
				}
			}
		}
		else if(d == depth-1){
			d++;
			for(int i=0; i<columns; i++){
				selectionArray[i] = thisb.getHeuristic(tBoard, i, player);
			}
			int min = 100000;
			for(int i=0; i<columns; i++){
				if(selectionArray[i] < min){
					min = selectionArray[i];
					selection = min;
				}
			}
		}
		//System.out.println(selection);
		return selection;
	}
	
	public int getHeuristic(int[][] b, int col, boolean player)
	{
		int color;
		int row = 3;
		int score = 0;
		boolean openRow = false;
		int numHoriz= 1;
		int enemyHoriz = 0;
		int numVert = 1;
		int enemyVert = 0;
		int numDiagBlUr = 1;
		int enemyDiagBlUr = 0;
		int numDiagUlBr = 1;
		int enemyDiagUlBr = 0;
		
		if(player == true)
			color = 1; //black
		else 
			color = 2; // red
		for(int ro = 0; ro < rows; ro++){
    		if(b[ro][col] == 0){
    			row = ro;
    			openRow = true;
    			break;
    		}
    	}
		if(openRow == false){
			score = 999999999;
			return 999999999;
		}
		//check horizontal
		for(int i = col-1; i >= 0; i--){
			if(b[row][i] == color){
				numHoriz++;
				score += 5;
				if(numHoriz == r){
					score += 1000;
				}
			}
			else if(b[row][i] == 0){
				score += 3;
				break;
			}
			else if(b[row][i] != 0 && b[row][i] != color){
				enemyHoriz++;
				score -=3;
				if(enemyHoriz == r-1)
					score += 1000;
				break;
			}
			else
				break;
		}
		for(int i = col+1; i < columns; i++){
			if(b[row][i] == color){
				numHoriz++;
				score += 5;
				if(numHoriz == r)
					score += 1000;
			}
			else if(b[row][i] == 0){
				score += 3;
				break;
			}
			else if(b[row][i] != 0 && b[row][i] != color){
				enemyHoriz++;
				score -=3;
				if(enemyHoriz == r-1)
					score += 1000;
				break;
			}
			else
				break;
		}
		//check vertical
		for(int i = row-1; i >= 0; i--){
			if(b[i][col] == color){
				numVert++;
				score += 5;
				if(numVert == r)
					score += 1000;
			}
			else if(b[i][col] == 0){
				score += 3;
				break;
			}
			else if(b[i][col] != 0 && b[i][col] != color){
				enemyVert++;
				score -=3;
				if(enemyVert == r-1)
					score += 1000;
				break;
			}
			else
				break;
		}
		for(int i = row+1; i < rows; i++){
			if(b[i][col] == color){
				numVert++;
				score += 5;
				if(numVert == r)
					score += 1000;
			}
			else if(b[i][col] == 0){
				score += 3;
				break;
			}
			else if(b[i][col] != 0 && b[i][col] != color){
				enemyVert++;
				score -=3;
				if(enemyVert == r-1)
					score += 1000;
				break;
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
						if(b[diagRow][diagCol] == color){
							numDiagBlUr++;
							score += 10;
							if(numDiagBlUr == r){
								score += 2000;
							}
						}
						else if(b[diagRow][diagCol] == 0){
							score += 6;
							break;
						}
						else if(b[diagRow][diagCol] != 0 && b[diagRow][diagCol] != color){
							enemyDiagBlUr++;
							score -=6;
							if(enemyDiagBlUr == r-1)
								score += 1000;
							break;
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
					if(diagRow >= rows || diagCol >= columns)
						break;
					else{
						if(b[diagRow][diagCol] == color){
							numDiagBlUr++;
							score += 10;
							if(numDiagBlUr == r){
								score += 2000;
							}
						}
						else if(b[diagRow][diagCol] == 0){
							score += 6;
							break;
						}
						else if(b[diagRow][diagCol] != 0 && b[diagRow][diagCol] != color){
							enemyDiagBlUr++;
							score -=6;
							if(enemyDiagBlUr == r-1)
							{
								score += 1000;
							}
							break;
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
						if(b[diagRow][diagCol] == color){
							numDiagUlBr++;
							score += 10;
							if(numDiagUlBr == r)
								score += 2000;
						}
						else if(b[diagRow][diagCol] == 0){
							score += 6;
							break;
						}
						else if(b[diagRow][diagCol] != 0 && b[diagRow][diagCol] != color){
							enemyDiagUlBr++;
							score -=6;
							if(enemyDiagUlBr == r-1)
								score += 1000;
							break;
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
						if(b[diagRow][diagCol] == color){
							numDiagUlBr++;
							score += 10;
							if(numDiagUlBr == r)
								score += 2000;
						}
						else if(b[diagRow][diagCol] == 0){
							score += 6;
							break;
						}
						else if(b[diagRow][diagCol] != 0 && b[diagRow][diagCol] != color){
							enemyDiagUlBr++;
							score -=6;
							if(enemyDiagUlBr == r-1)
								score += 1000;
							break;
						}
						else
							break;
					}
					
				}
		return score;
	}
}