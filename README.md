# SpellingBee

### Author: Shuchi Goyal

This program re-creates the Spelling Bee game from the New York Times website, found at: https://www.nytimes.com/puzzles/spelling-bee

NYT currently limits non-subscribers to 4 Spelling Bee games per week. However, even before a user reaches the limit, the online game will randomly stop a player in the middle of a game and request that he/she subscribe to continue. This simple Java program is intended to work around this issue.

This program depends on William Shunn (https://www.shunn.net/bee/) for the answer key to each puzzle, and Jsoup (https://jsoup.org/) to allow web scraping from Shunn's site.

To run the program in command line:
1. Press the **Fork** button (top right the page) to save copy of this project on your account.

2. Download the repository files (project) from the download section.

3. In Command Prompt, set the working directory the src folder.

4. Run the following command:
   java -cp /path/to/lib/jsoup-1.12.1.jar;. SpellingBee
   
This project will eventually be updated to run as an applet, rather than through a command line.

This is my first GitHub project, so feedback is appreciated!


