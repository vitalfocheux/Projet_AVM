/* Generated By:JJTree: Do not edit this line. ASTecrire.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.AST;

public
class ASTecrire extends SimpleNode {
  public ASTecrire(int id) {
    super(id);
  }

  public ASTecrire(MiniJaja p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJajaVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3df23f479820267e3c3047cbec5d4d0d (do not edit this line) */
