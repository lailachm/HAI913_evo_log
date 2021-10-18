package tp2p2_visitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class Parser {
	
	public static final String projectPath = "C:\\Users\\Laïla\\Downloads\\pourTP_Evol_logiciel_HLIN505\\HLIN505\\Genericite";
	public static final String projectSourcePath = projectPath + "\\src_genericite";
	public static final String jrePath = "C:\\Program Files (x86)\\Java\\jre1.8.0_301\\lib\\rt.jar";

	public static void main(String[] args) throws IOException {
		
		final File folder = new File(projectSourcePath);
		
		//qu1
		ArrayList<String> listClassInApp=new ArrayList<String>();
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder, listClassInApp);
		
		//qu2
		long lines = 0;
		
		//qu3
		int nbOfMethodInClass = 0;
		ArrayList<String> listMethodInApp=new ArrayList<String>();
		
		//qu4
		HashMap<String, Integer> listPackageInApp = new HashMap<String, Integer>();
		
		//qu6
		ArrayList<Integer> listLinesInMethod=new ArrayList<Integer>();
		
		//qu7
		int nbOfVariablesInClass = 0;
		ArrayList<Integer> listNbOfVariables=new ArrayList<Integer>();
		
		//qu8
		HashMap<String, Integer> nbOfMethodPerClass = new HashMap<String, Integer>();
		
		//qu9
		HashMap<String, Integer> nbOfVariablesPerClass = new HashMap<String, Integer>();
		
		//qu13
		long nbParamInClass = 0;
		ArrayList<Integer> listNbMaxParam=new ArrayList<Integer>();
		
		//qu12
		ArrayList<String> question12=new ArrayList<String>();
		
		//ex2
		System.out.println("\n ---GRAPHE D'APPEL---");
		
		
		for (File fileEntry : javaFiles) {
			
			String content = FileUtils.readFileToString(fileEntry);
			
			//qu2
			lines += content.lines().count();
			
			CompilationUnit parse = parse(content.toCharArray());

			//qu3
			nbOfMethodInClass = navigateMethodInfo(parse, listMethodInApp);
			
			//qu4
			navigatePackageInfo(parse, listPackageInApp);
			
			//qu6
			numberOfLineInMethod(parse, listLinesInMethod);
			
			//qu7
			nbOfVariablesInClass = navigateVariableInfo(parse, listNbOfVariables);
			
			//qu8
			nbOfMethodPerClass.put(fileEntry.getName(), nbOfMethodInClass);
			
			//qu9
			nbOfVariablesPerClass.put(fileEntry.getName(), nbOfVariablesInClass);
			
			//qu13
			nbParamInClass = navigateParamInfo(parse);
			listNbMaxParam.add((int) nbParamInClass);
			
			//qu12
			HashMap<String, Integer> listNbOfLinesInMethodInClass = new HashMap<String, Integer>();
			numberOfLineInMethodInClass(parse, listNbOfLinesInMethodInClass);
			HashMap<String, Integer> listLinesInMethodInClassSorted = sortByValue(listNbOfLinesInMethodInClass); //contient les couples (methodName, nbOfLine)
			int c=0;
			float pc = (float)(nbOfMethodInClass*10)/100;
			int i=0;
			for (Entry<String, Integer> entry : listLinesInMethodInClassSorted.entrySet()) {
			    String key = entry.getKey();
			    Integer value = entry.getValue();
				String methodName = key + c;
				c++;
				if(i<Math.ceil(pc)) {
		   			String strQu12 = "Dans le fichier " + fileEntry.getName() + " \n";
		   			strQu12 += "La méthode " + key.substring(0, key.length() - 1) + " contient " + value + " ligne(s) de code. \n";
			   	    question12.add(strQu12);
			   	    i++;
		   		}
			}
			
			//ex2
			printMethodInvocationInfo(parse);
		}
		
		System.out.println("\n ---1. NOMBRE DE CLASSES DANS L'APPLICATION---");
		int nbOfClassInApp = listClassInApp.size();
	   	System.out.println(nbOfClassInApp);
		
		System.out.println("\n ---2. NOMBRE DE LIGNES DE CODE DANS L'APPLICATION---");
	   	System.out.println(lines);
	   	
	   	System.out.println("\n ---3. NOMBRE TOTAL DE METHODES DANS L'APPLICATION---");
	   	int nbOfMethodInApp = listMethodInApp.size();
	   	System.out.println(nbOfMethodInApp);
	   	
	   	System.out.println("\n ---4. NOMBRE TOTAL DE PACKAGES DANS L'APPLICATION---");
	   	int nbOfPackagesInApp = listPackageInApp.keySet().size();
	   	System.out.println(nbOfPackagesInApp);
	   	
	   	System.out.println("\n ---5. NOMBRE MOYEN DE METHODES PAR CLASSE---");
	   	System.out.println(nbOfMethodInApp/nbOfClassInApp);
	   	
	   	System.out.println("\n ---6. NOMBRE MOYEN DE LIGNES DE CODE PAR METHODE---");
	   	int c=0;
		for(int nbOfLineInMethod : listLinesInMethod) { 
			c+=nbOfLineInMethod;
		}
	   	System.out.println(c/nbOfMethodInApp);
	   	
	   	System.out.println("\n ---7. NOMBRE MOYEN D'ATTRIBUTS PAR CLASSE---");
	   	int d = 0;
	   	for(int nbOfVariables : listNbOfVariables) {
			d+=nbOfVariables;
		}
	   	System.out.println(d/nbOfClassInApp);
	   	
	   	System.out.println("\n ---8. LES 10% DE CLASSES QUI ONT LE PLUS DE METHODES---");
	   	HashMap<String, Integer> nbOfMethodPerClassSorted = sortByValue(nbOfMethodPerClass);
	   	ArrayList<String> listKey1=new ArrayList<String>();
	   	float tenpcNbMethodClass = (float)(nbOfClassInApp*10)/100;
	   	int i=0;
	   	for (String name: nbOfMethodPerClassSorted.keySet()) {
	   		if(i<Math.ceil(tenpcNbMethodClass)) {
		   	    String key = name.toString();
		   	    listKey1.add(key);
		   	    String value = nbOfMethodPerClassSorted.get(name).toString();
		   	    System.out.println("La classe " + key + " a " + value + " méthode(s).");
		   	    i++;
	   		}		   	
	   	}
	   	
	   	System.out.println("\n ---9. LES 10% DE CLASSES QUI ONT LE PLUS D'ATTRIBUTS---");
	   	HashMap<String, Integer> nbOfVariablesPerClassSorted = sortByValue(nbOfVariablesPerClass);
	   	ArrayList<String> listKey2=new ArrayList<String>();
	   	float tenpcNbVariableClass = (float)(nbOfClassInApp*10)/100;
	   	int j=0;
	   	for (String name: nbOfVariablesPerClassSorted.keySet()) {
	   		if(j<Math.ceil(tenpcNbVariableClass)) {
		   	    String key = name.toString();
		   	    listKey2.add(key);
		   	    String value = nbOfVariablesPerClassSorted.get(name).toString();
		   	    System.out.println("La classe " + key + " a " + value + " variable(s).");
		   	    j++;
	   		}		   	
	   	}
	   	
	   	System.out.println("\n ---10. LES CLASSES QUI FONT PARTIE DES 2 CATEGORIES PRECEDENTES EN MÊME TEMPS---");
	   	ArrayList<String> compareKeys=new ArrayList<String>(listKey1);
	   	compareKeys.retainAll(listKey2);
	   	System.out.println(compareKeys);
	   	
	   	System.out.println("\n ---11. LES CLASSES QUI ONT PLUS DE X METHODES (ici 5)---");
	   	for (String name: nbOfMethodPerClassSorted.keySet()) {
	   	    String key = name.toString();
	   	    String value = nbOfMethodPerClassSorted.get(name).toString();
	   	    if(Integer.parseInt(value) > 5) {
	   	    	System.out.println("La classe " + key + " a " + value + " méthode(s).");
	   	    }
	   	}
	   	
	   	System.out.println("\n ---13. NOMBRE MAXIMAL DE PARAMETRES PARMI TOUTES LES METHODES DE L'APPLICATION---");
	   	System.out.println(Collections.max(listNbMaxParam));
	   	
	   	System.out.println("\n ---12. LES 10% DE METHODES QUI ONT LE PLUS GRAND NOMBRE DE LIGNES DE CODE (par classes)---");
	   	for(int q12=0; q12<question12.size(); q12++) {
	   		System.out.println(question12.get(q12));
	   	}
	   	
	}
	
	/*
	 * read all java files from specific folder
	 * parameter listClass will contain the name of all the classes
	 */
	public static ArrayList<File> listJavaFilesForFolder(final File folder, ArrayList<String> listClass) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry, listClass));
			} else if (fileEntry.getName().contains(".java")) {
	   			listClass.add(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}
	

	/*
	 * navigate method information 
	 * parameter listMethod will contain the name of all the methods
	 * return the total number of method in a class
	 */
	public static int navigateMethodInfo(CompilationUnit parse, ArrayList<String> listMethod) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		
	   	int nbDeMethodes=0;

		for (MethodDeclaration method : visitor.getMethods()) {
			nbDeMethodes++;
	  		listMethod.add(method.getName().toString());
		}
		
		return nbDeMethodes;
	}
	
	/*
	 * navigate package information 
	 * parameter listPackage will contain the name of all the packages
	 */
	public static void navigatePackageInfo(CompilationUnit parse, HashMap<String, Integer> listPackage) {
		PackageDeclarationVisitor visitor = new PackageDeclarationVisitor();
		parse.accept(visitor);
		
		int noPackage = 0;
		
		for (PackageDeclaration method : visitor.getPackages()) {
			listPackage.put(method.getName().toString(), noPackage);
			noPackage++;
		}
		
	}
	
	/*
	 * parameter listLinesInMethod will contain the list of the number of code lines for each method
	 * this will be useful to calculate the average number of line per method
	 */
	public static void numberOfLineInMethod(CompilationUnit parse, ArrayList<Integer> listLinesInMethod) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		
		long lines =0;

		for (MethodDeclaration method : visitor.getMethods()) {
			String content = method.toString();
			lines = content.lines().count();
			listLinesInMethod.add((int) lines);
		}
	}
	
	/*
	 * navigate variables information
	 * parameter listVariables will contain the list of the number of variables for each method in the app
	 * return the number of variable in a class
	 */
	public static int navigateVariableInfo(CompilationUnit parse, ArrayList<Integer> listVariables) {
		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		
		int nbOfVariables = 0;

		for (MethodDeclaration method : visitor1.getMethods()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2.getVariables()) {
				nbOfVariables++;
			}

		}
		listVariables.add(nbOfVariables);
		
		return nbOfVariables;
	}
	
	/*
	 * navigate parameters information
	 * return the maximum number of parameters in a class
	 */
	public static long navigateParamInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		
	   	long nbMaxParamInMethod=visitor.getMaxNbParamsPerMethod();

		return nbMaxParamInMethod;
	}
	
	/*
	 * parameter listLinesInMethodInClass will contain the number of lines per methods in a class
	 */
	public static void numberOfLineInMethodInClass(CompilationUnit parse, HashMap<String, Integer> listLinesInMethodInClass) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		
		long lines =0;
		int i=0;
		for (MethodDeclaration method : visitor.getMethods()) {
			String content = method.toString();
			lines = content.lines().count();
			String methodName = method.getName().toString() + i;
			i++;
			listLinesInMethodInClass.put(methodName, (int) lines);
			
		}
	}
	
	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethods()) {
			System.out.println("Method name: " + method.getName()
					+ " Return type: " + method.getReturnType2());
		}

	}
	
	// navigate method invocations inside method
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {
			
			System.out.println("Method name: " + method.getName()
			+ " Return type: " + method.getReturnType2());

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);

			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				System.out.println("\t\t|___ method " + method.getName() + " invoc method "
						+ methodInvocation.getName());
			}

		}
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}
	
	// function to sort hashmap by values
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
 
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
         
        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
