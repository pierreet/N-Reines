package v2;

public class Message {

	private Object m_dest;
	private String m_cont;
	
	Message(Object dest, String cont){
		m_dest = dest;
		m_cont = cont;
	}
	
	
	public Object getDestinataire(){
		return m_dest;
	}
	public String getContenu(){
		return m_cont;
	}
	
}
