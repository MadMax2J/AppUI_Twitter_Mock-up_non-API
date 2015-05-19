package TwitterClientGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This TweetCellTemplate class is a customised JPanel for use in my JList.
 * by John Byrne - R00050076
 */
public class TweetCellTemplate extends JPanel {
    private JPanel rootTweet;
    private JLabel lblProfilePic;
    private JLabel lblTweet;
    private JLabel lblFav;
    private JLabel lblReTweet;

    /**
     * This constructor builds the JList Element.
     */
    public TweetCellTemplate(){
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1;
        add(lblProfilePic,gridBagConstraints);

        add(lblTweet);  //added without gridBagConstraints so that it will float

        gridBagConstraints.anchor= GridBagConstraints.EAST;
        add(lblFav,gridBagConstraints);
        add(lblReTweet);
    }

    public void setColor(Color color){
        this.setBackground(color);
    }

    public JLabel getLblProfilePic() {
        return lblProfilePic;
    }

    public JLabel getLblTweet() {
        return lblTweet;
    }

    public JLabel getLblFav() {
        return lblFav;
    }

    public JLabel getLblReTweet() {
        return lblReTweet;
    }
}
