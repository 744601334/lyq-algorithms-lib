package Trie;

import java.util.ArrayList;

/**
 * 
 * 
 * 
 * @author lyq
 * 
 * 
 */
public class TreeNode {
	//�ڵ��ֵ
	String value;
	//�ڵ㺢�ӽڵ�
	ArrayList<TreeNode> childNodes;

	public TreeNode(String value) {
		this.value = value;
		this.childNodes = new ArrayList<TreeNode>();
	}

	public ArrayList<TreeNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ArrayList<TreeNode> childNodes) {
		this.childNodes = childNodes;
	}
}
