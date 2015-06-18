// ZHUONAN SUN, HW9, CSE331 AC
// 6/3/2014
package hw9;

import hw8.Building;
import hw8.CampusPaths;
import hw8.Path;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller of CampusPathsMain
 * responds to the actions of user
 */
public class CampusController {
	
	private String start, dest;		// abbreviated names of starting location and destination
	private CampusGUI gui;			// view
	private CampusPaths model;		// model
	
	/**
	 * @effect build gui and load model by reading files
	 * assign responses to actions of users
	 */
	public CampusController() {
		gui = new CampusGUI("Campus Map");
		model = new CampusPaths("campus_buildings.dat", "campus_paths.dat");
	
		assignActions();
	}
	
	/**
	 * @effect responses to actions of users, including reset button clicked, 
	 * combo boxes selected, and get direction button clicked
	 */
	public void assignActions() {
		gui.assignResetAction(new ActionListener() {		// reset
			
			/**
			 * @effect reset all variables in gui
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.reset();
				assignActions();
			}
		});
		
		gui.assignStartAction(new ActionListener() {		// starting location
			
			/**
			 * @effect mark the locations of start and dest
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				start = gui.getStart();
				dest = gui.getDest();
				gui.mark(model.findBuilding(start), model.findBuilding(dest), true);
			}
		});
		
		gui.assignDestAction(new ActionListener() {			// destination
			
			/**
			 * @effect mark the locations of start and dest
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				start = gui.getStart();
				dest = gui.getDest();
				gui.mark(model.findBuilding(start), model.findBuilding(dest), false);
			}
		});
		
		gui.assignFindAction(new ActionListener() {		// get direction
			/**
			 * @effect print the shortest path between start and dest on map
			 * print the distance in text field
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				start = gui.getStart();
				dest = gui.getDest();
				Path<Building> path = model.getShortestPath(start, dest);
				int distance = (int) Math.round(path.getCost());
				
				gui.printDistance(distance);
				gui.drawPath(path);
			}
		});
	}
}
