/* Generated By:JJTree: Do not edit this line. ASTdiv.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.Analyzer.mjj;

public
class ASTdiv extends SimpleNode {
  public ASTdiv(int id) {
    super(id);
  }

  public ASTdiv(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5e0a1ad945b668568f99e244d19f0518 (do not edit this line) */
