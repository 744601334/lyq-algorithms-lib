package TopK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * ɸѡ��TopK���㷨������
 * 
 * @author lyq
 * 
 */
public class SelectTool {
	// ɸѡ��ǰK��ֵ��K��ֵ
	private int k;
	// ����ͳ��ͼ
	private Map<String, Integer> countMap;
	// ɸѡ����TopK�Ĳ�ѯ����
	private ArrayList<Query> queryList;

	public SelectTool(int k, Map<String, Integer> countMap) {
		this.k = k;
		this.countMap = countMap;
	}

	/**
	 * �����ⲿ�������TopK��ѡ��,ά��K������
	 */
	public void selectTopKBySort() {
		int index;
		int count;
		String queryWord;
		Query insertQuery;
		Query query;
		Query query2;

		index = 0;
		queryList = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
			index++;
			count = entry.getValue();
			queryWord = entry.getKey();
			insertQuery = new Query(count, queryWord);

			if (index < k) {
				queryList.add(insertQuery);
			} else if (index == k) {
				queryList.add(insertQuery);
				// �Բ�ѯ������г�������
				Collections.sort(queryList);
			} else if (index > k) {
				for (int i = 0; i < queryList.size() - 1; i++) {
					query = queryList.get(i);
					query2 = queryList.get(i + 1);

					// Ѱ�Ҳ����λ�ã����countֵ��ǰ��query֮�䣬������滻
					if (query.count >= insertQuery.count
							&& query2.count < insertQuery.count) {
						queryList.set(i + 1, insertQuery);
						break;
					}
				}
			}
		}
		
		outputTopKQuerys();
	}

	/**
	 * ͨ���������㷨����TopK��ɸѡ
	 */
	public void selectTopKByMaxHeap() {
		int index;
		int count;
		String queryWord;
		Query insertQuery;

		index = 0;
		queryList = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
			index++;
			count = entry.getValue();
			queryWord = entry.getKey();
			insertQuery = new Query(count, queryWord);

			if (index < k) {
				queryList.add(insertQuery);
			} else if (index == k) {
				queryList.add(insertQuery);

				// ����ո�����k����ѯ��������г�ʼ������
				queryList = initMaxHeap(queryList);
			} else if (index > k) {
				// ����һ���µĲ�ѯֵ����ά������ѽṹ
				adjustHeap(insertQuery, queryList);
			}
		}
		
		outputTopKQuerys();
	}

	/**
	 * ��ʼ������Ϊk�Ĵ󶥶�
	 * 
	 * @param queryList
	 *            �����ź�����µĶ�
	 * @return
	 */
	private ArrayList<Query> initMaxHeap(ArrayList<Query> queryList) {
		// ��һ����ѯ��
		Query firstQuery;
		ArrayList<Query> newMaxHeap;

		newMaxHeap = new ArrayList<>();
		for (int i = 0; i < k; i++) {
			adjustMinValueFromHeap(queryList);

			// ����һ��Ԫ�������һ��Ԫ�ػ���
			firstQuery = queryList.get(0);

			newMaxHeap.add(firstQuery);
			// ����һ��������С���
			queryList.set(0, new Query(-Integer.MAX_VALUE, null));
		}

		return newMaxHeap;
	}

	/**
	 * ѡ����ǰ������С��Ԫ�أ������һ��λ�õ�Ԫ�ؽ��н���
	 * 
	 * @param queryList
	 *            Ŀǰά���Ĵ󶥶�
	 */
	private void adjustMinValueFromHeap(ArrayList<Query> queryList) {
		int currentIndex;
		int otherIndex;
		int leafIndex;
		Query temp;
		Query query;
		Query query2;
		Query parentQuery;

		// ����Ҷ�ӽڵ����С�±��
		leafIndex = k / 2;

		for (int i = leafIndex; i < k; i += 2) {
			currentIndex = i;

			// �����ǰ�жϻ�û�е����ڵ�
			while (currentIndex > 0) {
				query = queryList.get(currentIndex);

				// �жϽڵ��Ƿ�Ϊ���ӽڵ㻹�����ӽڵ㣬���ж�ȡ�Ĳ�Ľڵ�
				if (currentIndex % 2 == 0) {
					otherIndex = currentIndex - 1;
					query2 = queryList.get(otherIndex);
				} else {
					otherIndex = currentIndex + 1;
					query2 = queryList.get(otherIndex);
				}

				// ��ֵ�ӽڵ��±�
				if (query.count < query2.count) {
					currentIndex = otherIndex;
					temp = query2;
				} else {
					temp = query;
				}
				parentQuery = queryList.get((currentIndex - 1) / 2);

				// ���½��и�ֵ����
				if (temp.count > parentQuery.count) {
					queryList.set((currentIndex - 1) / 2, temp);
					queryList.set(currentIndex, parentQuery);
				}

				// �Ƚϲ������ϻ���
				currentIndex = (currentIndex - 1) / 2;
			}
		}
	}

	/**
	 * ���д󶥶ѵĵ���
	 * 
	 * @param insertQuery
	 *            ������Ĳ�ѯ��
	 * @param queryList
	 *            ������
	 */
	public void adjustHeap(Query insertQuery, ArrayList<Query> queryList) {
		int currentIndex;
		int leftIndex;
		int rightIndex;

		Query query;
		Query leftQuery;
		Query rightQuery;

		currentIndex = 0;
		while (currentIndex < queryList.size()) {
			query = queryList.get(currentIndex);

			// ���������Ĳ�ѯ�����ȵ�ǰ�������滻
			if (insertQuery.count > query.count) {
				queryList.set(currentIndex, insertQuery);
				break;
			} else {
				leftIndex = 2 * (currentIndex + 1) - 1;
				rightIndex = 2 * (currentIndex + 1);

				leftQuery = queryList.get(leftIndex);
				rightQuery = queryList.get(rightIndex);

				// ѡ��һ������ֵ��С�����ݹ�Ƚ�
				if (leftQuery.count < rightQuery.count) {
					// �±����任
					currentIndex = leftIndex;
					query = leftQuery;
				} else {
					// �±����任
					currentIndex = rightIndex;
					query = rightQuery;
				}
			}
		}
	}

	/**
	 * ���TopK��ͳ�ƽ��
	 */
	private void outputTopKQuerys() {
		int i = 0;

		for (Query q : queryList) {
			System.out.println("Top " + (i+1) + ":" + q.word + ":����" + q.count);
			i++;
		}
	}
}
