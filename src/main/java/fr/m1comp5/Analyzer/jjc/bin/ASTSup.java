/* Generated By:JJTree: Do not edit this line. ASTSup.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package fr.m1comp5.Analyzer.jjc.bin;

public class ASTSup extends SimpleNode {
  public ASTSup(int id) {
    super(id);
  }

  public ASTSup(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0d527837a1105b56bfd5c81ffabf2c59 (do not edit this line) */
