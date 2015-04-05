package InvertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ĵ�Ԥ��������
 * 
 * @author lyq
 * 
 */
public class PreTreatTool {
	// һЩ�޾�������Ĺ��˴�
	public static String[] FILTER_WORDS = new String[] { "at", "At", "The",
			"the", "is", "very" };

	// �����ĵ����ļ���ַ
	private ArrayList<String> docFilePaths;
	// �������Ч�ʵĴ��·��
	private ArrayList<String> effectWordPaths;

	public PreTreatTool(ArrayList<String> docFilePaths) {
		this.docFilePaths = docFilePaths;
	}

	/**
	 * ��ȡ�ĵ���Ч���ļ�·��
	 * 
	 * @return
	 */
	public ArrayList<String> getEFWPaths() {
		return this.effectWordPaths;
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
				words.add(word);
			}
		}

		return words;
	}

	/**
	 * ���ĵ����ݴʻ����Ԥ����
	 */
	public void preTreatWords() {
		String baseOutputPath = "";
		int endPos = 0;
		ArrayList<String> tempWords = null;
		effectWordPaths = new ArrayList<>();

		for (String filePath : docFilePaths) {
			tempWords = readDataFile(filePath);
			filterWords(tempWords, true);

			// ������װ���µ����·��
			endPos = filePath.lastIndexOf(".");
			baseOutputPath = filePath.substring(0, endPos);

			writeOutOperation(tempWords, baseOutputPath + "-efword.txt");
			effectWordPaths.add(baseOutputPath + "-efword.txt");
		}
	}

	/**
	 * 
	 * ���ĵ��еĴ�����й��˲���
	 * 
	 * @param words
	 *            �������ĵ�����
	 * @param canRepeated
	 *            ��Ч���Ƿ�����ظ�
	 */
	private void filterWords(ArrayList<String> words, boolean canRepeated) {
		boolean isFilterWord;
		// �����ݴ�ƥ��
		Pattern adjPattern;
		// ������ʱ̬��ƥ��
		Pattern formerPattern;
		// ����ƥ��
		Pattern numberPattern;
		Matcher adjMatcher;
		Matcher formerMatcher;
		Matcher numberMatcher;
		ArrayList<String> deleteWords = new ArrayList<>();

		adjPattern = Pattern.compile(".*(ly$|ful$|ing$)");
		formerPattern = Pattern.compile(".*ed$");
		numberPattern = Pattern.compile("[0-9]+(.[0-9]+)?");

		String w;
		for (int i = 0; i < words.size(); i++) {
			w = words.get(i);
			isFilterWord = false;

			for (String fw : FILTER_WORDS) {
				if (fw.equals(w)) {
					deleteWords.add(w);
					isFilterWord = true;
					break;
				}
			}

			if (isFilterWord) {
				continue;
			}

			adjMatcher = adjPattern.matcher(w);
			formerMatcher = formerPattern.matcher(w);
			numberMatcher = numberPattern.matcher(w);

			// ������ͳһСд��ĸ��
			w = w.toLowerCase();

			// ��������ݴ�,������ʽ�Ļ��Ǵ����ֵĴʣ�����й���
			if (adjMatcher.matches() || numberMatcher.matches()) {
				deleteWords.add(w);
			} else if (formerMatcher.matches()) {
				// �����ed��β�����Ƕ��ʵ���ʱ̬����ı仯�����б仯��תΪԭ�ж��ʵ���ʽ����ȥ��ĩβ2��������ӵĺ�׺��
				w = w.substring(0, w.length() - 2);
			}
			
			words.set(i, w);
		}

		// ������Ч�ʵĹ���
		words.removeAll(deleteWords);
		deleteWords.clear();

		String s1;
		String s2;

		// ���д����ȥ��
		for (int i = 0; i < words.size() - 1; i++) {
			s1 = words.get(i);

			for (int j = i + 1; j < words.size(); j++) {
				s2 = words.get(j);

				// �ҵ�������ͬ�Ĵ��ˣ�������ѭ��
				if (s1.equals(s2)) {
					deleteWords.add(s1);
					break;
				}
			}
		}

		// ɾ�������ظ��Ĵ���
		words.removeAll(deleteWords);
		words.addAll(deleteWords);
	}

	/**
	 * ������д���������ļ�����������ļ��Ѿ����ڣ������ļ�β����������׷��
	 * 
	 * @param buffer
	 *            ��ǰд�����е�����
	 * @param filePath
	 *            �����ַ
	 */
	private void writeOutOperation(ArrayList<String> buffer, String filePath) {
		StringBuilder strBuilder = new StringBuilder();

		// �������е���������ַ�д�뵽�ļ���
		for (String word : buffer) {
			strBuilder.append(word);
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

}
