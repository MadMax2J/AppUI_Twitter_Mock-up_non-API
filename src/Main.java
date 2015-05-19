import LoginGui.LoginForm;

import javax.swing.*;

/**
 * Application User Interfaces Assignment II
 * by John Byrne - R00050076
 *
 * Twitter Client Mock-up Application Launcher
 */
public class Main {
    public static void main(String[] args){

        //Setup my Look and Feel theme
        setTheme();

        //LoginForm loginForm = new LoginForm();
        new LoginForm();


    }

    private static void setTheme(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

}

