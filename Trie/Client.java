package Trie;

/**
 * 
 * Trie���㷨
 * 
 * @author lyq
 * 
 * 
 */
public class Client {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\lyq\\Desktop\\icon\\input.txt";
		
		TrieTool tool = new TrieTool(filePath);
		tool.constructTrieTree();
	}
}
