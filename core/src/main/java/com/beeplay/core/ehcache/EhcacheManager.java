package com.beeplay.core.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class EhcacheManager {

	private Cache ehCache;
	
	public Cache getEhCache() {
		return ehCache;
	}

	public void setEhCache(Cache ehCache) {
		this.ehCache = ehCache;
	}

	public void set(String key,Object value){
		Element e = new Element(key,value);
		ehCache.put(e);
	}
	public void set(String key,Object value,Integer expTimeSecond){
		Element e = new Element(key,value);
		e.setEternal(true);
		e.setTimeToLive(expTimeSecond);
		ehCache.put(e);
	}
	public Object get(String key){
		Element e = ehCache.get(key);
		if(e!=null){
			return e.getObjectValue();
		}
		return null;
	}
	public void remove(String key){
		ehCache.remove(key);
	}
	public void removeAll(){
		ehCache.removeAll();
	}
}
