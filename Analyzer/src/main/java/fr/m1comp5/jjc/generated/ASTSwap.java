/* Generated By:JJTree: Do not edit this line. ASTSwap.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.jjc.generated;

public
class ASTSwap extends SimpleNode {
  public ASTSwap(int id) {
    super(id);
  }

  public ASTSwap(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3a3acf6dfe17b07350a87aa0b2d608a1 (do not edit this line) */
