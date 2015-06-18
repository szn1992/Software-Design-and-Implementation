// Zhuonan Sun, HW9, CSE331 AC
// 6/3/2014

package hw9;

import hw8.Building;
import hw8.CampusPaths;
import hw8.Path;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// View of CampusPathsMain
public class CampusGUI {
	
	/**
	 * CampusGUI consist of two parts: user panel and map
	 * user panel: at the bottom of GUI, has the functions of reset, 
	 * marking locations of buildings, finding the shortest path
	 * between two locations, and getting its distance
	 * map: at the top of GUI, used as display of printing locations
	 * of buildings, the shortest path between two buildings
	 */
	
	private CampusPaths model;		// the model that stores all data and computes
	private JFrame frame;			// window of GUI
	
	private BufferedImage image;	// original image of map
	private JLabel mapImage;		// stores image of map
	private JScrollPane map;		// container of map
	
	private JPanel panel;			// container of user panel
	private JButton reset;			// reset button
	
	private JComboBox<String> startList;		// starting location combo box
	private JComboBox<String> destList;			// destination combo box
	private ArrayList<String> shortNames;		// abbreviated name list of buildings
	
	private JButton find;			// get direction button
	private JTextField distance;	// display the distance or "ERROR"
	
	/**
	 * Constructs a GUI with two main parts, user panel and map
	 * @param title title of the window
	 */
	public CampusGUI(String title) {
		 frame = new JFrame(title);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 prepareAndRunGUI();
	}
	
	/**
	 * 
	 * @param action action to be assigned to reset button
	 * @effect assign action to reset button
	 */
	public void assignResetAction(ActionListener action) {
		reset.addActionListener(action);
	}
	
	/**
	 * 
	 * @param action action to be assigned to starting location combo box
	 * @effect assign action to starting location combo box
	 */
	public void assignStartAction(ActionListener action) {
		startList.addActionListener(action);
	}
	
	/**
	 * 
	 * @param action action to be assigned to destination combo box
	 * @effect assign action to destination combo box
	 */
	public void assignDestAction(ActionListener action) {
		destList.addActionListener(action);
	}

	/**
	 * 
	 * @param action action to be assigned to get direction button
	 * @effect assign action to get direction button
	 */
	public void assignFindAction(ActionListener action) {
		find.addActionListener(action);
	}
	
	/**
	 * 
	 * @return the abbreviated name of starting location selected
	 */
	public String getStart() {
		return shortNames.get(startList.getSelectedIndex());
	}
	
	/**
	 * 
	 * @return the abbreviated name of destination selected
	 */
	public String getDest() {
		return shortNames.get(destList.getSelectedIndex());
	}
	
	/**
	 * 
	 * @param start starting location
	 * @param dest destination
	 * @param startCalls if start comboBox is clicked: true(start is clicked), false(dest is clicked)
	 * @effect mark starting location as a rectangular and destination as
	 * a circle on the map if start and dest are not null, display BAD LOCATIONS
	 * otherwise
	 * if start combo box is clicked, move view position to start, otherwise moves
	 * to dest 
	 */
	public void mark(Building start, Building dest, boolean startCalls) {
		if(start != null && dest != null) {
			BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			Graphics2D g = newImage.createGraphics();
			
			markHelper(g, start, dest);
			g.dispose();
			
			if(startCalls)
				showNewResult(newImage, (int)Math.round(start.getX()), (int)Math.round(start.getY()));
			else
				showNewResult(newImage, (int)Math.round(dest.getX()), (int)Math.round(dest.getY()));
		} else {
			distance.setText("BAD LOCATIONS");
		}
	}
	
	/**
	 * 
	 * @param result distance between two locations
	 * @effect display the result if it is greater than or equal to 0,
	 * display BAD DISTANCE otherwise
	 */
	public void printDistance(int result) {
		if(result >= 0) {
			distance.setText("" + result);
		} else {
			distance.setText("BAD DISTANCE");
		}
	}
	
	/**
	 * @effect reset all components in GUI, and run again.
	 */
	public void reset() {
		frame.remove(map);
		frame.remove(panel);
		
		prepareAndRunGUI();
	}
	
	/**
	 * helper method of constructor and reset, add map and panel
	 * to gui and display gui
	 */
	private void prepareAndRunGUI() {
		addMap();
		addUserPanel();
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * @param path path from start to dest
	 * @effect draw the path from start to dest on map, start will be represented
	 * as a red rectangular, dest will be represented as a red circle, their path 
	 * will be represented as a red curve.
	 * if path is null, display BAD PATH in text field
	 * moves the view point to starting location
	 */
	public void drawPath(Path<Building> path) {
		if(path != null) {
			int x, y, nextX, nextY;
			List<Building> pathList = path.getPath();
			
			BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			Graphics2D g = newImage.createGraphics();
			
			// First mark start and dest
			markHelper(g, pathList.get(0), pathList.get(pathList.size() - 1));
			
			// set thickness of line
			g.setStroke(new BasicStroke(4));		
			
			// draw lines
			for(int i = 0; i < pathList.size() - 1; i++) {
				x = (int) Math.round(pathList.get(i).getX());
				y = (int) Math.round(pathList.get(i).getY());
				nextX = (int) Math.round(pathList.get(i + 1).getX());
				nextY = (int) Math.round(pathList.get(i + 1).getY());
				
				g.drawLine(x, y, nextX, nextY);
			}
			
			g.dispose();
			showNewResult(newImage, (int)Math.round(pathList.get(0).getX()), 
						(int)Math.round(pathList.get(0).getY()));
		} else {
			distance.setText("BAD PATH");
		}
	}
	
	/**
	 * @effect add map container to the frame, display BAD FILE in text field 
	 * if there is IOException while reading jpg file.
	 */
	private void addMap() {
		try {
			image = ImageIO.read(new File("src/hw8/data/campus_map.jpg"));
			mapImage = new JLabel(new ImageIcon(image));
			
			map = new JScrollPane();
			map.setPreferredSize(new Dimension(1024, 660));
			map.setAutoscrolls(true);
			
			map.getViewport().add(mapImage);
			map.getViewport().setViewPosition(new Point(1000, 1000));	// initially set view
			frame.add(map, BorderLayout.NORTH);						// position at the center of school
		} catch (IOException e) {
			distance.setText("BAD FILE");
		}
	}
	
	/**
	 * Helper method for building GUI
	 * add user panel to the left of GUI
	 */
	private void addUserPanel() {
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(1024, 70));
		
		addReset();		// add reset button
		model = new CampusPaths("campus_buildings.dat", "campus_paths.dat");
		Map<String, String> nameMap = model.sortBuildingNames();
		
		panel.add(new Label("Starting Location:")); 
		
		addStartList(nameMap);		// add starting location combo box
		
		panel.add(new Label("Destination:"));
		addDestList(nameMap);		// add destination combo box
		
		addFind();		// add find button
		
		panel.add(new Label("Distance:"));
		addDistance();	// add distance text field
		panel.add(new Label("feet"));
		
		frame.add(panel, BorderLayout.SOUTH);
	
		
	}
	
	/**
	 * Helper method of addUserPanel
	 * add reset button to the panel
	 */
	private void addReset() {
		reset = new JButton("RESET");
		panel.add(reset);
	}
	
	/**
	 * helper method of addUserPanel
	 * add get direction button to the panel
	 */
	private void addFind() {
		find = new JButton("Get Direction");
		panel.add(find);
	}
	
	/**
	 * Helper method of addUserPanel, adds starting location combo box to the panel
	 * @param nameMap mapping short names of buildings to their long names
	 */
	private void addStartList(Map<String, String> nameMap) {
		startList = new JComboBox<String>();
		startList.setSize(new Dimension(100, 20));
		shortNames = new ArrayList<String>();
		
		for(String shortName : nameMap.keySet()) {
			startList.addItem(shortName + " (" + nameMap.get(shortName) + ")");
			shortNames.add(shortName);
		}
		panel.add(startList);
	}
	
	/**
	 * Helper method of addUserPanel, adds destination combo box to the panel
	 * @param nameMap mapping short names of buildings to their long names
	 */
	private void addDestList(Map<String, String> nameMap) {
		destList = new JComboBox<String>();
		destList.setSize(new Dimension(100, 20));
		for(String shortName : nameMap.keySet()) {
			destList.addItem(shortName + " (" + nameMap.get(shortName) + ")");
		}
		panel.add(destList);
	}
	
	/**
	 * helper method of addUserPanel, adds distance text field to the panel
	 */
	private void addDistance() {
		distance = new JTextField("0");
		distance.setColumns(6);
		distance.setSize(new Dimension(90, 20));
		panel.add(distance);
	}
	
	/**
	 * helper methods to mark and drawPath
	 * load new image of map into scroll pane and display it
	 * @param newImage new image updated from the original map image
	 */
	private void showNewResult(BufferedImage newImage, int x, int y) {
		mapImage = new JLabel(new ImageIcon(newImage));
		map.getViewport().removeAll();
		map.getViewport().add(mapImage);
		
		// always 
		map.getViewport().setViewPosition(new Point(x - 650, y - 300));
		frame.add(map, BorderLayout.NORTH);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * 
	 * @param g Graphics2D used to draw marks for start and dest
	 * @param start starting location to be marked
	 * @param dest destination to be marked
	 */
	private void markHelper(Graphics2D g, Building start, Building dest) {
		// starting location
		int startX = (int) Math.round(start.getX());
		int startY = (int) Math.round(start.getY());
		int destX = (int) Math.round(dest.getX());
		int destY = (int) Math.round(dest.getY());
		
		// draw the original image first
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		
		// Primary color in g, used for both locations and paths
		g.setColor(Color.RED);
		g.fillRect(startX - 7, startY - 7, 20, 20);		// STARTING LOCATION
		g.fillOval(destX - 7, destY - 7, 20, 20);		// DESTINATION
	}
}
