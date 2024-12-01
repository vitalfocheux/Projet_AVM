package fr.m1comp5;

import java.util.List;

public class MemoryObject
{
    private String id;
    private Object value;
    private ObjectNature nature;
    private ObjectType type;
    private List<ObjectType> paramTypes; // Liste des types de paramètres


    public MemoryObject(String id, Object value, ObjectNature nature, ObjectType type)
    {
        this.id = id;
        this.value = value;
        this.nature = nature;
        this.type = type;
    }

    public MemoryObject(String id, Object value, ObjectNature nature, ObjectType type, List<ObjectType> paramTypes) {
        this.id = id;
        this.value = value;
        this.nature = nature;
        this.type = type;
        this.paramTypes = paramTypes;
    }

    public String getId()
    {
        return id;
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

    public ObjectType getType()
    {
        return type;
    }

    public List<ObjectType> getParamTypes() {
        return paramTypes;
    }

    public String toString() { return "<"+ getId() +","+ getValue() +","+getNature()+","+getType()+">"; }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setType(ObjectType type)
    {
        this.type = type;
    }

    public void setNature(ObjectNature nature)
    {
        this.nature = nature;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MemoryObject) return ((MemoryObject)obj).id.equals(this.id);
        return false;
    }
}