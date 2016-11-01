package edu.towson.database;

public class KeyValuePair<K, V>
{
	private K key;
	private V value;

	public KeyValuePair(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	public K getKey()
	{
		return key;
	}

	public void setKey(K key)
	{
		this.key = key;
	}

	public V getValue()
	{
		return value;
	}

	public void setValue(V value)
	{
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object obj)
	{
		if (obj instanceof KeyValuePair)
		{
			KeyValuePair<K, V> tmpObj = (KeyValuePair<K, V>) obj;
			return key.equals(tmpObj.key);
		}
		return false;
	}
}
