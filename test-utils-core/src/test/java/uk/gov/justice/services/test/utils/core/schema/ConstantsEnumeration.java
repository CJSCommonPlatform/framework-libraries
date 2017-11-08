package uk.gov.justice.services.test.utils.core.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

class ConstantsEnumeration<T> implements Enumeration<T> {

    private List<T> backingList=  null;
    private Iterator<T> iterator = null;

    public ConstantsEnumeration(T... items)
    {
        this.backingList = new ArrayList<>(Arrays.asList(items));
    }

    @Override
    public boolean hasMoreElements() {
        if (backingList==null)
            return false;
        else if (iterator==null)
            iterator = backingList.iterator();
        return iterator.hasNext();
    }

    @Override
    public T nextElement() {
        if (backingList==null)
            return null;
        else if (iterator==null)
            iterator = backingList.iterator();
        return iterator.next();
    }
}
