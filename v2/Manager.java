package v2;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import v2.ManagerGUI.nbReinesListener;
public class Manager {
	
	 ManagerGUI m_fenetre;
	private int m_nbPas,
				m_nbTour;
	
	private List<Message> m_listeMsg;
	private List<Reine> m_listeReineParle;
	
	Manager(){
		m_nbPas = 0;
		m_nbTour = 0;
		m_listeMsg = new ArrayList<Message>();
		m_listeReineParle = new ArrayList<Reine>();
		
		m_fenetre = new ManagerGUI(6, this);
		m_fenetre.setVisible(true);
		addMessage(this, "init");
	
	}
	
	void init(){
		m_nbPas = 0;
		m_nbTour = 0;
		m_listeMsg = new ArrayList<Message>();
		m_listeReineParle = new ArrayList<Reine>();
	}
	
	private void traitement(Message msg){

		if(msg.getContenu().equals("init")){
			addMessage(setReineParle(), "parle");
		}
		
	}

	int getNbTours(){
		return m_nbTour;
	}

	
	private boolean economique(Reine r){
		//conflit entre une reine r et toutes les autres
		for(Reine q : m_fenetre.getListeReines()){
			if(!q.equals(r)){
	            if (r.getX() == q.getX())             return false;   // colonne
	            if (r.getY() == q.getY())             return false;  //ligne
	            if ((r.getX() - q.getX()) == (r.getY() - q.getY())) return false;   // diagonale
	            if ((q.getX() - r.getX()) == (r.getY() - q.getY())) return false;   // diagonale
			}
		}
		return true;
	}
	
	private boolean economique(Reine r, Reine q){
		//conflit entre deux reine r et q
	            if (r.getX() == q.getX())             return false;   // colonne
	            if (r.getY() == q.getY())             return false;  //ligne
	            if ((r.getX() - q.getX()) == (r.getY() - q.getY())) return false;   // diagonale
	            if ((q.getX() - r.getX()) == (r.getY() - q.getY())) return false;   // diagonale
		
		return true;
	}
	
	boolean jeuFini(){
		//le jeu est fini si aucune reine est en conflit avec une autre
		for (Reine  r : m_fenetre.getListeReines()) {
			if(!economique(r))
				return false;
		}
		return true;
	}
	
	
	private Reine setReineParle(){
		//vide la liste si toutes les reines ont parlees
		if(m_listeReineParle.size()==m_fenetre.getNbReines()){
			//empeche la derniere de parler deux fois de suite
			Reine r_last = m_listeReineParle.get(m_listeReineParle.size()-1);
			m_listeReineParle.clear();
			m_listeReineParle.add(r_last);
		}
		
		//choisi une reine qui n'a pas encore parle aleatoirement
		Reine r_parle = m_fenetre.getListeReines().get((int)(Math.random() * m_fenetre.getNbReines())); 
		while(m_listeReineParle.contains(r_parle)){
			r_parle = m_fenetre.getListeReines().get((int)(Math.random() * m_fenetre.getNbReines()));
		}
		m_listeReineParle.add(r_parle);
		return r_parle;
		
	}
	
	Reine getReineParle(){
		//recupere la reine qui est en train de parler
		for (Message  m : m_listeMsg) {
			if(m.getContenu().equals("parle"))
				return (Reine) m.getDestinataire();
		}
		return null;
	}
	
	private Reine getReineBouge(){
		List<Reine> listeReineMsg = new ArrayList<Reine>();
		//recupere toutes les reines qui on deja bouge ou parle
		for (Message  m : m_listeMsg) {
			if(m.getContenu().equals("parle") || m.getContenu().equals("recu"))
				listeReineMsg.add((Reine) m.getDestinataire());
		}
		//prend une reine qui n'est pas dans la liste (ni bouge ni parle)
		for (Reine  r : m_fenetre.getListeReines()) {
			if(!listeReineMsg.contains(r))
				return r;
		}
		return null;
	}
	
	private void bouge(Reine r, Reine q){
		//decale la reine r tant qu'elle est en conflit avec q (reine qui parle)
		while(!economique(r, q)){ 	
			r.setX(r.getX()==m_fenetre.getNbReines()?1:r.getX()+1);
			
		}
	}
	
	void pas(boolean dessine){
		Reine r = getReineBouge();
		//fait bouger une reine et ajoute le message
		bouge(r, getReineParle());
		addMessage(r, "recu");
		//incremente le nombre de pas
		m_nbPas++;
		//si un tour complet a ete fait, remet tout a zero
		if(m_nbPas == m_fenetre.getNbReines()-1){
			m_nbTour++;m_nbPas = 0;
			deleteMessage();
			addMessage(this, "init");
		}
		//redessine le damier
		if(dessine)
			m_fenetre.dessinerDamier();
		
		if(jeuFini()){
			m_nbTour=(m_nbPas==0)?m_nbTour:m_nbTour+1;//incremente le nb de tours si le jeu est fini en milieu de tour
			JOptionPane.showMessageDialog(null, "Toutes les reines ont ete placees correctement !!", "Bravo !", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	void tour(boolean dessine){
		int nTour = m_nbTour;
		//fait un pas tant que le tour est le meme
		while(nTour == m_nbTour && !jeuFini()){
			pas(false);
		}
		if(dessine)
			m_fenetre.dessinerDamier();
	}
	
	void jeu(){
		//fait un tour tant que le jeu n'est pas fini
		while(!jeuFini())
			tour(false);
		m_fenetre.dessinerDamier();
		
	}
	
	void addMessage(Object dest, String cont){
		//ajoute le message a la liste et le traite
		Message msg = new Message(dest, cont);
		m_listeMsg.add(msg);
		traitement(msg);
		
	}
	
	void deleteMessage(Message msg){
		m_listeMsg.remove(msg);
	}
	
	void deleteMessage(){
		m_listeMsg.clear();
	}
}
