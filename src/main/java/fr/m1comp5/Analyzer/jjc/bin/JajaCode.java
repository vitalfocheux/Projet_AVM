/* Generated By:JJTree&JavaCC: Do not edit this line. JajaCode.java */
package fr.m1comp5.Analyzer.jjc.bin;

public class JajaCode/*@bgen(jjtree)*/implements JajaCodeTreeConstants, JajaCodeConstants {/*@bgen(jjtree)*/
  protected static JJTJajaCodeState jjtree = new JJTJajaCodeState();

  static final public SimpleNode start() throws ParseException {
                            /*@bgen(jjtree) Root */
  ASTRoot jjtn000 = new ASTRoot(JJTROOT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      classe();
               jjtree.closeNodeScope(jjtn000, true);
               jjtc000 = false;
              {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final private void classe() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
      adresse();
      instr();
      jj_consume_token(SEMICOLON);
                                    ASTJajaCode jjtn001 = new ASTJajaCode(JJTJAJACODE);
                                    boolean jjtc001 = true;
                                    jjtree.openNodeScope(jjtn001);
      try {
        classe();
      } catch (Throwable jjte001) {
                                    if (jjtc001) {
                                      jjtree.clearNodeScope(jjtn001);
                                      jjtc001 = false;
                                    } else {
                                      jjtree.popNode();
                                    }
                                    if (jjte001 instanceof RuntimeException) {
                                      {if (true) throw (RuntimeException)jjte001;}
                                    }
                                    if (jjte001 instanceof ParseException) {
                                      {if (true) throw (ParseException)jjte001;}
                                    }
                                    {if (true) throw (Error)jjte001;}
      } finally {
                                    if (jjtc001) {
                                      jjtree.closeNodeScope(jjtn001,  3);
                                    }
      }
      break;
    default:
      jj_la1[0] = jj_gen;
      ASTJcnil jjtn002 = new ASTJcnil(JJTJCNIL);
      boolean jjtc002 = true;
      jjtree.openNodeScope(jjtn002);
      try {
        empty();
      } catch (Throwable jjte002) {
      if (jjtc002) {
        jjtree.clearNodeScope(jjtn002);
        jjtc002 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte002 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte002;}
      }
      if (jjte002 instanceof ParseException) {
        {if (true) throw (ParseException)jjte002;}
      }
      {if (true) throw (Error)jjte002;}
      } finally {
      if (jjtc002) {
        jjtree.closeNodeScope(jjtn002, true);
      }
      }
    }
  }

  static final private void instr() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INIT:
      ASTInit jjtn001 = new ASTInit(JJTINIT);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
        jj_consume_token(INIT);
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001, true);
      }
      }
      break;
    case SWAP:
      ASTSwap jjtn002 = new ASTSwap(JJTSWAP);
      boolean jjtc002 = true;
      jjtree.openNodeScope(jjtn002);
      try {
        jj_consume_token(SWAP);
      } finally {
      if (jjtc002) {
        jjtree.closeNodeScope(jjtn002, true);
      }
      }
      break;
    case NEW:
      jj_consume_token(NEW);
      jj_consume_token(LPAR);
      ident();
      jj_consume_token(COMMA);
      type();
      jj_consume_token(COMMA);
      sorte();
      jj_consume_token(COMMA);
      adresse();
                                                                            ASTNew jjtn003 = new ASTNew(JJTNEW);
                                                                            boolean jjtc003 = true;
                                                                            jjtree.openNodeScope(jjtn003);
      try {
        jj_consume_token(RPAR);
      } finally {
                                                                            if (jjtc003) {
                                                                              jjtree.closeNodeScope(jjtn003,  4);
                                                                            }
      }
      break;
    case NEWA:
      jj_consume_token(NEWA);
      jj_consume_token(LPAR);
      ident();
      jj_consume_token(COMMA);
      type();
                                           ASTNewArray jjtn004 = new ASTNewArray(JJTNEWARRAY);
                                           boolean jjtc004 = true;
                                           jjtree.openNodeScope(jjtn004);
      try {
        jj_consume_token(RPAR);
      } finally {
                                           if (jjtc004) {
                                             jjtree.closeNodeScope(jjtn004,  2);
                                           }
      }
      break;
    case INVOKE:
      jj_consume_token(INVOKE);
      jj_consume_token(LPAR);
      ident();
                              ASTInvoke jjtn005 = new ASTInvoke(JJTINVOKE);
                              boolean jjtc005 = true;
                              jjtree.openNodeScope(jjtn005);
      try {
        jj_consume_token(RPAR);
      } finally {
                              if (jjtc005) {
                                jjtree.closeNodeScope(jjtn005,  1);
                              }
      }
      break;
    case LENGTH:
      jj_consume_token(LENGTH);
      jj_consume_token(LPAR);
      ident();
                              ASTLength jjtn006 = new ASTLength(JJTLENGTH);
                              boolean jjtc006 = true;
                              jjtree.openNodeScope(jjtn006);
      try {
        jj_consume_token(RPAR);
      } finally {
                              if (jjtc006) {
                                jjtree.closeNodeScope(jjtn006,  1);
                              }
      }
      break;
    case RETURN:
      ASTReturn jjtn007 = new ASTReturn(JJTRETURN);
      boolean jjtc007 = true;
      jjtree.openNodeScope(jjtn007);
      try {
        jj_consume_token(RETURN);
      } finally {
      if (jjtc007) {
        jjtree.closeNodeScope(jjtn007, true);
      }
      }
      break;
    case WRITE:
      ASTWrite jjtn008 = new ASTWrite(JJTWRITE);
      boolean jjtc008 = true;
      jjtree.openNodeScope(jjtn008);
      try {
        jj_consume_token(WRITE);
      } finally {
      if (jjtc008) {
        jjtree.closeNodeScope(jjtn008, true);
      }
      }
      break;
    case WRITELN:
      ASTWriteLn jjtn009 = new ASTWriteLn(JJTWRITELN);
      boolean jjtc009 = true;
      jjtree.openNodeScope(jjtn009);
      try {
        jj_consume_token(WRITELN);
      } finally {
      if (jjtc009) {
        jjtree.closeNodeScope(jjtn009, true);
      }
      }
      break;
    case PUSH:
      jj_consume_token(PUSH);
      jj_consume_token(LPAR);
      value();
                            ASTPush jjtn010 = new ASTPush(JJTPUSH);
                            boolean jjtc010 = true;
                            jjtree.openNodeScope(jjtn010);
      try {
        jj_consume_token(RPAR);
      } finally {
                            if (jjtc010) {
                              jjtree.closeNodeScope(jjtn010,  1);
                            }
      }
      break;
    case POP:
      ASTPop jjtn011 = new ASTPop(JJTPOP);
      boolean jjtc011 = true;
      jjtree.openNodeScope(jjtn011);
      try {
        jj_consume_token(POP);
      } finally {
      if (jjtc011) {
        jjtree.closeNodeScope(jjtn011, true);
      }
      }
      break;
    case LOAD:
      jj_consume_token(LOAD);
      jj_consume_token(LPAR);
      ident();
                            ASTLoad jjtn012 = new ASTLoad(JJTLOAD);
                            boolean jjtc012 = true;
                            jjtree.openNodeScope(jjtn012);
      try {
        jj_consume_token(RPAR);
      } finally {
                            if (jjtc012) {
                              jjtree.closeNodeScope(jjtn012,  1);
                            }
      }
      break;
    case ALOAD:
      jj_consume_token(ALOAD);
      jj_consume_token(LPAR);
      ident();
                             ASTALoad jjtn013 = new ASTALoad(JJTALOAD);
                             boolean jjtc013 = true;
                             jjtree.openNodeScope(jjtn013);
      try {
        jj_consume_token(RPAR);
      } finally {
                             if (jjtc013) {
                               jjtree.closeNodeScope(jjtn013,  1);
                             }
      }
      break;
    case STORE:
      jj_consume_token(STORE);
      jj_consume_token(LPAR);
      ident();
                             ASTStore jjtn014 = new ASTStore(JJTSTORE);
                             boolean jjtc014 = true;
                             jjtree.openNodeScope(jjtn014);
      try {
        jj_consume_token(RPAR);
      } finally {
                             if (jjtc014) {
                               jjtree.closeNodeScope(jjtn014,  1);
                             }
      }
      break;
    case ASTORE:
      jj_consume_token(ASTORE);
      jj_consume_token(LPAR);
      ident();
                              ASTAStore jjtn015 = new ASTAStore(JJTASTORE);
                              boolean jjtc015 = true;
                              jjtree.openNodeScope(jjtn015);
      try {
        jj_consume_token(RPAR);
      } finally {
                              if (jjtc015) {
                                jjtree.closeNodeScope(jjtn015,  1);
                              }
      }
      break;
    case IF:
      jj_consume_token(IF);
      jj_consume_token(LPAR);
      adresse();
                            ASTIf jjtn016 = new ASTIf(JJTIF);
                            boolean jjtc016 = true;
                            jjtree.openNodeScope(jjtn016);
      try {
        jj_consume_token(RPAR);
      } finally {
                            if (jjtc016) {
                              jjtree.closeNodeScope(jjtn016,  1);
                            }
      }
      break;
    case GOTO:
      jj_consume_token(GOTO);
      jj_consume_token(LPAR);
      adresse();
                              ASTGoTo jjtn017 = new ASTGoTo(JJTGOTO);
                              boolean jjtc017 = true;
                              jjtree.openNodeScope(jjtn017);
      try {
        jj_consume_token(RPAR);
      } finally {
                              if (jjtc017) {
                                jjtree.closeNodeScope(jjtn017,  1);
                              }
      }
      break;
    case INC:
      jj_consume_token(INC);
      jj_consume_token(LPAR);
      ident();
                           ASTInc jjtn018 = new ASTInc(JJTINC);
                           boolean jjtc018 = true;
                           jjtree.openNodeScope(jjtn018);
      try {
        jj_consume_token(RPAR);
      } finally {
                           if (jjtc018) {
                             jjtree.closeNodeScope(jjtn018,  1);
                           }
      }
      break;
    case AINC:
      jj_consume_token(AINC);
      jj_consume_token(LPAR);
      ident();
                            ASTAInc jjtn019 = new ASTAInc(JJTAINC);
                            boolean jjtc019 = true;
                            jjtree.openNodeScope(jjtn019);
      try {
        jj_consume_token(RPAR);
      } finally {
                            if (jjtc019) {
                              jjtree.closeNodeScope(jjtn019,  1);
                            }
      }
      break;
    case NOP:
      ASTNop jjtn020 = new ASTNop(JJTNOP);
      boolean jjtc020 = true;
      jjtree.openNodeScope(jjtn020);
      try {
        jj_consume_token(NOP);
      } finally {
      if (jjtc020) {
        jjtree.closeNodeScope(jjtn020, true);
      }
      }
      break;
    case JCSTOP:
      ASTJcStop jjtn021 = new ASTJcStop(JJTJCSTOP);
      boolean jjtc021 = true;
      jjtree.openNodeScope(jjtn021);
      try {
        jj_consume_token(JCSTOP);
      } finally {
      if (jjtc021) {
        jjtree.closeNodeScope(jjtn021, true);
      }
      }
      break;
    case NEG:
    case NOT:
    case ADD:
    case SUB:
    case MUL:
    case DIV:
    case CMP:
    case SUP:
    case OR:
    case AND:
      oper();
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final private void ident() throws ParseException {
                                 /*@bgen(jjtree) JcIdent */
                                 ASTJcIdent jjtn000 = new ASTJcIdent(JJTJCIDENT);
                                 boolean jjtc000 = true;
                                 jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(IDENTIFIER);
                       jjtree.closeNodeScope(jjtn000, true);
                       jjtc000 = false;
                      jjtn000.value = t.image;
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void value() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMBER:
      number();
      break;
    case TRUE:
      trueValue();
      break;
    case FALSE:
      falseValue();
      break;
    case STRING:
      string();
      break;
    default:
      jj_la1[2] = jj_gen;
      ASTJcnil jjtn001 = new ASTJcnil(JJTJCNIL);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
        empty();
      } catch (Throwable jjte001) {
      if (jjtc001) {
        jjtree.clearNodeScope(jjtn001);
        jjtc001 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte001 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte001;}
      }
      if (jjte001 instanceof ParseException) {
        {if (true) throw (ParseException)jjte001;}
      }
      {if (true) throw (Error)jjte001;}
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001, true);
      }
      }
    }
  }

  static final private void adresse() throws ParseException {
                          Token t;
    number();
  }

  static final private void oper() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NEG:
    case NOT:
      oper1();
      break;
    case ADD:
    case SUB:
    case MUL:
    case DIV:
    case CMP:
    case SUP:
    case OR:
    case AND:
      oper2();
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final private void oper1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NEG:
      ASTNeg jjtn001 = new ASTNeg(JJTNEG);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
        jj_consume_token(NEG);
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001, true);
      }
      }
      break;
    case NOT:
      ASTNot jjtn002 = new ASTNot(JJTNOT);
      boolean jjtc002 = true;
      jjtree.openNodeScope(jjtn002);
      try {
        jj_consume_token(NOT);
      } finally {
      if (jjtc002) {
        jjtree.closeNodeScope(jjtn002, true);
      }
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final private void oper2() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ADD:
      ASTAdd jjtn001 = new ASTAdd(JJTADD);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
        jj_consume_token(ADD);
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001, true);
      }
      }
      break;
    case SUB:
      ASTSub jjtn002 = new ASTSub(JJTSUB);
      boolean jjtc002 = true;
      jjtree.openNodeScope(jjtn002);
      try {
        jj_consume_token(SUB);
      } finally {
      if (jjtc002) {
        jjtree.closeNodeScope(jjtn002, true);
      }
      }
      break;
    case MUL:
      ASTMul jjtn003 = new ASTMul(JJTMUL);
      boolean jjtc003 = true;
      jjtree.openNodeScope(jjtn003);
      try {
        jj_consume_token(MUL);
      } finally {
      if (jjtc003) {
        jjtree.closeNodeScope(jjtn003, true);
      }
      }
      break;
    case DIV:
      ASTDiv jjtn004 = new ASTDiv(JJTDIV);
      boolean jjtc004 = true;
      jjtree.openNodeScope(jjtn004);
      try {
        jj_consume_token(DIV);
      } finally {
      if (jjtc004) {
        jjtree.closeNodeScope(jjtn004, true);
      }
      }
      break;
    case CMP:
      ASTCmp jjtn005 = new ASTCmp(JJTCMP);
      boolean jjtc005 = true;
      jjtree.openNodeScope(jjtn005);
      try {
        jj_consume_token(CMP);
      } finally {
      if (jjtc005) {
        jjtree.closeNodeScope(jjtn005, true);
      }
      }
      break;
    case SUP:
      ASTSup jjtn006 = new ASTSup(JJTSUP);
      boolean jjtc006 = true;
      jjtree.openNodeScope(jjtn006);
      try {
        jj_consume_token(SUP);
      } finally {
      if (jjtc006) {
        jjtree.closeNodeScope(jjtn006, true);
      }
      }
      break;
    case OR:
      ASTOr jjtn007 = new ASTOr(JJTOR);
      boolean jjtc007 = true;
      jjtree.openNodeScope(jjtn007);
      try {
        jj_consume_token(OR);
      } finally {
      if (jjtc007) {
        jjtree.closeNodeScope(jjtn007, true);
      }
      }
      break;
    case AND:
      ASTAnd jjtn008 = new ASTAnd(JJTAND);
      boolean jjtc008 = true;
      jjtree.openNodeScope(jjtn008);
      try {
        jj_consume_token(AND);
      } finally {
      if (jjtc008) {
        jjtree.closeNodeScope(jjtn008, true);
      }
      }
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final private void type() throws ParseException {
                             /*@bgen(jjtree) Type */
                             ASTType jjtn000 = new ASTType(JJTTYPE);
                             boolean jjtc000 = true;
                             jjtree.openNodeScope(jjtn000);Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INT:
        t = jj_consume_token(INT);
                 jjtree.closeNodeScope(jjtn000, true);
                 jjtc000 = false;
                jjtn000.value = t.image;
        break;
      case BOOL:
        t = jj_consume_token(BOOL);
                 jjtree.closeNodeScope(jjtn000, true);
                 jjtc000 = false;
                jjtn000.value = t.image;
        break;
      case VOID:
        t = jj_consume_token(VOID);
                 jjtree.closeNodeScope(jjtn000, true);
                 jjtc000 = false;
                jjtn000.value = t.image;
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void sorte() throws ParseException {
                               /*@bgen(jjtree) Sorte */
                               ASTSorte jjtn000 = new ASTSorte(JJTSORTE);
                               boolean jjtc000 = true;
                               jjtree.openNodeScope(jjtn000);Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VAR:
        t = jj_consume_token(VAR);
                 jjtree.closeNodeScope(jjtn000, true);
                 jjtc000 = false;
                jjtn000.value = t.image;
        break;
      case CST:
        t = jj_consume_token(CST);
                jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
               jjtn000.value = t.image;
        break;
      case METH:
        t = jj_consume_token(METH);
                 jjtree.closeNodeScope(jjtn000, true);
                 jjtc000 = false;
                jjtn000.value = t.image;
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void number() throws ParseException {
                                 /*@bgen(jjtree) JcNbre */
                                 ASTJcNbre jjtn000 = new ASTJcNbre(JJTJCNBRE);
                                 boolean jjtc000 = true;
                                 jjtree.openNodeScope(jjtn000);int x = 0; Token t;
    try {
      t = jj_consume_token(NUMBER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        try
        {
            x = Integer.parseInt(t.image);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        jjtn000.value = x;
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void trueValue() throws ParseException {
                                    /*@bgen(jjtree) JcVrai */
                                    ASTJcVrai jjtn000 = new ASTJcVrai(JJTJCVRAI);
                                    boolean jjtc000 = true;
                                    jjtree.openNodeScope(jjtn000);boolean x = true;
    try {
      jj_consume_token(TRUE);
             jjtree.closeNodeScope(jjtn000, true);
             jjtc000 = false;
            jjtn000.jjtSetValue(x);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void falseValue() throws ParseException {
                                      /*@bgen(jjtree) JcFalse */
                                      ASTJcFalse jjtn000 = new ASTJcFalse(JJTJCFALSE);
                                      boolean jjtc000 = true;
                                      jjtree.openNodeScope(jjtn000);boolean x = false;
    try {
      jj_consume_token(FALSE);
              jjtree.closeNodeScope(jjtn000, true);
              jjtc000 = false;
             jjtn000.jjtSetValue(x);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void string() throws ParseException {
                                   /*@bgen(jjtree) JcChaine */
                                   ASTJcChaine jjtn000 = new ASTJcChaine(JJTJCCHAINE);
                                   boolean jjtc000 = true;
                                   jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(STRING);
                   jjtree.closeNodeScope(jjtn000, true);
                   jjtc000 = false;
                  jjtn000.value = t.image;
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private void empty() throws ParseException {

  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public JajaCodeTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[8];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x0,0xffffffe0,0x0,0xfc000000,0xc000000,0xf0000000,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x20000,0xf,0x6c000,0xf,0x0,0xf,0x700,0x3800,};
   }

  /** Constructor with InputStream. */
  public JajaCode(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public JajaCode(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new JajaCodeTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public JajaCode(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new JajaCodeTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public JajaCode(JajaCodeTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(JajaCodeTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List jj_expentries = new java.util.ArrayList();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[51];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 51; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
