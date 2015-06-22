package TopK;

import java.util.Map;

/**
 * TopK����������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//���������ļ�·��
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		int mapCotainNum;
		int k;
		StatisticTool statTool;
		SelectTool selectTool;
		Map<String, Integer> countMap;
		
		//��ϣ��������С1W��
		mapCotainNum = 10000;
		k = 7;
		statTool = new StatisticTool(filePath, mapCotainNum);
		
		statTool.statisticBySort();
		statTool.statisticByHash();
		countMap = statTool.getQuery2Count();
		
		selectTool = new SelectTool(k, countMap);
		System.out.println("��ͨ�����㷨ʵ��TopK");
		selectTool.selectTopKBySort();
		System.out.println();
		
		System.out.println("�������㷨ʵ��TopK");
		selectTool.selectTopKByMaxHeap();
	}
}
