import java.util.concurrent.Callable;

public class WhirlPoolKeygenCallable implements Callable<byte[][]>{
	
	byte[][] roundKey;
	int round;
	
	public WhirlPoolKeygenCallable(byte[][] roundKey, int round){
		this.roundKey = roundKey;
		this.round = round;
	}
	
	@Override
	public byte[][] call() throws Exception {
		// TODO Auto-generated method stub
		return WhirlpoolOps.getRoundKey(roundKey, round);
	}

}
