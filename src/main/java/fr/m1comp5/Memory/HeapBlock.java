package fr.m1comp5.Memory;

public class HeapBlock implements Comparable<HeapBlock>
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
        free = false;
    }

    public void setBlockFree()
    {
        free = true;
    }

    public int getAddress()
    {
        return address;
    }

    public int getEndAddress()
    {
        return address + size;
    }

    public int getSize()
    {
        return size;
    }

    @Override
    public int compareTo(HeapBlock o) {
        return address - o.address;
    }
}
