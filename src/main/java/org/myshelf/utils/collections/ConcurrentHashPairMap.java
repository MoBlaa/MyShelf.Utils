package org.myshelf.utils.collections;

import kotlin.Pair;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MoBlaa
 * @version 0.1
 */
public class ConcurrentHashPairMap<T, U, V> extends ConcurrentHashMap<Pair<T, U>, V> implements PairMap<T, U, V> {
}
