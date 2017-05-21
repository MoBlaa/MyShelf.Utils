package org.myshelf.utils.collections;

import kotlin.Pair;
import kotlin.Triple;
import org.myshelf.utils.functions.TriConsumer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author MoBlaa
 * @version 0.1
 */
public interface PairMap<KO, KT, V> extends Map<Pair<KO, KT>, V> {
    default Set<Triple<KO, KT, V>> entries() {
        // entries -> to stream of entries -> to stream of Triple -> to set
        return this.entrySet().stream().map(PairMap::toTriple).collect(Collectors.toSet());
    }
    default V get(KO keyOne, KT keyTwo) {
        return this.get(new Pair<>(keyOne, keyTwo));
    }
    default V put(KO keyOne, KT keyTwo, V value) {
        return this.put(new Pair<>(keyOne, keyTwo), value);
    }
    default boolean containsKey(KO keyOne, KT keyTwo) {
        return this.containsKey(new Pair<>(keyOne, keyTwo));
    }
    default void forEach(TriConsumer<KO, KT, V> consumer) {
        for (Triple<KO, KT, V> entry : this.entries()) {
            consumer.accept(entry.getFirst(), entry.getSecond(), entry.getThird());
        }
    }

    static <T, U, V> Triple<T, U, V> toTriple(Entry<Pair<T, U>, V> entry) {
        return new Triple<>(entry.getKey().getFirst(), entry.getKey().getSecond(), entry.getValue());
    }
}
