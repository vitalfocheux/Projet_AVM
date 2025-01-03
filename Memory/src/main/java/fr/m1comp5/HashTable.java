package fr.m1comp5;

import java.util.ArrayList;
import java.util.List;

public class HashTable
{
    private static final int INITIAL_SIZE = 32;

    private List<List<MemoryObject>> buckets;
    private int count;

    public HashTable()
    {
        count = 0;
        buckets = new ArrayList<>();
        for (int i = 0; i < INITIAL_SIZE; ++i)
        {
            buckets.add(null);
        }
    }

    public List<List<MemoryObject>> getBuckets()
    {
        return buckets;
    }

    /**
     * Compute location of an object in the symbol table based on his id,
     * this function is useful when we need to rehash the table.
     * This algorithm use FNV-1a hash algorithm
     * @param key The key to put in the symbol
     * @param size The size of the new buckets
     * @return Location where the object will be put
     */
    private int hashFunction(String key, int size)
    {
        final int FNV_PRIME = 0x01000193;
        int hashCode = 0x811c9dc5;
        if (key != null)
        {
            for (char c : key.toCharArray())
            {
                hashCode ^= c;
                hashCode *= FNV_PRIME;
            }
        }
        return Math.abs(hashCode % size);
    }

    /**
     * Compute location of an object in the symbol table based on his id
     * @param key The key to put in the symbol
     * @return Location where the object will be put
     */
    private int hashFunction(String key)
    {
        return hashFunction(key, buckets.size());
    }

    /**
     *
     * @param id The id of the object in the memory
     * @return The object if it's found and null otherwise
     */
    public MemoryObject get(String id)
    {
        List<MemoryObject> bucket = buckets.get(hashFunction(id));
        if (bucket == null)
        {
            return null;
        }
        for (MemoryObject mo : bucket)
        {
            if (mo.getId().equals(id))
            {
                return mo;
            }
        }
        return null;
    }

    /**
     * Put an memory object in the table
     * @param mo The memory object to put in the symbol table
     * @return True if the object was put in the table and false otherwise
     */
    public boolean put(MemoryObject mo) throws SymbolTableException
    {
        if (mo == null)
        {
            return false;
        }
        int hash = hashFunction(mo.getId());
        List<MemoryObject> bucket = buckets.get(hash);
        if (bucket == null)
        {
            bucket = new ArrayList<>();
            buckets.set(hash, bucket);
        }
        for (MemoryObject moo : bucket)
        {
            if (moo.equals(mo) || moo.getId().equals(mo.getId()))
            {
                throw new SymbolTableException("This object already exist in the symbol table");
            }
        }
        ++count;
        bucket.add(mo);
        System.out.println("Saving in symbol table: " + mo.toString());
        if (needToRehash())
        {
            rehash();
        }
        return true;
    }

    public void update(String id, Object value) throws SymbolTableException {
        if (id == null || value == null)
        {
            throw new SymbolTableException("The value and the identifier must not be null");
        }
        MemoryObject mo = get(id);
        if (mo != null)
        {
            mo.setValue(value);
        }
    }

    /**
     * Remove an element from the symbol table
     * @param mo Memory object to remove from the symbol table
     * @return True if the object was removed and false otherwise
     */
    public boolean remove(MemoryObject mo)
    {
        if (mo == null)
        {
            return false;
        }
        List<MemoryObject> lmo = buckets.get(hashFunction(mo.getId()));
        if (lmo == null)
        {
            return false;
        }
        int idx = -1;
        for (int i = 0; i < lmo.size(); ++i)
        {
            if (lmo.get(i).getId().equals(mo.getId()))
            {
                idx = i;
                break;
            }
        }
        if (idx != -1)
        {
            lmo.remove(idx);
            --count;
        }
        return idx != -1;
    }

    /**
     *
     * @return The number of elements in the symbol table
     */
    public int getCount()
    {
        return count;
    }

    /**
     *
     * @return The size of the symbol table
     */
    public int getSize()
    {
        return buckets.size();
    }

    /**
     * Tell if the symbol table need to be rehashed,
     * the symbol table need to be rehashed if load factor > 0.5
     * @return True if the symbol table need to be rehashed
     */
    private boolean needToRehash()
    {
        return ((double) count / buckets.size()) > 0.5;
    }

    private void rehash()
    {
        int size = buckets.size() * 2;
        List<List<MemoryObject>> newBuckets = new ArrayList<>();
        for (int i = 0; i < size; ++i)
        {
            newBuckets.add(null);
        }
        for (List<MemoryObject> lmo : buckets)
        {
            if (lmo == null)
            {
                continue;
            }
            for (MemoryObject mo : lmo)
            {
                int hashCode = hashFunction(mo.getId(), size);
                addToBucket(mo, newBuckets, hashCode);
            }
        }
        buckets = newBuckets;
    }

    private void addToBucket(MemoryObject mo, List<List<MemoryObject>> buckets, int hashCode)
    {
        if (mo == null)
        {
            return;
        }
        if (buckets == null)
        {
            return;
        }
        List<MemoryObject> bucket = buckets.get(hashCode);
        if (bucket == null)
        {
            bucket = new ArrayList<>();
            buckets.set(hashCode, bucket);
        }
        bucket.add(mo);
    }

    public List<MemoryObject> accessBucket(String id)
    {
        return buckets.get(hashFunction(id));
    }
}