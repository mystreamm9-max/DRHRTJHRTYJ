package com.example.data.protocol

import com.example.data.model.ClientItem

/**
 * Cálculo del hash de la lista de clientes tal como está definido en cierre_fichas.hta (L. 1472):
 *
 * function lhash(l){var s=0,c=0,i,j,h,t;
 *   for(i=0;i<l.length;i++){t=l[i].n;h=5381;for(j=0;j<t.length;j++)h=((h<<5)+h+t.charCodeAt(j))|0;s=(s+(h>>>0))>>>0;c++}
 *   return s.toString(16)+'-'+c}
 */
object LHash {

    fun compute(list: List<ClientItem>): String {
        var s: Long = 0L
        var c = 0

        for (item in list) {
            val t = item.n
            var h: Int = 5381
            for (j in 0 until t.length) {
                // ((h << 5) + h + charCode) | 0
                val charCode = t[j].code
                h = ((h shl 5) + h + charCode)
            }
            // (h >>> 0) converts signed 32-bit int to unsigned 32-bit representation
            val unsignedH = h.toLong() and 0xFFFFFFFFL
            // s = (s + unsignedH) >>> 0
            s = (s + unsignedH) and 0xFFFFFFFFL
            c++
        }

        // s.toString(16) in lowercase
        val hex = java.lang.Long.toHexString(s).lowercase()
        return "$hex-$c"
    }

    /**
     * Sobrecarga conveniente para lista de nombres string.
     */
    fun computeFromNames(names: List<String>): String {
        return compute(names.map { ClientItem(it) })
    }
}
