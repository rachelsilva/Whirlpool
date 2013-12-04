import java.util.LinkedList;
import java.util.Queue;

public abstract class WhirlpoolAbstract implements HashFunction{

	int necessaryPaddingInv = 32;
    
    byte[] messageCounter = new byte[32];
    
    Queue<Byte> message = new LinkedList<Byte>();
    
    public WhirlpoolAbstract(){
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
	public abstract void digest(byte[] d);
    
    protected abstract byte[][] WBlockCipher(byte[][] message, byte[][] key);
    
    protected void addPadding(){
    	int necPad;
    	if(necessaryPaddingInv == 0){
    		necPad = 64;
    	}
    	else{
    		necPad = 64 - necessaryPaddingInv;
    	}
    	byte l = (byte) 0x80; // If things are messed up later, look here.
        message.add(l);
        necPad--;
        while( necPad != 0){
            message.add((byte)0);
            necPad--;
        }
    }
    
    protected void appendMessageLength(){
    	for(int i = 0; i < messageCounter.length; i++){
    		message.add(messageCounter[i]);
    	}
    	
    	//Clear message counter
    	initializeByteArray(messageCounter);
    }
    
    private void addMessageCounter(){
    	//Update the message count
    	long add = 8;
    	int carry = 0;
    	for (int i = messageCounter.length-1; i >= 0; i--) {
            carry += ((int)add & 0xFF) + (messageCounter[i] & 0xFF);
            messageCounter[i] = (byte)carry;
            carry = carry >>> 8;
            add = add >>> 8;
        }
    	
    	//Keep track of padding necessary (inverse)
    	necessaryPaddingInv = (necessaryPaddingInv + 1) % 64;
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
    
    protected byte[] byte2Dto1DArray(byte[][] array2D){
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
    
    protected void byte1DarrayCopy(byte[] from, byte[] to){
    	for(int i = 0; i < from.length; i++){
    		to[i] = from[i];
    	}
    }
    
    protected static String niceDisplay(byte[] array){
    	String nice = "";
    	for(int i = 0; i < array.length; i++){
    		String temp = Integer.toHexString(array[i]);
    		temp = temp.toUpperCase();
    		if(temp.length() == 8){
    			temp = temp.substring(6, 8);
    		}
    		else if(temp.length() == 1){
    			temp = "0" + temp;
    		}
    		nice += temp;
    	}
    	return nice;
    }
    
    protected static String niceDisplay(byte[][] array2D){
    	int rowLen = array2D.length;
    	int colLen = array2D[0].length;
    	
    	String nice = "";
    	for(int r = 0; r < rowLen; r++){
    		for(int c = 0; c < colLen; c++){
	    		String temp = Integer.toHexString(array2D[r][c]);
	    		temp = temp.toUpperCase();
	    		if(temp.length() == 8){
	    			temp = temp.substring(6, 8);
	    		}
	    		else if(temp.length() == 1){
	    			temp = "0" + temp;
	    		}
	    		nice += temp + " ";
    		}
    		nice += "\n";
    	}
    	return nice;
    }
    
    protected void initializeByteArray(byte[] array){
    	for(int i = 0; i < array.length; i++){
    		array[i] = (byte)0;
    	}
    }
    
    protected void initialize2DByteArray(byte[][] array){
    	for(int i = 0; i < array.length; i++){
    		for(int j = 0; j < array[0].length; j++){
    			array[i][j] = (byte)0;
    		}
    	}
    }
}
