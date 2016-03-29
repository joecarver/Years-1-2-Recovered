import java.rmi.*;

public interface NotifSourceInterface extends Remote 
{
	public void registerSink(NotifSinkInterface nSink) throws RemoteException;
	public void viewSinks() throws RemoteException;
	public String getName() throws RemoteException;
	public void notifySinks(Notification not) throws RemoteException;
	public void unRegisterSink(NotifSinkInterface nSink) throws RemoteException;
}
