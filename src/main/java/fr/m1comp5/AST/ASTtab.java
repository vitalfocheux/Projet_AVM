/* Generated By:JJTree: Do not edit this line. ASTtab.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.AST;

public
class ASTtab extends SimpleNode {
  public ASTtab(int id) {
    super(id);
  }

  public ASTtab(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=75e791a8a6beb98f5fb760a1f9e9de1e (do not edit this line) */
