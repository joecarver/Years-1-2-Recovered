import javax.swing.*;


import java.awt.event.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

/*
 * Main panel includes three smaller panels, one for top details, one for options at the bottom,
 * and one for displaying the fractals.
 * 
 * Parts 1-5 completed and one extension task (live update of julia set), but no zoom feature implemented.
 */

@SuppressWarnings("serial")
public class MainPanel extends JPanel
{
	MandelPanel mandel = new MandelPanel();
	JuliaPanel julia = new JuliaPanel();
	
	JPanel frames = new JPanel(new GridLayout(1,2)); //holds the mandelbrot and julia panel
	JPanel top = new JPanel(); //hods top details
	
	Complex selPoint; //instance variable so can be accessed by whole class - this is where user clicks
	
	JPanel bottom = new JPanel();
	DecimalFormat df = new DecimalFormat("#.###"); //used to crop the value of complex number
	
	JTextField minX = new JTextField("" + MandelPanel.DEFAULT_xMin, 8);
	JTextField maxX = new JTextField("" + MandelPanel.DEFAULT_xMax, 8);
	JTextField minY = new JTextField("" + MandelPanel.DEFAULT_yMin, 8);
	JTextField maxY = new JTextField("" + MandelPanel.DEFAULT_yMax, 8);
	JTextField iter = new JTextField("" + MandelPanel.DEFAULT_ITERATIONS, 8);
	
	JRadioButton click = new JRadioButton("Update on click", true);
	JRadioButton live = new JRadioButton("Update Live (Click on any point to save as favourite)");
	ButtonGroup b = new ButtonGroup();
	
	JButton help = new JButton("HELP");
	HelpWindow hw = new HelpWindow("HELP");
	
	
	HashMap<String, Complex> favs = new HashMap<String, Complex>();
	
	JComboBox<String> favsBox = new JComboBox<String>();
	
	
	JTextField usp = new JTextField(10); //displays user selected points
	
	
	
	public MainPanel()
	{
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1600, 800));
		frames.add(mandel);
		frames.add(julia);
		this.add(frames, BorderLayout.CENTER);
		this.add(top, BorderLayout.NORTH);
		this.add(bottom, BorderLayout.SOUTH);
		
	}
	
	//init methods adds all components and adds all relevant mouse/action listeners
	public void init()
	{
		JLabel minXlab = new JLabel("Min X:");
		JLabel maxXlab = new JLabel("Max X:");
		JLabel minYlab = new JLabel("Min Y:");
		JLabel maxYlab = new JLabel("Max y:");
		JLabel iterlab = new JLabel("Iterations:");
		JButton generate = new JButton("Generate");
		JButton favourite = new JButton("Favourite");
		JButton reset = new JButton("Reset");
		JLabel usplab = new JLabel ("User Selected Point:");
		JButton favDisplay = new JButton("Display");
		
		usp.setEditable(false);
		
		top.setLayout(new FlowLayout());
		bottom.setLayout(new FlowLayout());
		
		
		
		top.add(minXlab); top.add(minX);
		top.add(maxXlab); top.add(maxX);
		top.add(minYlab); top.add(minY);
		top.add(maxYlab); top.add(maxY);
		top.add(iterlab); top.add(iter);
		
		mandel.addMouseListener(new pointListener());
		mandel.addMouseListener(new hoverListener());
		mandel.addMouseMotionListener(new hoverListener());
		
		mandel.add(new JLabel("MandelBrot Set"));
		julia.add(new JLabel("Julia Set"));
		
		bottom.add(generate);
		bottom.add(reset);
		bottom.add(favourite);
		bottom.add(usplab); bottom.add(usp);
		bottom.add(new JLabel("Favourites:"));
		bottom.add(favsBox);
		bottom.add(favDisplay);
		
		JPanel options = new JPanel(new GridLayout(2,1));
		b.add(click);
		b.add(live);
		options.add(click);
		options.add(live);
		bottom.add(options);
		bottom.add(help);
		
		reset.addActionListener(new resetListener());
		help.addActionListener(new helpListener());
		generate.addActionListener(new updateListener());
		favourite.addActionListener(new favAddListener());
		favDisplay.addActionListener(new favDisListener(favsBox));
				
	}
	
	/*
	 * returns mandel panel to default values upon press of reset button
	 */
	public class resetListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			MandelPanel.xMax = MandelPanel.DEFAULT_xMax;
			MandelPanel.xMin = MandelPanel.DEFAULT_xMin;
			MandelPanel.yMax = MandelPanel.DEFAULT_yMax;
			MandelPanel.yMin = MandelPanel.DEFAULT_yMin;
			MandelPanel.iterations = MandelPanel.DEFAULT_ITERATIONS;
			
			minX.setText("" + MandelPanel.DEFAULT_xMin);
			maxX.setText("" + MandelPanel.DEFAULT_xMax);
			minY.setText("" + MandelPanel.DEFAULT_yMin);
			maxY.setText("" + MandelPanel.DEFAULT_yMax);
			iter.setText("" + MandelPanel.DEFAULT_ITERATIONS);
			
			mandel.update();
		}
	}
	
	/*
	 * adds favourite upon press of "Favourite" button
	 */
	public class favAddListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			Complex ref = selPoint;
			String info = usp.getText();
			//creates error box if the value is already in there
			//should do drawing code elsewhere but not a very intensive operation
			if(favs.containsValue(ref))
			{
				JFrame error = new JFrame();
				error.setVisible(true);
				error.pack();
				error.setSize(300,70);
				JPanel view = new JPanel();
				error.add(view);
				view.add(new JLabel("ERROR - Point is already in favourites"));
				error.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			//if not in there, add the values to the favs hashmap and the string representation to the combobox
			else
			{
			favs.put(info, ref);
			favsBox.addItem(info);
			}

		}
	}
	
	// gets the current value in the combobox and updates the juliapanel the the relevant complex for this number
	public class favDisListener implements ActionListener
	{
		JComboBox<String> c;
		public favDisListener(JComboBox<String> c)
		{
			this.c = c;
		}
		public void actionPerformed(ActionEvent e)
		{
			String s = c.getSelectedItem().toString();
			Complex fav = favs.get(s);
			
			julia.update(fav);
		}
	}
	
	//displays help box 
	public class helpListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			hw.init();
		}
	}
	
	//for generate button - updates mandel with new values in boxes
	public class updateListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			updateBoxes();
			mandel.update();
		}
		
	}
	
	//displays value of clicked point in box and changes julia set to that value if option is selected
	public class pointListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			selPoint = mandel.createComplex(e.getX(), e.getY());
			
			double selReal = Double.valueOf(df.format(selPoint.getReal())); //crops the value for easy reading
			double selImag = Double.valueOf(df.format(selPoint.getImag()));
			
			usp.setText("" + selReal + " + " + selImag + "i");
			
			if(click.isSelected() == true){
				julia.update(selPoint);
				}
			
		}
		
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	//code to allow live updating of julia panel if the box is selected
	public class hoverListener implements MouseMotionListener, MouseListener
	{

		public void mouseMoved(MouseEvent e) 
		{
			Complex mPoint = mandel.createComplex(e.getX(), e.getY());
			if(live.isSelected() == true){
				julia.update(mPoint);
			}
		}
		//allows adding to favourite when user clicks on any point on mandelbrot
		public void mouseClicked(MouseEvent e)
		{
			if(live.isSelected() == true)
			{
				Complex ref = selPoint;
				String info = usp.getText();
				if(favs.containsValue(ref)) //for some reason this check doesn't work - on live mode user can add point to favourites multiple times
				{
					JFrame error = new JFrame();
					error.setVisible(true);
					error.pack();
					error.setSize(300,70);
					JPanel view = new JPanel();
					error.add(view);
					view.add(new JLabel("ERROR - Point is already in favourites"));
					error.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}
				else
				{
					favs.put(info, ref);
					favsBox.addItem(info);
				}
			}
		}
		public void mouseDragged(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	//updates values on mandel set with any new values in boxes, when generate is pressed
	public void updateBoxes()
	{
		double new_xMin = Double.parseDouble((minX.getText()));
		double new_xMax = Double.parseDouble((maxX.getText()));
		double new_yMin = Double.parseDouble((minY.getText()));
		double new_yMax = Double.parseDouble((maxY.getText()));
		int new_iter = Integer.parseInt((iter.getText()));
		
		MandelPanel.iterations = new_iter;
		MandelPanel.xMax = new_xMax;
		MandelPanel.xMin = new_xMin;
		MandelPanel.yMin = new_yMin;
		MandelPanel.yMax = new_yMax;
	}
}