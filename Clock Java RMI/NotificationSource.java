import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.io.*;
import java.net.*;
import java.rmi.server.*;


public class NotificationSource extends UnicastRemoteObject implements NotifSourceInterface
{
	HashMap<String, NotifSinkInterface> regSinks; //store sinks registered with each instance
	String name;
	
	//Constructor creates entry in registry for the Source just created
	public NotificationSource(String name) throws RemoteException
	{
		try
		{
			regSinks = new HashMap<String, NotifSinkInterface>();
			this.name = name;
			Naming.bind("sources/" +getName(), this);
			System.out.println("Source " + getName() + " ready remotely");

		}catch(Exception e){System.out.println("error creating " + e);}
		
		
	}
	
	public String getName(){	return name;	}
	
	
	//Adds the given sink (interface) to hashmap for source instance
	public void registerSink(NotifSinkInterface nSink)
	{
		try {
			if(regSinks.containsKey(nSink.getID()))
			{
				System.out.println("Sink " + nSink.getID() + " already registered with source " + getName());
			}
			else{
				regSinks.put(nSink.getID(), nSink);
				System.out.println("Sink " + nSink.getID() + " added to source " + getName());
			}
			
		} catch (RemoteException e) {	e.printStackTrace();	}
	}
	
	//removes the specified sink from the source which called this method
	public void unRegisterSink(NotifSinkInterface nSink)
	{
		try{
			if(regSinks.containsKey(nSink.getID()))
			{
				regSinks.remove(nSink.getID());
				System.out.println("Sink " + nSink.getID() + " removed from " + getName());
			}
			else{
				System.out.println("Sink " + nSink.getID() + " not registered with Source " + getName());
			}
			
		} catch (RemoteException e){	e.printStackTrace();}
	}
	
	//view all sinks in hashmap of source which called this method
	public void viewSinks()
	{
		if(regSinks.isEmpty())
		{
			System.out.println("Source " + getName() + " has no registered Sinks");
		}
		else
		{
			System.out.println("Source " + getName() + " contains:");
			for(Map.Entry<String, NotifSinkInterface> e : regSinks.entrySet())
			{
				System.out.println("Sink " + e.getKey());
			}
		}
		
		
	}
	
	//called by Clock class (can be called by any) to send notification to each sink, which deals with printing of message
	public void notifySinks(Notification not)
	{
		for(Map.Entry<String, NotifSinkInterface> e : regSinks.entrySet())
		{
			try {
				e.getValue().printMessage(not, this);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				unRegisterSink(e.getValue());
			}
			
		}
	}
	
}
