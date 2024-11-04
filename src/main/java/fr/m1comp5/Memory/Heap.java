package fr.m1comp5.Memory;

import java.util.*;

public class Heap
{
    private Object[] heap;
    private List<HeapElement> elements;
    private Map<Integer, List<HeapBlock>> blocks;
    int size;

    public Heap() throws HeapException
    {
       this(1024);
    }

    public Heap(int heapSize) throws HeapException
    {
        if (!isPowerOfTwo(heapSize))
        {
            throw new HeapException("The initial heap size must be a power of 2 value");
        }
        elements = new ArrayList<>();
        blocks = new TreeMap<>();
        blocks.put(heapSize, new ArrayList<>());
        blocks.get(heapSize).add(new HeapBlock(0, heapSize));
        size = heapSize;
        heap = new Object[heapSize];
        for (int i = 0; i < size; ++i)
        {
            heap[i] = null;
        }
    }

    private boolean isPowerOfTwo(int number)
    {
        if (number == 0)
        {
            return false;
        }
        return Math.ceil(log2(number)) == Math.floor(log2(number));
    }

    private double log2(int number)
    {
        return Math.log(number) / Math.log(2);
    }

    private int minimumBlockSizeForElement(int sizeOfElement)
    {
        int minSize = 1;
        while (sizeOfElement > minSize)
        {
            minSize *= 2;
        }
        return minSize;
    }

    public int getHeapSize()
    {
        return size;
    }

    /**
     *
     * @param size Block size in power of two
     * @return
     */
    private HeapBlock getNextUpperFreeBlock(int size) throws HeapException
    {
        while (size < getHeapSize())
        {
            size *= 2;
            if (blocks.containsKey(size))
            {
                List<HeapBlock> lhb = blocks.get(size);
                for (HeapBlock hb : lhb)
                {
                    if (hb.isFree())
                    {
                        return hb;
                    }
                }
            }
        }
        throw new HeapException("There is no free block for this element");
    }


    private void removeBlockFromList(List<HeapBlock> lhb, int address)
    {
        Integer index = null;
        for (int i = 0; i < lhb.size(); ++i)
        {
            if (lhb.get(i).getAddress() == address)
            {
                index = i;
                break;
            }
        }
        if (index != null)
        {
            lhb.remove(index.intValue());
        }
    }

    private void divideBlockToGetBestFit(HeapBlock hb, int wantedSize)
    {
        int size = hb.getSize();
        while (size > wantedSize)
        {
            removeBlockFromList(blocks.get(size), hb.getAddress());
            size /= 2;
            if (!blocks.containsKey(size))
            {
                blocks.put(size, new ArrayList<>());
            }
            List<HeapBlock> lhb = blocks.get(size);
            lhb.add(new HeapBlock(hb.getAddress(), size));
            lhb.add(new HeapBlock(hb.getAddress() + size, size));
        }
    }

    private HeapBlock getFreeBlock(int wanted) throws HeapException
    {
        if (!blocks.containsKey(wanted))
        {
            throw new HeapException("There is no free blocks of this size");
        }
        List<HeapBlock> lhb = blocks.get(wanted);
        for (HeapBlock hb : lhb)
        {
            if (hb.isFree())
            {
                return hb;
            }
        }
        throw new HeapException("There is no free block of this size");
    }

    /* TODO : Overload with allocateInHeap(int size, ObjectType type, Object value) */
    public int allocateInHeap(int size, ObjectType type) throws HeapException
    {
        int elementSize = size;
        if (!isPowerOfTwo(size))
        {
            elementSize = minimumBlockSizeForElement(size);
        }
        HeapBlock hb = null;
        if (!blocks.containsKey(elementSize))
        {
            hb = getNextUpperFreeBlock(elementSize);
        }
        else
        {
            hb = getFreeBlock(elementSize);
        }
        if (hb.getSize() != elementSize)
        {
            divideBlockToGetBestFit(hb, elementSize);
            hb = getFreeBlock(elementSize);
        }
        hb.setBlockUsed();
        elements.add(new HeapElement(hb.getAddress(), size, type));
        System.out.println(blocks);
        return hb.getAddress();
    }

    private HeapElement getHeapElement(int address) throws HeapException
    {
        for (HeapElement he : elements)
        {
            if (he.getAddress() == address)
            {
                return he;
            }
        }
        throw new HeapException("This element doesn't exists in the heap");
    }

    public Object accessValue(int address, int index) throws HeapException, ArrayIndexOutOfBoundsException
    {
        HeapElement he = getHeapElement(address);
        if (!accessedIndexOk(he, index))
        {
            throw new ArrayIndexOutOfBoundsException("The element can't be accessed");
        }
        return heap[address + index];
    }

    public void setValue(int address, int index, Object value) throws HeapException, ArrayIndexOutOfBoundsException
    {
        HeapElement he = getHeapElement(address);
        if (!accessedIndexOk(he, index))
        {
            throw new ArrayIndexOutOfBoundsException("The element can't be accessed");
        }
        heap[address + index] = value;
    }

    public ObjectType getTypeOfElement(int address) throws HeapException
    {
        return getHeapElement(address).getType();
    }

    private boolean accessedIndexOk(HeapElement he, int index)
    {
        return index >= 0 && index < he.getSize();
    }

    private void refragmentHeap()
    {
    }

    private void refragmentBlocks()
    {
        List<HeapBlock> freeBlocks = new ArrayList<>();
        for (Map.Entry<Integer, List<HeapBlock>> i : blocks.entrySet())
        {
            List<HeapBlock> lhb = i.getValue();
            for (HeapBlock hb : lhb)
            {
                if (hb.isFree())
                {
                    freeBlocks.add(hb);
                }
            }
        }
        if (freeBlocks.isEmpty())
        {
            return;
        }
        int i = 0;
        List<HeapBlock> add = new ArrayList<>();
        Set<HeapBlock> supress = new TreeSet<>();
        while (i < freeBlocks.size())
        {
            if (supress.contains(freeBlocks.get(i)))
            {
                ++i;
                continue;
            }
            int j = 0;
            while (j < freeBlocks.size())
            {
                if (supress.contains(freeBlocks.get(j)))
                {
                    ++j;
                    continue;
                }
                HeapBlock firstBlock = freeBlocks.get(i);
                HeapBlock secondBlock = freeBlocks.get(j);
                if (canAssembleBlocks(firstBlock, secondBlock))
                {
                    addNewBlock(add, firstBlock, secondBlock);
                    supress.add(firstBlock);
                    supress.add(secondBlock);
                }
                ++j;
            }
            ++i;
        }
        for (HeapBlock hb : supress)
        {
            if (blocks.containsKey(hb.getSize()))
            {
                blocks.get(hb.getSize()).remove(hb);
            }
        }
        for (HeapBlock hb : add)
        {
            if (!blocks.containsKey(hb.getSize()))
            {
                blocks.put(hb.getSize(), new ArrayList<>());
                blocks.get(hb.getSize()).add(hb);
            }
            else
            {
                blocks.get(hb.getSize()).remove(hb);
            }
        }
    }

    private boolean canAssembleBlocks(HeapBlock hb1, HeapBlock hb2)
    {
        return hb1 != hb2 && hb1.getAddress() == hb2.getEndAddress() || hb1.getEndAddress() == hb2.getAddress() && hb1.getSize() == hb2.getSize();
    }

    private void addNewBlock(List<HeapBlock> toAdd, HeapBlock hb1, HeapBlock hb2)
    {
        int newBlockSize = hb1.getSize() * 2;
        if (hb1.getAddress() > hb2.getEndAddress())
        {
            toAdd.add(new HeapBlock(hb2.getAddress(), newBlockSize));
        }
        else
        {
            toAdd.add(new HeapBlock(hb1.getAddress(), newBlockSize));
        }
    }
}
