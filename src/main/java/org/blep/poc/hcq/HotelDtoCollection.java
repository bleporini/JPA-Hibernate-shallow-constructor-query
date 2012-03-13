package org.blep.poc.hcq;

import org.blep.poc.hcq.Hotel;
import org.blep.poc.hcq.HotelDto;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author blep
 *         Date: 07/03/12
 *         Time: 20:06
 */
public class HotelDtoCollection implements Collection<HotelDto> {

    //TODO: cache DTO's to prevent multiple instances for the same entity.

    public HotelDtoCollection(Collection<Hotel> delegate) {
        this.delegate = delegate;
    }

    private Collection<Hotel> delegate;

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public boolean contains(Object o) {
        throw new RuntimeException("Not supported");
    }

    private static class HotelIterator implements Iterator<HotelDto>{
        private Iterator<Hotel> delegate;

        private HotelIterator(Iterator<Hotel> delegate) {
            this.delegate = delegate;
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public HotelDto next() {
            return new HotelDto(delegate.next());
        }

        public void remove() {
            delegate.remove();
        }
    }

    public Iterator<HotelDto> iterator() {
        return new HotelIterator(delegate.iterator());
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return delegate.toArray(ts);
    }

    public boolean add(HotelDto hotel) {
        throw new RuntimeException("Not supported");

    }

    public boolean remove(Object o) {
        throw new RuntimeException("Not supported");
    }

    public boolean containsAll(Collection<?> objects) {
        throw new RuntimeException("Not supported");
    }

    public boolean addAll(Collection<? extends HotelDto> hotels) {
        throw new RuntimeException("Not supported");
    }

    public boolean removeAll(Collection<?> objects) {
        throw new RuntimeException("Not supported");
    }

    public boolean retainAll(Collection<?> objects) {
        return delegate.retainAll(objects);
    }

    public void clear() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}

