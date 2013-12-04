import edu.rit.util.Hex;
import edu.rit.util.Instance;
import java.io.*;
import java.util.Scanner;
/*Modified vesrion of TimeHashFunction.java from CS lib */
public class WPFileReader
	{
	public static void main
		(String[] args)
		throws Exception
		{
		if (args.length != 2) usage();
		String hashclass = args[0];
		HashFunction hash = (HashFunction)
			Instance.newInstance (hashclass+"()");
		byte[] digest = new byte [hash.digestSize()];
		long t1 = System.currentTimeMillis();
	    Scanner     fileScanner = new Scanner( new File( args[1] ));
	    while( fileScanner.hasNext()) {
	         String current      = fileScanner.nextLine();
	         for (int i=0;i<current.length();i++){
	        	 //for each char...
	        	 int q = current.charAt(i);
	        	 System.out.print(current.charAt(i));
	        	 hash.hash(q); 
	         }
	    }
	    System.out.println();
	    fileScanner.close();
		hash.digest (digest);
		long t2 = System.currentTimeMillis();
		System.out.printf ("%s%n", Hex.toString (digest));
		System.out.printf ("%d msec%n", t2 - t1);
		}

	private static void usage()
		{
		System.err.println ("Usage: java WPFileReader  <hashclass> <filename>");
		System.err.println ("<hashclass> = Fully qualified name of HashFunction class");
		System.err.println ("<filename> = Name of the file you want to hash");
		System.exit (1);
		}
	}