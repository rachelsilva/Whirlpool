import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WhirlpoolAsyncKeygen implements HashFunction{
	
        int necessaryPaddingInv = 256;
        
        byte[] messageCounter = new byte[32];
        
        Queue<Byte> message = new LinkedList<Byte>();
        
        public WhirlpoolAsyncKeygen(){
        	initializeByteArray(messageCounter);
        }
        
        @Override
        public int digestSize() {
        	return 64;
        }

        @Override
        public void hash(int b) {
        	byte c = (byte)(b & 0xFF);
        	if(!messageLimitReach()){
        		message.add(c);
        		addMessageCounter();
        	}
        }

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
        	
        	d = byte2Dto1DArray(currentState);
        	
        }
        
        private byte[][] WBlockCipher(byte[][] message, byte[][] key){
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
        
        private void addPadding(){
        	int necPad;
        	if(necessaryPaddingInv == 0){
        		necPad = 512;
        	}
        	else{
        		necPad = 512 - necessaryPaddingInv;
        	}
        	byte l = (byte) 0x80; // If things are messed up later, look here.
            message.add(l);
            necPad--;
            while( necPad != 0){
                message.add((byte)0);
                necPad--;
            }
        }
        
        private void appendMessageLength(){
        	for(int i = 0; i < messageCounter.length; i++){
        		message.add(messageCounter[i]);
        	}
        }
        
        private void addMessageCounter(){
        	//Update the message count
        	long add = 1;
        	int carry = 0;
        	for (int i = messageCounter.length; i >= 0; i--) {
                carry += ((int)add & 0xFF) + (messageCounter[i] & 0xFF);
                messageCounter[i] = (byte)carry;
                carry >>>= 8;
                add >>>= 8;
            }
        	
        	//Keep track of padding necessary (inverse)
        	necessaryPaddingInv = (necessaryPaddingInv + 1) % 512;
        }
        
        private boolean messageLimitReach(){
        	for(int i = 0; i < messageCounter.length-2; i++){
        		if(messageCounter[i] != 0xFF){
        			return false;
        		}
        	}
        	if(messageCounter[30] != 0xFE && messageCounter[31] != 0x00){
        		return false;
        	}
        	else{
        		return true;
        	}
        }
        
        private byte[] byte2Dto1DArray(byte[][] array2D){
        	int rowLen = array2D.length;
        	int colLen = array2D[0].length;
        	byte[] array1D = new byte[rowLen * colLen];
        	
        	//loop through and populate the array
        	int pos = 0;
        	
        	for(int i = 0; i < rowLen; i++){
        		for(int j = 0; j < colLen; j++){
        			array1D[pos] = array2D[i][j];
        			pos++;
        		}
        	}
        	
        	return array1D;
        }
        
        private void initializeByteArray(byte[] array){
        	for(int i = 0; i < array.length; i++){
        		array[i] = (byte)0;
        	}
        }
        
        private void initialize2DByteArray(byte[][] array){
        	for(int i = 0; i < array.length; i++){
        		for(int j = 0; j < array[0].length; j++){
        			array[i][j] = (byte)0;
        		}
        	}
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