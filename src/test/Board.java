package test;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Board {

	//array representing game starting at house1-5, player 0's hole, house 1-5, player 1's hole
	int board[];
	
	//current player moving (player 0 first, player 1 next)
	int curMove;
	
	/* Constructor
	 * 3 feedback for initiation: houses, seeds, and randomize
	 */
	public Board(int houses, int seeds, boolean randomize) {
		
		//initiate array with proper length
		board = new int[houses * 2 + 2];
		
		//initiate house with seed amount
		for(int i = 0; i < houses; i++) {
			board[i] = seeds;
			board[i + houses + 1] = seeds;
		}
		
		//initiate player holes
		board[houses] = 0;
		board[houses * 2 + 1] = 0;
		
		//initiate player moves (always starts on player 0)
		curMove = 0; 
		
		//randomization
		//generate points on a line from 0-----houses*seeds
		if(randomize) {
			//System.out.println("Debug");
			int totalSeeds = seeds * houses;
			int sum = 0;
			int tempSeed;
			int range = 2;
			int[] randHouses = new int[houses];
			for(int i = 0; i < houses; i++) {
				randHouses[i] = 1;
			}
			Random generator = new Random();

			//Generate an array of random values along a normal distribution
			for(int i = 0; i < houses; i++) {
				if(seeds == 2) {
					range = 1;
				}
				double randDouble = generator.nextGaussian() * range + seeds -  1;
				tempSeed = (int) Math.round(randDouble);
				if(tempSeed < 0) {
					tempSeed *= -1;
				}
				sum += tempSeed;
				randHouses[i] += tempSeed;
				if(randHouses[i] > 10 ) {
					int tenDif = randHouses[i] - 10;
					randHouses[i] -= tenDif;
					sum -= tenDif;
				}
			}
			
			int dif = (totalSeeds - houses) - sum;
			

			//Debug: Display Difference

			//System.out.println("Diff: " + dif);
			

			//Check difference in totals
			if(dif > 0) {
				int min = 0;
				for(int i = 0; i < randHouses.length; i++) {
					if(randHouses[i] < randHouses[min]) {
						min = i;
					}
				}
				randHouses[min] += dif;
			}
			else if(dif < 0) {
				int max = 0;
				for(int i = 0; i < randHouses.length; i++) {
					if(seeds == 1 && dif < 0 && randHouses[i] > 0) {
						randHouses[i] -= 1;
						dif += 1;
					}
					if(randHouses[i] > randHouses[max] && seeds != 1) {
						max = i;
					}
				}
				if(seeds != 1) {
				randHouses[max] += dif;
				}
			}

			//Debug: Display Random Values

			//for(int i = 0; i < houses; i++) {
				//System.out.println(randHouses[i]);
			//}
			

			for(int j = 0; j < houses; j++) {
				if(seeds == 1) {
					board[j] = 1;
					board[j + houses + 1] = 1;
				}
				else {
					board[j] = randHouses[j];
					board[j + houses + 1] = randHouses[j];
				}
			}
		} //done randomize
		
		
	}
	
	/*
	 * nextTurn evaluates the board on next turn based on input
	 * returns true if player can move again, otherwise false
	 */
	public void nextTurn(int input) {
		if(curMove == 0) { //begin incrementing from the input move
			
			//seeds in board house
			int seeds = board[input];
			board[input] = 0;
			
			//move seeds through board
			int i = 0;
			int pos = (input + 1) % (board.length); //input+1 will always be in proper house, no checks needed
			while(i < seeds) {
				//increment current hole by 1
				board[pos]++;
				
				//check if LAST seed: i == seeds - 1
				//case 1: lands on empty hole, on plr side (take all in adjacent hole)
				//case 2: lands on plr house (move again)
				if(i == seeds - 1) {
					//check case 1:
					if(board[pos] == 1 && pos < (board.length/2 - 1)) {
						
					}
					//check case 2:
					else if(pos == (board.length/2 - 1)) {
						//don't swap curMove
						return;
					}
				}
				
				i++;
			}
		
			//return the opposite move
			curMove = 1;
		}else if(curMove == 1) {
			
		}
	}
	
	/*
	 * endgame returns true if no possible move can be made
	 */
	public boolean endgame() {
		//check if possible move based on current player 
		if(curMove == 0) {
			
		}else {
			
		}
	}
	
	/*
	 * toString
	 * gives stringified value to represent the current board
	 */
	public String toString() {
		String resp = "[ ";
		
		//give the board
		resp += "board= ";
		for(int i = 0; i < board.length; i++) {
			resp += (board[i] + " ");
		}
		
		//display the current move count
		resp += ("curMove= " + curMove);
		
		return resp + " ]";
	}
	
	public static void main(String[] args) {
		
	}

}
