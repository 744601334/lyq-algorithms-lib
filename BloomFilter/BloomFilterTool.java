package BloomFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * ��¡�������㷨������
 * 
 * @author lyq
 * 
 */
public class BloomFilterTool {
	// λ��������Ϊ10wλ�ĳ���
	public static final int BIT_ARRAY_LENGTH = 100000;

	// ԭʼ�ĵ���ַ
	private String filePath;
	// �����ĵ���ַ
	private String testFilePath;
	// ���ڴ洢��λ����,һ����Ԫ��1��λ�洢
	private BitSet bitStore;
	// ԭʼ����
	private ArrayList<String> totalDatas;
	// ���ԵĲ�ѯ����
	private ArrayList<String> queryDatas;

	public BloomFilterTool(String filePath, String testFilePath) {
		this.filePath = filePath;
		this.testFilePath = testFilePath;

		this.totalDatas = readDataFile(this.filePath);
		this.queryDatas = readDataFile(this.testFilePath);
	}

	/**
	 * ���ļ��ж�ȡ����
	 */
	public ArrayList<String> readDataFile(String path) {
		File file = new File(path);
		ArrayList<String> dataArray = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				for(String word: tempArray){
					dataArray.add(word);
				}
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		return dataArray;
	}
	
	/**
	 * ��ȡ��ѯ������
	 * @return
	 */
	public ArrayList<String> getQueryDatas(){
		return this.queryDatas;
	}

	/**
	 * ��λ�洢����
	 */
	private void bitStoreData() {
		long hashcode = 0;
		bitStore = new BitSet(BIT_ARRAY_LENGTH);

		for (String word : totalDatas) {
			// ��ÿ���ʽ���3�ι�ϣ��ֵ�����ٹ�ϣ��ͻ�ĸ���
			hashcode = BKDRHash(word);
			hashcode %= BIT_ARRAY_LENGTH;

			
			bitStore.set((int) hashcode, true);

			hashcode = SDBMHash(word);
			hashcode %= BIT_ARRAY_LENGTH;

			bitStore.set((int) hashcode, true);

			hashcode = DJBHash(word);
			hashcode %= BIT_ARRAY_LENGTH;

			bitStore.set((int) hashcode, true);
		}
	}

	/**
	 * �������ݵĲ�ѯ���ж�ԭ�������Ƿ����Ŀ���ѯ����
	 */
	public Map<String, Boolean> queryDatasByBF() {
		boolean isExist;
		long hashcode;
		int pos1;
		int pos2;
		int pos3;
		// ��ѯ�ʵ��������ͼ
		Map<String, Boolean> word2exist = new HashMap<String, Boolean>();

		hashcode = 0;
		isExist = false;
		bitStoreData();
		for (String word : queryDatas) {
			isExist = false;
			
			hashcode = BKDRHash(word);
			pos1 = (int) (hashcode % BIT_ARRAY_LENGTH);

			hashcode = SDBMHash(word);
			pos2 = (int) (hashcode % BIT_ARRAY_LENGTH);

			hashcode = DJBHash(word);
			pos3 = (int) (hashcode % BIT_ARRAY_LENGTH);

			// ֻ����3����ϣλ�ö����ڲ�����Ĵ���
			if (bitStore.get(pos1) && bitStore.get(pos2) && bitStore.get(pos3)) {
				isExist = true;
			}

			// ���������map
			word2exist.put(word, isExist);
		}

		return word2exist;
	}

	/**
	 * �������ݵĲ�ѯ������ͨ�Ĺ�������ʽ���ǣ������ѯ
	 */
	public Map<String, Boolean> queryDatasByNF() {
		boolean isExist = false;
		// ��ѯ�ʵ��������ͼ
		Map<String, Boolean> word2exist = new HashMap<String, Boolean>();

		// �����ķ�ʽȥ����
		for (String qWord : queryDatas) {
			isExist = false;
			for (String word : totalDatas) {
				if (qWord.equals(word)) {
					isExist = true;
					break;
				}
			}

			word2exist.put(qWord, isExist);
		}

		return word2exist;
	}

	/**
	 * BKDR�ַ���ϣ�㷨
	 * 
	 * @param str
	 * @return
	 */
	private long BKDRHash(String str) {
		int seed = 31; /* 31 131 1313 13131 131313 etc.. */
		long hash = 0;
		int i = 0;

		for (i = 0; i < str.length(); i++) {
			hash = (hash * seed) + (str.charAt(i));
		}

		hash = Math.abs(hash);
		return hash;
	}

	/**
	 * SDB�ַ���ϣ�㷨
	 * 
	 * @param str
	 * @return
	 */
	private long SDBMHash(String str) {
		long hash = 0;
		int i = 0;
		
		for (i = 0; i < str.length(); i++) {
			hash = (str.charAt(i)) + (hash << 6) + (hash << 16) - hash;
		}

		hash = Math.abs(hash);
		return hash;
	}

	/**
	 * DJB�ַ���ϣ�㷨
	 * 
	 * @param str
	 * @return
	 */
	private long DJBHash(String str) {
		long hash = 5381;
		int i = 0;

		for (i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + hash) + (str.charAt(i));
		}

		hash = Math.abs(hash);
		return hash;
	}

}
