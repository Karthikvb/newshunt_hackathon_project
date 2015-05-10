/*
 * TweetSearch - Search for Tweets based on Keyterm
 * Author: 
 * 
 */

import java.util.*;
import twitter4j.*;

public class TweetSearch {

	public static void getTweets() {
		Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query("IRCTC");
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                }
                try {
                	Thread.sleep(1);
                }
                catch (Exception e) {
                }
            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}
	
	public static void main(String[] args) {
		
	}
}