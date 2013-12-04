import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WhirlpoolAsyncKeygen extends WhirlpoolAbstract{
	
    @Override
    public void digest(byte[] d) {
    	byte[][] currentState = new byte[8][8];
    	
    	//Step 1: Do padding
    	addPadding();
    	
    	//Step 2: Append Message Length
    	appendMessageLength();
    	
    	//Step 3: Initialize Hash Matrix
    	initialize2DByteArray(currentState);
    	
    	//Step 4:
    	while(!message.isEmpty()){
    		//Get the current message block
    		byte[][] currentMessage = new byte[8][8];
    		
    		for(int i = 0; i < 8; i++){
    			for(int j = 0; j < 8; j++){
    				currentMessage[i][j] = message.remove();
    			}
    		}
    		
    		//Run the whirlpool block cipher
    		byte[][] output = WBlockCipher(currentMessage, currentState);
    		
    		//Update the state with the Merkle-Damgard Construct
    		currentState = WhirlpoolOps.matrixXOR(output, WhirlpoolOps.matrixXOR(currentMessage, currentState));
    	}
    	
    	byte[] finalOutput = byte2Dto1DArray(currentState);
    	
    	//Copy the data to the return array
    	byte1DarrayCopy(finalOutput, d);
    }
        
    protected byte[][] WBlockCipher(byte[][] message, byte[][] key){
    	byte[][] roundMessage = message;
    	byte[][] roundKey = key;
    	
    	//Set up the ExecutorService
    	ExecutorService executor = Executors.newFixedThreadPool(2);
    	
    	//Pre-Round key XOR
    	roundMessage = WhirlpoolOps.matrixXOR(roundMessage, roundKey);
    	
    	//Do the rounds
    	for(int i = 0; i < 10; i++){
    		Callable<byte[][]> keyGen = new WhirlPoolKeygenCallable(roundKey, i);
    		Future<byte[][]> keyGetter = executor.submit(keyGen);
    		
    		roundMessage = WhirlpoolOps.substituteBytes(roundMessage);
    		roundMessage = WhirlpoolOps.shiftColumns(roundMessage);
    		roundMessage = WhirlpoolOps.mixRows(roundMessage);
    		try {
    			roundKey = keyGetter.get();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		} catch (ExecutionException e) {
    			e.printStackTrace();
    		}
    		roundMessage = WhirlpoolOps.addRoundKey(roundMessage, roundKey);
    	}
    	
    	executor.shutdown();
    	return roundMessage;
    }
        
    /**
     * @param args
     */
    public static void main(String[] args) {
            
//                for (int q = 0; q < (Integer.parseInt(args[0])); q++){
//                        Whirlpool wP = new Whirlpool();
//                        byte[] d = new byte[wP.digestSize()];
//                        for(int w = 0; w < wP.digestSize(); w++){
//                                wP.hash(0);
//                        }
//                        wP.digest(d);
//                }
    	Whirlpool wP = new Whirlpool();
    	byte[] d = new byte[wP.digestSize()];
    	wP.digest(d);
    	for(int i = 0; i < wP.digestSize(); i++){
    		System.out.println(Integer.toHexString(d[i]));
    	}
        System.out.println("Not Broken!");
    }

}