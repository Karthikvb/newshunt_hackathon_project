/*
 * TimelineTweets - Get Tweets from user's timeline
 * Author:
 * 
 */

import java.util.*;
import twitter4j.*;

public class TimelineTweets {

	public static void main(String[] args) throws TwitterException {
		// The factory instance is re-useable and thread safe.
	    Twitter twitter = TwitterFactory.getSingleton();
	    List<Status> statuses = twitter.getHomeTimeline();
	    System.out.println("Showing home timeline.");
	    for (Status status : statuses) {
	        System.out.println(status.getUser().getName() + ":" +
	                           status.getText());
	    }
	}
}