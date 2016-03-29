import java.rmi.*;
import java.rmi.server.*;
import java.util.Arrays;

public class NotificationSink extends UnicastRemoteObject implements NotifSinkInterface
{

	String id;
	
	//Check if the sink already exists, if so, register it to given source.
	//If not, enter sink into registry then give it to source
	public NotificationSink(String id, NotifSourceInterface nSource) throws RemoteException
	{	
		this.id = id;
		try{
			if(Arrays.asList(Naming.list("rmi://localhost/")).contains("//localhost:1099/sinks/" +getID()))
			{
				nSource.registerSink((NotifSinkInterface) Naming.lookup("rmi://localhost/sinks/" + getID()));
			}
			else
			{
				Naming.bind("sinks/" +getID(), this);
				System.out.println("Sink " + getID()  + " ready remotely");
				nSource.registerSink((NotifSinkInterface) Naming.lookup("rmi://localhost/sinks/" + getID()));
			}
		} catch (Exception e){System.out.println("sink register error " + e);} //general exception as this constructor contains >1 kind
		
	}
	
	
	public String getID(){	return this.id;		}
	
	
	//Display Notification information
	public void printMessage(Notification n, NotifSourceInterface nSource)
	{
		try {
			System.out.println("Message::: " + n.getInfo() + " ::: received by " + getID() + " from " + nSource.getName());
		} catch (RemoteException e) {	System.out.println("error " + e);	}
		
	}
}
