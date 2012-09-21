package jhunovis.experiments.bowling;

import java.util.Arrays;

@SuppressWarnings("serial")
public class BowlingException extends RuntimeException {

	private static final String MESSGAGE 
		= "Cannot add roll %d to current game with rolls %s";

	public BowlingException(int roll, int[] currentGameRolls) {
		super(String.format(MESSGAGE, roll, Arrays.toString(currentGameRolls)));
	}
}
