package Simhash;

/**
 * ���ƹ�ϣ�㷨
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		//�����ƹ�ϣ����λ��
		int hashBitNum;
		//��ͬλ��ռ����С��ֵ
		double minRate;
		String newsPath1;
		String newsPath2;
		String newsPath3;
		SimHashTool tool;
		
		hashBitNum = 32;
		//������һ���λ��ֵ��ͬ
		minRate = 0.5;
		newsPath1 = "C:\\Users\\lyq\\Desktop\\icon\\test\\testNews1-split.txt";
		newsPath2 = "C:\\Users\\lyq\\Desktop\\icon\\test\\trainNews2-split.txt";
		newsPath3 = "C:\\Users\\lyq\\Desktop\\icon\\test\\trainNews1-split.txt";
		
		tool = new SimHashTool(hashBitNum, minRate);
		tool.compareArticals(newsPath1, newsPath2);
		tool.compareArticals(newsPath2, newsPath3);
	}
}
