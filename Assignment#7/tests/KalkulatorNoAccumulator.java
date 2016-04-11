class Kalkulator implements KalkulatorI {
	
	int a = Integer.MIN_VALUE;
	
	@Override
	public int addA(int i) {
		a += i;
		return a;
	}

	@Override
	public int getA() {
		return a;
	}

	@Override
	public void setAccumulatorA(AccumulatorI a) {
		this.a = 0;
	}

	@Override
	public void setAccumulatorB(AccumulatorI a) {
	}

	@Override
	public void swapAccumulators() {
	}

	@Override
	public int subA(int i) {
		a -= i;
		return a;
	}

	@Override
	public void zeroA() {
		a = 0;
	}

}
