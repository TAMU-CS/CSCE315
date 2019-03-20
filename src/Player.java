
public class Player {
	
	private int score;
	private int seedsAtPlay; // the amount of seeds
							// that the player chooses to play 
	
	public Player() {
		score = 0;
		seedsAtPlay = 0;
	}
	
	public void pickedUpSeeds(int seeds) {
		seedsAtPlay = seeds;
	}
	
	public void move() {
		seedsAtPlay--;
	}
	
	public int getScore() {
		return score;
	}
}
