import java.rmi.RemoteException;
import java.text.*;
import java.util.*;

public class Clock implements Runnable {

	NotifSourceInterface nSource;
	Calendar cal;
	

	public Clock(NotifSourceInterface nSource)
	{
		this.nSource = nSource;
	}
	
	public String getSourceName(){	try{
		return "Clock started by " + nSource.getName();	
		} catch (RemoteException e){
			System.out.println("Source not found");
			return null;
		}
	}

	public void run()
	{
		cal = Calendar.getInstance();
		
		try{
				System.out.println("Clock started for " + nSource.getName() + " at time " + cal.getTime().toString());
		} catch (RemoteException e){System.out.println("error " + e);}
		
	
		for(;;)
		{
			try{
				Notification not = new Notification(cal.getTime().toString());
				nSource.notifySinks(not);
				Thread.sleep(5000);
			}catch (Exception e){System.out.println("error " + e);}
		}
		
	}

}
