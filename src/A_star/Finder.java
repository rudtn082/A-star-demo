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

public class Finder extends JPanel implements MouseListener
{

	private Map map;
	private Player player;
	private List<Node> path;

	int[][] m0 = { //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, //
			{ 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1 }, //
			{ 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1 }, //
			{ 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1 }, //
			{ 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1 }, //
			{ 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1 }, //
			{ 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1 }, //
			{ 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1 }, //
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

	public Finder()
	{
		int[][] m = m0;

		setPreferredSize(new Dimension(m[0].length * 32, m.length * 32));
		addMouseListener(this);
		
		map = new Map(m);
		player = new Player(1, 1);
		Best_Exit();
	}

	public void update()
	{
		player.update();
	}

	public void render(Graphics2D g)
	{
		map.drawMap(g, path);
		g.setColor(Color.GRAY);
		for (int x = 0; x < getWidth(); x += 32)
		{
			g.drawLine(x, 0, x, getHeight());
		}
		for (int y = 0; y < getHeight(); y += 32)
		{
			g.drawLine(0, y, getWidth(), y);
		}
		
		g.setColor(Color.RED);
		g.fillRect(player.getX() * 32 + player.getSx(), player.getY() * 32 + player.getSy(), 32, 32);
	}
	
	// 최적 경로찾기
	public void Best_Exit() {
		List<Node> Temp_path[] = new List[3];
		Temp_path[0] = map.findPath(player.getX(), player.getY(), 11, 8); // 경로1
		Temp_path[1] = map.findPath(player.getX(), player.getY(), 9, 7); // 경로2
		Temp_path[2] = map.findPath(player.getX(), player.getY(), 5, 1); // 경로3
		
		int lowest_SF = map.lowestFList(Temp_path[0]);
		int temp = 0;
		
		for(int i = 0; i < Temp_path.length; i++) {
			if(map.lowestFList(Temp_path[i]) == 0) continue;
			if(lowest_SF > map.lowestFList(Temp_path[i])) {
				lowest_SF = map.lowestFList(Temp_path[i]);
				temp = i;
			}
		}
		
		for(int i = 0; i < Temp_path.length; i++) {
			if(map.lowestFList(Temp_path[i]) != 0) break;
			System.out.println(Temp_path.length);
			if(i+1 == Temp_path.length) {
				System.out.println("경로를 찾을 수 없습니다.. ㅠㅠ");
			}
		}
		
		path = Temp_path[temp];
		player.followPath(path);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX() / 32;
		int my = e.getY() / 32;
		System.out.printf("%d %d\n", mx, my); // 지울것
		if (map.getNode(mx, my).isWalkable())
		{
			System.out.println("클릭한 곳에 침수가 발생하여 길이 막힙니다.");
			m0[my][mx] = 1;
			map = new Map(m0);
			System.out.println("경로를 재 탐색 합니다...");
			Best_Exit(); // 침수 발생 시 경로 재 탐색
		}
		else
		{
			System.out.println("이미 갈 수 없는 곳입니다.");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

}
