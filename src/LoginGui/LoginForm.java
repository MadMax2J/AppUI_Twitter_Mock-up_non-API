package LoginGui;

import TwitterClientGui.TwitterClient;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Mock Twitter Client
 * by John Byrne - R00050076
 *
 * Login Page
 */
public class LoginForm extends JFrame{
    private JPanel loginPanel;
    private JButton btnLogin;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private int loginAttempts;          //Track the number of log-ins attempted in this session.

    public LoginForm(){
        super("App UI Twitter Mock-up - Login Screen");
        setContentPane(loginPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initialise instance variable;
        loginAttempts = 0;

        /**
         * Only one action is preformed on this window; to validate the users' login attempt.
         */
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loginAttempts++;
                String username = txtUsername.getText();
                String password = String.valueOf(txtPassword.getPassword());

                //// Testing
                //System.out.println(username + " **** " + password);

                if (username.equals("test") && password.equals("test")) {
                    setVisible(false);
                    JOptionPane.showMessageDialog(LoginForm.this, "Welcome " + WordUtils.capitalize(username) + "!");
                    new TwitterClient();

                } else {
                    if (loginAttempts < 3) {
                        JOptionPane.showMessageDialog(LoginForm.this, "Access Denied!\nAttempts remaining: " + (3 - loginAttempts));
                    } else { //On third failed login attempt.
                        JOptionPane.showMessageDialog(LoginForm.this, "Access Denied!\nToo many failed Login attempts!\nGoodbye!");
                        System.exit(0);
                    }
                }

            }
        });

        setSize(360, 160);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
