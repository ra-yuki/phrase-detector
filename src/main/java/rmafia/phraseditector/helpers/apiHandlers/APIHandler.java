package rmafia.phraseditector.helpers.apiHandlers;

import java.util.HashMap;
import java.util.Map;

public interface APIHandler<K, V> {
    public void initRequestQuery();
    public String executeGetRequest();
    public V addParam(K key, V value);
    public Map<K, V> addParams(HashMap<K, V> kayValuePair);
}
