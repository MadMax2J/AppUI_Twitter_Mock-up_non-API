package TwitterClientGui;

import javax.swing.*;
import java.awt.*;


/**
 * My Tweet Cell Renderer class
 * Each cell in my JList is rendered based on the rules defined here.
 * by John Byrne - R00050076
 */
public class TweetCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        Color cellSelectedColor = new Color (190, 193, 199);

        //Get the data and cast it as a Tweet object
        Tweet tweet = (Tweet)value;
            /* From Tweet object
            this.fromUser = fromUser;
            this.userProfilePic = userProfilePic;
            this.tweetMessage = tweetMessage;
            this.isFav = isFav;
            this.isReTweeted = isReTweeted;
             */

        //Extract all the data from the Tweet...
        String fromUser = tweet.getFromUser();
        String tweetMessage = tweet.getTweetMessage();
        boolean isFav = tweet.isFav();
        boolean isReTweeted = tweet.isReTweeted();

        //Build the Tweet cell...
        TweetCellTemplate cell = new TweetCellTemplate();   //Create an instance of my Cell template, which I will
                                                            //ultimately pass back to the JList as an element.
        ImageIcon icon = createImageIcon("../Images/" + fromUser + ".png", "User Profile Picture");
                                                            //Get profile picture
        cell.getLblProfilePic().setIcon(icon);              //Set the profile picture
        cell.getLblProfilePic().setText(fromUser);          //Set the profile name
        cell.getLblTweet().setText(tweetMessage);           //Set the Tweet message

        //The cells default to ifFav and isReTweeted == false, so is I set isFav to true and back again to false,
        //the cell will return to default configuration, so I don't need to repaint the isNotFav icon at runtime.
        if(isFav){
            ImageIcon favIcon = createImageIcon("../Images/isFavStar.png", "a Favourite Tweet");
            cell.getLblFav().setIcon(favIcon);
        }
        if(isReTweeted){
            ImageIcon reTweetIcon = createImageIcon("../Images/isReTweeted.png", "a ReTweeted Tweet");
            cell.getLblReTweet().setIcon(reTweetIcon);
        }

        /*if(isSelected){   //Commented this out as I found callHasFocus works better for this application.
            cell.setBackground(Color.GREEN);
        }*/

        if(cellHasFocus){
            //Color origCol = cell.getBackground();
            //cell.getLblReTweet().setText(origCol.toString());
            cell.setBackground(cellSelectedColor);
        }


        return cell;
    }


    /**
     *This method returns the ImageIcon to be used as the profile picture in my Tweet cell.
     * @param path          The Path of the profile pic
     * @param description   The description associated with the profile pic.
     * @return              ImageIcon for use as Tweet profile pic.
     */
    protected ImageIcon createImageIcon(String path, String description) {

        String defaultPath = "../Images/noProfilePictureConfigured.png";    //To allow for profiles with no Pic

        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);

        } else {    //If I can't find a profile pic, use the default one...
            imgURL = getClass().getResource(defaultPath);
            return new ImageIcon(imgURL, description);

        }

    }

}
