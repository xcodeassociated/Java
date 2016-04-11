
public class TestObject implements ValueI {
	
	private float value;
	
	public TestObject( float v ) {
		value = v;
	}

	@Override
	public float getValue() {
		return value;				
	}

}
