package TopK;

/**
 * ��ѯ��
 * @author lyq
 *
 */
public class Query implements Comparable<Query>{
	//��ѯ����
	Integer count;
	//��ѯ��
	String word;
	
	public Query(int count, String word){
		this.count = count;
		this.word = word;
	}

	@Override
	public int compareTo(Query o) {
		// TODO Auto-generated method stub
		return o.count.compareTo(this.count);
	}
	
	
}
