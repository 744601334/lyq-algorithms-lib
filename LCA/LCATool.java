package LCA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * LCA������������㷨
 * 
 * @author lyq
 * 
 */
public class LCATool {
	// �ڵ������ļ�
	private String dataFilePath;
	// ��ѯ���������ļ�
	private String queryFilePath;
	// �ڵ����ȼ���,�����±��������Ӧ�Ľڵ㣬������Ϊ������ֵ
	private int[] ancestor;
	// ������飬����˽ڵ��Ƿ��Ѿ������ʹ�
	private boolean[] checked;
	// ����������
	private ArrayList<int[]> querys;
	// ������ֵ
	private int[][] resultValues;
	// ��ʼ����ֵ
	private ArrayList<String> totalDatas;

	public LCATool(String dataFilePath, String queryFilePath) {
		this.dataFilePath = dataFilePath;
		this.queryFilePath = queryFilePath;

		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ����
	 */
	private void readDataFile() {
		File file = new File(dataFilePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

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

		totalDatas = new ArrayList<>();
		for (String[] array : dataArray) {
			for (String s : array) {
				totalDatas.add(s);
			}
		}
		checked = new boolean[totalDatas.size() + 1];
		ancestor = new int[totalDatas.size() + 1];

		// ��ȡ��ѯ��������
		file = new File(queryFilePath);
		dataArray.clear();
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

		int x = 0;
		int y = 0;
		querys = new ArrayList<>();
		resultValues = new int[dataArray.size()][dataArray.size()];

		for (int i = 0; i < dataArray.size(); i++) {
			for (int j = 0; j < dataArray.size(); j++) {
				// ֵ-1����δ�����LCAֵ
				resultValues[i][j] = -1;
			}
		}

		for (String[] array : dataArray) {
			x = Integer.parseInt(array[0]);
			y = Integer.parseInt(array[1]);

			querys.add(new int[] { x, y });
		}

	}

	/**
	 * �������ṹ���˴�Ĭ�Ϲ���ɶ���������ʽ����ʵ�������ʵ��������Ҫ
	 * 
	 * @param rootNode
	 *            ���ڵ����
	 */
	private void createTree(TreeNode rootNode) {
		TreeNode tempNode;
		TreeNode[] nodeArray;
		ArrayList<String> dataCopy;
		LinkedBlockingQueue<TreeNode> nodeSeqs = new LinkedBlockingQueue<>();

		rootNode.setValue(Integer.parseInt(totalDatas.get(0)));
		dataCopy = (ArrayList<String>) totalDatas.clone();
		// �Ƴ����ڵ���׸�����ֵ
		dataCopy.remove(0);
		nodeSeqs.add(rootNode);

		while (!nodeSeqs.isEmpty()) {
			tempNode = nodeSeqs.poll();

			nodeArray = new TreeNode[2];
			if (dataCopy.size() > 0) {
				nodeArray[0] = new TreeNode(dataCopy.get(0));
				dataCopy.remove(0);
				nodeSeqs.add(nodeArray[0]);
			} else {
				tempNode.setChildNodes(nodeArray);
				break;
			}

			if (dataCopy.size() > 0) {
				nodeArray[1] = new TreeNode(dataCopy.get(0));
				dataCopy.remove(0);
				nodeSeqs.add(nodeArray[1]);
			} else {
				tempNode.setChildNodes(nodeArray);
				break;
			}

			tempNode.setChildNodes(nodeArray);
		}
	}

	/**
	 * ����lca������������㷨�ļ���
	 * 
	 * @param node
	 *            ��ǰ����Ľڵ�
	 */
	private void lcaCal(TreeNode node) {
		if (node == null) {
			return;
		}

		// �������Ĵ�ɾ�������б�
		ArrayList<int[]> deleteQuerys = new ArrayList<>();
		TreeNode[] childNodes;
		int value = node.value;
		ancestor[value] = value;

		childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (TreeNode n : childNodes) {
				lcaCal(n);

				// ������ȱ�����ɣ�������������ֵ
				value = node.value;
				//ͨ�����ͽṹ�������ȵ����÷�ʽ���������
				// setNodeAncestor(n, value);
				if(n != null){
					//�ϲ�2������
					unionSet(n.value, value);
				}
			}
		}

		// ��Ǵ˵㱻���ʹ�
		checked[node.value] = true;
		int[] queryArray;
		for (int i = 0; i < querys.size(); i++) {
			queryArray = querys.get(i);

			if (queryArray[0] == node.value) {
				// �����ʱ��һ���Ѿ������ʹ�
				if (checked[queryArray[1]]) {
					resultValues[queryArray[0]][queryArray[1]] = findSet(queryArray[1]);

					System.out.println(MessageFormat.format(
							"�ڵ�{0}��{1}�������������Ϊ{2}", queryArray[0],
							queryArray[1],
							resultValues[queryArray[0]][queryArray[1]]));

					deleteQuerys.add(querys.get(i));
				}
			} else if (queryArray[1] == node.value) {
				// �����ʱ��һ���Ѿ������ʹ�
				if (checked[queryArray[0]]) {
					resultValues[queryArray[0]][queryArray[1]] = findSet(queryArray[0]);

					System.out.println(MessageFormat.format(
							"�ڵ�{0}��{1}�������������Ϊ{2}", queryArray[0],
							queryArray[1],
							resultValues[queryArray[0]][queryArray[1]]));
					deleteQuerys.add(querys.get(i));
				}
			}
		}

		querys.removeAll(deleteQuerys);
	}

	/**
	 * Ѱ�ҽڵ�x�����ĸ����ϣ�����Ѱ��x�����������
	 * 
	 * @param x
	 */
	private int findSet(int x) {
		// ������Ȳ����Լ�������������׽ڵ�Ѱ��
		if (x != ancestor[x]) {
			ancestor[x] = findSet(ancestor[x]);
		}

		return ancestor[x];
	}

	/**
	 * ������x�������Ϻϲ���y������
	 * 
	 * @param x
	 * @param y
	 */
	public void unionSet(int x, int y) {
		// �ҵ�x��y�ڵ������
		int ax = findSet(x);
		int ay = findSet(y);

		// ���2��������ͬһ�������ʾ��ͬһ�㣬ֱ�ӷ���
		if (ax != ay) {
			// ax�ĸ���ָ��y�ڵ������ay
			ancestor[ax] = ay;
		}
	}

	/**
	 * ���ýڵ������ֵ
	 * 
	 * @param node
	 *            �����ýڵ�
	 * @param value
	 *            Ŀ��ֵ
	 */
	private void setNodeAncestor(TreeNode node, int value) {
		if (node == null) {
			return;
		}

		TreeNode[] childNodes;
		ancestor[node.value] = value;

		// �ݹ����ýڵ���ӽڵ������ֵ
		childNodes = node.childNodes;
		if (childNodes != null) {
			for (TreeNode n : node.childNodes) {
				setNodeAncestor(n, value);
			}
		}

	}

	/**
	 * ִ�����߲�ѯ
	 */
	public void executeOfflineQuery() {
		TreeNode rootNode = new TreeNode();

		createTree(rootNode);
		lcaCal(rootNode);

		System.out.println("��ѯ������ʣ������" + querys.size() + "��");
	}
}
