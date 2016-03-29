import java.io.Serializable;


@SuppressWarnings("serial")
public class User implements Serializable
{
	String firstName;
	String lastName;
	String userID;
	char[] password;
	
	public User(String fn, String ln, char[] pw, String ID)
	{
		firstName = fn;
		lastName = ln;
		userID = ID;
		password = pw;
	}
	
	public void setPassword(char[] pw){		password = pw;	}
	
	public String getFirstName(){	return firstName;	}
	public String getLastName(){	return lastName;	}
	public String getUserID(){	return userID;	}
	public char[] getPassword(){	return password;	}
}
