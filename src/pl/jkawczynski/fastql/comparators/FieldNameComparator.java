package pl.jkawczynski.fastql.comparators;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 *
 * @author jkawczynski
 */
public class FieldNameComparator implements Comparator<Field> {

    @Override
    public int compare(Field obj1, Field obj2) {
        return obj2.getName().compareTo(obj1.getName());
    }
}
