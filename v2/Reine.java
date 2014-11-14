package v2;

public class Reine {
	private int m_x,
				m_y;
	
	
	Reine(int x, int y){
		m_x = x;
		m_y = y;
	}
	
	public void setX(int x){
		m_x = x;
	}
	public void setY(int y){
		m_y = y;
	}
	
	public int getX(){
		return m_x;
	}
	public int getY(){
		return m_y;
	}
	
	public String toString(){
		return "X: " + m_x + " Y: " + m_y;
	}
}
