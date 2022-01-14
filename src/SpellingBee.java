//import java.util.ArrayList;
//import java.util.List;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;

public class SpellingBee {

	public static void alphabetize(int numWordsFound, String[] wordsFound) {
		boolean swapped = false;

		do {
			swapped = false;
			for (int i=1; i<numWordsFound; i++) {
				if (wordsFound[i].compareTo(wordsFound[i-1]) < 0) {
					String tmp = wordsFound[i];
					wordsFound[i] = wordsFound[i-1];
					wordsFound[i-1] = tmp;
					swapped = true;
				}
			}
		} while (swapped);		
	}

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		boolean stillPlaying = true;
		String[] yesnoResponses = new String[] {"Y","N"};

		do {
			String lettersStr = null;

			boolean quit = false;
			System.out.println("A new game is beginning...");

			// PRINT RULES
			Game.printRules();

			// Get user input to begin game
			boolean readyToStart = false;
			do {
				String[] validResponses = new String[] {"E", "R", "T"};
				String gameType = Game.askUserQuestion("Would you like to enter your own letters ('E'), play today's game ('T'), or play a random game from the archives ('R')?",validResponses, keyboard);
				//				String gameType = keyboard.nextLine().trim().toUpperCase();
				//				if (gameType.equals("END")) {
				//					quit = true;
				//					break;
				//				}
				//				if (gameType.equals("RULES")) {
				//					Game.printRules();
				//				}
				if (gameType.contentEquals("E")) {
					boolean validLtr = false;
					// Check validity of initial letters.
					while (!validLtr) {
						System.out.println("Input the letters for today's game, with only the required letter capitalized.");
						lettersStr = keyboard.nextLine().trim();
						if (lettersStr.toUpperCase().equals("PRINT RULES")) {
							Game.printRules();
						}
						else if (lettersStr.toUpperCase().equals("END")) {
							quit = true;
							break;
						}
						else {
							validLtr = SequenceCheck.checkValidSequence(lettersStr);
						}
					}
				}
				else if (gameType.contentEquals("R")) {
					//TODO: RANDOMLY GENERATE A SEQUENCE OF LETTERS FROM THE ARCHIVE
					lettersStr = Game.getRandomSequence();
				}
				else if (gameType.contentEquals("T")) {
					Document latest = null;
					try {
						Response execute = Jsoup.connect("https://www.shunn.net/bee/latest").execute();
						latest = Jsoup.parse(execute.body());
					} catch (IOException e) {
						e.printStackTrace();
					}	
					lettersStr = latest.select("input[type=\"text\"]").attr("value");
				}

				char center = 's';
				System.out.print("\nYour letters are: ");
				for (char ltr : lettersStr.toCharArray()) {
					System.out.print(Character.toUpperCase(ltr)+" ");
					if (ltr == Character.toUpperCase(ltr)) {
						center = ltr;
					}
				}
				System.out.println("\nThe central letter is "+center+".");

				String readyToPlay = Game.askUserQuestion("Are you ready to play? (Y/N)", yesnoResponses, keyboard);
				if (readyToPlay.equals("Y")) readyToStart = true;
				if (readyToPlay.equals("N")) {
					readyToStart = false;
					System.out.println("Let's try again. Remember, you can exit the game any time by typing 'end'.");
				}
			} while (!readyToStart);


			 // Early exit 1; no gameplay completed
			if (quit) {
				System.out.print("Exiting game. Goodbye!");
				System.exit(0);
			}

			// Starting game
			System.out.println("\nGetting your game ready...");

			Game myGame = new Game(lettersStr);
			int points = 0;
			int pointsForGenius = myGame.totalPoints;
			String[] wordsFound = new String[myGame.answerKey.length];
			int numWordsFound = 0;

			String guess; 

			System.out.print("The maximum point total possible for this game is "+myGame.totalPoints+". ");
			String enterScore = Game.askUserQuestion("Do you want to enter a score required to achieve 'Genius'? (Y/N)", yesnoResponses, keyboard);
			if (enterScore.equals("N")) {
				pointsForGenius = myGame.totalPoints;
			}
			else {
				//TODO: Error handling
				System.out.println("Enter the number of points required to achieve 'Genius'");
				String pts = keyboard.nextLine();
				if (pts.toUpperCase().equals("END")) {
					quit = true;
					break;
				}
				else if (pts.toUpperCase().equals("PRINT RULES")) {
					Game.printRules();
				}
				else {
					pointsForGenius = Integer.parseInt(pts);
					while (pointsForGenius>myGame.totalPoints) {
						System.out.println("Oops. Points to 'Genius' must be no more than "+myGame.totalPoints+". Enter a different number.");
						pointsForGenius = keyboard.nextInt();
					}
				}
			}

			// Early exit 2; no game play completed
			if (quit) {
				System.out.print("Exiting game. Goodbye!");
				System.exit(0);
			}

			// For first word only
			while (numWordsFound == 0 && !quit) {
				boolean validGuess = false;

				System.out.print("\nYour letters are: ");
				for (char ltr : myGame.lettersArr) {
					System.out.print(Character.toUpperCase(ltr)+" ");
				}
				System.out.print("\nThe central letter is "+myGame.centralLetter+". ");
				do {
//					keyboard.nextLine();
					System.out.println("Enter your guess:");
					guess = keyboard.nextLine().trim().toUpperCase();
					if (guess.equals("END")) {
						quit = true;
						break;
					}
					else if (guess.equals("PRINT RULES")) {
						Game.printRules();
//						break;
					}
					else {// Check to see if guess is valid (4+ letters, has central letter, etc.)
						validGuess = myGame.guessChecker.isGuessValid(guess);
					}
				} while (!validGuess && !quit);

				if (quit) break;

				boolean guessIsCorrect = false;

				// Check to see if guess is in answer key
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
						System.out.println("And you found a pangram!");
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
					System.out.println("\nPoints: "+points+"\t\tPoints to Genius: "+pointsForGenius);
					System.out.print("Your letters are: ");
					for (char ltr : myGame.lettersArr) {
						System.out.print(Character.toUpperCase(ltr)+" ");
					}
					System.out.println("\nThe central letter is "+myGame.centralLetter+".");

					boolean foundNewWord = false;
					do {
						boolean validGuess = false;
						do {
							// Ask for next guess
							System.out.println("\nEnter your guess: ");
							guess = keyboard.nextLine().trim().toUpperCase();
							if (guess.equals("END")) {
								quit = true;
								break;
							}
							else if (guess.equals("PRINT RULES")) {
								Game.printRules();
							}
							else {
							// Check if guess is valid
								validGuess = myGame.guessChecker.isGuessValid(guess);
							}
						} while (!validGuess && !quit);

						if (quit) break;

						// Check if guess is in answer key			
						boolean guessInAnswerKey = false;
						for (String a:myGame.answerKey) {
							if (guess.equals(a.toUpperCase())) {
								guessInAnswerKey = true;
							}
						}

						// Check if guess is new (not already found)
						boolean guessIsNew = true;
						for (String w:wordsFound) {
							if (guess.equals(w)) {
								guessIsNew = false;
							}
						}

						if (guessInAnswerKey && guessIsNew) {
							System.out.print("Great! ");
							if (myGame.isPangram(guess)) {
								System.out.println("And you found a pangram!");
							}
							wordsFound[numWordsFound] = guess;
							points += myGame.calculatePoints(guess);					
							numWordsFound++;
							alphabetize(numWordsFound, wordsFound);
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
					System.out.println("\nCongratulations! The score for Genius is "+pointsForGenius+", and you've earned "+points+" points. That makes you a genius!");
					System.out.println("The maximum possible score for this sequence of letters is "+myGame.totalPoints+". Do you want to keep guessing words in this game? (Y/N)");
					String keepPlaying = keyboard.nextLine().trim();
					if (keepPlaying.toUpperCase().equals("Y")) {
						pointsForGenius = myGame.totalPoints;
						quit = false;
					}
					else break;
				}

				if (numWordsFound == myGame.answerKey.length) {
					System.out.println("\n\nWow! You found all the words for this game. You must be Einstein!");
					break;
				}
			}

			// Print game statistics
			System.out.println("Game Statistics:");
			System.out.println("You found "+numWordsFound+" words out of "+myGame.answerKey.length+" possible.");
			System.out.println("You won "+points+" points out of "+myGame.totalPoints+" possible.");

			// Exit game after letters initialized
			System.out.println("Would you like to see the answer key? (Y/N)");
			String answer = keyboard.nextLine().trim().toUpperCase();
			if (answer.equals("Y")) {
				System.out.println("Answer Key:");
				for (String word : myGame.answerKey) {
					System.out.print(word+" ");
				}
			}

			// Ask user if they want to start a new game
			String playAgain = Game.askUserQuestion("\nDo you want to play a new game? (Y/N)", yesnoResponses, keyboard);
			
			if (playAgain.equals("Y")) {
				stillPlaying = true;
			}
			else if (playAgain.equals("N")) {
				stillPlaying = false;
			}
		} while (stillPlaying);

		System.out.println("Thanks for playing! Goodbye.");
		keyboard.close();
	}
}