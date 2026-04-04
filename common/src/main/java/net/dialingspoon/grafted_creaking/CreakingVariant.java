package net.dialingspoon.grafted_creaking;

import java.util.HashMap;
import java.util.Map;

public enum CreakingVariant {
    OAK(0),
    SPRUCE(1),
    BIRCH(2),
    JUNGLE(3),
    ACACIA(4),
    DARK_OAK(5),
    MANGROVE(6),
    CHERRY(7),
    PALE_OAK(8),
    CRIMSON(9),
    WARPED(10),
    BAMBOO(11),
    MUSHROOM(12);

    private final int id;
    private static final Map<Integer, CreakingVariant> ID_MAP = new HashMap<>();

    static {
        for (CreakingVariant variant : values()) {
            ID_MAP.put(variant.id, variant);
        }
    }

    CreakingVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CreakingVariant getById(int id) {
        return ID_MAP.get(id);
    }
}
