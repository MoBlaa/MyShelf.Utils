package org.myshelf.utils.collections;

import kotlin.Pair;

import java.util.LinkedHashMap;

/**
 * @author MoBlaa
 * @version 0.1
 */
public class LinkedHashPairMap<K, T, V> extends LinkedHashMap<Pair<K, T>, V> implements PairMap<K, T, V> {
    public LinkedHashPairMap() {
        super();
    }
}
