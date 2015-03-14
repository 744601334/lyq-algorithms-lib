package Tarjan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Tarjan�㷨-����ͼǿ��ͨ�����㷨
 * 
 * @author lyq
 * 
 */
public class TarjanTool {
	// ��ǰ�ڵ�ı�����
	public static int currentSeq = 1;

	// ͼ���������ļ���ַ
	private String graphFile;
	// �ڵ�u�����Ĵ�����
	private int DFN[];
	// u��u�������ܻ��ݵ�������Ľڵ�Ĵ�����
	private int LOW[];
	// ��ͼ���ݹ��������ͼ
	private Graph graph;
	// ͼ�����ڵ�ջ
	private Stack<Integer> verticStack;
	// ǿ��ͨ�������
	private ArrayList<ArrayList<Integer>> resultGraph;
	// ͼ��δ�����ĵ�ı���б�
	private ArrayList<Integer> remainVertices;
	// ͼδ�����ıߵ��б�
	private ArrayList<int[]> remainEdges;

	public TarjanTool(String graphFile) {
		this.graphFile = graphFile;
		readDataFile();
	}

	/**
	 * ���ļ��ж�ȡ����
	 * 
	 */
	private void readDataFile() {
		File file = new File(graphFile);
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

		// �������ݹ�������ͼ
		graph = new Graph(dataArray);
		graph.constructGraph();
	}
	
	/**
	 * ��ʼ��2����������
	 */
	private void initDfnAndLow(){
		int verticNum = 0;
		verticStack = new Stack<>();
		remainVertices = (ArrayList<Integer>) graph.vertices.clone();
		remainEdges = new ArrayList<>();
		resultGraph = new ArrayList<>();

		for (int i = 0; i < graph.edges.length; i++) {
			remainEdges.add(graph.edges[i]);
		}

		verticNum = graph.vertices.size();
		DFN = new int[verticNum + 1];
		LOW = new int[verticNum + 1];

		// ��ʼ���������
		for (int i = 1; i <= verticNum; i++) {
			DFN[i] = Integer.MAX_VALUE;
			LOW[i] = -1;
		}
	}

	/**
	 * ����ǿ��ͨ����
	 */
	public void searchStrongConnectedGraph() {
		int label = 0;
		int verticNum = graph.vertices.size();
		initDfnAndLow();
		
		// ���õ�һ����DFN[1]=1;
		DFN[1] = 1;
		// �Ƴ��׸��ڵ�
		label = remainVertices.get(0);

		verticStack.add(label);
		remainVertices.remove((Integer) 1);
		while (remainVertices.size() > 0) {
			for (int i = 1; i <= verticNum; i++) {
				if (graph.edges[label][i] == 1) {
					// ����˱������Ľڵ�Ҳ����ջ��
					verticStack.add(i);
					remainVertices.remove((Integer) i);

					dfsSearch(verticStack);
				}
			}

			LOW[label] = searchEarliestDFN(label);
			// ���»��ݵ���һ�������DFN��LOWֵ���ж�
			if (LOW[label] == DFN[label]) {
				popStackGraph(label);
			}
		}

		printSCG();
	}

	/**
	 * ������ȱ����ķ�ʽѰ��ǿ��ͨ����
	 * 
	 * @param stack
	 *            ��ŵĽڵ�ĵ�ǰջ
	 * @param seqNum
	 *            ��ǰ�����Ĵ����
	 */
	private void dfsSearch(Stack<Integer> stack) {
		int currentLabel = stack.peek();
		// ������������ţ���ԭ�ȵĻ���������1
		currentSeq++;
		DFN[currentLabel] = currentSeq;
		LOW[currentLabel] = searchEarliestDFN(currentLabel);

		int[] edgeVertic;
		edgeVertic = remainEdges.get(currentLabel);
		for (int i = 1; i < edgeVertic.length; i++) {
			if (edgeVertic[i] == 1) {
				// ���ʣ���ѡ�ڵ��а����˽ڵ�����˽ڵ����
				if (remainVertices.contains(i)) {
					stack.add(i);
				} else {
					// ��������������
					continue;
				}

				// ����˱������ĵ����ջ��
				remainVertices.remove((Integer) i);
				remainEdges.set(currentLabel, null);

				// ����������ȱ���
				dfsSearch(stack);
			}
		}

		if (LOW[currentLabel] == DFN[currentLabel]) {
			popStackGraph(currentLabel);
		}

	}

	/**
	 * ��ջ�е����ֲ����
	 * 
	 * @param label
	 *            �������ٽ���
	 */
	private void popStackGraph(int label) {
		// ���2��ֵ��ȣ��򽫴˽ڵ��Լ��˽ڵ��ĵ��Ƴ�ջ��
		int value = 0;

		ArrayList<Integer> scg = new ArrayList<>();
		while (label != verticStack.peek()) {
			value = verticStack.pop();
			scg.add(0, value);
		}
		scg.add(0, verticStack.pop());

		resultGraph.add(scg);
	}

	/**
	 * ��ǰ�Ľڵ����������������Ĵ����
	 * 
	 * @param label
	 *            ��ǰ�Ľڵ���
	 * @return
	 */
	private int searchEarliestDFN(int label) {
		// �жϴ˽ڵ��Ƿ����ӱ�
		boolean hasSubEdge = false;
		int minDFN = DFN[label];

		// ����������Ĵ�����Ѿ�����С�Ĵ���ţ��򷵻�
		if (DFN[label] == 1) {
			return DFN[label];
		}

		int tempDFN = 0;
		for (int i = 1; i <= graph.vertices.size(); i++) {
			if (graph.edges[label][i] == 1) {
				hasSubEdge = true;

				// ����ڶ�ջ�к�ʣ��ڵ��ж�δ�����˽ڵ�˵���Ѿ�����ջ�ˣ��������ٴα���
				if (!remainVertices.contains(i) && !verticStack.contains(i)) {
					continue;
				}
				tempDFN = searchEarliestDFN(i);

				if (tempDFN < minDFN) {
					minDFN = tempDFN;
				}
			}
		}

		// ���û���ӱߣ����������Ĵ���ž���������
		if (!hasSubEdge && DFN[label] != -1) {
			minDFN = DFN[label];
		}

		return minDFN;
	}
	
	/**
	 * ��׼����ǿ��ͨ�����㷨
	 */
	public void standardSearchSCG(){
		initDfnAndLow();
		
		verticStack.add(1);
		remainVertices.remove((Integer)1);
		//�ӱ��Ϊ1�ĵ�һ���ڵ㿪ʼ����
		dfsSearchSCG(1);
		
		//�������е�ǿ��ͨ����
		printSCG();
	}

	/**
	 * �����������ǿ��ͨ����
	 * 
	 * @param u
	 *            ��ǰ�����Ľڵ���
	 */
	private void dfsSearchSCG(int u) {
		DFN[u] = currentSeq;
		LOW[u] = currentSeq;
		currentSeq++;

		for (int i = 1; i <graph.edges[u].length; i++) {
			// �ж�u,i���ڵ��Ƿ�����
			if (graph.edges[u][i] == 1) {
				// ����������£���iδ�����ʹ���ʱ�򣬼���ջ��
				if (remainVertices.contains(i)) {
					verticStack.add(i);
					remainVertices.remove((Integer) i);
					// �ݹ�����
					dfsSearchSCG(i);
					LOW[u] = (LOW[u] < LOW[i] ? LOW[u] : LOW[i]);
				} else if(verticStack.contains(i)){
					// ����Ѿ����ʹ������һ�δ��ջ����
					LOW[u] = (LOW[u] < DFN[i] ? LOW[u] : DFN[i]);
					//LOW[u] = (LOW[u] < LOW[i] ? LOW[u] : LOW[i]); �������LOW���жϣ�Ҳ����ͨ������
				}
			}
		}

		// ����ж�DFN��LOW�Ƿ����
		if (DFN[u] == LOW[u]) {
			popStackGraph(u);
		}
	}

	/**
	 * �������ͼ�е�ǿ��ͨ����
	 */
	private void printSCG() {
		int i = 1;
		String resultStr = "";
		System.out.println("����ǿ��ͨ������ͼ:");
		for (ArrayList<Integer> graph : resultGraph) {
			resultStr = "";
			resultStr += "ǿ��ͨ����" + i + "��{";
			for (Integer v : graph) {
				resultStr += (v + ", ");
			}
			resultStr = (String) resultStr.subSequence(0,
					resultStr.length() - 2);
			resultStr += "}";

			System.out.println(resultStr);
			i++;
		}
	}
}
