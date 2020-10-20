package database;

import lombok.extern.log4j.Log4j2;
import models.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
public class CollectionDBManager {

    private final Connection connection;

    public CollectionDBManager(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Movie> getCollection() throws SQLException {
        ArrayList<Movie> collection = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.MOVIE);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            Location location = new Location(rs.getLong("location_x"), rs.getDouble("location_y"), rs.getLong("location_z"));
            Person person = new Person(rs.getString("person_name"), rs.getString("passport_id"), Color.byOrdinal(rs.getInt("color")), location);

            LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();
            Movie movie = new Movie(
                    rs.getInt("id"),
                    rs.getString("name"),
                    new Coordinates(rs.getLong("coordinates_x"), rs.getInt("coordinates_y")),
                    creationDate,
                    rs.getInt("oscars_count"),
                    MovieGenre.byOrdinal(rs.getInt("genre")),
                    MpaaRating.byOrdinal(rs.getInt("mpaa_rating")),
                    person,
                    rs.getString("username"),
                    rs.getInt("user_id")
            );

            collection.add(movie);
        }

        return collection;
    }

    public boolean hasPermissions(Credentials credentials, int id) throws SQLException {
        if (credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return true;

        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.USER_HAS_PERMISSIONS);
        int pointer = 0;
        preparedStatement.setInt(1, credentials.id);
        preparedStatement.setInt(2, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("exists");
        }
        return false;
    }

    public String add(Movie movie, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Add.MOVIE);
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setLong(2, movie.getCoordinates().getX());
            preparedStatement.setInt(3, movie.getCoordinates().getY());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(movie.getCreationDate().atStartOfDay()));
            preparedStatement.setInt(5, movie.getOscarsCount());
            preparedStatement.setInt(6, MovieGenre.getId(movie.getGenre()));
            preparedStatement.setInt(7, MpaaRating.getId(movie.getMpaaRating()));
            preparedStatement.setString(8, movie.getOperator().getName());
            preparedStatement.setString(9, movie.getOperator().getPassportID());
            preparedStatement.setInt(10, Color.getId(movie.getOperator().getHairColor()));
            preparedStatement.setLong(11, movie.getOperator().getLocation().getX());
            preparedStatement.setDouble(12, movie.getOperator().getLocation().getY());
            preparedStatement.setLong(13, movie.getOperator().getLocation().getZ());
            preparedStatement.setInt(14, credentials.id);
            preparedStatement.setString(15, credentials.username);

            ResultSet rs = preparedStatement.executeQuery();
            int cityID = 0;
            if (rs.next())
                cityID = rs.getInt(1);

            connection.commit();

            return String.valueOf(cityID);
        } catch (Throwable e) {
            connection.rollback();
            log.error(e);
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String update(int id, Movie movie, Credentials credentials) throws SQLException {
        if (!hasPermissions(credentials, id))
            return "You don't have permissions";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Update.MOVIE);
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setLong(2, movie.getCoordinates().getX());
            preparedStatement.setInt(3, movie.getCoordinates().getY());
            preparedStatement.setInt(4, movie.getOscarsCount());
            preparedStatement.setInt(5, MovieGenre.getId(movie.getGenre()));
            preparedStatement.setInt(6, MpaaRating.getId(movie.getMpaaRating()));
            preparedStatement.setString(7, movie.getOperator().getName());
            preparedStatement.setString(8, movie.getOperator().getPassportID());
            preparedStatement.setInt(9, Color.getId(movie.getOperator().getHairColor()));
            preparedStatement.setLong(10, movie.getOperator().getLocation().getX());
            preparedStatement.setDouble(11, movie.getOperator().getLocation().getY());
            preparedStatement.setLong(12, movie.getOperator().getLocation().getZ());
            preparedStatement.setLong(13, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

    public int getMovieByID(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.MOVIE_BY_ID);
        int pointer = 0;
        preparedStatement.setInt(++pointer, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public String deleteAll(Credentials credentials) throws SQLException {
        if (!credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return "You don't have permissions to delete all database, only root";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.ALL_MOVIE);
            preparedStatement.executeUpdate();
            connection.commit();
            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String delete(int id, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            int movieID = getMovieByID(id);
            if (!hasPermissions(credentials, movieID))
                return "You don't have permissions";

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.MOVIE_BY_ID);
            int pointer = 0;
            preparedStatement.setInt(++pointer, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

}
