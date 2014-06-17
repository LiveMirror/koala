package org.openkoala.koalacommons.resourceloader.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable {

	private static final long serialVersionUID = 3801124242820219131L;

	private final Map<K, List<V>> targetMap;


	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}.
	 */
	public LinkedMultiValueMap() {
		this.targetMap = new LinkedHashMap<K, List<V>>();
	}

	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}
	 * with the given initial capacity.
	 * @param initialCapacity the initial capacity
	 */
	public LinkedMultiValueMap(int initialCapacity) {
		this.targetMap = new LinkedHashMap<K, List<V>>(initialCapacity);
	}

	/**
	 * Copy constructor: Create a new LinkedMultiValueMap with the same mappings
	 * as the specified Map.
	 * @param otherMap the Map whose mappings are to be placed in this Map
	 */
	public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
		this.targetMap = new LinkedHashMap<K, List<V>>(otherMap);
	}


	// MultiValueMap implementation

	public void add(K key, V value) {
		List<V> values = this.targetMap.get(key);
		if (values == null) {
			values = new LinkedList<V>();
			this.targetMap.put(key, values);
		}
		values.add(value);
	}

	public V getFirst(K key) {
		List<V> values = this.targetMap.get(key);
		return (values != null ? values.get(0) : null);
	}

	public void set(K key, V value) {
		List<V> values = new LinkedList<V>();
		values.add(value);
		this.targetMap.put(key, values);
	}

	public void setAll(Map<K, V> values) {
		for (Entry<K, V> entry : values.entrySet()) {
			set(entry.getKey(), entry.getValue());
		}
	}

	public Map<K, V> toSingleValueMap() {
		LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K,V>(this.targetMap.size());
		for (Entry<K, List<V>> entry : targetMap.entrySet()) {
			singleValueMap.put(entry.getKey(), entry.getValue().get(0));
		}
		return singleValueMap;
	}


	// Map implementation

	public int size() {
		return this.targetMap.size();
	}

	public boolean isEmpty() {
		return this.targetMap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.targetMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.targetMap.containsValue(value);
	}

	public List<V> get(Object key) {
		return this.targetMap.get(key);
	}

	public List<V> put(K key, List<V> value) {
		return this.targetMap.put(key, value);
	}

	public List<V> remove(Object key) {
		return this.targetMap.remove(key);
	}

	public void putAll(Map<? extends K, ? extends List<V>> m) {
		this.targetMap.putAll(m);
	}

	public void clear() {
		this.targetMap.clear();
	}

	public Set<K> keySet() {
		return this.targetMap.keySet();
	}

	public Collection<List<V>> values() {
		return this.targetMap.values();
	}

	public Set<Entry<K, List<V>>> entrySet() {
		return this.targetMap.entrySet();
	}

	@Override
	public boolean equals(Object obj) {
		return this.targetMap.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.targetMap.hashCode();
	}

	@Override
	public String toString() {
		return this.targetMap.toString();
	}

}