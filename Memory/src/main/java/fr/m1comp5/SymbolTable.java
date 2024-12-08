package fr.m1comp5;

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
        MemoryObject obj = scopes.get(scopes.size() - 1).get(id);
        if (obj == null)
        {
            obj = getGlobalScopeSymbols().get(id);
            if (obj == null)
            {
                throw new SymbolTableException("The object don't exist");
            }
        }
        return obj;
    }

    public void updateObjInCurrentScope(String id, Object val) throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No scope");
        }
        scopes.get(scopes.size() - 1).update(id, val);
    }

    private HashTable getMainScopeSymboles() throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No scope");
        }
        if (scopes.size() < 2)
        {
            throw new SymbolTableException("Main scope don't exist");
        }
        return scopes.get(1);
    }

    private HashTable getGlobalScopeSymbols() throws SymbolTableException
    {
        if (scopes.isEmpty())
        {
            throw new SymbolTableException("No scope");
        }
        return scopes.get(0);
    }

    public void newScopeFromListOfObject(List<MemoryObject> lmo) throws SymbolTableException
    {
        newScope();
        for (MemoryObject mo : lmo)
        {
            putObjectInCurrentScope(mo);
        }
    }

    public List<HashTable> getScopes() {
        return scopes;
    }

}
