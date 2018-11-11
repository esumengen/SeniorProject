import java.util.*;

public class StructerList implements List<Structure> {
    private ArrayList<Structure> structures;

    public StructerList() {
        structures = new ArrayList<>();
    }

    @Override
    public int size() {
        return structures.size();
    }

    @Override
    public boolean isEmpty() {
        return structures.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return structures.contains(o);
    }

    @Override
    public Iterator<Structure> iterator() {
        return structures.iterator();
    }

    @Override
    public Object[] toArray() {
        return structures.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return structures.toArray(a);
    }

    @Override
    public boolean add(Structure structure) {
        return structures.add(structure);
    }

    @Override
    public boolean remove(Object o) {
        return structures.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return structures.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Structure> c) {
        return structures.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Structure> c) {
        return structures.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return structures.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return structures.retainAll(c);
    }

    @Override
    public void clear() {
        structures.clear();
    }

    @Override
    public Structure get(int index) {
        return structures.get(index);
    }

    @Override
    public Structure set(int index, Structure element) {
        return structures.set(index, element);
    }

    @Override
    public void add(int index, Structure element) {
        structures.add(index, element);
    }

    @Override
    public Structure remove(int index) {
        return structures.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return structures.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return structures.lastIndexOf(o);
    }

    @Override
    public ListIterator<Structure> listIterator() {
        return structures.listIterator();
    }

    @Override
    public ListIterator<Structure> listIterator(int index) {
        return structures.listIterator(index);
    }

    @Override
    public List<Structure> subList(int fromIndex, int toIndex) {
        return structures.subList(fromIndex, toIndex);
    }
}
