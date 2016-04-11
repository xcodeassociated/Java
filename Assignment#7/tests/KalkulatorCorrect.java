class Kalkulator implements KalkulatorI {
	
	private KalkulatorI.AccumulatorI kaA;
	private KalkulatorI.AccumulatorI kaB;
	
	@Override
	public int addA(int i) {
		if ( kaA == null ) return Integer.MIN_VALUE;
		kaA.setValue( kaA.getValue() + i );
		return kaA.getValue();
	}

	@Override
	public int getA() {
		if ( kaA == null ) return Integer.MIN_VALUE;
		return kaA.getValue();
	}

	@Override
	public void setAccumulatorA(AccumulatorI a) {
		kaA = a;
	}

	@Override
	public void setAccumulatorB(AccumulatorI a) {
		kaB = a;
	}

	@Override
	public void swapAccumulators() {
		KalkulatorI.AccumulatorI tmp = kaA;
		kaA = kaB;
		kaB = tmp;
	}

	@Override
	public int subA(int i) {
		if ( kaA == null ) return Integer.MIN_VALUE;
		return addA( -i );
	}

	@Override
	public void zeroA() {
		if ( kaA == null ) return; 
		kaA.setValue( 0 );
	}

}
