
public class IndicesExt extends Indices {
	public IndicesExt( int p, int d, int t ) {
		firstIndex = p;
		secondIndex = d;
		thirdIndex = t;
	}
	
	public IndicesExt( Indices o ) {
		firstIndex = o.firstIndex;
		secondIndex = o.secondIndex;
		thirdIndex = o.thirdIndex;
	}
	
	public boolean equals( Object o ) {
		if ( ! ( o instanceof Indices ) ) return false;
		
		Indices i = (Indices)o;
		
		if ( i.firstIndex != firstIndex ) return false;
		if ( i.secondIndex != secondIndex ) return false;
		if ( i.thirdIndex != thirdIndex ) return false;
		
		return true;
	}
	
}
