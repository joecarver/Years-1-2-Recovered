import java.io.*;

public class Notification implements Serializable
{
	String info;
	
	public Notification(String info){	this.info = info;	}
	
	public String getInfo(){	return info;	}
}
