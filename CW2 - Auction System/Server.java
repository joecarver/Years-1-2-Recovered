import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JOptionPane;


public class Server
{
	HashMap<String, User> users = new HashMap<String, User>();
	HashMap<String, Item> items = new HashMap<String, Item>();
	User currentUser;
	
	public void receiveAddUserMsg(AddUserMsg msg)
	{	
		if(users.containsKey(msg.getUser().getUserID()))
		{
			JOptionPane.showMessageDialog(null, "Username Taken", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			addUser(msg.getUser());
			JOptionPane.showMessageDialog(null, "Successfully registered as " + msg.getUser().getUserID(),
					"Registration Complete", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	public boolean receiveLoginMsg(LoginMsg msg)
	{
		String check = msg.getUsername();
		char[] pw = msg.getPassword();
		boolean pass = checkPassword(check, pw);
		if(pass){
			currentUser = users.get(check);
		}
		return pass;
	}
	
	public void receiveAddItemMsg(AddItemMsg msg)
	{
		addItem(msg.getItem());
	}
	
	public void addUser(User u)
	{
		users.put(u.getUserID(), u);
		System.out.println("User added: " + u.getUserID());
	}
	
	public boolean checkPassword(String s, char[] c)
	{
		if (users.containsKey(s)) {
			User u = users.get(s);
			if (u.getUserID().equals(s) && Arrays.equals(u.getPassword(), c)) {
				currentUser = u;
				return true;
			} else
				return false;
		} else
			return false;
	}
	
	public void addItem(Item i)
	{
		i.setSellerID(currentUser.getUserID());
		items.put(i.getItemID(), i);
		System.out.println("Item added: " + i.getTitle() + " by user " + i.getSellerID());
		System.out.println("Item start date: " + i.getStartDate().toString());
		System.out.println("Item end date: " + i.getEndDate().toString());
	}
	
	public HashMap<String, Item> giveUpdateItemsMsg()
	{
		return items;
	}

}
