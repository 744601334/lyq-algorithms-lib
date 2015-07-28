package Simhash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * ���ƹ�ϣ�㷨������
 * 
 * @author lyq
 * 
 */
public class SimHashTool {
	// �����ƹ�ϣλ��
	private int hashBitNum;
	// ��ͬλ����С��ֵ
	private double minSupportValue;

	public SimHashTool(int hashBitNum, double minSupportValue) {
		this.hashBitNum = hashBitNum;
		this.minSupportValue = minSupportValue;
	}

	/**
	 * �Ƚ����µ����ƶ�
	 * 
	 * @param news1
	 *            ����·��1
	 * @param news2
	 *            ����·��2
	 */
	public void compareArticals(String newsPath1, String newsPath2) {
		String content1;
		String content2;
		int sameNum;
		int[] hashArray1;
		int[] hashArray2;


		// ��ȡ�ִʽ��
		content1 = readDataFile(newsPath1);
		content2 = readDataFile(newsPath2);
		hashArray1 = calSimHashValue(content1);
		hashArray2 = calSimHashValue(content2);

		// �ȽϹ�ϣλ����ͬ����
		sameNum = 0;
		for (int i = 0; i < hashBitNum; i++) {
			if (hashArray1[i] == hashArray2[i]) {
				sameNum++;
			}
		}

		// ����С��ֵ���бȽ�
		if (sameNum > this.hashBitNum * this.minSupportValue) {
			System.out.println(String.format("���ƶ�Ϊ%s,������ֵ%s,��������1������2�����Ƶ�",
					sameNum * 1.0 / hashBitNum, minSupportValue));
		} else {
			System.out.println(String.format("���ƶ�Ϊ%s,С����ֵ%s,��������1������2�������Ƶ�",
					sameNum * 1.0 / hashBitNum, minSupportValue));
		}
	}

	/**
	 * �����ı������ƹ�ϣֵ
	 * 
	 * @param content
	 *            ������������
	 * @return
	 */
	private int[] calSimHashValue(String content) {
		int index;
		long hashValue;
		double weight;
		int[] binaryArray;
		int[] resultValue;
		double[] hashArray;
		String w;
		String[] words;
		News news;

		news = new News(content);
		news.statWords();
		hashArray = new double[hashBitNum];
		resultValue = new int[hashBitNum];

		words = content.split(" ");
		for (String str : words) {
			index = str.indexOf('/');
			if (index == -1) {
				continue;
			}
			w = str.substring(0, index);
			
			// ��ȡȨ��ֵ�����ݴ�Ƶ����
			weight = news.getWordFrequentValue(w);
			if(weight == -1){
				continue;
			}
			// ���й�ϣֵ�ļ���
			hashValue = BKDRHash(w);
			// ȡ���λ����Ϊnλ
			hashValue %= Math.pow(2, hashBitNum);

			// תΪ�����Ƶ���ʽ
			binaryArray = new int[hashBitNum];
			numToBinaryArray(binaryArray, (int) hashValue);

			for (int i = 0; i < binaryArray.length; i++) {
				// �����λ����Ϊ1����Ȩ��
				if (binaryArray[i] == 1) {
					hashArray[i] += weight;
				} else {
					// Ϊ0���Ȩ�ز���
					hashArray[i] -= weight;
				}
			}
		}

		// ����������������������ֵ�������ţ����¸�Ϊ������������ʽ
		for (int i = 0; i < hashArray.length; i++) {
			if (hashArray[i] > 0) {
				resultValue[i] = 1;
			} else {
				resultValue[i] = 0;
			}
		}

		return resultValue;
	}

	/**
	 * ����תΪ��������ʽ
	 * 
	 * @param binaryArray
	 *            ת����Ķ�����������ʽ
	 * @param num
	 *            ��ת������
	 */
	private void numToBinaryArray(int[] binaryArray, int num) {
		int index = 0;
		int temp = 0;
		while (num != 0) {
			binaryArray[index] = num % 2;
			index++;
			num /= 2;
		}

		// ��������ǰ��β���ĵ���
		for (int i = 0; i < binaryArray.length / 2; i++) {
			temp = binaryArray[i];
			binaryArray[i] = binaryArray[binaryArray.length - 1 - i];
			binaryArray[binaryArray.length - 1 - i] = temp;
		}
	}

	/**
	 * BKDR�ַ���ϣ�㷨
	 * 
	 * @param str
	 * @return
	 */
	public static long BKDRHash(String str) {
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
	 * ���ļ��ж�ȡ����
	 */
	private String readDataFile(String filePath) {
		File file = new File(filePath);
		StringBuilder strBuilder = null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			strBuilder = new StringBuilder();
			while ((str = in.readLine()) != null) {
				strBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		return strBuilder.toString();
	}

	/**
	 * ���÷ִ�ϵͳ�����������ݵķִ�
	 * 
	 * @param srcPath
	 *            �����ļ�·��
	 */
	private void parseNewsContent(String srcPath) {
		// TODO Auto-generated method stub
		int index;
		String dirApi;
		String desPath;

		dirApi = System.getProperty("user.dir") + "\\lib";
		// ��װ���·��ֵ
		index = srcPath.indexOf('.');
		desPath = srcPath.substring(0, index) + "-split.txt";

		try {
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			// �ִ�������·������ʼ��
			if (testICTCLAS50.ICTCLAS_Init(dirApi.getBytes("GB2312")) == false) {
				System.out.println("Init Fail!");
				return;
			}
			// ���ļ���string����תΪbyte����
			byte[] Inputfilenameb = srcPath.getBytes();

			// �ִʴ��������ļ��������ļ���string����תΪbyte����
			byte[] Outputfilenameb = desPath.getBytes();

			// �ļ��ִ�(��һ������Ϊ�����ļ�����,�ڶ�������Ϊ�ļ���������,����������Ϊ�Ƿ��Ǵ��Լ�1 yes,0
			// no,���ĸ�����Ϊ����ļ���)
			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 1,
					Outputfilenameb);
			// �˳��ִ���
			testICTCLAS50.ICTCLAS_Exit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
