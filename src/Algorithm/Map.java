package Algorithm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class Map {

	private int width;

	private int height;

	private Node[][] nodes;

	// 맵은 1은 벽, 2는 계단 및 출구, 3은 방, 0은 갈 수 있는 곳으로 초기화한다.
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
	 * isWalkable이 1일때는 벽으로, 흰색 노드이다. isWalkable이 2일때는 탈출구 또는 계단으로, 초록색 노드이다.
	 * isWalkable이 3일때는 방으로, 주황색 노드이다. isWalkable이 0일때는 길으로, 검은색 노드이다.
	 * 
	 * 최적 경로에 대해서는 노란색으로 표시한다.
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

	// 시작좌표에서 목표좌표까지의 경로를 찾는 함수
	public final List<Node> findPath(int startX, int startY, int goalX, int goalY) {
		// 시작좌표와 목표좌표가 같을경우 빈 경로를 리턴
		if (startX == goalX && startY == goalY) {
			return new LinkedList<Node>();
		}

		// 열린목룍(지나갈 수 있는 노드)
		List<Node> openList = new LinkedList<Node>();
		// 닫힌목룍(이미 지나온 노드)
		List<Node> closedList = new LinkedList<Node>();

		// 처음 노드를 열린목룍에 넣어준다.
		openList.add(nodes[startX][startY]);

		// 현재노드가 목표노드가 될 때까지 반복
		while (true) {
			Node current = lowestFInList(openList);
			openList.remove(current);
			closedList.add(current);

			// 현재노드와 목표노드의 위치가 같을 경우.
			if ((current.getX() == goalX) && (current.getY() == goalY)) {
				return calcPath(nodes[startX][startY], current);
			}

			List<Node> adjacentNodes = getAdjacent(current, closedList);
			for (Node adjacent : adjacentNodes) {
				// 노드가 열린목록에 없을 경우, 인접노드는 열린목록에 추가.
				if (!openList.contains(adjacent)) {
					adjacent.setParent(current);
					adjacent.setH(nodes[goalX][goalY]);
					adjacent.setG(current);
					openList.add(adjacent);
				}
				// 현재노드의 G가 이전노드보다 비용이 낮을 경우.
				else if (adjacent.getG() > adjacent.calculateG(current)) {
					adjacent.setParent(current);
					adjacent.setG(current);
				}
			}

			// 만약 경로가 없을 경우
			if (openList.isEmpty()) {
				// 빈 리스트를 리턴
				return new LinkedList<Node>();
			}
		}
	}

	// 노드를 거슬러 올라가면서 경로를 생성하여 List로 리턴하는 함수.
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

	// 리스트 중에서 가장 작은 F를 가진 노드를 리턴하는 함수
	private Node lowestFInList(List<Node> list) {
		Node cheapest = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getF() < cheapest.getF()) {
				cheapest = list.get(i);
			}
		}
		return cheapest;
	}

	// 리스트 중에서 가장 작은 F를 가진 리스트를 리턴하는 함수(Finder.java에서 여러 경로중 최적 경로를 찾을 때 사용)
	public int lowestFList(List<Node> list) {
		int SumAllF = 0;
		for (int i = 0; i < list.size(); i++) {
			SumAllF += list.get(i).getF();
		}
		return SumAllF;
	}

	// 인접노드를 체크해서 인접노드 리스트에 넣고 리턴해주는 함수.
	// 닫힌목록에 있는 노드는 포함하지 않는다.
	private List<Node> getAdjacent(Node node, List<Node> closedList) {
		List<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();

		Node adjacent;

		// 왼쪽 노드 체크
		if (x > 0) {
			adjacent = getNode(x - 1, y);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}

		// 오른쪽 노드 체크
		if (x < width) {
			adjacent = getNode(x + 1, y);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}

		// 위쪽 노드 체크
		if (y > 0) {
			adjacent = this.getNode(x, y - 1);
			if (adjacent != null && (adjacent.isWalkable() == 0 || adjacent.isWalkable() == 2)
					&& !closedList.contains(adjacent)) {
				adjacentNodes.add(adjacent);
			}
		}

		// 아랫쪽 노드 체크
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
