
public class Whirlpool 
	implements HashFunction
	{
	

	@Override
	public int digestSize() {
		return 64;
	}

	@Override
	public void hash(int b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void digest(byte[] d) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int q = 0; q < (Integer.parseInt(args[0])); q++){
			//TODO Do encryption thingy
		}
	}
	


}
