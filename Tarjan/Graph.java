package Tarjan;

import java.util.ArrayList;

/**
 * ����ͼ��
 * 
 * @author lyq
 * 
 */
public class Graph {
	// ͼ�����ĵ�ı��
	ArrayList<Integer> vertices;
	// ͼ����������ߵķֲ���edges[i][j]�У�i��j�������ͼ�ı��
	int[][] edges;
	// ͼ����
	ArrayList<String[]> graphDatas;

	public Graph(ArrayList<String[]> graphDatas) {
		this.graphDatas = graphDatas;
		vertices = new ArrayList<>();
	}

	/**
	 * ����ͼ���ݹ�������ͼ
	 */
	public void constructGraph() {
		int v1 = 0;
		int v2 = 0;
		int verticNum = 0;

		for (String[] array : graphDatas) {
			v1 = Integer.parseInt(array[0]);
			v2 = Integer.parseInt(array[1]);

			if (!vertices.contains(v1)) {
				vertices.add(v1);
			}

			if (!vertices.contains(v2)) {
				vertices.add(v2);
			}
		}

		verticNum = vertices.size();
		// ������1���ռ䣬�Ǳ�ź��±�һ��
		edges = new int[verticNum + 1][verticNum + 1];

		// ���ߵĳ�ʼ��������-1 ������Ǵ˷���û����ͨ�ı�
		for (int i = 1; i < verticNum + 1; i++) {
			for (int j = 1; j < verticNum + 1; j++) {
				edges[i][j] = -1;
			}
		}

		for (String[] array : graphDatas) {
			v1 = Integer.parseInt(array[0]);
			v2 = Integer.parseInt(array[1]);

			edges[v1][v2] = 1;
		}
	}
}
