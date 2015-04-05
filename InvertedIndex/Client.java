package InvertedIndex;

import java.util.ArrayList;

/**
 * ��������������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//��д�������Ĵ�С
		int readBufferSize;
		int writeBufferSize;
		String baseFilePath;
		PreTreatTool preTool;
		//BSBI���ڴ��̵��ⲿ�����㷨
		BSBITool bTool;
		//SPIMI�ڴ�ʽ����ɨ�蹹���㷨
		SPIMITool sTool;
		//��Ч���ļ�·��
		ArrayList<String> efwFilePaths;
		ArrayList<String> docFilePaths;
		
		readBufferSize = 10;
		writeBufferSize = 20;
		baseFilePath = "C:\\Users\\lyq\\Desktop\\icon\\";
		docFilePaths = new ArrayList<>();
		docFilePaths.add(baseFilePath + "doc1.txt");
		docFilePaths.add(baseFilePath + "doc2.txt");
		
		//�ĵ�Ԥ��������
		preTool = new PreTreatTool(docFilePaths);
		preTool.preTreatWords();
		
		//Ԥ�������ȡ��Ч���ļ�·��
		efwFilePaths = preTool.getEFWPaths();
		bTool = new BSBITool(efwFilePaths, readBufferSize, writeBufferSize);
		bTool.outputInvertedFiles();
		
		sTool = new SPIMITool(efwFilePaths);
		sTool.createInvertedIndexFile();
	}
}
