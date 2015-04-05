package InvertedIndex;

import java.util.ArrayList;

/**
 * �ĵ���
 * @author lyq
 *
 */
public class Document {
	//�ĵ���Ψһ��ʶ
	int docId;
	//�ĵ����ļ���ַ
	String filePath;
	//�ĵ��е���Ч��
	ArrayList<String> effectWords;
	
	public Document(ArrayList<String> effectWords, String filePath){
		this.effectWords = effectWords;
		this.filePath = filePath;
	}
	
	public Document(ArrayList<String> effectWords, String filePath, int docId){
		this(effectWords, filePath);
		this.docId = docId;
	}
}
