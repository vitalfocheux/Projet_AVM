/* Generated By:JJTree: Do not edit this line. ASTAdd.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package fr.m1comp5.jjc.generated;

public
class ASTAdd extends SimpleNode {
  public ASTAdd(int id) {
    super(id);
  }

  public ASTAdd(JajaCode p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JajaCodeVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=34d78959548ef53b009b19a3e06a4324 (do not edit this line) */
