package lib;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *  LoginScreen – a JPanel that shows the email / password form
 *  centred in the window.
 */
public class LoginScreen extends JPanel
{
    private final JFrame parentFrame;
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginScreen(JFrame frame)
    {
        this.parentFrame = frame;

        // using GridBagLayout that way, everything we add will
        // stay perfectly centred even when the frame resizes.
        setLayout(new GridBagLayout());


        // Holds the actual widgets (labels, fields, buttons)
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // title
        form.add(header("Login"));

        //  gap
        form.add(Box.createRigidArea(new Dimension(0, 24)));

        // “Username” label + text field
        form.add(createFieldPanel("Username", usernameField));

        // gap
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        // “Password” label + password field
        form.add(createFieldPanel("Password", new JPasswordField()));

        // gap
        form.add(Box.createRigidArea(new Dimension(0, 24)));

        // “Login” button
        form.add(loginButton());

        // gap
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        // “Register here” link / button
        form.add(registerButton());

        /* put the  column in the center */
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;                          // column 0
        gbc.gridy   = 0;                          // row    0
        gbc.weightx = 1.0;                        // cell eats any extra width
        gbc.weighty = 1.0;                        // cell eats any extra height
        gbc.anchor  = GridBagConstraints.CENTER;  // keep form centred
        add(form, gbc);                           // finally add to JPanel
    }

    /* Helper: makes the big title label (“Login”) */
    private JLabel header(String text)
    {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    /*
    Helper: returns a row with a left-aligned label + field. Helps
    keep all fields aligned, and accepts any JComponent so it can
    be reused
    */
    private JPanel createFieldPanel(String labelText, JComponent field)
    {
        // left-aligned row
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // -------- label --------
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.PLAIN, 18));

        /* key line → reserve the same width for every label      *
         * adjust 80 to something that fits your longest caption. */
        lbl.setPreferredSize(new Dimension(80, 30));

        // -------- input field --------
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(300, 40));

        // add components in order
        row.add(lbl);
        row.add(field);
        return row;
    }

    /* ------------------------------------------------------------------
       helper that swaps the panel to the Registration screen
       ------------------------------------------------------------------ */
    private void navigateToRegistration()
    {
        parentFrame.getContentPane().removeAll();                 // clear old panel
        parentFrame.getContentPane().add(new RegistrationScreen(parentFrame));
        parentFrame.revalidate();   // re-layout
        parentFrame.repaint();      // refresh
    }

    // used to go to RegistrationScreen
    private JButton registerButton()
    {
        JButton b = new JButton("Don't have an account? Register here.");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add your ActionListener here
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.addActionListener(e -> navigateToRegistration());
        return b;
    }

    // Login button so once clicked it goes to homescreen
    private JButton loginButton()
    {
        JButton b = new JButton("Login");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setPreferredSize(new Dimension(200, 45));

        // swap to homescreen whenever the user clicks login.
        b.addActionListener(e -> navigateToHome());
        return b;
    }
    private void navigateToHome()
    {
        /*
        Ethan:
        I ran into issues when I realized I made the class based on my test cases where I manually inputed the user filepath
        The flaw is it doesnt work dynamically, so instead im just using users.xml
        When navigating to the home screen, pulls the active user from storage


         */
        // retrieves users in form of list to check sign in
        List<User> userList = RetrieveXML.loadUserListFromXML("users.xml");
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // checks user login to user list and verifies passwords match
        boolean found = false;
        for (User u : userList)
        {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
            {
                found = true;
                break;
            }
        }

        if (found)
        {
            parentFrame.getContentPane().removeAll();
            parentFrame.revalidate();
            parentFrame.repaint();
        } else
        {
            JOptionPane.showMessageDialog(this, "Invalid login credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

}
