package fr.m1comp5.Memory;

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
}