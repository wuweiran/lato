package ardash.lato.terrain;

import java.util.Map;
import java.util.TreeMap;

public class RangeMap<K, V> extends TreeMap<K, V> {
    private static final long serialVersionUID = 3047600270133519982L;

    private static <K, V> V mappedValue(TreeMap<K, V> map, K key) {
        Map.Entry<K, V> e = map.floorEntry(key);
        if (e != null && e.getValue() == null) {
            e = map.lowerEntry(key);
        }
        return e == null ? null : e.getValue();
    }

    public V mappedVal(K key) {
        return mappedValue(this, key);

    }
}
