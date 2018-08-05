package Algorithm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class Map {

	private int width;

	private int height;

	private Node[][] nodes;

	// ���� 1�� ��, 2�� ��� �� �ⱸ, 3�� ��, 0�� �� �� �ִ� ������ �ʱ�ȭ�Ѵ�.
	public Map(int[][] map) {
		this.width = map[0].length;
		this.height = map.length;
		nodes = new Node[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				nodes[x][y] = new Node(x, y, map[y][x]);
			}
		}
	}

	/**
	 * isWalkable�� 1�϶��� ������, ��� ����̴�. isWalkable�� 2�϶��� Ż�ⱸ �Ǵ� �������, �ʷϻ� ����̴�.
	 * isWalkable�� 3�϶��� ������, ��Ȳ�� ����̴�. isWalkable�� 0�϶��� ������, ������ ����̴�.
	 * 
	 * ���� ��ο� ���ؼ��� ��������� ǥ���Ѵ�.
	 */
	public void drawMap(Graphics g, List<Node> path) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (nodes[x][y].isWalkable() == 1) {
					g.setColor(Color.WHITE);
				} else if (nodes[x][y].isWalkable() == 2) {
					g.setColor(Color.GREEN);
				} else if (nodes[x][y].isWalkable() == 3) {
					g.setColor(Color.ORANGE);
				} else if (path != null && path.contains(new Node(x, y, 0))) {
					g.setColor(Color.YELLOW);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(x * 32, y * 32, 32, 32);
			}
		}
	}

	public Node getNode(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return nodes[x][y];
		} else {
			return null;
		}
	}

	// ������ǥ���� ��ǥ��ǥ������ ��θ� ã�� �Լ�
	public final List<Node> findPath(int startX, int startY, int goalX, int goalY) {
		// ������ǥ�� ��ǥ��ǥ�� ������� �� ��θ� ����
		if (startX == goalX && startY == goalY) {
			return new LinkedList<Node>();
		}

		// ������(������ �� �ִ� ���)
		List<Node> openList = new LinkedList<Node>();
		// ������(�̹� ������ ���)
		List<Node> closedList = new LinkedList<Node>();

		// ó�� ��带 �����񏋿� �־��ش�.
		openList.add(nodes[startX][startY]);

		// �����尡 ��ǥ��尡 �� ������ �ݺ�
		while (true) {
			Node current = lowestFInList(openList);
			openList.remove(current);
			closedList.add(current);

			// ������� ��ǥ����� ��ġ�� ���� ���.
			if ((current.getX() == goalX) && (current.getY() == goalY)) {
				return calcPath(nodes[startX][startY], current);
			}

			List<Node> adjacentNodes = getAdjacent(current, closedList);
			for (Node adjacent : adjacentNodes) {
				// ��尡 ������Ͽ� ���� ���, �������� ������Ͽ� �߰�.
				if (!openList.contains(adjacent)) {
					adjacent.setParent(current);
					adjacent.setH(nodes[goalX][goalY]);
					adjacent.setG(current);
					openList.add(adjacent);
				}
				// �������� G�� ������庸�� ����� ���� ���.
				else if (adjacent.getG() > adjacent.calculateG(current)) {
					adjacent.setParent(current);
					adjacent.setG(current);
				}
			}

			// ���� ��ΰ� ���� ���
			if (openList.isEmpty()) {
				// �� ����Ʈ�� ����
				return new LinkedList<Node>();
			}
		}
	}

	// ��带 �Ž��� �ö󰡸鼭 ��θ� �����Ͽ� List�� �����ϴ� �Լ�.
	private List<Node> calcPath(Node start, Node goal) {
		LinkedList<Node> path = new LinkedList<Node>();

		Node node = goal;
		boolean done = false;
		while (!done) {
			path.addFirst(node);
			node = node.getParent();
			if (node.equals(start)) {
				done = true;
			}
		}
		return path;
	}

	// ����Ʈ �߿��� ���� ���� F�� ���� ��带 �����ϴ� �Լ�
	private Node lowestFInList(List<Node> list) {
		Node cheapest = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getF() < cheapest.getF()) {
				cheapest = list.get(i);
			}
		}
		return cheapest;
	}

	// ����Ʈ �߿��� ���� ���� F�� ���� ����Ʈ�� �����ϴ� �Լ�(Finder.java���� ���� ����� ���� ��θ� ã�� �� ���)
	public int lowestFList(List<Node> list) {
		int SumAllF = 0;
		for (int i = 0; i < list.size(); i++) {
			SumAllF += list.get(i).getF();
		}
		return SumAllF;
	}

	// ������带 üũ�ؼ� ������� ����Ʈ�� �ְ� �������ִ� �Լ�.
	// ������Ͽ� �ִ� ���� �������� �ʴ´�.
	private List<Node> getAdjacent(Node node, List<Node> closedList) {
		List<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();

		Node adjacent;

		// ���� ��� üũ
		if (x > 0) {
			adjacent = getNode(x - 1, y);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}

		// ������ ��� üũ
		if (x < width) {
			adjacent = getNode(x + 1, y);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}

		// ���� ��� üũ
		if (y > 0) {
			adjacent = this.getNode(x, y - 1);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}

		// �Ʒ��� ��� üũ
		if (y < height) {
			adjacent = this.getNode(x, y + 1);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}
		return adjacentNodes;
	}

}
