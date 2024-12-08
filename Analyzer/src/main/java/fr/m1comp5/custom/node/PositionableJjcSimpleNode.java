package fr.m1comp5.custom.node;

import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.SimpleNode;

public class PositionableJjcSimpleNode extends SimpleNode
{
    public PositionableJjcSimpleNode(int id)
    {
        super(id);
    }

    public PositionableJjcSimpleNode(JajaCode p, int i)
    {
        super(p, i);
    }

    public int getColumn()
    {
        return jjtGetFirstToken().beginColumn;
    }

    public int getLine()
    {
        return  jjtGetFirstToken().beginLine;
    }
}
