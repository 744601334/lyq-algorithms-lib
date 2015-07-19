package ConsistentHash;

import java.util.ArrayList;

/**
 * һ���Թ�ϣ�㷨������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//��������
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		int virtualNodeNum;
		ArrayList<Entity> testEntities;
		ConsistentHashTool tool;
		
		virtualNodeNum = 3;
		testEntities = new ArrayList<>();
		testEntities.add(new Entity("ZhangSan", 100));
		testEntities.add(new Entity("LiSi", 200));
		testEntities.add(new Entity("WangWu", 300));
		
		tool = new ConsistentHashTool(filePath, virtualNodeNum, testEntities);
		System.out.println("����һ���Թ�ϣʵ�ַ���");
		tool.hashAssigned();
		System.out.println("\n��������ڵ�һ���Թ�ϣʵ�ַ���");
		tool.hashAssignedByVirtualNode();
	}
}
