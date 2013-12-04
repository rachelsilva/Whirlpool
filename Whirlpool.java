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
                    roundMessage = WhirlpoolOps.substituteBytes(roundMessage);
                    roundMessage = WhirlpoolOps.shiftColumns(roundMessage);
                    roundMessage = WhirlpoolOps.mixRows(roundMessage);
                    roundKey = WhirlpoolOps.getRoundKey(roundKey, i);
                    roundMessage = WhirlpoolOps.addRoundKey(roundMessage, roundKey);
            }
            
            return roundMessage;
    }
        
    /**
* @param args
*/
    public static void main(String[] args) {
            
// for (int q = 0; q < (Integer.parseInt(args[0])); q++){
// Whirlpool wP = new Whirlpool();
// byte[] d = new byte[wP.digestSize()];
// for(int w = 0; w < wP.digestSize(); w++){
// wP.hash(0);
// }
// wP.digest(d);
// }
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