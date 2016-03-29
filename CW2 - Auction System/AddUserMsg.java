
public class AddUserMsg extends Message
{
	User u;
	
	public AddUserMsg(User u)
	{
		this.u = u;
	}
	
	public User getUser()
	{
		return u;
	}
	
}
