/* Generated By:JJTree: Do not edit this line. ASTType.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package fr.m1comp5.Analyzer.jjc.bin;

public class ASTType extends SimpleNode {
  public ASTType(int id) {
    super(id);
  }

  public ASTType(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=589ed275dd7f1f72eaf675785527d23a (do not edit this line) */
