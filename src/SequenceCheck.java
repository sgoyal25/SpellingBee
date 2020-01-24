public class SequenceCheck {
	
	public static boolean checkValidSequence(String lettersStr) {
		boolean valid = true;
		char[] lettersArr = lettersStr.toCharArray();
		
		// String should be correct length
		if (lettersArr.length!=7) {
			System.out.println("The sequence must have exactly 7 letters.");
			valid = false;
			return valid;
		}
		
		// String should have 7 unique letters
		char[] uniqueLtrs = new char[7];
		boolean allLettersUnique = true;
		
		for (int ltr = 0; allLettersUnique && ltr<lettersArr.length; ltr++) {
			boolean letterUnique = true;
			for (int ind = 0; letterUnique && ind<ltr; ind++) {
				if (Character.toUpperCase(lettersArr[ltr]) == Character.toUpperCase(uniqueLtrs[ind])) {
					letterUnique = false;
				}
			}
			if (letterUnique) {
				uniqueLtrs[ltr] = Character.toUpperCase(lettersArr[ltr]);
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
		for (char ltr : lettersArr) {
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
