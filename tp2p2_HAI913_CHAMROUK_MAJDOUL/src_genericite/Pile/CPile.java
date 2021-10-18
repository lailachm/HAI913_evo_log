package Pile;
import java.util.LinkedList;

public class CPile<A> implements IPile<A> {
	private LinkedList<A> data;
	
	public CPile(){
		data = new LinkedList<A>();
	}
	
	public boolean estVide(){
		return data.size() == 0;
	}

	public void empile(A a){
		data.addFirst(a);
	}
	
	public A depile(){
		return data.removeFirst();
	}
	
	public int nbElement(){
		return data.size();
	}
	
	public A sommet(){
		return data.element();
	}

	public String toString(){
		String s = new String();
		for(A a : data){
			s += a.toString() + " ";
		}
		return s;
	}

}
