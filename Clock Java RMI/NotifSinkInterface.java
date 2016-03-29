import java.rmi.*;

public interface NotifSinkInterface extends Remote 
{
	public void printMessage(Notification n, NotifSourceInterface nSource) throws RemoteException;
	public String getID() throws RemoteException;
}
