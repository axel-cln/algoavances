package etudemap.map;

import java.util.Iterator;
import java.util.NoSuchElementException;

import etudemap.map.exceptions.CompleteTableException;
import etudemap.map.exceptions.NullKeyException;

public class MapImpl<K, V> implements Map<K, V> {
	private MapEntryImpl<K, V>[] map;
	private int nbElements;

	public MapImpl(int size) {
		map = new MapEntryImpl[size];
		nbElements = 0;
	}

	public MapEntryImpl<K, V>[] getMap() {
		return this.map;
	}

	private int hash(K key) throws NullKeyException {
		if (key == null) {
			throw new NullKeyException();
		}
		return key.hashCode() % getMap().length;
	}

	@Override
	public void put(K key, V value) throws NullKeyException, CompleteTableException {
		int id = this.hash(key);
		int cpt = 0;
		while (getMap()[id] != null && !getMap()[id].getKey().equals(key) && cpt < this.map.length) {
			id = (id + 1) % getMap().length;
			cpt++;
		}
		if (cpt == this.getMap().length) {
			throw new CompleteTableException();
		}
		if (getMap()[id] == null) {
			getMap()[id] = new MapEntryImpl<>(key, value);
			nbElements++;
		} else {
			getMap()[id].setValue(value);
		}
	}

	private Integer findIdKey(K key) {
		int id = this.hash(key);
		int cpt = 0;
		while ((getMap()[id] == null || !getMap()[id].getKey().equals(key)) && cpt < this.getMap().length) {
			id = (id + 1) % getMap().length;
			cpt++;
		}
		return cpt == this.getMap().length ? null : id;
	}

	@Override
	public V remove(K key) throws NullKeyException {
		final Integer id = this.findIdKey(key);
		if (id == null) {
			return null;
		} else {
			V v = this.getMap()[id].getValue();
			this.getMap()[id] = null;
			nbElements--;
			return v;
		}
	}

	@Override
	public V get(K key) throws NullKeyException {
		final Integer id = this.findIdKey(key);
		return id == null ? null : this.getMap()[id].getValue();
	}

	@Override
	public boolean contains(K key) throws NullKeyException {
		return this.findIdKey(key) != null;
	}

	@Override
	public int size() {
		return this.nbElements;
	}

	@Override
	public Iterator<MapEntry<K, V>> iterator() {
		return new IteratorMapImpl();
	}

	public class IteratorMapImpl implements Iterator<MapEntry<K, V>> {
		private int idxCourant;
		private MapEntry<K, V> elementPrecedent;

		public IteratorMapImpl() {
			this.idxCourant = trouverSuivant(-1);
		}

		private int trouverSuivant(int idxDepart) {
			int i = idxDepart + 1;
			while (i < getMap().length && getMap()[i] == null) {
				i++;
			}
			return i; // retourne indice élément non null OU entries.length
		}

		@Override
		public boolean hasNext() {
			return this.idxCourant < getMap().length;
		}

		@Override
		public MapEntry<K, V> next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			final MapEntry<K, V> me = getMap()[this.idxCourant];
			elementPrecedent = me;
			this.idxCourant = this.trouverSuivant(this.idxCourant);
			return me;
		}

		public void remove() {
			if (elementPrecedent == null) {
				throw new IllegalStateException();
			} else {
				MapImpl.this.remove(elementPrecedent.getKey());
				elementPrecedent = null;
			}
		}
	}
}