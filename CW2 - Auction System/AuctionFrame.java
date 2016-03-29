import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;

public class AuctionFrame extends ClientFrame
{
	long DAY_IN_MS = 1000 * 60 * 60 * 24;
	int itemCount = 1;
	String user;
	JFrame auctionFrame;
	JPanel mainPanel, sellPanel, viewPanel, welcomePanel;
	JLabel sellTitle;
	JTextField titleField;
	JTextArea descriptionBox;
	JButton submit, clear, updateListings;
	JComboBox<String> categoriesBox, daysBox;
	JTable itemsForSale;
	JTabbedPane auctionPanels;
	HashMap<String, Item> itemMap;
	DefaultTableModel itemTable;
	
	
	public AuctionFrame(){}
	
	public AuctionFrame(String s)
	{
		user = s;
		initialiseGUI();
	}
	
	public void initialiseGUI()
	{
		auctionFrame = new JFrame("Logged in as: " + user);
			
		viewPanel = new JPanel(new FlowLayout());
		
		updateListings = new JButton("Update Items for sale");
		updateListings.addActionListener(new UpdateItemsListener());
		JLabel viewLab = new JLabel("ITEMS FOR SALE");
		viewLab.setFont(new Font("SansSerif", Font.BOLD, 30));
		
		Container labCont = new Container();		labCont.setLayout(new FlowLayout());
		labCont.add(viewLab);						labCont.add(updateListings);
		
		viewPanel.add(labCont);
		
		
		auctionPanels = new JTabbedPane();
		auctionPanels.add("Home", createWelcomePanel());
		auctionPanels.add("Sell an Item",createSellPanel());
		auctionPanels.add("View Items for Sale", viewPanel);
		

		
		auctionFrame.setContentPane(auctionPanels);
		auctionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		auctionFrame.setSize(800, 500);
		auctionFrame.setResizable(false);
		auctionFrame.setVisible(true);
	}
	
	//first panel user sees, and home sends user here
	public JPanel createWelcomePanel()
	{
		welcomePanel = new JPanel(new BorderLayout());
		
		
		JLabel welcomeLab = new JLabel("WELCOME TO THE AUCTION SYSTEM");
		welcomeLab.setFont(new Font("SansSerf", Font.BOLD, 30));
		Container welLabC = new Container();		welLabC.setLayout(new FlowLayout());
		welLabC.add(welcomeLab);
		
		welcomePanel.add(welLabC, BorderLayout.NORTH);
		
		
		return welcomePanel;
	}

	
	
	//panel for creating an auction
	public JPanel createSellPanel()
	{
		sellPanel = new JPanel(new GridLayout(4,1));
		
		String[] categories = {"Select Category", "Fashion", "Home and Garden", "Electronics", "Sport", "Music & Films", "Vehicles"};
		String[] days = {"Select Duration", "1 Day", "2 Days", "3 Days", "4 Days", "5 Days", "6 Days", "7 Days" };
		categoriesBox = new JComboBox<String>(categories);
		daysBox = new JComboBox<String>(days);
		titleField = new JTextField(30);
		descriptionBox = new JTextArea("Description Here...", 5, 55);
		descriptionBox.setBorder(BorderFactory.createLineBorder(Color.black));
	
		
		submit = new JButton("Submit Item");			submit.addActionListener(new SellListener());
		clear = new JButton("Clear information");		clear.addActionListener(new SellListener());

		Container titleDetsCont = new Container();		titleDetsCont.setLayout(new FlowLayout());
		Container boxCont = new Container();			boxCont.setLayout(new FlowLayout());
		Container sellCont = new Container();			sellCont.setLayout(new FlowLayout());
		Container buttCont = new Container();			buttCont.setLayout(new FlowLayout());
		titleDetsCont.add(new JLabel("Item Title:"));	titleDetsCont.add(titleField);	
		titleDetsCont.add(categoriesBox);				titleDetsCont.add(daysBox);
		boxCont.add(descriptionBox);
		sellCont.add(submit);								sellCont.add(clear);
		
		
		sellTitle = new JLabel("SELL AN ITEM");
		sellTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
		sellPanel.add(sellTitle);			sellPanel.add(titleDetsCont);
		sellPanel.add(boxCont);				sellPanel.add(sellCont);
		
		
		return sellPanel;
	}

	public class SellListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//submits an item if all fields filled in
			if(e.getSource().equals(submit))
			{
				int confSell = JOptionPane.showConfirmDialog(auctionFrame, "Submit " + titleField.getText() + " for auction?",
															"Sell Item?", JOptionPane.YES_NO_OPTION);
				if(categoriesBox.getSelectedIndex() == 0 
				||daysBox.getSelectedIndex() == 0
				||titleField.getText().isEmpty()
				||descriptionBox.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(auctionFrame, "Please complete all options",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(confSell == 0){
					//successful item listing
					String[] dateS = daysBox.getSelectedItem().toString().split(" ");
					int dayN = Integer.parseInt(dateS[0]);
					Item i = new Item(titleField.getText(), descriptionBox.getText(),
								categoriesBox.getSelectedItem().toString(), new Date(),
								new Date(System.currentTimeMillis()+dayN*DAY_IN_MS), Integer.toString(itemCount));
					Comms.sendAddItemMsg(new AddItemMsg(i));
					JOptionPane.showMessageDialog(auctionFrame, "Thanks, your " +i.getTitle() + " has been listed",
							"Success!", JOptionPane.INFORMATION_MESSAGE);
					titleField.setText("");
					descriptionBox.setText("Description Here...");
					categoriesBox.setSelectedIndex(0);
					daysBox.setSelectedIndex(0);
					itemCount++;
				}
			}
			//clears all entered information upon user confirmation
			if(e.getSource().equals(clear))
			{
				int confClear = JOptionPane.showConfirmDialog(auctionFrame, "Sure you want to clear?","Clear?", JOptionPane.YES_NO_OPTION);
				if(confClear == 0)
				{
					titleField.setText("");
					descriptionBox.setText("Description Here...");
					categoriesBox.setSelectedIndex(0);
					daysBox.setSelectedIndex(0);
				}
			}
		}
	}
	
	//retrieves the item map from server class when clicked
	public class UpdateItemsListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource().equals(updateListings))
			{
				itemMap = Comms.getUpdateItemsMsg();
			
				
				for(Item item : itemMap.values())
				{
					System.out.println(item.getTitle());
					viewPanel.add(new JLabel("ITEM " +item.getItemID() + ": "
							+ item.getTitle() + ". Seller: " +item.getSellerID() + ", Category: "
							+ item.getCategory() + ", End Date: " + item.getEndDate().toString()));
				}
				auctionPanels.remove(viewPanel);
				auctionPanels.add("View Items For Sale", viewPanel);
				auctionPanels.setSelectedComponent(viewPanel);
			}
			
			
			
		}
	}

}

