/* Generated By:JJTree: Do not edit this line. ASTCst.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.mjj.generated;

public
class ASTCst extends SimpleNode {
  public ASTCst(int id) {
    super(id);
  }

  public ASTCst(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f76112f3b098417c3ed980eee8474518 (do not edit this line) */
