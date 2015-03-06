package LZW;

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
 * LZW��ѹ���㷨������
 * 
 * @author lyq
 * 
 */
public class LZWTool {
	// ��ʼ�ı���ı���Ŵ�256��ʼ
	public static int LZW_CODED_NUM = 256;

	// ��ѹ���ļ���ַ
	private String srcFilePath;
	// Ŀ���ļ���ַ
	private String desFileLoc;
	// ѹ�����Ŀ���ļ���
	private String desFileName;
	// ����ַ�������д������ļ���
	private String resultStr;
	// �������ӳ���
	HashMap<WordFix, Integer> word2Code;
	// Դ�ļ�����
	private ArrayList<String> totalDatas;

	public LZWTool(String srcFilePath, String desFileLoc, String desFileName) {
		this.srcFilePath = srcFilePath;
		this.desFileLoc = desFileLoc;
		this.desFileName = desFileName;

		word2Code = new HashMap<>();
		totalDatas = new ArrayList<>();
		readDataFile(totalDatas);
	}

	/**
	 * ���ļ��ж�ȡ����
	 * 
	 * @param inputData
	 *            ������������
	 */
	private void readDataFile(ArrayList<String> inputData) {
		File file = new File(srcFilePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = new String[str.length()];
				for (int i = 0; i < str.length(); i++) {
					tempArray[i] = str.charAt(i) + "";
				}

				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		System.out.print("ѹ��ǰ���ַ���");
		for (String[] array : dataArray) {
			for (String s : array) {
				inputData.add(s);
				System.out.print(s);
			}
		}
		System.out.println();
	}

	/**
	 * ����lzwѹ��
	 */
	public void compress() {
		resultStr = "";
		boolean existCoded = false;
		String prefix = totalDatas.get(0);
		WordFix wf = null;

		for (int i = 1; i < totalDatas.size(); i++) {
			wf = new WordFix(prefix, totalDatas.get(i), word2Code);
			existCoded = false;

			// �����ǰ���������Ӧ���룬����������׺
			while (wf.hasWordCode()) {
				i++;
				// ���������������ѭ��
				if (i == totalDatas.size()) {
					// ˵�������ڴ�������
					existCoded = true;
					wf.readSuffix("");
					break;
				}

				wf.readSuffix(totalDatas.get(i));
			}

			if (!existCoded) {
				// ��δ������Ĵ�����б���
				wf.wordFixCoded(LZW_CODED_NUM);
				LZW_CODED_NUM++;
			}

			// ��ǰ׺���
			resultStr += wf.getPrefix() + ",";
			// ��׺��ǰ׺
			prefix = wf.suffix;
		}

		// ��ԭ����ĺ�׺����Ҳ�����µĴ����ǰ׺
		resultStr += prefix;
		System.out.println("ѹ������ַ���" + resultStr);
		writeStringToFile(resultStr, desFileLoc + desFileName);
	}

	public void unCompress(String srcFilePath, String desFilePath) {
		String result = "";
		int code = 0;

		File file = new File(srcFilePath);
		ArrayList<String[]> datas = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(",");
				datas.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		for (String[] array : datas) {
			for (String s : array) {
				for (Map.Entry entry : word2Code.entrySet()) {
					code = (int) entry.getValue();
					if (s.equals(code + "")) {
						s = ((WordFix) entry.getKey()).transToStr();
						break;
					}
				}

				result += s;
			}
		}

		System.out.println("��ѹ����ַ���" + result);
		writeStringToFile(result, desFilePath);
	}

	/**
	 * д�ַ�����Ŀ���ļ���
	 * 
	 * @param resultStr
	 */
	public void writeStringToFile(String resultStr, String desFilePath) {
		try {
			File file = new File(desFilePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(resultStr);// ���ļ���д���ַ���
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
