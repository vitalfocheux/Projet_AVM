/* Generated By:JJTree: Do not edit this line. ASTFaux.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.mjj.generated;

public
class ASTFaux extends fr.m1comp5.custom.node.PositionableMjjSimpleNode {
  public ASTFaux(int id) {
    super(id);
  }

  public ASTFaux(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) throws fr.m1comp5.custom.exception.VisitorException {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=034d7cfc69cb8c899ba80de8c1a8fbbc (do not edit this line) */
