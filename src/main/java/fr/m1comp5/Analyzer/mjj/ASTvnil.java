/* Generated By:JJTree: Do not edit this line. ASTvnil.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.Analyzer.mjj;

public
class ASTvnil extends SimpleNode {
  public ASTvnil(int id) {
    super(id);
  }

  public ASTvnil(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2010c26f898b7231dda69a95b9d4892c (do not edit this line) */
