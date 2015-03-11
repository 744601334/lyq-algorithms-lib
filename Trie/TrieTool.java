package Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * 
 * 
 * @author lyq
 * 
 * 
 */
public class TrieTool {
	// ���������ļ���ַ
	private String filePath;
	// ԭʼ����
	private ArrayList<String[]> datas;

	public TrieTool(String filePath) {
		this.filePath = filePath;
		readDataFile();
	}

	/**
	 * 
	 * ���ļ��ж�ȡ����
	 */
	private void readDataFile() {
		File file = new File(filePath);
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

		datas = dataArray;
	}

	/**
	 * 
	 * ����Trie��
	 * 
	 * 
	 * 
	 * @return
	 */
	public TreeNode constructTrieTree() {
		TreeNode rootNode = new TreeNode(null);
		ArrayList<String> tempStr;

		for (String[] array : datas) {
			tempStr = new ArrayList<String>();

			for (String s : array) {
				tempStr.add(s);
			}

			// ����ַ��������
			addStrToTree(rootNode, tempStr);
		}

		return rootNode;
	}

	/**
	 * 
	 * ����ַ��������ݵ�Trie����
	 * 
	 * 
	 * 
	 * @param node
	 * 
	 * @param strArray
	 */
	private void addStrToTree(TreeNode node, ArrayList<String> strArray) {
		boolean hasValue = false;
		TreeNode tempNode;
		TreeNode currentNode = null;

		// �ӽڵ��б���Ѱ���뵱ǰ��һ���ַ���Ӧ�Ľڵ�
		for (TreeNode childNode : node.childNodes) {
			if (childNode.value.equals(strArray.get(0))) {
				hasValue = true;
				currentNode = childNode;
				break;
			}

		}

		// ���û���ҵ���Ӧ�ڵ㣬�򽫴˽ڵ���Ϊ�µĽڵ�
		if (!hasValue) {
			// ��������δ�����ڵ��ַ�ֵ�ģ����¼��ڵ���Ϊ��ǰ�ڵ���ӽڵ�
			tempNode = new TreeNode(strArray.get(0));
			// node.childNodes.add(tempNode);
			insertNode(node.childNodes, tempNode);
			currentNode = tempNode;
		}
		strArray.remove(0);

		// ����ַ��Ѿ�ȫ��������ϣ�������ѭ��
		if (strArray.size() == 0) {
			return;
		} else {
			addStrToTree(currentNode, strArray);
		}
	}

	/**
	 * 
	 * ���½��Ľڵ㰴����ĸ�����˳����뵽���ӽڵ���
	 * 
	 * 
	 * 
	 * @param childNodes
	 * 
	 *            ���ӽڵ�
	 * 
	 * @param node
	 * 
	 *            �¼��Ĵ�����Ľڵ�
	 */
	private void insertNode(ArrayList<TreeNode> childNodes, TreeNode node) {
		String value = node.value;
		int insertIndex = 0;

		for (int i = 0; i < childNodes.size() - 1; i++) {
			if (childNodes.get(i).value.compareTo(value) <= 0
					&& childNodes.get(i + 1).value.compareTo(value) > 0) {
				insertIndex = i + 1;
				break;
			}
		}

		if (childNodes.size() == 0) {
			childNodes.add(node);
		} else if (childNodes.size() == 1) {
			// ֻ��1������������ж�
			if (childNodes.get(0).value.compareTo(value) > 0) {
				childNodes.add(0, node);
			} else {
				childNodes.add(node);
			}
		} else {
			childNodes.add(insertIndex, node);
		}

	}

}
