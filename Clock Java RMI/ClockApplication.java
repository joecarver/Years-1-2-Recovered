import javax.swing.*;

import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

public class ClockApplication extends JFrame
{
	JButton createSource, createSink, viewRegSinks, removeSink, viewExistingSources, startClock;
	JTextField sourceNameField, sinkNameField, targetSourceField, viewSinksField, sinkRemoveField, sinkRemoveFieldSource, startClockField;
	JPanel servAddPanel, sinkAddPanel, sinkRemovePanel;
	ArrayList<String> clockList;
	
	
	public ClockApplication(String title){ 	super(title);		}
	
	public static void main(String[] args) throws Exception
	{
		try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI Registry started on port 1099");
		}catch(Exception e){System.out.println(e);}
		
		ClockApplication nf = new ClockApplication("Notifications");
		nf.init();

	}
	
	public void init() throws Exception
	{
		clockList = new ArrayList<String>(); //keep track of which sources have as clock
		
		servAddPanel = new JPanel();
		sinkAddPanel = new JPanel();
		sinkRemovePanel = new JPanel();
		
		createSource = new JButton("Create Source");
		createSink = new JButton("Create Sink");
		viewRegSinks = new JButton("View Registered Sinks of...");
		removeSink = new JButton("Remove");
		viewExistingSources = new JButton("View All Sources");
		startClock = new JButton("Start Clock for Source...");
		
		sourceNameField = new JTextField(5);
		sinkNameField = new JTextField(5);
		targetSourceField = new JTextField(5);
		viewSinksField = new JTextField(5);
		sinkRemoveField = new JTextField(5);
		sinkRemoveFieldSource = new JTextField(5);
		startClockField = new JTextField(5);
		
		viewRegSinks.addActionListener(new NotifListener());
		createSource.addActionListener(new NotifListener());
		createSink.addActionListener(new NotifListener());
		removeSink.addActionListener(new NotifListener());
		viewExistingSources.addActionListener(new NotifListener());
		startClock.addActionListener(new NotifListener());
		
		this.setLayout(new GridLayout(6,1));
		
		this.add(new JLabel("<html><b>ADD A NEW NOTIFICATION SOURCE</b></html>"));
		servAddPanel.add(new JLabel("Source Name:"));
		servAddPanel.add(sourceNameField);
		servAddPanel.add(createSource);
		servAddPanel.add(startClock);
		servAddPanel.add(startClockField);
		servAddPanel.add(viewRegSinks);
		servAddPanel.add(viewSinksField);
		servAddPanel.add(viewExistingSources);
		this.add(servAddPanel);
		
		this.add(new JLabel("<html><b>ADD A NEW NOTIFICATION SINK/REGISTER PRE-EXISTING SINK</b></html>"));
		sinkAddPanel.add(new JLabel("Sink ID:"));
		sinkAddPanel.add(sinkNameField);
		sinkAddPanel.add(new JLabel("Source name to connect to:"));
		sinkAddPanel.add(targetSourceField);
		sinkAddPanel.add(createSink);
		this.add(sinkAddPanel);
		
		this.add(new JLabel("<html><b>REMOVE A NOTIFICATION SINK</b></html>"));
		sinkRemovePanel.add(new JLabel("Remove sink"));
		sinkRemovePanel.add(sinkRemoveField);
		sinkRemovePanel.add(new JLabel("from source"));
		sinkRemovePanel.add(sinkRemoveFieldSource);
		sinkRemovePanel.add(removeSink);
		this.add(sinkRemovePanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700,500);
		this.setVisible(true);
		
	}
	
	public class NotifListener implements ActionListener
	{
		public void actionPerformed(ActionEvent a)
		{
				
			if(a.getSource().equals(createSource))
			{
				if(!sourceNameField.getText().isEmpty())
				{
					try
					{
						NotificationSource nSource = null;
					
						if(Arrays.asList(Naming.list("rmi://localhost/")).contains("//localhost:1099/sources/" +sourceNameField.getText()))
						{
							System.out.println("Source already registered");
						}
						else
						{
							nSource = new NotificationSource(sourceNameField.getText());
							targetSourceField.setText(nSource.getName());
							viewSinksField.setText(nSource.getName());
							startClockField.setText(nSource.getName());
						}
							
					} catch (Exception e){System.out.println("source error " + e);}

				}
				else
				{
					System.out.println("Enter a reference for the source");
				}
			}
			
			else if(a.getSource().equals(startClock))
			{
				if(!startClockField.getText().isEmpty())
				{
					NotifSourceInterface nSourceInt = null;
					try
					{
						if(clockList.contains(startClockField.getText()))
						{
							System.out.println("Clock already started for Source " + startClockField.getText());
						}
						else
						{
							nSourceInt = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + startClockField.getText());
							Thread clThread = new Thread(new Clock(nSourceInt), nSourceInt.getName());
							clThread.start();
							clockList.add(nSourceInt.getName());
						}
							
					}catch(Exception e){ 	System.out.println("clock start error " + e);	}
				}
			
			}
			
			else if(a.getSource().equals(createSink))
			{
				NotifSourceInterface nSourceInt = null;
				try
				{	
					nSourceInt = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + targetSourceField.getText());
					NotificationSink nSink = new NotificationSink(sinkNameField.getText(), nSourceInt);
					
				} catch(Exception e){System.out.println("sink error " + e);}	
			}
			
			else if(a.getSource().equals(viewRegSinks))
			{
				NotifSourceInterface nSourceInt = null;
				try
				{
					nSourceInt = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + viewSinksField.getText());
					nSourceInt.viewSinks();
					
				} catch(Exception e){System.out.println("view error " + e);}
			}
			
			else if(a.getSource().equals(removeSink))
			{
				NotifSourceInterface nSourceInt = null;
				NotifSinkInterface nSinkInt = null;
				try
				{
					nSourceInt = (NotifSourceInterface)Naming.lookup("rmi://localhost/sources/" + sinkRemoveFieldSource.getText());
					nSinkInt = (NotifSinkInterface)Naming.lookup("rmi://localhost/sinks/" + sinkRemoveField.getText());
					nSourceInt.unRegisterSink(nSinkInt);
					
				} catch(Exception e){System.out.println("remove error " + e);}
			}
			
			else if(a.getSource().equals(viewExistingSources))
			{
				try{
					for(String s : Arrays.asList(Naming.list("rmi://localhost/")))
					{
						if(s.contains("sources"))
						{
							System.out.println(s);
						}
					}
				} catch (Exception e){System.out.println("view sources error " + e);}
				
				
			}

			
		}
		
	}
}
