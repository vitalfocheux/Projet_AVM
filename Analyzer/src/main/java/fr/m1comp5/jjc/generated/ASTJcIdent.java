/* Generated By:JJTree: Do not edit this line. ASTJcIdent.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.jjc.generated;

public
class ASTJcIdent extends fr.m1comp5.custom.node.PositionableJjcSimpleNode {
  public ASTJcIdent(int id) {
    super(id);
  }

  public ASTJcIdent(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) throws fr.m1comp5.custom.exception.VisitorException {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b7c85fc68014c5a83b3e86f8ad1f4cf8 (do not edit this line) */
