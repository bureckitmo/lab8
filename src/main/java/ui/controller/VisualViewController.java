package ui.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Movie;
import utils.I18N;
import ui.NetworkManager;
import ui.listener.EventListener;
import ui.model.TableModel;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class VisualViewController implements Initializable {
    public EventListener showEvent;
    public Pane mainPane;

    private ResourceBundle resources;

    private double mainW;
    private double mainH;
    private Dimension panelSize;

    private HashMap<Integer, Circle> circleHashMap = new HashMap<>();
    private List<Movie> bufMovieList = null;

    private Tooltip tooltip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        tooltip = new Tooltip();

        javafx.scene.control.MenuItem add = new MenuItem();
        add.textProperty().bind(I18N.createStringBinding("key.add"));
        add.setOnAction(event1 -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
                Parent root = loader.load();
                AddUpdateView addUpdateView = loader.getController();
                addUpdateView.transferData(null, false);

                Stage stage = new Stage();
                stage.setResizable(false);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.titleProperty().bind(I18N.createStringBinding("key.add"));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ContextMenu contextMenu = new ContextMenu(add);
        mainPane.setOnContextMenuRequested(e -> contextMenu.show(mainPane, e.getScreenX(), e.getScreenY()));

        showEvent = new EventListener() {
            @Override
            public void onResponse(Object event) {
                Platform.runLater(() -> {
                    List<Movie> movieList = (List) event;
                    updatePoints(movieList);
                });
            }

            @Override
            public void onError(Object message) {

            }
        };
    }

    void draw(double w, double h){
        this.mainW = w;
        this.mainH = h;

        panelSize = new Dimension(Double.valueOf(w).intValue()/2, Double.valueOf(h).intValue()/2);
        NetworkManager.getInstance().show(showEvent);
    }

    private void updatePoints(List<Movie> movieList){
        if(bufMovieList == null){
            bufMovieList = movieList;
            bufMovieList.forEach(this::addPoint);
        } else {
            List<Integer> deletedMovie = bufMovieList.stream().filter(o1 -> movieList.stream().noneMatch(o2 -> o2.getId().equals(o1.getId()))).map(Movie::getId)
                    .collect(Collectors.toList());

            List<Movie> addedMovie = movieList.stream().filter(o1 -> bufMovieList.stream().noneMatch(o2 -> o2.getId().equals(o1.getId())))
                    .collect(Collectors.toList());

            Map<Integer, Movie> changes = movieList.stream().filter(k -> bufMovieList.stream().allMatch(v -> v.compare(k)))
                    .collect(Collectors.toMap(Movie::getId, x -> x));

            deletedMovie.forEach(this::removeById);
            addedMovie.forEach(this::addPoint);
            changes.forEach(this::changePoint);

            bufMovieList = movieList;
        }
    }

    private void addPoint(Movie movie){
        int diam;
        if (movie.getOscarsCount() <= 50) {
            diam = 5;
        } else if (movie.getOscarsCount() <= 200) {
            diam = 10;
        } else diam = 15;

        double x_coord = movie.getCoordinates().getX() % panelSize.width + panelSize.width +  10.0;
        double y_coord = movie.getCoordinates().getY() % panelSize.height + panelSize.height + 10.0;

        int red = movie.getUsername().hashCode()*67%255;
        int green = movie.getUsername().hashCode()*54%255;
        int blue = movie.getUsername().hashCode()*78%255;


        Circle circle = new Circle();
        circle.setFill(Color.rgb(red, green, blue));
        circle.setCenterX(x_coord);
        circle.setCenterY(y_coord);
        circle.setRadius(diam);
        circle.setCursor(Cursor.HAND);
        circle.setUserData(movie);



        circle.setOnMouseEntered(event -> {
            Circle self = (Circle) event.getSource();
            Movie _movie = (Movie) self.getUserData();

            double eventX = event.getX();
            double eventY = event.getY();
            double tooltipX = eventX + self.getScene().getX() + self.getScene().getWindow().getX() + 200;
            double tooltipY = eventY + self.getScene().getY() + self.getScene().getWindow().getY() ;

            tooltip.setText(_movie.getName());
            tooltip.show(circle, tooltipX, tooltipY);
        });

        circle.setOnMouseExited(event -> {
            tooltip.hide();
        });


        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY){
                    Circle self = (Circle) event.getSource();
                    Movie movie = (Movie) self.getUserData();
                    TableModel movieModel = new TableModel();
                    movieModel.setId(movie.getId());
                    movieModel.setName(movie.getName());
                    movieModel.setX(movie.getCoordinates().getX());
                    movieModel.setY(movie.getCoordinates().getY());
                    movieModel.setCreationDate(movie.getCreationDate());
                    movieModel.setOscarsCount(movie.getOscarsCount());
                    movieModel.setGenre(movie.getGenre().name());
                    movieModel.setMpaaRating(movie.getMpaaRating().name());
                    movieModel.setPersonName(movie.getOperator().getName());
                    movieModel.setPassportID(movie.getOperator().getPassportID());
                    movieModel.setHairColor(movie.getOperator().getHairColor().name());
                    movieModel.setLocationX(movie.getOperator().getLocation().getX());
                    movieModel.setLocationY(movie.getOperator().getLocation().getY());
                    movieModel.setLocationZ(movie.getOperator().getLocation().getZ());
                    movieModel.setUserID(movie.getUser_id());
                    movieModel.setUsername(movie.getUsername());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
                        Parent root = loader.load();
                        AddUpdateView addUpdateView = loader.getController();
                        addUpdateView.transferData(movieModel, true);

                        Stage stage = new Stage();
                        stage.setResizable(false);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.titleProperty().bind(I18N.createStringBinding("key.update"));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mainPane.getChildren().add(circle);
        circleHashMap.put(movie.getId(), circle);
    }

    private void removeById(Integer id){
        mainPane.getChildren().remove(circleHashMap.get(id));
        circleHashMap.remove(id);
    }

    private void changePoint(Integer id, Movie movie){
        int diam;
        if (movie.getOscarsCount() <= 50) {
            diam = 5;
        } else if (movie.getOscarsCount() <= 200) {
            diam = 10;
        } else diam = 15;

        double x_coord = movie.getCoordinates().getX() % panelSize.width + panelSize.width +  10.0;
        double y_coord = movie.getCoordinates().getY() % panelSize.height + panelSize.height + 10.0;
        Circle circle = circleHashMap.get(id);
        if(circle != null) {
            circle.setCenterX(x_coord);
            circle.setCenterY(y_coord);
            circle.setUserData(movie);

            Timeline timeline = new Timeline();
            KeyValue keyRadius = new KeyValue(circle.radiusProperty(), diam);
            Duration duration = Duration.millis(2000);
            KeyFrame keyFrame = new KeyFrame(duration, event -> {} , keyRadius);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            circleHashMap.put(id, circle);
        }
    }
}
