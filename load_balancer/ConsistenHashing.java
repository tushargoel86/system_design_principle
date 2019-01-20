import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing<T> {
	private int numberOfReplicas;
	private MessageDigest md;

	//to store hashes of servers and their replicas
	private SortedMap<Long, T> hashes = new TreeMap<Long, T>();
	
	public ConsistentHashing(Collection<T> servers, 
					int numberOfReplicas, MessageDigest md) {
		this.numberOfReplicas = numberOfReplicas;
		this.md = md;
		
		for (T node : servers) {
			add(node);
		}
	}

	//add new server and their replicas
	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			byte[] digest = md.digest((i + node.toString()).getBytes(Charset.defaultCharset()));
			ByteBuffer wrapped = ByteBuffer.wrap(digest);
			hashes.put(wrapped.getLong(), node);
		}
	}

	
	//Return the server whose either hashes matches or
	//as next available server or its replicas as per consistent
	//hashing design
	public T get(T key) {
		if (hashes.isEmpty()) return null;
		
		byte[] digest = md.digest(key.toString().getBytes(Charset.defaultCharset()));
		ByteBuffer wrapped = ByteBuffer.wrap(digest);
		
		long hash = wrapped.getLong();
		if (!hashes.containsKey(hash)) {
			SortedMap<Long, T> tailMap = hashes.tailMap(hash);
			Long tailKey = tailMap.isEmpty() ? hashes.firstKey() : tailMap.firstKey();
			return hashes.get(tailKey);
		}
		return hashes.get(hash);
	}
	
	//remove server and all its replicas
	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			byte[] digest = md.digest((i + node.toString()).getBytes(Charset.defaultCharset()));
			ByteBuffer wrapped = ByteBuffer.wrap(digest);
			hashes.remove(wrapped.getLong());
		}
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		List<String> nodes = Arrays.asList("red1", "red2", "red3");
		MessageDigest md = MessageDigest.getInstance("SHA1");
		
		//we should use more replicas so that more servers can be distributed
		ConsistentHashing<String>  consistentHash = new ConsistentHashing<>(nodes, 10, md);
		
		System.out.println(consistentHash);
		String[] userIds = 
	        {"-32435466128",
	        "-23234675788",
		"-131344666qwew",
	        "-124324565777",
	        "-12132565767688"
	        };
		
		
		  for (String userId : userIds) {
	            System.out.println(consistentHash.get(userId));
	        }
		  
		  //removed 3rd servers and all its replicas
		  consistentHash.remove(nodes.get(2));

		  //should not be server 3.
		  System.out.println(consistentHash.get("-124324565777"));
	}

	@Override
	public String toString() {
		return "ConsistentHashing [numberOfReplicas=" + numberOfReplicas + ", md=" + md + ", hashes=" + hashes + "]";
	}
	
	
	
}
