package tp2p2_visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class PackageDeclarationVisitor extends ASTVisitor {
	private List<PackageDeclaration> packages = new ArrayList<>();
	private HashMap<String,Integer> names = new HashMap<String,Integer>();
	public static int noPackage = 0;
	
	@Override
	public boolean visit(PackageDeclaration node) {
		packages.add(node);
		names.put(node.getName().toString(), noPackage);
		noPackage++;
		
		return super.visit(node);
	}
	
	public List<PackageDeclaration> getPackages() {
		return packages;
	}
	
	public Set<String> getNames() {
		return names.keySet();
	}

	public long getNbPackages() {
		return names.size();
	}
	
	public static String getFullName(TypeDeclaration type) {
		String name = type.getName().getIdentifier();
		ASTNode parent = type.getParent();
		
		while(parent != null && parent.getClass() == TypeDeclaration.class) {
			name = ((TypeDeclaration) parent).getName().getIdentifier() + "." + name;
			parent = parent.getParent();
		}
		
		if (type.getRoot().getClass() == CompilationUnit.class) {
			CompilationUnit root = (CompilationUnit) type.getRoot();
			
			if(root.getPackage() != null) {
				PackageDeclaration pack = root.getPackage();
				name = pack.getName().getFullyQualifiedName() + "." + name;
			}
		}
		
		return name;
	}
}