package A_star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import Algorithm.*;
import entity.Player;

public class Finder extends JPanel implements MouseListener {

	private Map map;
	private Player player;
	private List<Node> path;

	// 1�� ��, 2�� ��� �� �ⱸ, 3�� ��, 0�� �� �� �ִ� ��
	int[][] m0 = { // 1��
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 }, //
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 }, //
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 }, //
			{ 3, 0, 0, 0, 0, 0, 0, 0, 0, 3 }, //
			{ 3, 0, 0, 3, 0, 0, 3, 0, 0, 3 }, //
			{ 3, 0, 0, 3, 0, 0, 3, 0, 0, 3 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 2, 2, 0, 0, 0, 0 }, //
			{ 0, 3, 0, 0, 0, 0, 0, 0, 3, 0 }, //
			{ 0, 3, 0, 0, 0, 0, 0, 0, 3, 0 }, //
			{ 0, 3, 0, 0, 0, 0, 0, 0, 3, 0 }, //
			{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, //
			{ 2, 0, 1, 0, 1, 1, 0, 1, 0, 2 }, //
			{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 3, 0, 0, 0, 0, 0, 0, 0, 0, 3 }, //
			{ 3, 0, 3, 3, 3, 3, 3, 3, 0, 3 }, //
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 }, //
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 } };

	int[][] m1 = { // 2��
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	public Finder() {
		int[][] map0 = m0;
		int[][] map1 = m1;

		setPreferredSize(new Dimension(map0[0].length * 32, map0.length * 32));
		addMouseListener(this);

		map = new Map(map0);
		player = new Player(1, 3);
		Move_Map();
		Best_Exit();
	}

	public void update() {
		player.update();
	}

	public void render(Graphics2D g) {
		map.drawMap(g, path);
		g.setColor(Color.GRAY);
		for (int x = 0; x < getWidth(); x += 32) {
			g.drawLine(x, 0, x, getHeight());
		}
		for (int y = 0; y < getHeight(); y += 32) {
			g.drawLine(0, y, getWidth(), y);
		}

		g.setColor(Color.RED);
		g.fillRect(player.getX() * 32 + player.getSx(), player.getY() * 32 + player.getSy(), 32, 32);
	}

	public void Move_Map() {
		if (player.getFloor() == 0) {
			map = new Map(m0);
		} else if (player.getFloor() == 1) {
			map = new Map(m1);
		} else {
			System.out.println("�� �̵� �����Դϴ�.");
		}
	}

	// ���� ���ã��
	public void Best_Exit() {
		Map map2 = new Map(m1); // ��θ� ã������ �ӽ� 2���� ����
		List<Node> Temp_path[] = new List[4];
		List<Node> Temp_path2[] = new List[3];
		Temp_path[0] = map.findPath(player.getX(), player.getY(), 4, 9); // 1�� ���1
		Temp_path[1] = map.findPath(player.getX(), player.getY(), 5, 9); // 1�� ���2
		Temp_path[2] = map.findPath(player.getX(), player.getY(), 0, 14); // 1�� Ż�ⱸ1
		Temp_path[3] = map.findPath(player.getX(), player.getY(), 9, 14); // 1�� Ż�ⱸ2
		/* 3,3�� ����̶�� ����(2������ ���� �̿�) */
		Temp_path2[0] = map2.findPath(player.getX(), player.getY(), 1, 1); // 2�� Ż�ⱸ1
		Temp_path2[1] = map2.findPath(player.getX(), player.getY(), 2, 2); // 2�� Ż�ⱸ2
		Temp_path2[2] = map2.findPath(player.getX(), player.getY(), 3, 3); // 2�� Ż�ⱸ3

		if (player.getFloor() == 0) { // 1���� ���� ���
			int lowest_SF = map.lowestFList(Temp_path[0]);
			int lowest_SF2 = map2.lowestFList(Temp_path2[0]);
			int temp = 0, temp2 = 0;

			for (int i = 0; i < Temp_path.length; i++) {
				if (map.lowestFList(Temp_path[i]) != 0)
					break;
				if (i + 1 == Temp_path.length) {
					for (int j = 0; j < Temp_path2.length; j++) {
						if (map2.lowestFList(Temp_path2[j]) != 0)
							break;
						if (j + 1 == Temp_path2.length) {
							System.out.println("��θ� ã�� �� �����ϴ�.. �Ф�");
						}
					}
				}
			}

			for (int i = 2; i < Temp_path.length; i++) { // 1�� Ż�ⱸ�� ���� ���� ���
				if (map.lowestFList(Temp_path[i]) == 0)
					continue;
				if (lowest_SF > map.lowestFList(Temp_path[i])) {
					lowest_SF = map.lowestFList(Temp_path[i]);
					temp = i;
				}
			}

			for (int i = 0; i < Temp_path2.length; i++) { // 1�� ��� + 2�� Ż�ⱸ�� ���� ���� ���
				if (map2.lowestFList(Temp_path2[i]) == 0 || map.lowestFList(Temp_path[0]) == 0)
					continue;
				if (lowest_SF2 > map2.lowestFList(Temp_path2[i])) {
					lowest_SF2 = map2.lowestFList(Temp_path2[i]);
					temp2 = i;
				}
			}

			if (lowest_SF > lowest_SF2 + map.lowestFList(Temp_path[0])) {
				path = Temp_path2[temp2];
				player.followPath(path);
			} else if (lowest_SF <= lowest_SF2 + map.lowestFList(Temp_path[0])) {
				path = Temp_path[temp];
				player.followPath(path);
			} else {
				System.out.println("��� ã�� �����Դϴ�.");
			}

		} else if (player.getFloor() == 1) { // 2���� ���� ���
			int lowest_SF = map2.lowestFList(Temp_path2[0]);
			int temp = 0;

			for (int i = 0; i < Temp_path2.length; i++) {
				if (map2.lowestFList(Temp_path2[i]) != 0)
					break;
				if (i + 1 == Temp_path2.length) {
					System.out.println("��θ� ã�� �� �����ϴ�.. �Ф�");
				}
			}

			for (int i = 0; i < Temp_path2.length; i++) {
				if (map2.lowestFList(Temp_path2[i]) == 0)
					continue;
				if (lowest_SF > map2.lowestFList(Temp_path2[i])) {
					lowest_SF = map2.lowestFList(Temp_path2[i]);
					temp = i;
				}
			}

			path = Temp_path[temp];
			player.followPath(path);
		} else {
			System.out.println("��� ã�� �����Դϴ�.");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX() / 32;
		int my = e.getY() / 32;
		System.out.printf("%d %d\n", mx, my); // ��ǥȮ��! (�����)
		if (map.getNode(mx, my).isWalkable() == 0) {
			System.out.println("Ŭ���� ���� ħ���� �߻��Ͽ� ���� �����ϴ�.");

			if (player.getFloor() == 0) {
				m0[my][mx] = 1;
				map = new Map(m0);
			} else if (player.getFloor() == 1) {
				m1[my][mx] = 1;
				map = new Map(m1);
			} else {
				System.out.println("ħ�� �̺�Ʈ �����Դϴ�.");
			}
			System.out.println("��θ� �� Ž�� �մϴ�...");
			Best_Exit(); // ħ�� �߻� �� ��� �� Ž��
		} else {
			System.out.println("�̹� �� �� ���� ���Դϴ�.");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
