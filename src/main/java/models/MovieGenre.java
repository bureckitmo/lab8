package models;

import exceptions.EnumParseException;
import exceptions.InvalidValueException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum MovieGenre implements Serializable {
    WESTERN("WESTERN"),
    DRAMA("DRAMA"),
    TRAGEDY("TRAGEDY"),
    FANTASY("FANTASY"),
    SCIENCE_FICTION("SCIENCE_FICTION");

    private final String name;

    private MovieGenre(String s){
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static Map<MovieGenre, Integer> getMap = new HashMap<MovieGenre, Integer>() {
        {
            put(WESTERN, 1);
            put(DRAMA, 2);
            put(TRAGEDY, 3);
            put(FANTASY, 4);
            put(SCIENCE_FICTION, 5);
        }
    };

    public static MovieGenre parse(String str) {

        switch (str.toUpperCase()) {
            case "WESTERN":
                return WESTERN;
            case "DRAMA":
                return DRAMA;
            case "TRAGEDY":
                return TRAGEDY;
            case "FANTASY":
                return FANTASY;
            case "SCIENCE_FICTION":
                return SCIENCE_FICTION;
            default:
                throw new EnumParseException(str);
        }
    }


    public static MovieGenre byOrdinal(int s) {
        for (Map.Entry<MovieGenre, Integer> entry : MovieGenre.getMap.entrySet()) {
            if (entry.getValue() == s) {
                return entry.getKey();
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public static int getId(MovieGenre s){
        for (Map.Entry<MovieGenre, Integer> entry : MovieGenre.getMap.entrySet()) {
            if (entry.getKey() == s) {
                return entry.getValue();
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }
}