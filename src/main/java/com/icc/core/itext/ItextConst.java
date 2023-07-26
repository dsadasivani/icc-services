package com.icc.core.itext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItextConst {
    public static final Map<String, String> PRODUCTS;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("product1", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(1 Ltrs x 20 Btls)");
        map.put("product2", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(5 Ltrs x 4 Jars)");
        map.put("product3", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(100 Ml x 100 Pouches)");
        PRODUCTS = Collections.unmodifiableMap(map);
    }
}
