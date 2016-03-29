import java.rmi.Naming;
import java.rmi.RemoteException;



public class TestHarness 
{
	public static void main(String[] args)
	{
		new TestHarness().testSingleSource();
		new TestHarness().testMultipleSources();
	}
	
	public void testSingleSource()
	{
		System.out.println("Testing single Source");
		try 
		{

			//create a source
			NotificationSource so = new NotificationSource("so");
			NotifSourceInterface soInt = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + so.getName());
			
			//add 3 sinks to it
			NotificationSink nSink1 = new NotificationSink("sn1", soInt);
			NotificationSink nSink2 = new NotificationSink("sn2", soInt);
			NotificationSink nSink3 = new NotificationSink("sn3", soInt);
			
			//send them a notification
			so.notifySinks(new Notification("Test"));
			
			
		} catch (Exception e) {	e.printStackTrace();	}
	}
	
	
	public void testMultipleSources()
	{

		System.out.println("Testing multiple Source");
		try{
			//create and instantiate two sources and their interfaces
			NotificationSource so = new NotificationSource("so");
			NotifSourceInterface soInt = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + so.getName());
			
			NotificationSource so2 = new NotificationSource("so2");
			NotifSourceInterface so2Int = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + so2.getName());
			
		
			//create 3 sinks and register with first source
			NotificationSink nSink1 = new NotificationSink("sn1", soInt);
			NotificationSink nSink2 = new NotificationSink("sn2", soInt);
			NotificationSink nSink3 = new NotificationSink("sn3", soInt);
			
			
			//register each sinks with second source
			so2Int.registerSink((NotifSinkInterface)Naming.lookup("rmi://localhost/sinks/" + nSink1.getID()));
			so2Int.registerSink((NotifSinkInterface)Naming.lookup("rmi://localhost/sinks/" + nSink2.getID()));
			so2Int.registerSink((NotifSinkInterface)Naming.lookup("rmi://localhost/sinks/" + nSink3.getID()));

			//have both sources send a notification 
			so.notifySinks(new Notification("Test"));
			so2.notifySinks(new Notification("Test"));

			
		}catch(Exception e){	e.printStackTrace();	}
	}
}
