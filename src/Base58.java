import static java.lang.Math.toIntExact;

public class Base58 {

    private static String sTable = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static long numbase = 58;
    
    public static String IntToBase58(long key, int length)
    {
		StringBuilder base58String = new StringBuilder("");
		
        int[] result = new int[64];
        int maxBit = result.length;

        for (int i = length; i > 0; i--)
        {
            int q = toIntExact(key % numbase);
            base58String.append(sTable.charAt(q));
            key /= numbase;
        }
        
        return base58String.reverse().toString();
    }
    
    public static int IntFromBase58(String str)
    {
    	char[] chars = new StringBuilder(str).reverse().toString().toCharArray();

		int n = 0;
		for (int i = chars.length - 1; i >= 0; i--) {
			n += sTable.indexOf(chars[i]) * (int) Math.pow(numbase, i);
		}
		return n;
    }
}
