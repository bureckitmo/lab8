package database;

public class SqlQuery {
    public static class Get {
        //City
        public static final String MOVIE = "SELECT id, name, coordinates_x, coordinates_y, creation_date, oscars_count, genre, mpaa_rating, person_name, passport_id, color, location_x, location_y, location_z, user_id, username " +
                "FROM movie;";

        public static final String MOVIE_BY_ID = "SELECT id FROM movie where id = ?";

        //Users
        public static final String USERS = "SELECT * FROM users";
        public static final String PASS_USING_USERNAME = "SELECT password, id FROM users WHERE username = ?";
        public static final String ID_USING_USERNAME = "SELECT id FROM users WHERE username = ?";

        public static final String USER_HAS_PERMISSIONS = "" +
                "SELECT exists(SELECT 1 from movie where user_id = ? AND id = ?)";
    }
    public static class Add {
        //City
        public static final String MOVIE = "" +
                "INSERT INTO movie(" +
                "name, coordinates_x, coordinates_y, creation_date, oscars_count, genre, mpaa_rating, person_name, passport_id, color, location_x, location_y, location_z, user_id, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        //Users
        public static final String USER = "" +
                "INSERT INTO users(username, password) VALUES(?, ?)";
    }
    public static class Update {
        public static final String MOVIE = "" +
        "UPDATE movie SET name=?, coordinates_x=?, coordinates_y=?, oscars_count=?, genre=?, mpaa_rating=?, person_name=?, passport_id=?, color=?, location_x=?, location_y=?, location_z=? "+
                "WHERE movie.id = ?;";
    }
    public static class Delete {
        public static final String ALL_MOVIE = "DELETE FROM movie";
        public static final String MOVIE_BY_ID = "DELETE FROM movie where id = ?";

        public static final String USER = "DELETE FROM users where username = ?";
    }
}
