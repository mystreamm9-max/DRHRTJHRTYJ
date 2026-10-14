package com.example.data.protocol

import com.example.data.model.ClientItem
import kotlin.math.abs
import kotlin.math.min

/**
 * Motor de búsqueda fonética y distancia de edición del HTA (cierre_fichas.hta):
 * - nkey (L. 1355)
 * - phon (L. 1611)
 * - lev (L. 1620)
 * - sugScore (L. 1635)
 * - sugUpdate (L. 1659)
 */
object ClientSearch {

    fun nkey(s: String?): String {
        var str = (s ?: "").uppercase()
        str = str.replace(Regex("[\\u00c0-\\u00c5]"), "A")
            .replace(Regex("[\\u00c8-\\u00cb]"), "E")
            .replace(Regex("[\\u00cc-\\u00cf]"), "I")
            .replace(Regex("[\\u00d2-\\u00d6]"), "O")
            .replace(Regex("[\\u00d9-\\u00dc]"), "U")
            .replace(Regex("\\u00d1"), "N")
        return str.replace(Regex("[^A-Z ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    fun phon(s: String?): String {
        var str = s ?: ""
        str = str.replace("CH", "3")
            .replace("LL", "Y")
            .replace("QU", "K")
            .replace("Q", "K")
            .replace(Regex("GU([EI])"), "G$1")
            .replace(Regex("G([EI])"), "J$1")
            .replace(Regex("C([EI])"), "S$1")
            .replace("C", "K")
            .replace("Z", "S")
            .replace("V", "B")
            .replace("H", "")
            .replace(Regex("(.)\\1+"), "$1")
        return str
    }

    fun lev(a: String, b: String, mx: Int): Int {
        val la = a.length
        val lb = b.length
        if (abs(la - lb) > mx) return mx + 1
        var p = IntArray(lb + 1) { it }
        var pp: IntArray? = null

        for (i in 1..la) {
            val c = IntArray(lb + 1)
            c[0] = i
            for (j in 1..lb) {
                var d = p[j - 1] + (if (a[i - 1] == b[j - 1]) 0 else 1)
                val t1 = p[j] + 1
                if (t1 < d) d = t1
                val t2 = c[j - 1] + 1
                if (t2 < d) d = t2
                if (pp != null && i > 1 && j > 1 && a[i - 1] == b[j - 2] && a[i - 2] == b[j - 1]) {
                    val t3 = pp[j - 2] + 1
                    if (t3 < d) d = t3
                }
                c[j] = d
            }
            pp = p
            p = c
        }
        return p[lb]
    }

    fun sugScore(clientKey: String, tk: List<String>, ptk: List<String>): Double {
        val ws = clientKey.split(" ").filter { it.isNotEmpty() }
        val pw = ws.map { phon(it) }
        var tot = 0.0

        for (j in tk.indices) {
            val t = tk[j]
            val pt = ptk[j]
            var best = 99.0

            for (z in ws.indices) {
                val w = ws[z]
                val s = when {
                    w == t -> 0.0
                    w.startsWith(t) -> 1.0
                    w.indexOf(t) > 0 -> 3.0
                    else -> 99.0
                }
                if (s < best) best = s
                if (best == 0.0) break
            }

            if (best > 2.0) {
                for (z in pw.indices) {
                    val w = pw[z]
                    val s = when {
                        w.startsWith(pt) -> 2.0
                        w.indexOf(pt) > 0 -> 4.0
                        else -> 99.0
                    }
                    if (s < best) best = s
                }
            }

            if (best > 4.0 && pt.length >= 4) {
                val mx = if (pt.length >= 8) 2 else 1
                for (z in pw.indices) {
                    val w = pw[z]
                    val d1 = lev(pt, w, mx)
                    val subW = if (w.length >= pt.length) w.substring(0, pt.length) else w
                    val d2 = lev(pt, subW, mx)
                    val d = min(d1, d2)
                    if (d <= mx && (5.0 + d) < best) {
                        best = 5.0 + d
                    }
                }
            }

            if (best >= 99.0) return -1.0
            tot += best
        }
        return tot
    }

    fun search(clients: List<ClientItem>, query: String): List<ClientItem> {
        val q = nkey(query)
        if (q.isEmpty()) return emptyList()
        val tk = q.split(" ").filter { it.isNotEmpty() }
        val ptk = tk.map { phon(it) }
        val results = mutableListOf<Pair<ClientItem, Double>>()

        for (c in clients) {
            val ck = if (c.k.isNotEmpty()) c.k else nkey(c.n)
            var s = sugScore(ck, tk, ptk)
            if (s >= 0.0) {
                if (ck.startsWith(q)) s -= 0.5
                results.add(Pair(c, s))
            }
        }

        results.sortWith { a, b ->
            if (a.second != b.second) a.second.compareTo(b.second)
            else a.first.n.compareTo(b.first.n)
        }
        return results.map { it.first }
    }
}
