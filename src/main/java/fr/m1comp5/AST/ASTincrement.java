/* Generated By:JJTree: Do not edit this line. ASTincrement.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.AST;

public
class ASTincrement extends SimpleNode {
  public ASTincrement(int id) {
    super(id);
  }

  public ASTincrement(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=28ae33858d1e6026904ddfd4836ce923 (do not edit this line) */
