public class GuessCheck {
	public char centerLetter;
	public String sequence;
	
	public GuessCheck(String sequence, char centerLetter) {
		this.centerLetter = centerLetter;
		this.sequence = sequence;
	}
	
	public boolean isGuessValid(String guess) {
		boolean validGuess = true;
		
		if (isCorrectLength(guess) & includesCenterLetter(guess) & onlyBeeLetters(guess)) {
			validGuess = true;
		}
		else {
			validGuess = false;
		}
		
		return validGuess;
	}
	
	// Check if guess has 4 or more letters
	public boolean isCorrectLength(String guess) {
		boolean isCorrectLength = true;
		
		if (guess.length() < 4) {
			System.out.println("Guesses must be 4 letters or longer. Try again.");
			isCorrectLength = false;
			return isCorrectLength;
		}
		
		return isCorrectLength;
	}
	
	// Check if guess uses center letter
	public boolean includesCenterLetter(String guess) {
		boolean foundCenterLetter = false;
		
		for (char ltr : guess.toCharArray()) {
			if (ltr==centerLetter) {
				foundCenterLetter = true;
			}
		}
		
		if (!foundCenterLetter) {
			System.out.println("Guesses must include the center letter. Try again.");
		}
		return foundCenterLetter;
	}
	
	// Check if guess only uses letters in game sequence
	public boolean onlyBeeLetters(String guess) {
		boolean onlyBeeLetters = true;
		
		for (char gltr : guess.toLowerCase().toCharArray()) {
			boolean ltrInSequence = false;
			for (char sltr : sequence.toLowerCase().toCharArray()) {
				if (gltr == sltr) ltrInSequence = true;
			}
			if (ltrInSequence==false) onlyBeeLetters = false;
		}
		
		if (!onlyBeeLetters) {
			System.out.println("Guesses can only include letters in the sequence. Try again.");
		}
		
		return onlyBeeLetters;
	}
}
