public class SequenceCheck {
	
	public static boolean checkValidSequence(char[] letters) {
		boolean valid = true;
		
		// String should be correct length
		if (letters.length!=7) {
			System.out.println("The sequence must have exactly 7 letters.");
			valid = false;
			return valid;
		}
		
		// String should have 7 unique letters
		char[] uniqueLtrs = new char[7];
		boolean allLettersUnique = true;
		
		for (int ltr = 0; allLettersUnique && ltr<letters.length; ltr++) {
			boolean letterUnique = true;
			for (int ind = 0; letterUnique && ind<ltr; ind++) {
				if (Character.toUpperCase(letters[ltr]) == Character.toUpperCase(uniqueLtrs[ind])) {
					letterUnique = false;
				}
			}
			if (letterUnique) {
				uniqueLtrs[ltr] = Character.toUpperCase(letters[ltr]);
			}
			else {allLettersUnique = false;}
		}
		if (!allLettersUnique) {
			System.out.println("The sequence must have 7 unique letters.");
			valid = false;
			return valid;

		}
		
		// String should have exactly 1 uppercase letter
		int numUpperCase = 0;
		for (char ltr : letters) {
			if (ltr == Character.toUpperCase(ltr)) {
				numUpperCase++;
			}
		}
		if (numUpperCase != 1) {
			System.out.println("You must have exactly one uppercase letter in your sequence.");
			valid = false;
			return valid;
		}
		return valid;
	}
}
