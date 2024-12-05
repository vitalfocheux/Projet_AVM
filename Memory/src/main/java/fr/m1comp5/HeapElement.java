package fr.m1comp5;

/**
 * An element in the heap have :
 * - An address
 * - A reference count
 * - A size
 */
public class HeapElement
{
    private final int address;
    private final int size;
    private int reference;
    private final ObjectType type;

    public HeapElement(int address, int size, int reference, ObjectType type)
    {
        this.address = address;
        this.size = size;
        this.reference = reference;
        this.type = type;
    }

    public HeapElement(int address, int size, ObjectType type)
    {
        this(address, size, 1, type);
    }

    public void incrementReferenceNumber()
    {
        ++reference;
    }

    public void decrementReferenceNumber()
    {
        --reference;
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }

    public int getNbReference()
    {
        return reference;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof HeapElement))
        {
            return false;
        }
        HeapElement hb = (HeapElement) o;
        return hb.getAddress() == getAddress() && hb.getSize() == getSize() && hb.getNbReference() == getNbReference();
    }

    public ObjectType getType()
    {
        return type;
    }
}
