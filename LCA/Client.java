package LCA;

/**
 * LCA������������㷨������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//�ڵ������ļ�
		String dataFilePath = "C:\\Users\\lyq\\Desktop\\icon\\dataFile.txt";
		//��ѯ���������ļ�
		String queryFilePath = "C:\\Users\\lyq\\Desktop\\icon\\queryFile.txt";
		
		LCATool tool = new LCATool(dataFilePath, queryFilePath);
		tool.executeOfflineQuery();
	}
}
