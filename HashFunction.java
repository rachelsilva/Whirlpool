
public interface HashFunction {

	   /**
	    * Returns this hash function's digest size in bytes.
	    *
	    * @return  Digest size.
	    */
	   public int digestSize();

	   /**
	    * Append the given byte to the message being hashed. Only the least
	    * significant 8 bits of <TT>b</TT> are used.
	    *
	    * @param  b  Message byte.
	    */
	   public void hash
	      (int b);

	   /**
	    * Obtain the message digest. <TT>d</TT> must be an array of bytes whose
	    * length is equal to <TT>digestSize()</TT>. The message consists of the
	    * series of bytes provided to the <TT>hash()</TT> method. The digest of the
	    * message is stored in the <TT>d</TT> array.
	    *
	    * @param  digest  Message digest (output).
	    */
	   public void digest
	      (byte[] d);

	
	
}
