/* Generated By:JJTree: Do not edit this line. ASTJcNbre.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.jjc.generated;

public
class ASTJcNbre extends fr.m1comp5.custom.node.PositionableJjcSimpleNode {
  public ASTJcNbre(int id) {
    super(id);
  }

  public ASTJcNbre(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) throws fr.m1comp5.custom.exception.VisitorException {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e9dd3708a7f13a943ccad31cb047cffe (do not edit this line) */