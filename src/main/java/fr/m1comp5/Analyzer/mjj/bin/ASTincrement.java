/* Generated By:JJTree: Do not edit this line. ASTincrement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package fr.m1comp5.Analyzer.mjj.bin;

public class ASTincrement extends SimpleNode {
  public ASTincrement(int id) {
    super(id);
  }

  public ASTincrement(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3e6e35892b76c75cdeba58b8d557435c (do not edit this line) */
