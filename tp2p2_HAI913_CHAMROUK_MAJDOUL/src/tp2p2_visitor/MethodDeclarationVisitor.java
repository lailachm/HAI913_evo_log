package tp2p2_visitor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public class MethodParamNumberComparator implements Comparator<MethodDeclaration> {

		@Override
		public int compare(MethodDeclaration o1, MethodDeclaration o2) {
			return o1.parameters().size() - o2.parameters().size();
		}
		
	}
	
	public long getMaxNbParamsPerMethod() {
		return this.getMethods()
				.stream()
				.max(new MethodParamNumberComparator())
				.get().parameters().size();
	}
}
