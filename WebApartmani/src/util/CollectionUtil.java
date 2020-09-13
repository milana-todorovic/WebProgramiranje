package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtil {

	public static <T> void addIf(Collection<T> collection, Collection<? extends T> elementsToAdd,
			Predicate<T> predicate) {
		for (T element : elementsToAdd) {
			if (predicate.test(element))
				collection.add(element);
		}
	}

	public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
		for (T t : collection) {
			if (predicate.test(t))
				return t;
		}
		return null;
	}

	public static <T> Collection<T> findAll(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
	}

}
