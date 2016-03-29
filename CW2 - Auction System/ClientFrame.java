import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

import javax.swing.*;


public class ClientFrame 
{
	public static void main(String[] args) throws IOException
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientFrame gui = new ClientFrame();
				gui.initialiseGUI();
			}
		});
	}
	
	JLabel loginLab, registerLab;
	JFrame loginFrame;
	JPanel loginCards;
	JButton back, login, confirm;
	JTextField regUserIDField, userIDField, firstNameField, lastNameField;
	JPasswordField logPasswordField, regPasswordField, regPasswordField2;
	
	//This method only has the GUI Components in
	public void initialiseGUI()
	{
		loginFrame = new JFrame("Login");
		JPanel mainPanel = new JPanel();
		JPanel loginPanel = new JPanel(new GridLayout(4,1));
		JPanel registerPanel = new JPanel(new GridLayout(7,1));
		
		//login panel
		loginLab = new JLabel("Login");
		loginLab.setFont(new Font("SansSerif", Font.BOLD, 18));
		back = new JButton("Register");
		login = new JButton("Login");
		userIDField = new JTextField(10);
		logPasswordField = new JPasswordField(10);
		Container labC = new Container();		labC.setLayout(new FlowLayout());
		labC.add(loginLab);
		Container userC = new Container();		userC.setLayout(new FlowLayout());
		Container passwC = new Container();		passwC.setLayout(new FlowLayout());
		Container buttonC = new Container(); 	buttonC.setLayout(new FlowLayout());
		userC.add(new JLabel("User ID: "));		userC.add(userIDField);
		passwC.add(new JLabel("Password: ")); 	passwC.add(logPasswordField);
		buttonC.add(login);						buttonC.add(back);
		loginPanel.add(labC);
		loginPanel.add(userC);
		loginPanel.add(passwC);
		loginPanel.add(buttonC);
		
		
		//register panel
		registerLab = new JLabel("Register an Account");
		registerLab.setFont(new Font("SansSerif", Font.BOLD, 18));
		confirm = new JButton("Confirm");
		//logSwitch = new JButton("");
		regUserIDField = new JTextField(10);
		firstNameField = new JTextField(10);
		lastNameField = new JTextField(10);
		regPasswordField = new JPasswordField(10);
		regPasswordField2 = new JPasswordField(10);
		Container rLabC = new Container();			rLabC.setLayout(new FlowLayout());
		registerPanel.add(rLabC);
		Container uNameC = new Container();			uNameC.setLayout(new FlowLayout());
		registerPanel.add(uNameC);
		Container firstNC = new Container();		firstNC.setLayout(new FlowLayout());		
		registerPanel.add(firstNC);		
		Container lastNC = new Container();			lastNC.setLayout(new FlowLayout());
		registerPanel.add(lastNC);
		Container passWC = new Container();			passWC.setLayout(new FlowLayout());
		registerPanel.add(passWC);
		Container passWC2 = new Container(); 		passWC2.setLayout(new FlowLayout());
		registerPanel.add(passWC2);
		Container buttC = new Container();			buttC.setLayout(new FlowLayout());
		registerPanel.add(buttC);
		rLabC.add(registerLab);
		uNameC.add(new JLabel("Username"));				uNameC.add(regUserIDField);
		firstNC.add(new JLabel("First Name: "));		firstNC.add(firstNameField);
		lastNC.add(new JLabel("Last Name: "));			lastNC.add(lastNameField);
		passWC.add(new JLabel("Password: "));			passWC.add(regPasswordField);
		passWC2.add(new JLabel("Retype Password: "));	passWC2.add(regPasswordField2);
		buttC.add(confirm);			
		
		login.addActionListener(new ButtonsListener());
		back.addActionListener(new ButtonsListener());
		confirm.addActionListener(new ButtonsListener());
		
		
		loginCards = new JPanel(new CardLayout());
		loginCards.add(registerPanel, "back");
		loginCards.add(loginPanel, "register");
		
		mainPanel.add(loginCards);
		
		loginFrame.setResizable(false);
		loginFrame.setContentPane(mainPanel);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setVisible(true);
		loginFrame.pack();
	}
	
	/*
	 * Same listener class for all buttons - detect source then do action based on each one
	 */
	public class ButtonsListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//to switch cards
			CardLayout cards = (CardLayout) (loginCards.getLayout());
			
			//Check user registration
			if(e.getSource().equals(confirm))
			{
				if (firstNameField.getText().isEmpty()
					|| lastNameField.getText().isEmpty()
					|| regPasswordField2.getPassword().length == 0
					|| regPasswordField.getPassword().length == 0)
				{
						JOptionPane.showMessageDialog(loginFrame,	"Please fill in all fields to register.",
							"Error Message", JOptionPane.ERROR_MESSAGE);
				} 
				else if (!Arrays.equals(regPasswordField.getPassword(),
						regPasswordField2.getPassword()))
				{
					JOptionPane.showMessageDialog(loginFrame,	"Passwords do not match",
							"Error Message", JOptionPane.ERROR_MESSAGE);
				} 
				else 
				{
					//successful registration
					User u = new User(firstNameField.getText(), lastNameField.getText(), regPasswordField.getPassword(),regUserIDField.getText());
					Comms.sendAddUserMsg(new AddUserMsg(u));
					
					cards.last(loginCards);
				}
			}
			
			//check login
			if(e.getSource().equals(login))
			{
				if(userIDField.getText().isEmpty()
				|| logPasswordField.getPassword().length == 0)
				{
					JOptionPane.showMessageDialog(loginFrame, "Please fill in both fields",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(Comms.sendLoginMsg(new LoginMsg(userIDField.getText(), logPasswordField.getPassword())))
				{
					//successful login
					new AuctionFrame(userIDField.getText());
					loginFrame.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(loginFrame,	"Incorrect Username or password",
							"Error Message", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(e.getSource().equals(back))
			{
				cards.first(loginCards);
			}
		}
	}
}

