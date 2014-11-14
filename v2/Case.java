package v2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Case extends JPanel
{
	public static BufferedImage m_image;

	private boolean m_fill,
					m_parle,
					m_conflit = false;
	
	private int m_x, m_y;

	public Case(Color color, int p_x, int p_y, boolean p_fill, boolean p_parle)
	{
		this.setBackground(color);		
		this.m_fill = p_fill;
		this.m_parle = p_parle;
		this.m_x = p_x;
		this.m_y = p_y;
	}

	public Case(Color color, int p_x, int p_y, boolean p_fill, boolean p_parle, boolean p_conflit)
	{
		this(color, p_x, p_y, p_fill, p_parle);
		this.m_conflit = p_conflit;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if(m_fill)
		{ 
			int height = (int) (getWidth()/1.5);
			int width = (int) (getHeight()/1.5);
			int left = (int) ((getWidth()-getWidth()/1.5)/2);
			int right = (int) ((getHeight()-getHeight()/1.5)/2);
			
			try 
			{
				if(m_conflit)
					m_image = ImageIO.read(new File("reine_c.png"));
				else if(m_parle)
					m_image = ImageIO.read(new File("reine_p.png"));
				else
					m_image = ImageIO.read(new File("reine.png"));
				
				g.drawImage(m_image, left, right, height, width, this);
			}
			catch (Exception ex)
			{
			}
		}
	}
	
	boolean isFill()
	{
		return m_fill;
	}
	
	int X(){
		return m_x;
	}
	int Y(){
		return m_y;
	}
}
