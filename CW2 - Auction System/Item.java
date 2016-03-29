import java.io.Serializable;
import java.util.*;


@SuppressWarnings("serial")
public class Item implements Serializable
{
	String title;
	String description;
	String category;
	String sellerID;
	Date startTime;
	Date endTime;
	String itemID;
	
	public Item(String tit, String desc, String cat, Date st, Date et, String ID)
	{
		title = tit;
		description = desc;
		category = cat;
		startTime = st;
		endTime = et;
		itemID = ID;
	}
	
	public String getTitle(){		return title;		}
	public String getDescription(){	return description;	}
	public String getCategory(){	return category;	}
	public String getSellerID(){	return sellerID;	}
	public String getItemID(){		return itemID;		}
	public Date getStartDate(){		return startTime;	}
	public Date getEndDate(){		return endTime;		}
	
	public void setSellerID(String s){	sellerID = s;	}
	
}
