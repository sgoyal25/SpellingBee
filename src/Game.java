import java.io.IOException;

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
	public char centerLetter;
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
				centerLetter = Character.toUpperCase(ltr);
			}
		}
		
		guessChecker = new GuessCheck(seq, centerLetter);
		
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
	
}
