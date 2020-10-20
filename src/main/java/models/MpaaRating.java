package models;

import exceptions.EnumParseException;
import exceptions.InvalidValueException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum MpaaRating implements Serializable {
    G("G"),
    PG("PG"),
    PG_13("PG_13"),
    R("R"),
    NC_17("NC_17");

    private final String name;

    private MpaaRating(String s){
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public static Map<MpaaRating, Integer> getMap = new HashMap<MpaaRating, Integer>() {
        {
            put(G, 1);
            put(PG, 2);
            put(PG_13, 3);
            put(R, 4);
            put(NC_17, 5);
        }
    };

    public static MpaaRating parse(String str) {

        switch (str.toUpperCase()) {

            case "G":
                return G;
            case "PG":
                return PG;
            case "PG_13":
                return PG_13;
            case "R":
                return R;
            case "NC_17":
                return NC_17;
            default:
                throw new EnumParseException(str);
        }
    }

    public static MpaaRating byOrdinal(int s) {
        for (Map.Entry<MpaaRating, Integer> entry : MpaaRating.getMap.entrySet()) {
            if (entry.getValue() == s) {
                return entry.getKey();
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public static int getId(MpaaRating s){
        for (Map.Entry<MpaaRating, Integer> entry : MpaaRating.getMap.entrySet()) {
            if (entry.getKey() == s) {
                return entry.getValue();
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }
}
