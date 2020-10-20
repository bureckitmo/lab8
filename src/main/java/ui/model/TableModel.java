package ui.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class TableModel {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private LongProperty x = new SimpleLongProperty();
    private IntegerProperty y = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>();
    private IntegerProperty oscarsCount = new SimpleIntegerProperty();
    private StringProperty genre = new SimpleStringProperty();
    private StringProperty mpaaRating = new SimpleStringProperty();
    private StringProperty personName = new SimpleStringProperty();
    private StringProperty passportID = new SimpleStringProperty();
    private StringProperty hairColor = new SimpleStringProperty();
    private LongProperty locationX = new SimpleLongProperty();
    private DoubleProperty locationY = new SimpleDoubleProperty();
    private LongProperty locationZ = new SimpleLongProperty();
    private IntegerProperty userID = new SimpleIntegerProperty();
    private StringProperty username = new SimpleStringProperty();


    public TableModel(){}
    public TableModel(IntegerProperty id,
                      StringProperty name,
                      LongProperty x,
                      IntegerProperty y,
                      ObjectProperty<LocalDate> creationDate,
                      IntegerProperty oscarsCount,
                      StringProperty genre,
                      StringProperty mpaaRating,
                      StringProperty personName,
                      StringProperty passportID,
                      StringProperty hairColor,
                      LongProperty locationX,
                      DoubleProperty locationY,
                      LongProperty locationZ,
                      IntegerProperty userID,
                      StringProperty username){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.personName = personName;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.userID = userID;
        this.username = username;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public long getX() {
        return x.get();
    }

    public LongProperty xProperty() {
        return x;
    }

    public void setX(long x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }


    public int getOscarsCount() {
        return oscarsCount.get();
    }

    public IntegerProperty oscarsCountProperty() {
        return oscarsCount;
    }

    public void setOscarsCount(int oscarsCount) {
        this.oscarsCount.set(oscarsCount);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getMpaaRating() {
        return mpaaRating.get();
    }

    public StringProperty mpaaRatingProperty() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating.set(mpaaRating);
    }

    public String getPersonName() {
        return personName.get();
    }

    public StringProperty personNameProperty() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName.set(personName);
    }

    public String getPassportID() {
        return passportID.get();
    }

    public StringProperty passportIDProperty() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID.set(passportID);
    }

    public String getHairColor() {
        return hairColor.get();
    }

    public StringProperty hairColorProperty() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor.set(hairColor);
    }

    public long getLocationX() {
        return locationX.get();
    }

    public LongProperty locationXProperty() {
        return locationX;
    }

    public void setLocationX(long locationX) {
        this.locationX.set(locationX);
    }

    public double getLocationY() {
        return locationY.get();
    }

    public DoubleProperty locationYProperty() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY.set(locationY);
    }

    public long getLocationZ() {
        return locationZ.get();
    }

    public LongProperty locationZProperty() {
        return locationZ;
    }

    public void setLocationZ(long locationZ) {
        this.locationZ.set(locationZ);
    }

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
}
