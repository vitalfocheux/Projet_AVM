/* Generated By:JJTree: Do not edit this line. ASTentier.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.Analyzer.mjj.generated;

public
class ASTentier extends SimpleNode {
  public ASTentier(int id) {
    super(id);
  }

  public ASTentier(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9c54c23ea77f236097342df8b1124075 (do not edit this line) */
