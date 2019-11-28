package etudemap.map;

public class MapEntryImpl<K,V> implements MapEntry<K,V>{
	private K key;
	private V value;
	
	public MapEntryImpl(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
}