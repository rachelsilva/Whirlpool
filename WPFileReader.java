import edu.rit.util.Hex;
import edu.rit.util.Instance;
import java.io.*;
import java.util.Scanner;
/*Modified version of TimeHashFunction.java from RIT CS lib */
public class WPFileReader
	{
	public static void main
		(String[] args)
		throws Exception
		{
		if (args.length != 2) usage(); //make sure we have the correct number of arguments
		String hashclass = args[0];   // picks which hash based on parameter
		HashFunction hash = (HashFunction)
			Instance.newInstance (hashclass+"()");
		byte[] digest = new byte [hash.digestSize()]; // greats output holder
		long t1 = System.currentTimeMillis();
	    Scanner     fileScanner = new Scanner( new File( args[1] )); // opens the file in a scanner
	    while( fileScanner.hasNext()) {  			//for each line in the file
	         String current      = fileScanner.nextLine(); 
	         for (int i=0;i<current.length();i++){ 
	        	 //for each char...
	        	 int q = current.charAt(i);
	        	 System.out.print(current.charAt(i)); //print out chars for verification
	        	 hash.hash(q); //hash each char in the line
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
