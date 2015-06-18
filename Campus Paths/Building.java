package hw8;

public class Building {
  
	private String shortName;
	private String longName;
	private double x;
	private double y;
	
	/**
	 * Abstract function: Building b consists of four parts of information:
	 * shortName: abbreviation of b
	 * longName: full name of b
	 * x: coordinate x of b
	 * y: coordinate y of b
	 * 
	 * Representation invariant: shortName != null && longName != null
	 */
	
	/**
	 * 
	 * @param shortName abbreviation of the building
	 * @param longName full name of the building
	 * @param x coordinate x of the building
	 * @param y coordinate y of the building
	 */
	public Building(String shortName, String longName, double x, double y) {
		this.shortName = shortName;
		this.longName = longName;
		this.x = x;
		this.y = y;
		checkRep();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Building))
			return false;
		Building another = (Building) o;
		return this.shortName.equals(another.getShortName()) && 
				this.longName.equals(another.getLongName()) && 
				this.x == another.getX() && this.y == another.getY();
	}
	
	public int hashCode() {
		return (int) (shortName.hashCode() + 31 * longName.hashCode() + 31 * x + 31 * y);
	}
	
	/**
	 * 
	 * @return shortName of Building
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * 
	 * @return longName of Building
	 */
	public String getLongName() {
		return longName;
	}
	
	/**
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}
	
	public String toString() {
		return "(" + shortName + ", " + longName + ", " + x + ", " + y + ")";
	}
	
	/**
	 * Checks that the representation invariant holds
	 */
	private void checkRep() {
		assert(shortName != null);
		assert(longName != null);
	}
}

