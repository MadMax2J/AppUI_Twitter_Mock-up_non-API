package TwitterClientGui;

import java.io.Serializable;

/**
 * Created by jbyrne on 12/05/2015.
 */
public class Tweet implements Serializable {
    private String fromUser;        //Who is sending the tweet  @John_Byrne
    private String tweetMessage;    //The tweet being sent
    private boolean isFav;          //Has the tweet been marked as a Favourite?
    private boolean isReTweeted;    //Has the tweet been marked as ReTweeted?

    public Tweet(String fromUser, String tweetMessage, boolean isFav, boolean isReTweeted){
        this.fromUser = fromUser;
        this.tweetMessage = tweetMessage;
        this.isFav = isFav;
        this.isReTweeted = isReTweeted;
    }


    //// Getters and Setters
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getTweetMessage() {
        return tweetMessage;
    }

    public void setTweetMessage(String tweetMessage) {
        this.tweetMessage = tweetMessage;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public boolean isReTweeted() {
        return isReTweeted;
    }

    public void setIsReTweeted(boolean isReTweeted) {
        this.isReTweeted = isReTweeted;
    }
}
