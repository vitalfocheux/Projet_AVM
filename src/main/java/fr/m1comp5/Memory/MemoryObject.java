package fr.m1comp5.Memory;

public class MemoryObject
{
    private String id;
    private Object value;
    private ObjectNature nature;
    private ObjectType type;

    MemoryObject(String id, Object value, ObjectNature nature, ObjectType type)
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

    public Object getValue()
    {
        return value;
    }

    public ObjectNature getNature()
    {
        return nature;
    }

    public ObjectType getType()
    {
        return type;
    }
}