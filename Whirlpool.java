import java.util.ArrayList;

public class Whirlpool implements HashFunction{
        
        int mesLength = 0;
        
        int necessaryPaddingInv = 256;
        
        byte[] currentState = new byte[64];
        
        byte[] messageCounter = new byte[32];
        
        ArrayList<Byte> message = new ArrayList<Byte>();
        
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
        	
        	//Step 1: Do padding
        	addPadding();
        	
        	//Step 2: Append Message Length
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
