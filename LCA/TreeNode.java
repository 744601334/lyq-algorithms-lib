package LCA;

/**
 * �������
 * @author lyq
 *
 */
public class TreeNode {
	//�����ֵ
	int value;
	//���ӽڵ㣬��һ��ֻ��2���ڵ�
	TreeNode[] childNodes;
	
	public TreeNode(){
		
	}
	
	public TreeNode(int value){
		this.value = value;
	}
	
	public TreeNode(String value){
		this.value = Integer.parseInt(value);
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public TreeNode[] getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(TreeNode[] childNodes) {
		this.childNodes = childNodes;
	}
}
