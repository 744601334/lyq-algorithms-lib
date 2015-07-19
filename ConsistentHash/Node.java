package ConsistentHash;

/**
 * �ڵ���
 * @author lyq
 *
 */
public class Node implements Comparable<Node>{
	//�ڵ�����
	String name;
	//������IP��ַ
	String ip;
	//�ڵ��hashֵ
	Long hashValue;
	
	public Node(String name, String ip, long hashVaule){
		this.name = name;
		this.ip = ip;
		this.hashValue = hashVaule;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.hashValue.compareTo(o.hashValue);
	}
}
