package Simhash;

import java.util.HashMap;
import java.util.Map;

/**
 * ����ʵ����
 * 
 * @author lyq
 * 
 */
public class News {
	// ���ž�������
	String content;
	// ���Ű����Ĵʵĸ���ͳ��ֵ
	HashMap<String, Double> word2Count;

	public News(String content) {
		this.content = content;
		this.word2Count = new HashMap<String, Double>();
	}

	/**
	 * ���ִʺ���ַ������йؼ��ʴ���ͳ��
	 */
	public void statWords() {
		int index;
		int invalidCount;
		double count;
		// ��Ƶ
		double wordRate;
		String w;
		String[] array;

		invalidCount = 0;
		array = this.content.split(" ");
		for (String str : array) {
			index = str.indexOf('/');
			if (index == -1) {
				continue;
			}
			w = str.substring(0, index);

			// ֻ���˵�����/wn������,
			if (str.contains("wn") || str.contains("wd")) {
				invalidCount++;
				continue;
			}

			count = 0;
			if (this.word2Count.containsKey(w)) {
				count = this.word2Count.get(w);
			}

			// �������ĸ���
			count++;
			this.word2Count.put(w, count);
		}

		// �����ܴ���ļ�¼����
		for (Map.Entry<String, Double> entry : this.word2Count.entrySet()) {
			w = entry.getKey();
			count = entry.getValue();

			wordRate = 1.0 * count / (array.length - invalidCount);
			this.word2Count.put(w, wordRate);
		}
	}

	/**
	 * ���ݴ������ƻ�ȡ��Ƶ
	 * 
	 * @param word
	 *            �ʵ�����
	 * 
	 */
	public double getWordFrequentValue(String word) {
		if(this.word2Count.containsKey(word)){
			return this.word2Count.get(word);
		}else{
			return -1;
		}
	}
}
