/* Generated By:JJTree: Do not edit this line. ASTexp.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package fr.m1comp5.Analyzer.mjj.bin;

public class ASTexp extends SimpleNode {
  public ASTexp(int id) {
    super(id);
  }

  public ASTexp(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=31c1735b768ead6b381a8ede727d815b (do not edit this line) */
