/* MiniJajaTokenManager.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. MiniJajaTokenManager.java */
package fr.m1comp5.Analyzer;
import java.io.FileReader;

/** Token Manager. */
@SuppressWarnings ("unused")
public class MiniJajaTokenManager implements MiniJajaConstants {

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x1000000L) != 0L)
            return 7;
         if ((active0 & 0x3fff80L) != 0L)
         {
            jjmatchedKind = 43;
            return 1;
         }
         return -1;
      case 1:
         if ((active0 & 0x2fff80L) != 0L)
         {
            jjmatchedKind = 43;
            jjmatchedPos = 1;
            return 1;
         }
         if ((active0 & 0x100000L) != 0L)
            return 1;
         return -1;
      case 2:
         if ((active0 & 0x2ffb80L) != 0L)
         {
            jjmatchedKind = 43;
            jjmatchedPos = 2;
            return 1;
         }
         if ((active0 & 0x400L) != 0L)
            return 1;
         return -1;
      case 3:
         if ((active0 & 0xfd880L) != 0L)
         {
            jjmatchedKind = 43;
            jjmatchedPos = 3;
            return 1;
         }
         if ((active0 & 0x202300L) != 0L)
            return 1;
         return -1;
      case 4:
         if ((active0 & 0x28800L) != 0L)
         {
            if (jjmatchedPos != 4)
            {
               jjmatchedKind = 43;
               jjmatchedPos = 4;
            }
            return 1;
         }
         if ((active0 & 0xd5080L) != 0L)
            return 1;
         return -1;
      case 5:
         if ((active0 & 0x80800L) != 0L)
         {
            jjmatchedKind = 43;
            jjmatchedPos = 5;
            return 1;
         }
         if ((active0 & 0x28000L) != 0L)
            return 1;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 33:
         return jjStopAtPos(0, 31);
      case 38:
         return jjMoveStringLiteralDfa1_0(0x10000000L);
      case 40:
         return jjStopAtPos(0, 39);
      case 41:
         return jjStopAtPos(0, 40);
      case 42:
         return jjStopAtPos(0, 25);
      case 43:
         jjmatchedKind = 22;
         return jjMoveStringLiteralDfa1_0(0x300000000L);
      case 44:
         return jjStopAtPos(0, 36);
      case 45:
         return jjStopAtPos(0, 23);
      case 47:
         return jjStartNfaWithStates_0(0, 24, 7);
      case 59:
         return jjStopAtPos(0, 35);
      case 60:
         return jjStopAtPos(0, 27);
      case 61:
         jjmatchedKind = 34;
         return jjMoveStringLiteralDfa1_0(0x40000000L);
      case 62:
         return jjStopAtPos(0, 26);
      case 91:
         return jjStopAtPos(0, 41);
      case 93:
         return jjStopAtPos(0, 42);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x200000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x5000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x100400L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x20000L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x2000L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0xd0000L);
      case 123:
         return jjStopAtPos(0, 37);
      case 124:
         return jjMoveStringLiteralDfa1_0(0x20000000L);
      case 125:
         return jjStopAtPos(0, 38);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 38:
         if ((active0 & 0x10000000L) != 0L)
            return jjStopAtPos(1, 28);
         break;
      case 43:
         if ((active0 & 0x100000000L) != 0L)
            return jjStopAtPos(1, 32);
         break;
      case 61:
         if ((active0 & 0x40000000L) != 0L)
            return jjStopAtPos(1, 30);
         else if ((active0 & 0x200000000L) != 0L)
            return jjStopAtPos(1, 33);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x4100L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x28000L);
      case 102:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(1, 20, 1);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x10000L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x200080L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0xa00L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0xc2000L);
      case 124:
         if ((active0 & 0x20000000L) != 0L)
            return jjStopAtPos(1, 29);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x80L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0xd0300L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x9000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x800L);
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x200000L);
      case 116:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(2, 10, 1);
         return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
      case 100:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(3, 9, 1);
         break;
      case 101:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(3, 13, 1);
         else if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(3, 21, 1);
         break;
      case 103:
         return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x10800L);
      case 110:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(3, 8, 1);
         break;
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x4080L);
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0xc0000L);
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x20000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(4, 14, 1);
         else if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(4, 16, 1);
         else if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 18;
            jjmatchedPos = 4;
         }
         return jjMoveStringLiteralDfa5_0(active0, 0x80800L);
      case 108:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(4, 12, 1);
         break;
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x20000L);
      case 115:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(4, 7, 1);
         break;
      case 116:
         return jjMoveStringLiteralDfa5_0(active0, 0x8000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa6_0(active0, 0x800L);
      case 104:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(5, 15, 1);
         break;
      case 108:
         return jjMoveStringLiteralDfa6_0(active0, 0x80000L);
      case 110:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(5, 17, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(6, 11, 1);
         else if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(6, 19, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 18;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 44)
                        kind = 44;
                     { jjCheckNAdd(2); }
                  }
                  else if (curChar == 47)
                     { jjAddStates(0, 1); }
                  else if (curChar == 34)
                     { jjCheckNAddTwoStates(4, 5); }
                  break;
               case 7:
                  if (curChar == 42)
                     { jjCheckNAddTwoStates(13, 14); }
                  else if (curChar == 47)
                     { jjCheckNAddStates(2, 4); }
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 43)
                     kind = 43;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 44)
                     kind = 44;
                  { jjCheckNAdd(2); }
                  break;
               case 3:
                  if (curChar == 34)
                     { jjCheckNAddTwoStates(4, 5); }
                  break;
               case 4:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(4, 5); }
                  break;
               case 5:
                  if (curChar == 34 && kind > 45)
                     kind = 45;
                  break;
               case 6:
                  if (curChar == 47)
                     { jjAddStates(0, 1); }
                  break;
               case 8:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     { jjCheckNAddStates(2, 4); }
                  break;
               case 9:
                  if ((0x2400L & l) != 0L && kind > 5)
                     kind = 5;
                  break;
               case 10:
                  if (curChar == 10 && kind > 5)
                     kind = 5;
                  break;
               case 11:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 12:
                  if (curChar == 42)
                     { jjCheckNAddTwoStates(13, 14); }
                  break;
               case 13:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(13, 14); }
                  break;
               case 14:
                  if (curChar == 42)
                     { jjAddStates(5, 6); }
                  break;
               case 15:
                  if ((0xffff7fffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(16, 14); }
                  break;
               case 16:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(16, 14); }
                  break;
               case 17:
                  if (curChar == 47 && kind > 6)
                     kind = 6;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 43)
                     kind = 43;
                  { jjCheckNAdd(1); }
                  break;
               case 1:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 43)
                     kind = 43;
                  { jjCheckNAdd(1); }
                  break;
               case 4:
                  { jjAddStates(7, 8); }
                  break;
               case 8:
                  { jjAddStates(2, 4); }
                  break;
               case 13:
                  { jjCheckNAddTwoStates(13, 14); }
                  break;
               case 15:
               case 16:
                  { jjCheckNAddTwoStates(16, 14); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 4:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjAddStates(7, 8); }
                  break;
               case 8:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjAddStates(2, 4); }
                  break;
               case 13:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjCheckNAddTwoStates(13, 14); }
                  break;
               case 15:
               case 16:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjCheckNAddTwoStates(16, 14); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 18 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, "\143\154\141\163\163", 
"\155\141\151\156", "\166\157\151\144", "\151\156\164", "\142\157\157\154\145\141\156", 
"\146\151\156\141\154", "\164\162\165\145", "\146\141\154\163\145", "\154\145\156\147\164\150", 
"\167\150\151\154\145", "\162\145\164\165\162\156", "\167\162\151\164\145", 
"\167\162\151\164\145\154\156", "\151\146", "\145\154\163\145", "\53", "\55", "\57", "\52", "\76", "\74", 
"\46\46", "\174\174", "\75\75", "\41", "\53\53", "\53\75", "\75", "\73", "\54", "\173", 
"\175", "\50", "\51", "\133", "\135", null, null, null, };
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}
static final int[] jjnextStates = {
   7, 12, 8, 9, 11, 15, 17, 4, 5, 
};

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(Exception e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

void SkipLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
void MoreLexicalActions()
{
   jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
void TokenLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

    /** Constructor. */
    public MiniJajaTokenManager(SimpleCharStream stream){

      if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

    input_stream = stream;
  }

  /** Constructor. */
  public MiniJajaTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  
  public void ReInit(SimpleCharStream stream)
  {


    jjmatchedPos =
    jjnewStateCnt =
    0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 18; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream, int lexState)
  
  {
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public void SwitchTo(int lexState)
  {
    if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }


/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x3fffffffff81L, 
};
static final long[] jjtoSkip = {
   0x7eL, 
};
static final long[] jjtoSpecial = {
   0x0L, 
};
static final long[] jjtoMore = {
   0x0L, 
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[18];
    private final int[] jjstateSet = new int[2 * 18];
    private final StringBuilder jjimage = new StringBuilder();
    private StringBuilder image = jjimage;
    private int jjimageLen;
    private int lengthOfMatch;
    protected int curChar;
}
