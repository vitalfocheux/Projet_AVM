package fr.m1comp5.Memory;

public class HeapBlock
{
    private boolean free;
    final private int size;
    final private int address;

    HeapBlock(int address, int size)
    {
        this.address = address;
        this.size = size;
        free = true;
    }

    public boolean isFree()
    {
        return free;
    }

    public void setBlockUsed()
    {
        this.free = true;
    }

    public int getAddress()
    {
        return address;
    }

    private int getEndAddress()
    {
        return address + size;
    }

    public int getSize()
    {
        return size;
    }
}
