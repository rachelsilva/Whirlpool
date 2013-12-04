public class Whirlpool extends WhirlpoolAbstract{

	public Whirlpool(){
    	super();
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
    	
    	//Step 4: Do the blocks
    	while(!message.isEmpty()){
    		//Get the current message block
    		byte[][] currentMessage = new byte[8][8];
    		
    		for(int i = 0; i < 8; i++){
    			for(int j = 0; j < 8; j++){
    				currentMessage[i][j] = message.remove();
    			}
    		}
    		
    		System.out.println("Message before block cipher");
    		System.out.println(niceDisplay(currentMessage));
    		System.out.println();
    		
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
    	
    	//Pre-Round key XOR
    	roundMessage = WhirlpoolOps.matrixXOR(roundMessage, roundKey);
    	
    	for(int i = 1; i < 11; i++){
    		System.out.println("Current round is: " + (i));
    		System.out.println();
    		roundMessage = WhirlpoolOps.substituteBytes(roundMessage);
    		System.out.println("Message after byte substitution");
    		System.out.println(niceDisplay(roundMessage));
    		System.out.println();
    		roundMessage = WhirlpoolOps.shiftColumns(roundMessage);
    		System.out.println("Message after column shift");
    		System.out.println(niceDisplay(roundMessage));
    		System.out.println();
    		roundMessage = WhirlpoolOps.mixRows(roundMessage);
    		System.out.println("Message after row mix");
    		System.out.println(niceDisplay(roundMessage));
    		System.out.println();
    		roundKey = WhirlpoolOps.getRoundKey(roundKey, i);
    		System.out.println("ROUND " + i + "KEY");
    		System.out.println(niceDisplay(roundKey));
    		System.out.println();
    		roundMessage = WhirlpoolOps.addRoundKey(roundMessage, roundKey);
    		System.out.println("Message after round key add");
    		System.out.println(niceDisplay(roundMessage));
    		System.out.println();
    	}
    	
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
    	wP.hash('a');
    	wP.hash('b');
    	wP.hash('c');
    	wP.digest(d);
    	System.out.println("FINALMESSAGE!!!!!!!!!!");
    	System.out.println(niceDisplay(d));
        System.out.println("Not Broken!");
    }

}