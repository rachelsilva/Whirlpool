import java.util.ArrayList;

public class Whirlpool 
	implements HashFunction
	{
	
	int overflow = 0;
	int mesLength = 0;
	
	byte[] currentState = new byte[64];
	ArrayList<Byte> message = new ArrayList<Byte>();
	

	@Override
	public int digestSize() {
		return 64;
	}

	@Override
	public void hash(int b) {
		byte c = (byte)b;
		message.add(c);
		if( mesLength == Integer.MAX_VALUE ){
			mesLength = 0;
			if( overflow > 8 ){
				System.err.println("Whirlpool message length exceeded");
				return;
			}
			overflow ++;
		}
		else{ mesLength++; }
	}

	@Override
	public void digest(byte[] d) {
		long necPad = (message.size() - 32)%64;
		if( necPad != 0){
			byte l =  (byte) 0x80; // If things are messed up later, look here.
			message.add(l);
			necPad++;
			while( necPad %64 != 0 ){
				message.add((byte)0);				
				necPad++;
			}
		}
		System.out.println(message.size());
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (int q = 0; q < (Integer.parseInt(args[0])); q++){
			Whirlpool wP = new Whirlpool();
			byte[] d = new byte[wP.digestSize()];
			for(int w = 0; w < wP.digestSize(); w++){
				wP.hash(0);
			}
			wP.digest(d);
		}
		System.out.println("Not Broken!");
	}
	


}
