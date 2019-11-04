//import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;

public class SpellingBee {
	
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
		String lettersStr = null;
		char[] lettersArr = null;
		boolean quit = false;
		
		System.out.println("Rules:");
		System.out.println("1) Every game has a sequence of seven unique letters. One of these letters is designated the 'required letter'.");
		System.out.println("2) To earn points, you must enter words using only the letters in the original sequence. The same letter can be used multiple times.");
		System.out.println("3) To be considered valid, words must be 4 or more letters. Words which NYT considers obscure or offensive are not accepted.");
		System.out.println("4) To exit the game at any time, simply enter 'goodbye'.");
		
		boolean readyToStart = false;
		
		while (!readyToStart) {	
			boolean validLtr = false;
			// Check validity of initial letters.
			while (!validLtr) {
				System.out.println("\nInput the letters for today's game, with only the required letter capitalized.");
				lettersStr = keyboard.nextLine().trim();
				if (lettersStr.toUpperCase().equals("END")) {
					quit = true;
					break;
				}
				lettersArr = lettersStr.toCharArray();
				validLtr = SequenceCheck.checkValidSequence(lettersArr);
			}
			
			if (quit) break;			
			// Get user consent to begin.
			System.out.println("Your letters are '"+lettersStr+"'. Are you ready to play? (Y/N)");
			String answer = keyboard.nextLine().trim();
			if (answer.toUpperCase().equals("END")) {
				quit = true;
				break;
			}
			if (answer.toUpperCase().equals("Y")) {
				readyToStart = true;
			}
		}
		
		// Early exit; no gameplay completed
		if (quit) {
			System.out.print("Exiting game. Goodbye!");
			System.exit(0);
		}
				
		System.out.println("\nGetting your game ready...");
		
		Game myGame = new Game(lettersStr);
		int points = 0;
		int pointsForGenius = myGame.totalPoints;
		String[] wordsFound = new String[myGame.answerKey.length];
		int numWordsFound = 0;
		
		String guess; 
		
		System.out.print("The total number of points possible in this game is "+myGame.totalPoints+". ");
		System.out.println("Do you want to enter a score required to achieve 'Genius'? (Y/N)");
		String answer2 = keyboard.nextLine().trim().toUpperCase();
		if (answer2.contentEquals("END")) {
			quit = true;
		}
		else if (answer2.equals("N")) {
			pointsForGenius = myGame.totalPoints;
		}
		else {
			System.out.println("Enter the number of points required to achieve 'Genius'");
			int num = keyboard.nextInt();
			while (num>myGame.totalPoints) {
				System.out.println("Oops. Points to 'Genius' must be no more than "+myGame.totalPoints+". Enter a different number");
				num = keyboard.nextInt();
			}
			pointsForGenius = num;
			keyboard.nextLine();
		}
		
		// Early exit 2; no game play completed
		if (quit) {
			System.out.print("Exiting game. Goodbye!");
			System.exit(0);
		}
		
		// For first word only
		while (numWordsFound == 0 && !quit) {
			boolean validGuess = false;
			
			System.out.print("Your letters are: ");
			for (char ltr : myGame.lettersArr) {
				System.out.print(Character.toUpperCase(ltr)+" ");
			}
			System.out.println("\nThe center letter is "+myGame.centerLetter+". Enter your guess: ");
			do {
				guess = keyboard.nextLine().trim().toUpperCase();
				if (guess.equals("END")) {
					quit = true;
					break;
				}
				validGuess = myGame.guessChecker.isGuessValid(guess);
			} while (!validGuess && !quit);
			
			if (quit) break;
		
			boolean guessIsCorrect = false;
			
			// System.out.println("Checking your guess...");
			for (String a:myGame.answerKey) {
				if (guess.equals(a.toUpperCase())) {
					guessIsCorrect = true;
				}
			}

			if (!guessIsCorrect) {
				System.out.println("Sorry, your guess is not correct.");
			}
			else {
				System.out.print("Great! ");
				if (myGame.isPangram(guess)) {
					System.out.print("You found a pangram!");
				}
				wordsFound[numWordsFound] = guess;
				points += myGame.calculatePoints(guess);					
				numWordsFound++;
			}
		}
		
		// All turns after first word
		while (!quit) {
			while (points<pointsForGenius && !quit) {
				System.out.print("\nYou've found "+numWordsFound+" words: ");
				for (int ind=0; ind<numWordsFound; ind++) {
					System.out.print(wordsFound[ind]);
					if (ind != numWordsFound-1) {
						System.out.print(", ");
					}
				}
				System.out.println("\nPoints: "+points);
				System.out.print("Your letters are: ");
				for (char ltr : myGame.lettersArr) {
					System.out.print(Character.toUpperCase(ltr)+" ");
				}
				System.out.println("\nThe center letter is "+myGame.centerLetter+".");
				
				boolean foundNewWord = false;
				do {
					// Ask for next guess
					System.out.print("\nEnter your guess: ");
					
					// Check if guess is valid
					boolean validGuess = false;
					do {
						guess = keyboard.nextLine().trim().toUpperCase();
						if (guess.equals("END")) {
							quit = true;
						}
						if (quit==true) break;
						validGuess = myGame.guessChecker.isGuessValid(guess);
					} while (!validGuess && !quit);
					
					if (quit==true) break;
					
					// Check if guess is in answer key			
					boolean guessInAnswerKey = false;
					for (String a:myGame.answerKey) {
						if (guess.equals(a.toUpperCase())) {
							guessInAnswerKey = true;
						}
					}
					
					boolean guessIsNew = true;
					for (String w:wordsFound) {
						if (guess.equals(w)) {
							guessIsNew = false;
						}
					}
								
					if (guessInAnswerKey && guessIsNew) {
						System.out.print("Great! ");
						if (myGame.isPangram(guess)) {
							System.out.print("And you found a pangram!");
						}
						wordsFound[numWordsFound] = guess;
						points += myGame.calculatePoints(guess);					
						numWordsFound++;
						foundNewWord = true;
					}
					
					else if (!guessInAnswerKey) {
						System.out.println("Sorry, your guess is not correct.");
					}
					else if (!guessIsNew) {
						System.out.println("You have already guessed this word.");
					}
				} while (!foundNewWord && !quit);
			}

			if (numWordsFound < myGame.answerKey.length && points >= pointsForGenius) {
				System.out.println("Congratulations! You're a genius!");
				System.out.println("You won "+points+" points out of "+myGame.totalPoints+" possible. Do you want to keep playing? (Y/N)");
				String keepPlaying = keyboard.next().trim();
				if (keepPlaying.toUpperCase().equals("Y")) {
					quit = false;
					pointsForGenius = myGame.totalPoints;
				}
				else quit = true;
			}
		
			if (points == myGame.totalPoints) {
				System.out.println("Wow! You found all the words for this game!");
				break;
			}				
		}
		
		if (quit) {
			System.out.println("Before you go, would you like to see the answer key? (Y/N)");
			String answer = keyboard.nextLine().trim().toUpperCase();
			if (answer.equals("Y")) {
				System.out.println("Answer Key:");
				for (String word : myGame.answerKey) {
					System.out.println(word);
				}
			}
		}
		
		// Print game statistics
		System.out.println("Game Statistics:");
		System.out.println("You found "+numWordsFound+" words out of "+myGame.answerKey.length+" possible.");
		System.out.println("You won "+points+" points out of "+myGame.totalPoints+" possible.");
		System.out.println("Thanks for playing! Goodbye.");
		keyboard.close();
	}
}