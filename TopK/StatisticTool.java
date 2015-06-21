package TopK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ͳ�ƹ�����
 * 
 * @author lyq
 * 
 */
public class StatisticTool {
	// ��ϣ���Ų�ѯ���Լ���ѯ��
	public static int[] countMap;

	// query��ѯ�ļ���ַ
	private String filePath;
	// ��ϣ������
	private int mapCotainNum;
	// ��ѯ�ʼ�
	private ArrayList<String> queryWords;
	// ��Ų�ѯ�ʼ�����ֵ��
	private Map<String, Integer> query2Count;

	public StatisticTool(String filePath, int mapCotainNum) {
		this.filePath = filePath;
		this.mapCotainNum = mapCotainNum;
		
		//ִ�г�ʼ������
		initOperation();
		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ����
	 */
	private void readDataFile() {
		File file = new File(filePath);
		ArrayList<String> dataArray = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] array;
			
			while ((str = in.readLine()) != null) {
				array = str.split(" ");
				
				for(String s: array){
					dataArray.add(s);
				}
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		queryWords = dataArray;
	}

	/**
	 * ��ʼ����������ÿ�ν���ͳ�Ʋ���ǰ����
	 */
	public void initOperation() {
		this.countMap = new int[mapCotainNum];
		this.query2Count = new HashMap<String, Integer>();
	}

	/**
	 * ���ܲ�ѯ�ʽ���ð���������
	 */
	public String[] sortQuerys() {
		int k;
		String str1;
		String str2;
		String temp;
		String[] tempWords;

		tempWords = new String[queryWords.size()];
		queryWords.toArray(tempWords);

		// ͨ��ð������Բ�ѯ�ʽ�������
		for (int i = 0; i < tempWords.length - 1; i++) {
			k = i;

			for (int j = i + 1; j < tempWords.length; j++) {
				str1 = tempWords[k];
				str2 = tempWords[j];

				if (str1.compareTo(str2) > 0) {
					k = j;
				}
			}

			if (k != i) {
				temp = tempWords[i];
				tempWords[i] = tempWords[k];
				tempWords[k] = temp;
			}
		}

		return tempWords;
	}

	/**
	 * ͨ���ⲿ������㷨ʵ��ͳ��
	 */
	public void statisticBySort() {
		int count;
		//���Ĵ��Ƿ����
		boolean isEndSame;
		//��һ����
		String lastWord;
		String[] sortedWord;

		sortedWord = sortQuerys();

		lastWord = sortedWord[0];
		count = 0;
		isEndSame = false;
		
		this.query2Count.clear();
		// ��������ɨ��ͳ��
		for (String w : sortedWord) {
			// ������εĴʵ����ϴεĴʣ��������1
			if (w.equals(lastWord)) {
				count++;
				isEndSame = true;
			} else {
				// ���ϴεĴʴ���map
				query2Count.put(lastWord, count);
				
				//���ò���
				lastWord = w;
				count = 1;
				isEndSame = false;
			}
		}
		
		//������Ĵ�����ȵģ���ͳ�ƽⷨ����
		if(isEndSame){
			query2Count.put(lastWord, count);
		}
	}

	/**
	 * �ù�ϣ��ķ������в�ѯ�ʵ�ͳ�Ƽ���
	 */
	public void statisticByHash() {
		long pos;
		int count;

		count = 0;
		pos = -1;
		
		this.query2Count.clear();
		for (String word : queryWords) {
			pos = HashTool.BKDRHash(word);
			pos %= mapCotainNum;

			if (countMap[(int) pos] != 0) {
				countMap[(int) pos]++;
			} else {
				//countMap�е�����Ĭ��ֵΪ0
				countMap[(int) pos] = 1;
			}
		}

		// ��ͳ�ƽ������map�У����¸��׶�ʹ��
		for (String word : queryWords) {
			pos = HashTool.BKDRHash(word);
			pos %= mapCotainNum;

			count = countMap[(int) pos];
			// ֱ�Ӵ���map��
			query2Count.put(word, count);
		}
	}

	/**
	 * ��ȡ����ͼ
	 * @return
	 */
	public Map<String, Integer> getQuery2Count() {
		return this.query2Count;
	}

}
