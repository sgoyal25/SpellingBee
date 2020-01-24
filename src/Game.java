import java.io.IOException;
import java.util.Scanner;


import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Game {
	public String seq;
	public char[] lettersArr;
	public Document doc;
	public String[] answerKey;
	public char centralLetter;
	public GuessCheck guessChecker; 
	public int totalPoints;
	
	
	public Game(String sequence) {
		seq = sequence.toUpperCase();
		String site = "https://www.shunn.net/bee/?string="+sequence;
		try {
			Response execute = Jsoup.connect(site).execute();
			doc = Jsoup.parse(execute.body());
			
		} catch (IOException e) {
			e.printStackTrace();
		}		

		Elements words = doc.select("td[style=\"padding: 3px 3px 3px 0; font-weight: bold;\"]"); 
		answerKey = new String[words.size()];
		int ind = 0;
		for (Element w : words) {
			  answerKey[ind] = w.text();
			  ind++;
		}
		
		lettersArr = sequence.toCharArray();
		
		for (char ltr : lettersArr) {
			if (ltr == Character.toUpperCase(ltr)) {
				centralLetter = Character.toUpperCase(ltr);
			}
		}
		
		guessChecker = new GuessCheck(seq, centralLetter);
		
		totalPoints = 0;
		for (String w : answerKey) {
			totalPoints += calculatePoints(w);
		}
	}
	
	// Check if a guess is a pangram
	public boolean isPangram(String guess) {
		boolean pangram = true;
		
		for (char ltr: lettersArr) {
			if (guess.indexOf(Character.toUpperCase(ltr))==-1) {
				pangram = false;
			}
		}
		return pangram;
	}
	
	public int calculatePoints(String guess) {
		int points = 0;
		
		if (guess.length()<=4) {
			points = 1;
		}
		else {
			points = guess.length();
		}
		
		if (isPangram(guess)) {
			points += 7;
		}
		
		return points;
	}
	
	public static String askUserQuestion(String question, String[] validResponses, Scanner keyboard) {
		boolean questionNotAnswered = true;
		String response = "";
		do {
			System.out.println(question);
			response = keyboard.nextLine().toUpperCase();
			if (response.equals("END")) {
				System.out.println("Thanks for playing. Goodbye!");
				System.exit(0);
			}
			if (response.equals("PRINT RULES")) {
				printRules();
			}
			for (int idx = 0; idx<validResponses.length && questionNotAnswered; idx++) {
				if (response.equalsIgnoreCase(validResponses[idx])) questionNotAnswered = false;
			}
		} while (questionNotAnswered);
		return response;
	}
	
	public static String getRandomSequence() {
		String archiveString = "";
		//TODO: WRITE METHOD TO GET RANDOM STRING FROM ARCHIVES
		return archiveString;
	}
	
	public static void printRules() {
		System.out.println("\nRules:");
		System.out.println("1) Every game has a sequence of seven unique letters. One of these letters is designated the 'required letter'.");
		System.out.println("2) To earn points, you must enter words using only the letters in the original sequence. The same letter can be used multiple times.");
		System.out.println("3) To be considered valid, words must be 4 or more letters. Words which NYT considers obscure or offensive are not accepted.");
		System.out.println("4) To exit the current game at any time, simply enter 'end'.");
		System.out.println("To see these rules again, enter 'print rules' any time during the game.\n");
	}
	
}
