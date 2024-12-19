package fr.m1comp5.custom.node;

import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.SimpleNode;

public class PositionableMjjSimpleNode extends SimpleNode
{
    public PositionableMjjSimpleNode(int id)
    {
        super(id);
    }

    public PositionableMjjSimpleNode(MiniJaja p, int i)
    {
       super(p, i);
    }

    public int getColumn()
    {
        return jjtGetFirstToken() == null ? -1 : jjtGetFirstToken().beginColumn;
    }

    public int getLine()
    {
        return jjtGetFirstToken() == null ? -1 : jjtGetFirstToken().beginLine;
    }
}
