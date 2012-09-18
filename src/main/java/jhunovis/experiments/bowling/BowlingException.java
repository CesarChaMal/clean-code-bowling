package jhunovis.experiments.bowling;

@SuppressWarnings("serial")
public class BowlingException extends RuntimeException {

	private static final String MESSGAGE 
		= "Cannot add roll %d to current game with rolls %s";

	public int[] currentGameRolls;
	public int roll;

	public BowlingException(int roll, int[] currentGameRolls) {
		super(String.format(MESSGAGE, roll, currentGameRolls));
		this.roll = roll;
		this.currentGameRolls = currentGameRolls;
	}
}
