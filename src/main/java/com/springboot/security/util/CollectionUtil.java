package com.springboot.security.util;

import java.util.*;

/**
 * 集合运算
 * 
 * @author ztgreat
 *
 */
public class CollectionUtil {

	/**
	 * 两个集合的交集
	 * 
	 * @param ls
	 * @param ls2
	 * @return
	 */
	public static <T> Set<T> intersect(List<T> ls, List<T> ls2) {
		Set<T> result = new HashSet<T>(ls);
		result.retainAll(ls2);
		return result;
	}

	/**
	 * 两个集合的并集
	 * 
	 * @param ls
	 * @param ls2
	 * @return
	 */
	public static <T> Set<T> union(List<T> ls, List<T> ls2) {
		Set<T> result = new HashSet<T>(ls);
		result.addAll(ls2);
		return result;
	}

	/**
	 * 集合1-集合2 的内容（差集，注意集合顺序）
	 * 
	 * @param ls
	 * @param ls2
	 * @return
	 */
	public static <T> Set<T> diff(List<T> ls, List<T> ls2) {
		Set<T> result = new HashSet<T>(ls);
		result.removeAll(ls2);
		return result;
	}

	public static <E> Set<E> asSet(E... elements) {
		if (elements == null || elements.length == 0) {
			return Collections.emptySet();
		}

		if (elements.length == 1) {
			return Collections.singleton(elements[0]);
		}

		LinkedHashSet<E> set = new LinkedHashSet<E>(elements.length * 4 / 3 + 1);
		Collections.addAll(set, elements);
		return set;
	}

	public static <E> List<E> asList(E... elements) {
		if (elements == null || elements.length == 0) {
			return Collections.emptyList();
		}

		// Integer overflow does not occur when a large array is passed in because the list array already exists
		return Arrays.asList(elements);
	}

	public static boolean isEmpty(Collection c) {
		return c == null || c.isEmpty();
	}

}
