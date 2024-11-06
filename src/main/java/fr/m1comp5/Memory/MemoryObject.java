package fr.m1comp5.Memory;

public class MemoryObject
{
    private String id;
    private Object value;
    private ObjectNature nature;
    private ObjectType type;

    public MemoryObject(String id, Object value, ObjectNature nature, ObjectType type)
    {
        this.id = id;
        this.value = value;
        this.nature = nature;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ObjectNature getNature()
    {
        return nature;
    }

    public void setNature(ObjectNature nature)
    {
        this.nature = nature;
    }

    public ObjectType getType()
    {
        return type;
    }

    public void setType(ObjectType type)
    {
        this.type = type;
    }

    public String toString() { return "<"+ getId() +","+ getValue() +","+getNature()+","+getType()+">"; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MemoryObject) return ((MemoryObject)obj).id.equals(this.id);
        return false;
    }
}