import java.util.*;

public class Comms 
{	
	static Server server = new Server();
	static AuctionFrame af = new AuctionFrame();
	
	public static void sendAddUserMsg(AddUserMsg msg)
	{
		server.receiveAddUserMsg(msg);
	}
	
	public static boolean sendLoginMsg(LoginMsg msg)
	{
		return server.receiveLoginMsg(msg);
	}
	
	public static void sendAddItemMsg(AddItemMsg msg)
	{
		server.receiveAddItemMsg(msg);
	}
	
	public static HashMap<String, Item> getUpdateItemsMsg()
	{
		return server.giveUpdateItemsMsg();
	}
}
