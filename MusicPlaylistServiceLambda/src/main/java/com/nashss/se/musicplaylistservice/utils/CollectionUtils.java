package com.nashss.se.musicplaylistservice.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nashss.se.musicplaylistservice.utils.NullUtils.ifNotNull;

/**
 * Various helpful utilities for converting collections.
 */
public class CollectionUtils {
    private CollectionUtils() { }

    /**
     * If the parameter is not null, create a new Set with the collections contents.
     * If the parameter is null, return null.
     * @param collectionToWrap The collection to copy into a Set.
     * @param <E> The type of element in the collection.
     * @return A new Set or null.
     */
    public static <E> Set<E> copyToSet(Collection<E> collectionToWrap) {
        return ifNotNull(collectionToWrap, () -> new HashSet<>(collectionToWrap));
    }

    /**
     * If the parameter is not null, create a new List with the collections contents.
     * If the parameter is null, return null.
     * @param collectionToWrap The collection to copy into a List.
     * @param <E> The type of element in the collection.
     * @return A new Set or null.
     */
    public static <E> List<E> copyToList(Collection<E> collectionToWrap) {
        return ifNotNull(collectionToWrap, () -> new ArrayList<>(collectionToWrap));
    }
}
