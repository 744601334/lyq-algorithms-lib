package InvertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * BSBI���ڴ��̵��ⲿ�����㷨
 * 
 * @author lyq
 * 
 */
public class BSBITool {
	// �ĵ�Ψһ��ʶID
	public static int DOC_ID = 0;

	// ���������Ĵ�С
	private int readBufferSize;
	// д�������Ĵ�С
	private int writeBufferSize;
	// ������ĵ�����Ч���ļ���ַ
	private ArrayList<String> effectiveWordFiles;
	// ������������ļ���ַ
	private String outputFilePath;
	// ������ 1
	private String[][] readBuffer1;
	// ������2
	private String[][] readBuffer2;
	// д������
	private String[][] writeBuffer;
	// ��Ч����hashcode��ӳ��
	private Map<String, String> code2word;

	public BSBITool(ArrayList<String> effectiveWordFiles, int readBufferSize,
			int writeBufferSize) {
		this.effectiveWordFiles = effectiveWordFiles;
		this.readBufferSize = readBufferSize;
		this.writeBufferSize = writeBufferSize;

		initBuffers();
	}

	/**
	 * ��ʼ��������������
	 */
	private void initBuffers() {
		readBuffer1 = new String[readBufferSize][2];
		readBuffer2 = new String[readBufferSize][2];
		writeBuffer = new String[writeBufferSize][2];
	}

	/**
	 * ���ļ��ж�ȡ��Ч�ʲ����б����滻
	 * 
	 * @param filePath
	 *            �����ĵ�
	 */
	private Document readEffectWords(String filePath) {
		long hashcode = 0;

		String w;
		Document document;
		code2word = new HashMap<String, String>();
		ArrayList<String> words;

		words = readDataFile(filePath);

		for (int i = 0; i < words.size(); i++) {
			w = words.get(i);

			hashcode = BKDRHash(w);
			hashcode = hashcode % 10000;

			// ����Ч�ʵ�hashcodeȡģֵ��Ϊ��Ӧ�Ĵ���
			code2word.put(hashcode + "", w);
			w = hashcode + "";

			words.set(i, w);
		}

		document = new Document(words, filePath, DOC_ID);
		DOC_ID++;

		return document;
	}

	/**
	 * ���ַ�����ϣֵ��ת��
	 * 
	 * @param str
	 *            ��ת���ַ�
	 * @return
	 */
	private long BKDRHash(String str) {
		int seed = 31; /* 31 131 1313 13131 131313 etc.. */
		long hash = 0;
		int i = 0;

		for (i = 0; i < str.length(); i++) {
			hash = (hash * seed) + (str.charAt(i));
		}

		return hash;

	}

	/**
	 * �����������Ч��������������ļ�
	 */
	public void outputInvertedFiles() {
		int index = 0;
		String baseFilePath = "";
		outputFilePath = "";
		Document doc;
		ArrayList<String> tempPaths;
		ArrayList<String[]> invertedData1;
		ArrayList<String[]> invertedData2;

		tempPaths = new ArrayList<>();
		for (String filePath : effectiveWordFiles) {
			doc = readEffectWords(filePath);
			writeOutFile(doc);

			index = doc.filePath.lastIndexOf(".");
			baseFilePath = doc.filePath.substring(0, index);
			writeOutOperation(writeBuffer, baseFilePath + "-temp.txt");

			tempPaths.add(baseFilePath + "-temp.txt");
		}

		outputFilePath = baseFilePath + "-bsbi-inverted.txt";

		// ���м�����ĵ����������ݽ����ܵĺϲ��������һ���ļ���
		for (int i = 1; i < tempPaths.size(); i++) {
			if (i == 1) {
				invertedData1 = readInvertedFile(tempPaths.get(0));
			} else {
				invertedData1 = readInvertedFile(outputFilePath);
			}

			invertedData2 = readInvertedFile(tempPaths.get(i));

			mergeInvertedData(invertedData1, invertedData2, false,
					outputFilePath);

			writeOutOperation(writeBuffer, outputFilePath, false);
		}
	}

	/**
	 * ���ĵ������յĵ����������д�����ļ�
	 * 
	 * @param doc
	 *            �������ĵ�
	 */
	private void writeOutFile(Document doc) {
		// �ڶ����������Ƿ���Ҫ������
		boolean ifSort = true;
		int index = 0;
		String baseFilePath;
		String[] temp;
		ArrayList<String> tempWords = (ArrayList<String>) doc.effectWords
				.clone();
		ArrayList<String[]> invertedData1;
		ArrayList<String[]> invertedData2;

		invertedData1 = new ArrayList<>();
		invertedData2 = new ArrayList<>();

		// ���ĵ�������ƽ����ֳ�2�ݣ����ڶ�������2����������
		for (int i = 0; i < tempWords.size() / 2; i++) {
			temp = new String[2];
			temp[0] = tempWords.get(i);
			temp[1] = doc.docId + "";
			invertedData1.add(temp);

			temp = new String[2];
			temp[0] = tempWords.get(i + tempWords.size() / 2);
			temp[1] = doc.docId + "";
			invertedData2.add(temp);
		}

		// ������������������һ������
		if (tempWords.size() % 2 == 1) {
			temp = new String[2];
			temp[0] = tempWords.get(tempWords.size() - 1);
			temp[1] = doc.docId + "";
			invertedData2.add(temp);
		}

		index = doc.filePath.lastIndexOf(".");
		baseFilePath = doc.filePath.substring(0, index);
		mergeInvertedData(invertedData1, invertedData2, ifSort, baseFilePath
				+ "-temp.txt");
	}

	/**
	 * �ϲ�������������д��д�������У��õ��˹鲢�����㷨
	 * 
	 * @param outputPath
	 *            д��������д����·��
	 */
	private void mergeWordBuffers(String outputPath) {
		int i = 0;
		int j = 0;
		int num1 = 0;
		int num2 = 0;
		// д�������±�
		int writeIndex = 0;

		while (readBuffer1[i][0] != null && readBuffer2[j][0] != null) {
			num1 = Integer.parseInt(readBuffer1[i][0]);
			num2 = Integer.parseInt(readBuffer2[j][0]);

			// �������1С�������ȴ滺��1��д��������
			if (num1 < num2) {
				writeBuffer[writeIndex][0] = num1 + "";
				writeBuffer[writeIndex][1] = readBuffer1[i][1];

				i++;
			} else if (num2 < num1) {
				writeBuffer[writeIndex][0] = num2 + "";
				writeBuffer[writeIndex][1] = readBuffer1[j][1];

				j++;
			} else if (num1 == num2) {
				// ��������������е�����һ����˵����ͬ����Ч�ʣ��Ƚ��кϲ���д��
				writeBuffer[writeIndex][0] = num1 + "";
				writeBuffer[writeIndex][1] = readBuffer1[i][1] + ":"
						+ readBuffer2[j][1];

				i++;
				j++;
			}

			// д��ָ������Ųһλ
			writeIndex++;

			// ���д��д������ʱ������д�����ļ�����
			if (writeIndex >= writeBufferSize) {
				writeOutOperation(writeBuffer, outputPath);
				writeIndex = 0;
			}
		}

		if (readBuffer1[i][0] == null) {
			writeRemainReadBuffer(readBuffer2, j, outputPath);
		}

		if (readBuffer2[j][0] == null) {
			writeRemainReadBuffer(readBuffer1, j, outputPath);
		}
	}

	/**
	 * ������д���������ļ�����������ļ��Ѿ����ڣ������ļ�β����������׷��
	 * 
	 * @param buffer
	 *            ��ǰд�����е�����
	 * @param filePath
	 *            �����ַ
	 */
	private void writeOutOperation(String[][] buffer, String filePath) {
		String word;
		StringBuilder strBuilder = new StringBuilder();

		// �������е���������ַ�д�뵽�ļ���
		for (String[] array : buffer) {
			if (array[0] == null) {
				continue;
			}

			word = array[0];

			strBuilder.append(word);
			strBuilder.append(" ");
			strBuilder.append(array[1]);
			strBuilder.append("\n");
		}

		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.print(strBuilder.toString());// ���ļ���д���ַ���
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ������д���������ļ�����������ļ��Ѿ����ڣ������ļ�β����������׷��
	 * 
	 * @param buffer
	 *            ��ǰд�����е�����
	 * @param filePath
	 *            �����ַ
	 * @param isCoded
	 *            �Ƿ��Ա���ķ�ʽ���
	 */
	private void writeOutOperation(String[][] buffer, String filePath, boolean isCoded) {
		String word;
		StringBuilder strBuilder = new StringBuilder();

		// �������е���������ַ�д�뵽�ļ���
		for (String[] array : buffer) {
			if (array[0] == null) {
				continue;
			}

			if(!isCoded){
				word = code2word.get(array[0]);
			}else{
				word = array[0];
			}

			strBuilder.append(word);
			strBuilder.append(" ");
			strBuilder.append(array[1]);
			strBuilder.append("\n");
		}

		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.print(strBuilder.toString());// ���ļ���д���ַ���
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ʣ��Ķ��������е����ݶ���д��������
	 * 
	 * @param remainBuffer
	 *            ����������ʣ�໺��
	 * @param currentReadPos
	 *            ��ǰ�Ķ�ȡλ��
	 * @param outputPath
	 *            д��������д���ļ�·��
	 */
	private void writeRemainReadBuffer(String[][] remainBuffer,
			int currentReadPos, String outputPath) {
		while (remainBuffer[currentReadPos][0] != null
				&& currentReadPos < readBufferSize) {
			removeRBToWB(remainBuffer[currentReadPos]);

			currentReadPos++;

			// ���д��д������ʱ������д�����ļ�����
			if (writeBuffer[writeBufferSize - 1][0] != null) {
				writeOutOperation(writeBuffer, outputPath);
			}
		}

	}

	/**
	 * ��ʣ����������е�����ͨ����������ķ�ʽ����д������
	 * 
	 * @param record
	 */
	private void removeRBToWB(String[] record) {
		int insertIndex = 0;
		int endIndex = 0;
		long num1;
		long num2;
		long code = Long.parseLong(record[0]);

		// ���д������ĿǰΪ�գ���ֱ�Ӽ���
		if (writeBuffer[0][0] == null) {
			writeBuffer[0] = record;
			return;
		}

		// Ѱ�Ҵ������λ��
		for (int i = 0; i < writeBufferSize - 1; i++) {
			if (writeBuffer[i][0] == null) {
				endIndex = i;
				break;
			}

			num1 = Long.parseLong(writeBuffer[i][0]);

			if (writeBuffer[i + 1][0] == null) {
				if (code > num1) {
					endIndex = i + 1;
					insertIndex = i + 1;
				}
			} else {
				num2 = Long.parseLong(writeBuffer[i + 1][0]);

				if (code > num1 && code < num2) {
					insertIndex = i + 1;
				}
			}
		}

		// ���в��������������ݽ���λ��Ǩ��
		for (int i = endIndex; i > insertIndex; i--) {
			writeBuffer[i] = writeBuffer[i - 1];
		}
		writeBuffer[insertIndex] = record;
	}

	/**
	 * �������е�2�������������ݽ��кϲ�
	 * 
	 * @param invertedData1
	 *            ��������Ϊ�ļ�����1
	 * @param invertedData2
	 *            ���������ļ�����2
	 * @param isSort
	 *            �Ƿ���Ҫ�Ի������е����ݽ�������
	 * @param outputPath
	 *            ������������ļ���ַ
	 */
	private void mergeInvertedData(ArrayList<String[]> invertedData1,
			ArrayList<String[]> invertedData2, boolean ifSort, String outputPath) {
		int rIndex1 = 0;
		int rIndex2 = 0;

		// ���³�ʼ��������
		initBuffers();

		while (invertedData1.size() > 0 && invertedData2.size() > 0) {
			readBuffer1[rIndex1][0] = invertedData1.get(0)[0];
			readBuffer1[rIndex1][1] = invertedData1.get(0)[1];

			readBuffer2[rIndex2][0] = invertedData2.get(0)[0];
			readBuffer2[rIndex2][1] = invertedData2.get(0)[1];

			invertedData1.remove(0);
			invertedData2.remove(0);
			rIndex1++;
			rIndex2++;

			if (rIndex1 == readBufferSize) {
				if (ifSort) {
					wordBufferSort(readBuffer1);
					wordBufferSort(readBuffer2);
				}

				mergeWordBuffers(outputPath);
				initBuffers();
			}
		}

		if (ifSort) {
			wordBufferSort(readBuffer1);
			wordBufferSort(readBuffer2);
		}

		mergeWordBuffers(outputPath);
		readBuffer1 = new String[readBufferSize][2];
		readBuffer2 = new String[readBufferSize][2];

		if (invertedData1.size() == 0 && invertedData2.size() > 0) {
			readRemainDataToRB(invertedData2, outputPath);
		} else if (invertedData1.size() > 0 && invertedData2.size() == 0) {
			readRemainDataToRB(invertedData1, outputPath);
		}
	}

	/**
	 * ʣ�����Ч�����ݶ����������
	 * 
	 * @param remainData
	 *            ʣ������
	 * @param outputPath
	 *            ����ļ�·��
	 */
	private void readRemainDataToRB(ArrayList<String[]> remainData,
			String outputPath) {
		int rIndex = 0;
		while (remainData.size() > 0) {
			readBuffer1[rIndex][0] = remainData.get(0)[0];
			readBuffer1[rIndex][1] = remainData.get(0)[1];
			remainData.remove(0);

			rIndex++;

			// ������ ��д��������д�뵽д��������
			if (readBuffer1[readBufferSize - 1][0] != null) {
				wordBufferSort(readBuffer1);

				writeRemainReadBuffer(readBuffer1, 0, outputPath);
				initBuffers();
			}
		}

		wordBufferSort(readBuffer1);

		writeRemainReadBuffer(readBuffer1, 0, outputPath);

	}

	/**
	 * ���������ݽ�������
	 * 
	 * @param buffer
	 *            ����ռ�
	 */
	private void wordBufferSort(String[][] buffer) {
		String[] temp;
		int k = 0;

		long num1 = 0;
		long num2 = 0;
		for (int i = 0; i < buffer.length - 1; i++) {
			// ����������û����
			if (buffer[i][0] == null) {
				continue;
			}

			k = i;
			for (int j = i + 1; j < buffer.length; j++) {
				// ����������û����
				if (buffer[j][0] == null) {
					continue;
				}
				// ��ȡ2��������С�����ʼ���ֵ
				num1 = Long.parseLong(buffer[k][0]);
				num2 = Long.parseLong(buffer[j][0]);

				if (num2 < num1) {
					k = j;
				}
			}

			if (k != i) {
				temp = buffer[k];
				buffer[k] = buffer[i];
				buffer[i] = temp;
			}
		}
	}

	/**
	 * ���ļ��ж�ȡ������������
	 * 
	 * @param filePath
	 *            �����ļ�
	 */
	private ArrayList<String[]> readInvertedFile(String filePath) {
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		return dataArray;
	}

	/**
	 * ���ļ��ж�ȡ����
	 * 
	 * @param filePath
	 *            �����ļ�
	 */
	private ArrayList<String> readDataFile(String filePath) {
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		ArrayList<String> words = new ArrayList<>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		// ��ÿ�д�����ּ��뵽���б�������
		for (String[] array : dataArray) {
			for (String word : array) {
				if (!word.equals("")) {
					words.add(word);
				}
			}
		}

		return words;
	}
}
