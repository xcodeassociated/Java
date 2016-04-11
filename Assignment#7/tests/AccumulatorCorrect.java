class Accumulator implements KalkulatorI.AccumulatorI {
	private int i = 0;

	@Override
	public void setValue(int acc) {
		i = acc;
	}

	@Override
	public int getValue() {
		return i;
	}

}
