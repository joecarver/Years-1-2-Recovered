
public class LoginMsg extends Message
{
	String username;
	char[] password;
	
	public LoginMsg(String username, char[] password)
	{
		this.username = username;
		this.password = password;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public char[] getPassword()
	{
		return password;
	}
}
