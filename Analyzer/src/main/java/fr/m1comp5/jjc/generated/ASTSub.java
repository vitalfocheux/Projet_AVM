/* Generated By:JJTree: Do not edit this line. ASTSub.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.jjc.generated;

public
class ASTSub extends SimpleNode {
  public ASTSub(int id) {
    super(id);
  }

  public ASTSub(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2f047def66d1198980b94fedc54c4f52 (do not edit this line) */
