package LZW;

/**
 * LZW��ѹ���㷨
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//Դ�ļ���ַ
		String srcFilePath = "C:\\Users\\lyq\\Desktop\\icon\\srcFile.txt";
		//ѹ������ļ���
		String desFileName = "compressedFile.txt";
		//ѹ���ļ���λ��
		String desFileLoc = "C:\\Users\\lyq\\Desktop\\icon\\";
		//��ѹ����ļ���
		String unCompressedFilePath = "C:\\Users\\lyq\\Desktop\\icon\\unCompressedFile.txt";
		
		LZWTool tool = new LZWTool(srcFilePath, desFileLoc, desFileName);
		//ѹ���ļ�
		tool.compress();
		
		//��ѹ�ļ�
		tool.unCompress(desFileLoc + desFileName, unCompressedFilePath);
	}
}
