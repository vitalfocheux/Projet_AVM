/* Generated By:JJTree: Do not edit this line. ASTInvoke.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.jjc.generated;

public
class ASTInvoke extends fr.m1comp5.custom.node.PositionableJjcSimpleNode {
  public ASTInvoke(int id) {
    super(id);
  }

  public ASTInvoke(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) throws fr.m1comp5.custom.exception.VisitorException {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f9195a0c46ef057ae9bd6b49c3c3fe42 (do not edit this line) */
