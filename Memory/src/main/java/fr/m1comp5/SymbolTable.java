package fr.m1comp5.Memory;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable
{
    private List<HashTable> scopes;

    public SymbolTable()
    {
        scopes = new ArrayList<>();
    }

    public void newScope()
    {
        scopes.add(new HashTable());
    }

    public void popScope() throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("Can't pop scope because scope stack is empty");
        }
        scopes.remove(scopes.size() - 1);
    }

    public void putObjectInCurrentScope(MemoryObject mo) throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No available scope");
        }
        for (HashTable htb : scopes)
        {
            List<MemoryObject> bucket = htb.accessBucket(mo.getId());
            if (bucket != null)
            {
                for (MemoryObject moo : bucket)
                {
                    if (moo == mo)
                    {
                        throw new SymbolTableException("This object already exists in another scope");
                    }
                }
            }
        }
        scopes.get(scopes.size() - 1).put(mo);
    }

    public void removeObjectFromCurrentScope(MemoryObject mo) throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No scope");
        }
        scopes.get(scopes.size() - 1).remove(mo);
    }

    public MemoryObject get(String id) throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No available scope");
        }
        for (int i = scopes.size() - 1; i >= 0; --i)
        {
            MemoryObject obj = scopes.get(i).get(id);
            if (obj != null)
            {
                return obj;
            }
        }
        throw new SymbolTableException("The object don't exist");
    }

    public void updateObjInCurrentScope(String id, Object val) throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No scope");
        }
        scopes.get(scopes.size() - 1).update(id, val);
    }

}
