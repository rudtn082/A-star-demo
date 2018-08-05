package Algorithm;

public class Node {

	protected static final int MOVEMENT_COST = 10;

	private Node parent;

	private int x;
	private int y;
	private int walkable;
	private int g; // 시작 노드로부터 새로운 노드까지의 이동비용.
	private int h; // 얻어진 노드로부터 최종목적지점 까지의 예상이동비용.

	public Node(int x, int y, int walkable) {
		this.x = x;
		this.y = y;
		this.walkable = walkable;
	}

	public void setG(Node parent) {
		g = (parent.getG() + MOVEMENT_COST);
	}

	public int calculateG(Node parent) {
		return (parent.getG() + MOVEMENT_COST);
	}

	public void setH(Node goal) {
		h = (Math.abs(getX() - goal.getX()) + Math.abs(getY() - goal.getY())) * MOVEMENT_COST;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int isWalkable() {
		return walkable;
	}

	public void setWalkable(int walkable) {
		this.walkable = walkable;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	// F(비용) = G + H
	public int getF() {
		return g + h;
	}

	public int getG() {
		return g;
	}

	public int getH() {
		return h;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Node))
			return false;
		if (o == this)
			return true;

		Node n = (Node) o;
		if (n.getX() == x && n.getY() == y && n.isWalkable() == walkable)
			return true;
		return false;
	}

}
