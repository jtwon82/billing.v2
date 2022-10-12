package com.mobon.code;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	public static <Q, W> MapWrapper<Q, W> map(Q q, W w) {
		return new MapWrapper<Q, W>(q, w);
	}

	public static final class MapWrapper<Q, W> {
		private final HashMap<Q, W> map;

		public MapWrapper(Q q, W w) {
			map = new HashMap<Q, W>();
			map.put(q, w);
		}

		public MapWrapper<Q, W> map(Q q, W w) {
			map.put(q, w);
			return this;
		}

		public Map<Q, W> getMap() {
			return map;
		}
	}
}
