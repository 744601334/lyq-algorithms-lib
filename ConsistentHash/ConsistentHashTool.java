package ConsistentHash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * һ���Թ�ϣ�㷨������
 * 
 * @author lyq
 * 
 */
public class ConsistentHashTool {
	// �����ڵ���Ϣ�ļ���ַ
	private String filePath;
	// ÿ���ڵ�����ڵ�ĸ���
	private int virtualNodeNum;
	// ����ʵ������б�
	private ArrayList<Entity> entityLists;
	// �ڵ��б�
	private ArrayList<Node> totalNodes;
	// ��������б�
	private HashMap<Entity, Node> assignedResult;

	public ConsistentHashTool(String filePath, int virtualNodeNum,
			ArrayList<Entity> entityLists) {
		this.filePath = filePath;
		this.virtualNodeNum = virtualNodeNum;
		this.entityLists = entityLists;

		readDataFile();
	}

	/**
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
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		Node node;
		String name;
		String ip;
		long hashValue;

		this.totalNodes = new ArrayList<>();
		// ������ÿ�еĽڵ����ƺ�ip��ַ
		for (String[] array : dataArray) {
			name = array[0];
			ip = array[1];

			// ����IP��ַ����hashӳ��
			hashValue = ip.hashCode();
			node = new Node(name, ip, hashValue);
			this.totalNodes.add(node);
		}

		// �Խڵ㰴��hashValueֵ������������
		Collections.sort(this.totalNodes);
	}

	/**
	 * ��ϣ�㷨�������ʵ��
	 */
	public void hashAssigned() {
		Node desNode;

		this.assignedResult = new HashMap<>();
		for (Entity e : this.entityLists) {
			desNode = selectDesNode(e, this.totalNodes);

			this.assignedResult.put(e, desNode);
		}

		outPutAssginedResult();
	}

	/**
	 * ͨ������ڵ�Ĺ�ϣ�㷨����
	 */
	public void hashAssignedByVirtualNode() {
		String name;
		String ip;
		long hashValue;

		// ����������������ֺ�׺
		Random random;
		Node node;
		ArrayList<Node> virtualNodes;

		random = new Random();
		// ��������ڵ�
		virtualNodes = new ArrayList<>();
		for (Node n : this.totalNodes) {
			name = n.name;
			ip = n.ip;

			// ��������ڵ����
			for (int i = 0; i < this.virtualNodeNum; i++) {
				// ����ڵ�Ĺ�ϣֵ��ip+���ֺ�׺����ʽ����
				hashValue = (ip + "#" + (random.nextInt(1000) + 1)).hashCode();

				node = new Node(name, ip, hashValue);
				virtualNodes.add(node);
			}
		}
		// ������������
		Collections.sort(virtualNodes);

		// ��ϣ�㷨����ڵ�
		Node desNode;
		this.assignedResult = new HashMap<>();
		for (Entity e : this.entityLists) {
			desNode = selectDesNode(e, virtualNodes);

			this.assignedResult.put(e, desNode);
		}

		outPutAssginedResult();
	}

	/**
	 * �ڹ�ϣ����Ѱ�ҹ����Ľڵ�
	 * 
	 * @param entity
	 *            �������ʵ��
	 * @param nodeList
	 *            �ڵ��б�
	 * @return
	 */
	private Node selectDesNode(Entity entity, ArrayList<Node> nodeList) {
		Node desNode;
		int hashValue;

		desNode = null;
		hashValue = entity.hashCode();

		for (Node n : nodeList) {
			// ����˳ʱ�뷽��ѡ��һ����������Ĺ�ϣֵ�ڵ�
			if (n.hashValue > hashValue) {
				desNode = n;
				break;
			}
		}

		// ���û���ҵ�˵���Ѿ���������hashValue,���ջ�״�������ֵ���һ��
		if (desNode == null) {
			desNode = nodeList.get(0);
		}

		return desNode;
	}

	/**
	 * ���������
	 */
	private void outPutAssginedResult() {
		Entity e;
		Node n;

		for (Map.Entry<Entity, Node> entry : this.assignedResult.entrySet()) {
			e = entry.getKey();
			n = entry.getValue();

			System.out.println(MessageFormat.format("ʵ��{0}�����䵽�˽ڵ�({1}, {2})",
					e.name, n.name, n.ip));
		}
	}
}
