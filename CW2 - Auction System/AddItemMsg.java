
public class AddItemMsg extends Message 
{
	Item i;
	
	public AddItemMsg(Item i)
	{
		this.i = i;
	}
	
	public Item getItem()
	{
		return i;
	}
}
