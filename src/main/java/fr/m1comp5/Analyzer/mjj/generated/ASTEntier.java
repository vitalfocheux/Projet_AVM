/* Generated By:JJTree: Do not edit this line. ASTEntier.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.Analyzer.mjj.generated;

public
class ASTEntier extends SimpleNode {
  public ASTEntier(int id) {
    super(id);
  }

  public ASTEntier(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8d080c077b2dc4d367924e26753532be (do not edit this line) */
