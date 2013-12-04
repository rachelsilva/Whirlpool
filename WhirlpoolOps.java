
public class WhirlpoolOps {
	
	final static int A = 10;
	final static int B = 11;
	final static int C = 12;
	final static int D = 13;
	final static int E = 14;
	final static int F = 15;
	
	static int[][][] sBox = { 
		{{1,8},{2,3},{C,6},{E,8},{8,7},{B,8},{0,1},{4,F},{3,6},{A,6},{D,2},{F,5},{7,9},{6,F},{9,1},{5,2}},
		{{6,0},{B,C},{9,B},{8,E},{A,3},{0,C},{7,B},{3,5},{1,D},{E,0},{D,7},{C,2},{2,E},{4,B},{F,E},{5,7}},
		{{1,5},{7,7},{3,7},{E,5},{9,F},{F,0},{4,A},{D,A},{5,8},{C,9},{2,9},{0,A},{B,1},{A,0},{6,B},{8,5}},
		{{B,D},{5,E},{1,0},{F,4},{C,B},{3,E},{0,5},{6,7},{E,4},{2,7},{4,1},{8,B},{A,7},{7,D},{9,5},{D,8}},
		{{F,B},{E,E},{7,C},{6,6},{D,D},{1,7},{4,7},{9,E},{C,A},{2,D},{B,F},{0,7},{A,D},{5,A},{8,3},{3,3}},
		{{6,3},{0,2},{A,A},{7,1},{C,8},{1,9},{4,9},{D,9},{F,2},{E,3},{5,B},{8,8},{9,A},{2,6},{3,2},{B,0}},
		{{E,9},{0,F},{D,5},{8,0},{B,E},{C,D},{3,4},{4,8},{F,F},{7,A},{9,0},{5,F},{2,0},{6,8},{1,A},{A,E}},
		{{B,4},{5,4},{9,3},{2,2},{6,4},{F,1},{7,3},{1,2},{4,0},{0,8},{C,3},{E,C},{D,B},{A,1},{8,D},{3,D}},
		{{9,7},{0,0},{C,F},{2,B},{7,6},{8,2},{D,6},{1,B},{B,5},{A,F},{6,A},{5,0},{4,5},{F,3},{3,0},{E,F}},
		{{3,F},{5,5},{A,2},{E,A},{6,5},{B,A},{2,F},{C,0},{D,E},{1,C},{F,D},{4,D},{9,2},{7,5},{0,6},{8,A}},
		{{B,2},{E,6},{0,E},{1,F},{6,2},{D,4},{A,8},{9,6},{F,9},{C,5},{2,5},{5,9},{8,4},{7,2},{3,9},{4,C}},
		{{5,E},{7,8},{3,8},{8,C},{D,1},{A,5},{E,2},{6,1},{B,3},{2,1},{9,C},{1,E},{4,3},{C,7},{F,C},{0,4}},
		{{5,1},{9,9},{6,D},{0,D},{F,A},{D,F},{7,E},{2,4},{3,B},{A,B},{C,E},{1,1},{8,F},{4,E},{B,7},{E,B}},
		{{3,C},{8,1},{9,4},{F,7},{B,9},{1,3},{2,C},{D,3},{E,7},{6,E},{C,4},{0,3},{5,6},{4,4},{7,F},{A,9}},
		{{2,A},{B,B},{C,1},{5,3},{D,C},{0,B},{9,D},{6,C},{3,1},{7,4},{F,6},{4,6},{A,C},{8,9},{1,4},{E,1}},
		{{1,6},{3,A},{6,9},{0,9},{7,0},{B,6},{D,0},{E,D},{C,C},{4,2},{9,8},{A,4},{2,8},{5,C},{F,8},{8,6}}
	};
	
	static byte[][] mrTransform = {
		{((byte)1),((byte)1),((byte)4),((byte)1),((byte)8),((byte)5),((byte)2),((byte)9)},
		{((byte)9),((byte)1),((byte)1),((byte)4),((byte)1),((byte)8),((byte)5),((byte)2)},
		{((byte)2),((byte)9),((byte)1),((byte)1),((byte)4),((byte)1),((byte)8),((byte)5)},
		{((byte)5),((byte)2),((byte)9),((byte)1),((byte)1),((byte)4),((byte)1),((byte)8)},
		{((byte)8),((byte)5),((byte)2),((byte)9),((byte)1),((byte)1),((byte)4),((byte)1)},
		{((byte)1),((byte)8),((byte)5),((byte)2),((byte)9),((byte)1),((byte)1),((byte)4)},
		{((byte)4),((byte)1),((byte)8),((byte)5),((byte)2),((byte)9),((byte)1),((byte)1)},
		{((byte)1),((byte)4),((byte)1),((byte)8),((byte)5),((byte)2),((byte)9),((byte)1)}
	};
	
	public static byte[][] substituteBytes(byte[][] state){
		byte[][] newState = new byte[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				byte oldByte = state[i][j];
				newState[i][j] = sBoxSubstitution(oldByte); 
			}
		}
		return newState;
	}
	
	public static byte[][] shiftColumns(byte[][] state){
		byte[][] newState = new byte[8][8];
		for(int shift = 0; shift < 8;shift++){
			for(int colPos = 0; colPos < 8; colPos++){
				int row = shift;
				int shiftPos = ((colPos+shift)%8);
				newState[row][shiftPos] = state[row][colPos];
			}
		}
		return newState;
	}
	
	public static byte[][] mixRows(byte[][] state){
		byte[][] newState = new byte[8][8];
		newState = matrixMultiplication(state, mrTransform);
		return newState;
	}
	
	public static byte[][] addRoundKey(byte[][] state, byte[][] roundKey){
		byte[][] newState;
		newState = matrixXOR(state, roundKey);
		return newState;
	}
	
	//prints a byte matrix nicely
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
	
	public static byte[][] getRoundKey(byte[][] key, int roundCounter){
		byte[][] newState = key;
		byte[][] roundConstantMatrix;
		newState = substituteBytes(newState);
		newState = shiftColumns(newState);
		newState = mixRows(newState);
		roundConstantMatrix = makeRoundConstantMatrix(roundCounter);
		newState = matrixXOR(newState, roundConstantMatrix);
		return newState;
	}
	
	private static byte[][] makeRoundConstantMatrix(int roundCounter){
		byte[][] roundCounterMatrix = new byte[8][8];
		for(int f = 0; f < 8; f++){
			byte roundConstant = ((byte)((8 * (roundCounter-1))+f));
			roundCounterMatrix[0][f] = sBoxSubstitution(roundConstant);
		}
		for(int i = 1; i < 8; i++){
			for(int j = 0; j < 8; j++){
				roundCounterMatrix[i][j] = ((byte)0);
			}
		}
		return roundCounterMatrix;
	}
	
	private static byte sBoxSubstitution(byte oldByte){
		int oldLBits = ((oldByte >>> 4) & 0x0F); 
		int oldRBits = (((oldByte << 4) >>> 4) & 0x0F);
		int newLBits = sBox[oldLBits][oldRBits][0];
		int newRBits = sBox[oldLBits][oldRBits][1];
		byte newLByte = ((byte)(((byte) newLBits) << 4));
		byte newRByte = ((byte) newRBits);
		byte newByte = ((byte)(newLByte ^ newRByte));
		return newByte;
	}
	
	public static byte[][] matrixXOR(byte[][] aMat, byte[][] bMat){
		int aRows = aMat.length;
		int aCols = aMat[0].length;
		int bRows = bMat.length;
		int bCols = bMat[0].length;
		
		if(aRows != bRows && aCols != bCols){
			throw new IllegalArgumentException("Illegal Matrix XOR");
		}
		
		byte[][] cMat = new byte[aRows][aCols];
		
		for(int i = 0; i < aRows; i++){
			for(int j = 0; j < aCols; j++){
				cMat[i][j] = ((byte)(aMat[i][j] ^ bMat[i][j]));
			}
		}
		
		return cMat;
	}
	
	private static byte[][] matrixMultiplication(byte[][] aMat, byte[][] bMat){
		
		int aRows = aMat.length;
		int aCols = aMat[0].length;
		int bRows = bMat.length;
		int bCols = bMat[0].length;
		
		if(aCols != bRows){
			throw new IllegalArgumentException("Illegal Matrix mulitplication");
		}
		//Create a 0 matrix 
		byte[][] cMat = new byte[aRows][bCols];
		for( int crow = 0; crow < aRows; crow++){
			for( int ccol = 0; ccol < bCols; ccol++){
				cMat[crow][ccol]=0;
			}
		}
		// Calculate the i,j element of cMat
		for( int row = 0; row <aRows; row++){
			for(int col = 0; col< bCols; col++){
				for(int multMuve = 0; multMuve<aCols; multMuve++){
					cMat[row][col] ^= (aMat[row][multMuve] * bMat[multMuve][col]);
				}
			}
		}
		
		return cMat;
	}
	
}