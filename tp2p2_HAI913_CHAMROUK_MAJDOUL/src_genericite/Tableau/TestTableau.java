package Tableau;

public class TestTableau {
	
	public static void main(String[] args){
		
		Integer[] intTab = { 10, 2, 6, 11, 7, 2, -1, 0, 9};
		String[] sTab = {"Avion", "zèle", "bien", "moustache", "...", ";", "bonheur", "non", "écolo", "yeux"};

		System.out.println("Integer:");
		Tableau<Integer> t = new Tableau<>(intTab);
		System.out.println(t);
		t.triBulle();
		System.out.println(t);
		System.out.println("String:");
		Tableau<String> t2 = new Tableau<>(sTab);
		System.out.println(t2);
		t2.triBulle();
		System.out.println(t2);
	}

}
