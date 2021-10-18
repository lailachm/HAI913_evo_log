package Pile;
public class TestPile {
	
	public static void main(String[] args){
		
		CPile<Integer> p = new CPile<>();

		System.out.println("Integer:");

		for(int i = 0 ; i < 10 ; i++)
			p.empile(i);

		System.out.println(p);

		for(int i = 0 ; i < 3 ; i++)
			p.depile();

		System.out.println(p);

		System.out.println("String:");

		CPile<String> p2 = new CPile<>();

		p2.empile("lol");
		p2.empile("L'informatique");
		p2.empile("Beaucoup");
		p2.empile("J'aime");

		System.out.println(p2);
		System.out.println(p2.nbElement());

	}

}
