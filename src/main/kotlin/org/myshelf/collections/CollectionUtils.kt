package org.myshelf.collections

import java.util.*

/**
 * @author MoBlaa
 * @version 0.1
 */
class CollectionUtils {
    companion object {
        fun <T> fromIterable(iter: Iterable<T>) : Collection<T> {
            val list = LinkedList<T>()
            iter.forEach({ list.add(it) })
            return list
        }
    }
}