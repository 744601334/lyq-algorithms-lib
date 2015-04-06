package BloomFilter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * BloomFileter��¡������������
 * 
 * @author lyq
 * 
 */
public class Client {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\realSample.txt";
		String testFilePath = "C:\\Users\\lyq\\Desktop\\icon\\testInput.txt";
		// �ܵĲ�ѯ����
		int totalCount;
		// ��ȷ�Ľ����
		int rightCount;
		long startTime = 0;
		long endTime = 0;
		// ��¡��������ѯ���
		Map<String, Boolean> bfMap;
		// ��ͨ��������ѯ���
		Map<String, Boolean> nfMap;
		//��ѯ������
		ArrayList<String> queryDatas;

		BloomFilterTool tool = new BloomFilterTool(filePath, testFilePath);

		// ���ò�¡�������ķ�ʽ���дʵĲ�ѯ
		startTime = System.currentTimeMillis();
		bfMap = tool.queryDatasByBF();
		endTime = System.currentTimeMillis();
		System.out.println("BloomFilter�㷨��ʱ" + (endTime - startTime) + "ms");

		// ������ͨ�������ķ�ʽ���дʵĲ�ѯ
		startTime = System.currentTimeMillis();
		nfMap = tool.queryDatasByNF();
		endTime = System.currentTimeMillis();
		System.out.println("��ͨ������ѯ������ʱ" + (endTime - startTime) + "ms");

		boolean isExist;
		boolean isExist2;

		rightCount = 0;
		queryDatas = tool.getQueryDatas();
		totalCount = queryDatas.size();
		for (String qWord: queryDatas) {
			// �Ա����Ĳ�ѯ�Ľ����Ϊ��׼���
			isExist = nfMap.get(qWord);
			isExist2 = bfMap.get(qWord);

			if (isExist == isExist2) {
				rightCount++;
			}else{
				System.out.println("Ԥ�д���Ĵ��" + qWord);
			}
		}
		System.out.println(MessageFormat.format(
				"Bloom Filter����ȷ����Ϊ{0}���ܲ�ѯ��Ϊ{1}������ȷ��{2}", rightCount,
				totalCount, 1.0 * rightCount / totalCount));
	}
}
