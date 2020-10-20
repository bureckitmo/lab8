package models;

import exceptions.EnumParseException;
import exceptions.InvalidValueException;

import java.io.Serializable;
import java.util.*;

public enum Color implements Serializable {
    BLACK("BLACK"),
    WHITE("WHITE"),
    BROWN("BROWN");

    private final String name;

    private Color(String s){
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public static Map<Color, Integer> getMap = new HashMap<Color, Integer>() {
        {
            put(BLACK, 1);
            put(WHITE, 2);
            put(BROWN, 3);
        }
    };

    public static Color parse(String str) {

        switch (str.toUpperCase()) {

            case "BLACK":
                return BLACK;
            case "WHITE":
                return WHITE;
            case "BROWN":
                return BROWN;
            default:
                throw new EnumParseException(str);
        }
    }


    public static Color byOrdinal(int s) {
        for (Map.Entry<Color, Integer> entry : Color.getMap.entrySet()) {
            if (entry.getValue() == s) {
                return entry.getKey();
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public static int getId(Color s){
        for (Map.Entry<Color, Integer> entry : Color.getMap.entrySet()) {
            if (entry.getKey() == s) {
                return entry.getValue();
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }
}