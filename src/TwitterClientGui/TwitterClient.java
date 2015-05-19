package TwitterClientGui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Application User Interfaces Assignment II
 * by John Byrne - R00050076.
 */
public class TwitterClient extends JFrame {
    private JPanel twitterClient;
    private JButton btnSendTweet;
    private JList lstTweets;
    private JTextArea txtTweet;
    private JButton btnFavourite;
    private JButton btnReTweet;
    private JLabel lblOf140Chars;

    private ArrayList<Tweet> tweetFeedFromFile; //Transition ArrayList. Store the tweets loaded from file,
                                                // before they are passed into the defaultListModel
    DefaultListModel defaultListModel;          //The data model for the JList
    ArrayList<Tweet> builtInTweets;             //An ArrayList storing a series of 'built-in tweets' to simulate a feed.
    Timer timer;                                //Used to spread-out when the 'built-in tweets' are added.

    public TwitterClient(){
        super("App UI Twitter Mock-up - Client");

        //// Initialise may variables
        tweetFeedFromFile = new ArrayList<>();
        defaultListModel = new DefaultListModel();
        builtInTweets = new ArrayList<>();

        //// Configure the JFrame
        setContentPane(twitterClient);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //// JList Formatting...How the Live Feed looks and feels...
        lstTweets.setCellRenderer(new TweetCellRenderer());
        lstTweets.setModel(defaultListModel);

        ////Read in the auto Tweets from file.
        loadAutoTweetsFromFile();

        ////Read in the initial Tweets from file.
        loadSessionFromFile("initialTweetSession.twc");

        //// Setup Menu and MenuItems
        menuSetup();

        //// Setup Button Action Listeners
        buttonSetup();

        //// Setup the new Tweet TextArea
        setupTweetTextArea();

        //// Setup the autoTweet timer
        setupAutoTweetTimer();

/*      //// TESTING
        defaultListModel.add(0, new Tweet("@John_Byrne", "This is a test message", false, false));
        defaultListModel.add(0, new Tweet("@Bob_Hope", "Something that Bob would say...", true, true));
        defaultListModel.add(0, new Tweet("@Ringo_Starr", "I say this with peace and love, peace and love!", true, false));
        defaultListModel.add(0, new Tweet("@Bob_Hope", "<html>Whether it is Twitter, Facebook, Yelp or just <br>a post to co-workers or business officials, the <br>number of actual characters matters. Bob Build</html>", true, false));
        defaultListModel.add(0, new Tweet("@Jimi_Hendrix ", "<html>Whether it is Twitter, Facebook, Yelp or just<br> a post to co - workers or business officials, the <br>number of actual characters matters. Bob Build</html>", true, false));
*/


        //// Finish JFrame prep...
        pack();
        setSize(560, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * The method adds a DocumentListener to the TextArea.
     * The Document listener acts upon changes to the textArea and updates the character counter.
     */
    private void setupTweetTextArea() {
        //////////////////////////////////////
        //// Tweet textarea configuration ////
        //////////////////////////////////////
        txtTweet.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lblOf140Chars.setText(String.valueOf("Characters: " + e.getDocument().getLength()) + "/140");
                if (e.getDocument().getLength() > 140) {
                    lblOf140Chars.setForeground(Color.RED);
                }
                //Characters: 0 out of a maximum 140
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lblOf140Chars.setText(String.valueOf("Characters: " + e.getDocument().getLength()) + "/140");
                if (e.getDocument().getLength() <= 140) {
                    lblOf140Chars.setForeground(Color.BLACK);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

    /**
     * This method sets up the Buttons and their ActionListeners
     */
    private void buttonSetup() {

        /**
         * Favourite Button Action Listener
         * Sets the isFav flag of the selected Tweet(s)
         * The TweetCellRenderer looks after updating the Favourite icon once repaint() is called.
         */
        btnFavourite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedItems = lstTweets.getSelectedIndices();
                for (int item : selectedItems){
                    Tweet tweet = (Tweet)defaultListModel.get(item);
                    if(tweet.isFav()){
                        tweet.setIsFav(false);
                    }else {
                        tweet.setIsFav(true);
                    }
                    repaint();

                }


            }
        });

        /**
         * Re-Tweet Button Action Listener
         * Sets the isReTweeted flag of the selected Tweet.
         * The TweetCellRenderer looks after updating the Re-Tweeted
         * icon once the re-tweeted tweet is displayed.
         */
        btnReTweet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedItems = lstTweets.getSelectedIndices();
                if (selectedItems.length > 1) {
                    JOptionPane.showMessageDialog(TwitterClient.this,
                            "Please select only one Tweet to re-Tweet. Tweet, Tweet! :)");
                } else if (selectedItems.length > 0) {
                    for (int item : selectedItems) {
                        Tweet tweet = (Tweet) defaultListModel.get(item);
                        if (!tweet.isReTweeted()) {
                            tweet.setIsReTweeted(true);
                        }
                        defaultListModel.add(0, new Tweet("@John_Byrne", "<html>" + tweet.getFromUser() +
                                " said: <br>" + tweet.getTweetMessage(), false, false));
                    }
                }
            }
        });

        /**
         * Send Tweet Button Action Listener
         * If the message is not too long, it takes the content
         * of the Tweet TextArea and adds a new Tweet to the feed.
         */
        btnSendTweet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtTweet.getDocument().getLength() > 140) {
                    JOptionPane.showMessageDialog(TwitterClient.this,
                            "Your tweet is too long!\n Maximum of 140 characters.");

                } else if (txtTweet.getDocument().getLength() > 0) {
                    String formattedTweet = formatTweet(txtTweet.getText());

                    defaultListModel.add(0, new Tweet("@John_Byrne", formattedTweet, false, false));

                    txtTweet.setText("");   //Blank out the new tweet text box, ready for a new entry.

                }
            }
        });
    }

    /**
     * This method sets up the Menu, MenuItems and their ActionListeners
     */
    private void menuSetup() {

        ////////////////////
        //// Menu Items ////
        ////////////////////
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);

        JMenu help = new JMenu("Help");
        menuBar.add(help);
        JMenuItem about = new JMenuItem("About");
        help.add(about);

        ////////////////////////////////////
        //// Menu Item Action Listeners ////
        ////////////////////////////////////
        /**
         * Exit MenuItem Action Listener
         * Exits the application
         */
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        /**
         * About MenuItem Action Listener
         * Shows an 'About' Dialogue box.
         */
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(TwitterClient.this, "Application User Interfaces Assignment 2\n" +
                        "by John Byrne - R00050076\n" +
                        "Twitter Client Mock-up v1");
            }
        });


        /**
         * Open MenuItem Action Listener.
         * It produces an Option Dialogue box to get confirmation.
         * If confirmed, it loads the saved twitter feed from file.
         */
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Create an OK / Cancel Dialogue box
                Object[] options = {"OK", "Cancel"};
                int overwriteConfirmation = JOptionPane.showOptionDialog(TwitterClient.this,
                        "** Warning ** Any unsaved tweets will be lost. Continue?", "Confirm overwrite session ",
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (overwriteConfirmation == 0) {
                    loadSessionFromFile("savedTweetSession.twc");
                }

            }
        });

        /**
         * Save MenuItem Action Listener.
         * It produces an Option Dialogue box to get confirmation.
         * If confirmed, it saves the twitter feed to the "savedTweetSession.twc" file.
         */
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Create an OK / Cancel Dialogue box
                Object[] options = {"OK", "Cancel"};
                int overwriteConfirmation = JOptionPane.showOptionDialog(TwitterClient.this,
                        "** Warning ** Previously saved session will be overwritten. Continue?",
                        "Confirm overwrite session ",
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (overwriteConfirmation == 0) {
                    ArrayList<Tweet> sessionTweets = new ArrayList<>();

                    for (int index = 0; index < defaultListModel.getSize(); index++) {
                        Tweet tweet = (Tweet) defaultListModel.get(index);
                        sessionTweets.add(tweet);
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream("savedTweetSession.twc");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(sessionTweets);
                        oos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * This method loads the Auto-Tweets in the to Twitter Client GUI.
     * This method is only called at start-up
     */
    private void loadAutoTweetsFromFile() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("autoTweets.twc");
            ObjectInputStream ois = new ObjectInputStream(fis);
            builtInTweets = (ArrayList<Tweet>) ois.readObject();
            ois.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            //Could throw in a Dialogue box to say there was an issue, but I don't think it's necessary for this app.
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Starts a background timer that will add a random Auto-Tweet at a random interval, between 10 and 25 seconds.
     */
    private void setupAutoTweetTimer() {
        Random random = new Random();
        timer = new Timer(random.nextInt(15000) + 10000,
                new ActionListener() { //Every 10 - 25 seconds, do this...
            @Override
            public void actionPerformed(ActionEvent e) {

                defaultListModel.add(0, builtInTweets.get(random.nextInt(builtInTweets.size())));

            }
        });
        timer.start();
    }

    /**
     * This method loads the content of the .twc file and adds the contained tweets to the Client view.
     * @param filename      The filename to load
     */
    private void loadSessionFromFile(String filename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tweetFeedFromFile = (ArrayList<Tweet>) ois.readObject();
            ois.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        defaultListModel.removeAllElements();
        tweetFeedFromFile.forEach(defaultListModel::addElement);

    }

    /**
     * This method takes in a String and adds html <br> tags to it,
     * to allow for multiple lines in the Tweet Cell JLabel
     * @param tweetText     The source Tweet text
     * @return              The returned multi-lined html text
     */
    private String formatTweet(String tweetText) {
        // Need to format the tweet, so that it fits properly in the feed.
        // I try to split the line at the end of a whole word, where possible.
        int tweetLength = tweetText.length();
        if (tweetLength > 140) {
            if(tweetText.indexOf(' ', 140) < 155 && tweetText.indexOf(' ') != -1) {
                tweetText = new StringBuilder(tweetText).insert(tweetText.indexOf(' ', 140), "<br>").toString();
            }else{
                tweetText = new StringBuilder(tweetText).insert(140, "<br>").toString();
            }
        }
        if (tweetLength > 94) {
            if(tweetText.indexOf(' ', 94) < 105 && tweetText.indexOf(' ') != -1) {
                tweetText = new StringBuilder(tweetText).insert(tweetText.indexOf(' ', 94), "<br>").toString();
            }else{
                tweetText = new StringBuilder(tweetText).insert(94, "<br>").toString();
            }
        }
        if (tweetLength > 47) {
            if(tweetText.indexOf(' ', 47) < 58 && tweetText.indexOf(' ') != -1) {
                tweetText = new StringBuilder(tweetText).insert(tweetText.indexOf(' ', 47), "<br>").toString();
            }else{
                tweetText = new StringBuilder(tweetText).insert(47, "<br>").toString();
            }
        }
        return "<html>" + tweetText + "</html>";
    }

    /**
     * The method is not used in normal runtime.
     * It is used to create the autoTweets.twc file which is read in when the program starts
     * to populate the builtInTweets ArrayList.
     */
    private void makeAutoTweets(){
        ArrayList<Tweet> builtInTweets = new ArrayList<>();
        builtInTweets.add(new Tweet("@Sean_Connery", "<html>...Bond. James Bond.</html>", false, false));
        builtInTweets.add(new Tweet("@Humphrey_Bogart", "<html>Of all the gin joints in all the towns in all the<br> world, she walks into mine.</html>", false, false));
        builtInTweets.add(new Tweet("@Arnold_Schwarz", "<html>I'll be back.</html>", false, false));
        builtInTweets.add(new Tweet("@Tom_Hanks", "<html>My Mama always said, 'Life was like a box of<br> chocolates; you never know what you're gonna get.</html>", false, false));
        builtInTweets.add(new Tweet("@Robert_De_Niro", "<html>You talkin' to me? Well, who the hell else are<br> you talkin' to? Well, I'm the only one here.<br> Who the f--k do you think you're talkin' to?</html>", false, false));
        builtInTweets.add(new Tweet("@Arnold_Schwarz", "<html>Hasta la vista, baby.</html>", false, false));
        builtInTweets.add(new Tweet("@Bruce_Willis", "<html>Yippie-Kai-Yay, Motherfucker!</html>", false, false));
        builtInTweets.add(new Tweet("@Ringo_Starr", "<html>I say this with peace and love, peace and love!</html>", false, false));
        builtInTweets.add(new Tweet("@Bob_Hope", "<html>Whether it is Twitter, Facebook, Yelp or just <br>a post to co-workers or business officials, the <br>number of actual characters matters. Bob Build</html>", false, false));
        builtInTweets.add(new Tweet("@Jimi_Hendrix", "<html>Whether it is Twitter, Facebook, Yelp or just<br> a post to co - workers or business officials, the <br>number of actual characters matters. Bob Build</html>", false, false));

        try {
            FileOutputStream fos = new FileOutputStream("autoTweets.twc");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(builtInTweets);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}