/* Generated By:JJTree: Do not edit this line. ASTRetour.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.mjj.generated;

public
class ASTRetour extends SimpleNode {
  public ASTRetour(int id) {
    super(id);
  }

  public ASTRetour(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e8413b9afa8b13b32adc1e9f73c7b198 (do not edit this line) */
