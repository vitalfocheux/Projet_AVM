/* Generated By:JavaCC: Do not edit this line. JajaCodeVisitor.java Version 7.0.13 */
package fr.m1comp5.jjc.generated;

public interface JajaCodeVisitor
{
  public Object visit(SimpleNode node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTRoot node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJajaCode node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcnil node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTInit node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTSwap node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTNew node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTNewArray node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTInvoke node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTLength node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTReturn node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTWrite node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTWriteLn node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTPush node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTPop node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTLoad node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTALoad node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTStore node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTAStore node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTIf node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTGoTo node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTInc node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTAInc node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTNop node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcStop node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcIdent node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTNeg node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTNot node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTAdd node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTSub node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTMul node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTDiv node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTCmp node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTSup node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTOr node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTAnd node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTType node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTSorte node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcNbre node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcVrai node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcFalse node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
  public Object visit(ASTJcChaine node, Object data) throws fr.m1comp5.custom.exception.VisitorException;
}
/* JavaCC - OriginalChecksum=651d4a498c62e44786683e7349b508ac (do not edit this line) */
