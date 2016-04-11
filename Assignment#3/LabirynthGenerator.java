public class LabirynthGenerator {
	private boolean[][][] lab;
	private int sizeX, sizeY, sizeZ;
	private Indices currentPosition;
	private Indices start;

	// do pracy generatora ponizsze elementy nie sa potrzebne, ale ulatwiaja
	// generowanie testow
	private Indices finish;
	private boolean isPassable;
	
	private boolean security = true;
	
	private final static boolean WALL = false;
	private final static boolean PATH = !WALL;

	
	public void offSecurity() {
		security = false;
	}
	
	public Indices getStart() {
		return start;
	}
	
	public Indices getFinish() {
		return finish;
	}

	public void setFinish(Indices finish) {
		this.finish = finish;
	}

	public boolean isPassable() {
		return isPassable;
	}

	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}

	public LabirynthGenerator(int sizeX, int sizeY, int sizeZ) {
		lab = new boolean[sizeX][sizeY][sizeZ];
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}

	public boolean[][][] getLabirynth() {
		return lab;
	}

	public void setStartPosition(Indices start) {
		this.start = start;
		setIfDifferent( start,  PATH ); // pierwsza komorka sciezki
 		currentPosition = new IndicesExt(start);
	}

	private String decode(int x, int y, int z) {
		Indices id = new IndicesExt(x, y, z);

		if (id.equals(start))
			return " S ";
		if (id.equals(finish)) {
			return " F ";
		}
		if (id.equals(currentPosition))
			return " x ";

		if ( lab[x][y][z] == WALL ) return " # ";
		
		return " . ";
	}

	private boolean get(Indices i) {
		return lab[i.firstIndex][i.secondIndex][i.thirdIndex];
	}

	private boolean setIfDifferent(Indices i, boolean v) {

		if (get(i) == v)
			return false; // ta wartosc juz tam jest

		lab[i.firstIndex][i.secondIndex][i.thirdIndex] = v;

		return true;
	}

	private void fatality(String descr) {
		System.out.println("Error> " + descr);
		System.exit(1);
	}

	/**
	 * Rysuje sciezke w labiryncie. Dlugosc steps krokow, inc = true oznacza inkrementacje.
	 * @param steps liczba pol zajeta przez sciezke
	 * @param inc true - inkrementacja, false - dekrementacja
	 */
	public void moveX(int steps, boolean inc) {
		testConnectionToPrevious();
		for (int i = 0; i < steps; i++) {
			if (inc)
				currentPosition.firstIndex++;
			else
				currentPosition.firstIndex--;
			extendPath();
		}
	}

	public void moveY(int steps, boolean inc) {
		testConnectionToPrevious();
		for (int i = 0; i < steps; i++) {
			if (inc)
				currentPosition.secondIndex++;
			else
				currentPosition.secondIndex--;
			extendPath();
		}
	}

	public void moveZ(int steps, boolean inc) {
		testConnectionToPrevious();
		for (int i = 0; i < steps; i++) {
			if (inc)
				currentPosition.thirdIndex++;
			else
				currentPosition.thirdIndex--;
			extendPath();
		}
	}
	
	private void extendPath() {
		if (!setIfDifferent(currentPosition, PATH)) {
			if ( ! security ) return;
			fatality("Doszlo do zapetlenia - sciezki sie przecinaja");
		}
	}

	private void testConnectionToPrevious() {
		if ( ! security ) return;
		if (get(currentPosition) == WALL) {
			fatality("Na pozycji startowej jest sciana");
		}
	}

	public Indices getCurrentPosition() {
		return new IndicesExt( currentPosition );
	}
	
	public void setCurrentPosition( Indices pos ) {
		currentPosition = pos;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int z = 0; z < sizeZ; z++) {
			sb.append("Pietro " + z + "\n");
			for (int y = sizeY - 1; y >= 0; y--) {
				for (int x = 0; x < sizeX; x++) {
					sb.append(decode(x, y, z));
				}
				sb.append("\n");
			}
		}

		return sb.toString();
	}

}
