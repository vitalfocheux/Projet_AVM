/* Generated By:JJTree: Do not edit this line. ASTRien.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.Analyzer.mjj.generated;

public
class ASTRien extends SimpleNode {
  public ASTRien(int id) {
    super(id);
  }

  public ASTRien(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=caf059f2a3fe524b207acbe4e0597fc5 (do not edit this line) */
