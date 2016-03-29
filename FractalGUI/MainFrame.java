import javax.swing.JFrame;

/*
 * This class is purely to create the window and add the main panel
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	public MainFrame(String title)
	{
		super(title);
	}
	
	public void init()
	{
		MainPanel p = new MainPanel();
		p.init();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(p);
		setVisible(true);
		pack();
	}
}
