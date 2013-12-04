import edu.rit.util.Hex;
import edu.rit.util.Instance;

public class TimeHashFunction
	{
	public static void main
		(String[] args)
		throws Exception
		{
		if (args.length != 2) usage();
		String hashclass = args[0];
		int N = Integer.parseInt (args[1]);
		HashFunction hash = (HashFunction)
			Instance.newInstance (hashclass+"()");
		byte[] digest = new byte [hash.digestSize()];
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < N; ++ i)
			hash.hash (0);
		hash.digest (digest);
		long t2 = System.currentTimeMillis();
		System.out.printf ("%s%n", Hex.toString (digest));
		System.out.printf ("%d msec%n", t2 - t1);
		}

	private static void usage()
		{
		System.err.println ("Usage: java TimeHashFunction <hashclass> <N>");
		System.err.println ("<hashclass> = Fully qualified name of HashFunction class");
		System.err.println ("<N> = Message length (bytes)");
		System.exit (1);
		}
	}