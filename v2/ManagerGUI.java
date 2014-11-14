package v2;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class ManagerGUI  extends JFrame{
	 Manager m_parent;
	 List<Reine> m_listeReines;
	 static int m_nReines = 5;
	private JPanel m_container = new JPanel();
	private JPanel m_damier;
	private JMenuBar m_menuBar = new JMenuBar();
	
	private JMenu 	m_fichier = new JMenu("Fichier"),
					m_option = new JMenu("Option");
	
	private JMenuItem 	m_nouveau = new JMenuItem("RaZ"),
						m_quitter = new JMenuItem("Quitter"),
						m_param = new JMenuItem("Parametres");
	
	
	private JButton m_btnRaz,
					m_btnPaP,
					m_btnTour,
					m_btnResultat;
	
	private JTextField m_txtNbReines;
	
	private JLabel m_lblTour;
	
	 ManagerGUI(int nbReines, Manager parent){
		
		m_parent = parent;
		this.setTitle("N Reines v2.0");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(620, 620);
		this.setResizable(false);
		this.setContentPane(m_container);
		m_nReines = nbReines;
		
		initMenu(); 
		initComposant();

	}
	
	 ManagerGUI(Manager parent){
		this(m_nReines, parent);
	}
	
	private void initMenu(){
		
		m_quitter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg) {
				System.exit(0);
			}			
		});
		
		m_param.addActionListener(new ActionListener(){
			
			JFrame frameParam = new JFrame("Parametres");
			
			public void actionPerformed(ActionEvent arg) {
				frameParam.setResizable(false);
				frameParam.setVisible(true);
				frameParam.setSize(200,100);
				
				JButton btnFermer = new JButton("Fermer");
				btnFermer.setSize(20, 20);
				
				btnFermer.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg) {
						fermer();
					}			
				});
				
				frameParam.add(btnFermer, BorderLayout.SOUTH);
				
			}	
			public void fermer(){
				frameParam.dispose();
			}
		});
		
		m_nouveau.addActionListener(new RazListener());

		m_param.setMnemonic('P');
		m_nouveau.setMnemonic('R');
		m_quitter.setMnemonic('Q');
		
		m_fichier.add(m_nouveau);
		m_fichier.addSeparator();
		m_fichier.add(m_quitter);
		m_fichier.setMnemonic('F');
		
		m_option.add(m_param);
		m_option.setMnemonic('O');
		
		m_menuBar.add(m_fichier);
		m_menuBar.add(m_option);
		
		this.setJMenuBar(m_menuBar);
	}
	
	private void initComposant(){

		JPanel panelBoutons = new JPanel();
		
		m_btnRaz = new JButton("RaZ");
		m_btnRaz.addActionListener(new RazListener());
		m_btnPaP = new JButton("Pas a pas");
		m_btnPaP.addActionListener(new PapListener());
		m_btnTour = new JButton("Tour complet");
		m_btnTour.addActionListener(new TourListener());
		m_btnResultat = new JButton("Jeu complet");
		m_btnResultat.addActionListener(new ResListener());
		JLabel lblNbReines = new JLabel("Nombre de reines: ");
		m_txtNbReines = new JTextField();
		m_txtNbReines.setPreferredSize(new Dimension(35, 25));
		m_txtNbReines.addActionListener(new nbReinesListener());
		m_txtNbReines.setText(String.valueOf(m_nReines));
		
		m_lblTour = new JLabel("Tour(s): ");
		
		panelBoutons.add(m_btnRaz);
		panelBoutons.add(m_btnPaP);
		panelBoutons.add(m_btnTour);
		panelBoutons.add(m_btnResultat);
		panelBoutons.add(lblNbReines);
		panelBoutons.add(m_txtNbReines);
		
		m_container.add(panelBoutons, BorderLayout.NORTH);
		
		initPanelDamier();
	}
	
	private void initPanelDamier() {

		m_parent.init();
		m_listeReines = new ArrayList<Reine>();
		
		for(int i=1; i<=m_nReines; i++)
			m_listeReines.add(new Reine(1, i));

		dessinerDamier();
		m_btnResultat.setEnabled(true);
		m_btnPaP.setEnabled(true);
		
	}
	
	private void clearDamier(){
		m_container.remove(m_damier);
		m_container.remove(m_lblTour);
	}
	
	 void dessinerDamier(){
		 try{
			 clearDamier();
			m_btnResultat.setEnabled(!m_parent.jeuFini());
			m_btnPaP.setEnabled(!m_parent.jeuFini());
			m_btnTour.setEnabled(!m_parent.jeuFini());
		 }catch(Exception e){
			 //premiere fois que le damier est dessine
		 }
		 
		GridLayout layout = new GridLayout(m_nReines,m_nReines);
		m_damier = new JPanel(layout);
		
		JPanel [][]cases=new Case[m_nReines][m_nReines]; 

		for(int i = 0; i < m_nReines; i++) 
		{
			for(int j = 0; j < m_nReines; j++)
			{
				//dessine le damier
				if(((i+j)%2)==0){
					cases[i][j] = new Case(Color.black, i+1, j+1, isBoxFill(i, j), isTalking(i, j));
				}else{
					cases[i][j] = new Case(Color.white, i+1, j+1, isBoxFill(i, j), isTalking(i, j));
				}
				cases[i][j].setPreferredSize(new Dimension(500/m_nReines,500/m_nReines));
				cases[i][j].addMouseListener(new CaseMouseListener());
				
				m_damier.add(cases[i][j]);
			}
		}
		
		m_lblTour = new JLabel("Tour(s): " + Integer.toString(m_parent.getNbTours()));
		m_container.add(m_damier, BorderLayout.CENTER);
		m_container.remove(m_lblTour);
		m_container.add(m_lblTour , BorderLayout.SOUTH);
	    m_container.updateUI();
		m_container.validate();
		
	}
	
	int getNbReines(){
		return m_nReines;
	}
	
	 List<Reine> getListeReines(){
		return m_listeReines;
	}
	
	private boolean isBoxFill(int i, int j){
		for (Reine r : m_listeReines) {
			if(r.getX() == j+1 && r.getY() == i+1)
				return true;
		}
		return false;
	}
	
	private boolean isTalking(int i, int j){
		try{
			if(m_parent.getReineParle().getX() == j+1 && m_parent.getReineParle().getY() == i+1)
				return true;
			else
				return false;
		}catch(Exception ex){
			
		}
		return false;
	}
	
	class RazListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			initPanelDamier();
			m_parent.addMessage(m_parent, "init");
		}		
	}
	class PapListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			m_parent.pas(true);
		}		
	}
	class TourListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			m_parent.tour(true);
		}		
	}
	class ResListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			m_parent.jeu();	
		}		
	}
	class nbReinesListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			try {
				m_nReines = Integer.parseInt(m_txtNbReines.getText());
				m_container.requestFocus(); 
			} catch(NumberFormatException ex) {
//				System.out.println(ex);
				JOptionPane.showMessageDialog(null, "Veuillez entrer un entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
				m_txtNbReines.requestFocus();
			}

			
			if(m_nReines<4 || m_nReines>12){
				m_nReines = m_listeReines.size();//remet l'ancienne taille
				JOptionPane.showMessageDialog(null, "Veuillez entrer un entier compris entre 4 et 12.", "Erreur", JOptionPane.ERROR_MESSAGE);
				m_txtNbReines.requestFocus();
			}else{
				//redessine avec la nouvelle taille
				initPanelDamier();
				m_parent.addMessage(m_parent, "init");
			}
		}		
	}
	
	class CaseMouseListener implements MouseListener{

		@Override
		public void mouseEntered(MouseEvent arg0) {
			Case c = ((Case)arg0.getSource());
			if(c.isFill()){//si il y a une dame sur la case
				clearDamier();
				GridLayout layout = new GridLayout(m_nReines,m_nReines);
				m_damier = new JPanel(layout);
				
				JPanel [][]cases=new Case[m_nReines][m_nReines]; 

				for(int i = 0; i < m_nReines; i++) 
				{
					for(int j = 0; j < m_nReines; j++)
					{
						//si la case est dans la zone de la reine (sauf la reine elle meme)
						if(((i+1) == c.X() || (j+1) == c.Y() || ((c.X()-(i+1))==(c.Y()-(j+1))) || (((i+1)-c.X())==(c.Y()-(j+1)))) && !(i+1 == c.X() && j+1 == c.Y()) ){
							if(((i+j)%2)==0){
								cases[i][j] = new Case(Color.darkGray, i+1, j+1, isBoxFill(i, j), isTalking(i, j), isBoxFill(i, j));
							}else{
								cases[i][j] = new Case(Color.gray, i+1, j+1, isBoxFill(i, j), isTalking(i, j), isBoxFill(i, j));
							}
						}else{
							if(((i+j)%2)==0){
								cases[i][j] = new Case(Color.black, i+1, j+1, isBoxFill(i, j), isTalking(i, j), false);
							}else{
								cases[i][j] = new Case(Color.white, i+1, j+1, isBoxFill(i, j), isTalking(i, j), false);
							}
						}
						
						cases[i][j].setPreferredSize(new Dimension(500/m_nReines,500/m_nReines));
						cases[i][j].addMouseListener(new CaseMouseListener());
						
						m_damier.add(cases[i][j]);
					}
				}
				//on redessine tout ça correctement
				m_container.add(m_damier, BorderLayout.CENTER);
				m_container.remove(m_lblTour);
				m_container.add(m_lblTour , BorderLayout.SOUTH);
			    m_container.updateUI();
				m_container.validate();
			}else{
				dessinerDamier();
			}
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			//si la souris sors on redessine normalement
			dessinerDamier();
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {

		}


		@Override
		public void mousePressed(MouseEvent arg0) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}
		

	}
}