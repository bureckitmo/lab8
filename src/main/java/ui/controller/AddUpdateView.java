package ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.*;
import utils.I18N;
import ui.NetworkManager;
import ui.model.TableModel;
import utils.NumUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class AddUpdateView implements Initializable {
    public TextField id_tb;
    public TextField name_tb;
    public TextField x_tb;
    public TextField y_tb;
    public TextField date_tb;
    public Label id_lb;
    public Label name_lb;
    public Label x_lb;
    public Label y_lb;
    public Label date_lb;
    public Label oscars_lb;
    public TextField oscars_tb;
    public Label genre_lb;
    public ComboBox genre_cb;
    public Label rating_lb;
    public ComboBox rating_cb;
    public Label person_name_lb;
    public TextField person_name_tb;
    public Label passport_id_lb;
    public TextField passport_id_tb;
    public Label hair_color_lb;
    public ComboBox hair_color_cb;
    public Label loc_x_lb;
    public TextField loc_x_tb;
    public Label loc_y_lb;
    public TextField loc_y_tb;
    public Label loc_z_lb;
    public TextField loc_z_tb;

    public Button add_btn;

    private ResourceBundle resources;


    private TableModel currentMovie = new TableModel();
    private boolean isUpdate = false;
    private Integer errorCount = 0;


    public void btnOnClick(ActionEvent actionEvent) {
        if(errorCount != 0) showErrorDialog(I18N.get("error.input_ua"));
        else {
            /*
            UPDATE
             */
            if(isUpdate){
                Movie movie = new Movie(
                        currentMovie.getId(),
                        name_tb.getText(),
                        new Coordinates(Long.parseLong(x_tb.getText()), Integer.parseInt(y_tb.getText())),
                        currentMovie.getCreationDate(),
                        Integer.parseInt(oscars_tb.getText()),
                        MovieGenre.parse(genre_cb.getSelectionModel().getSelectedItem().toString()),
                        MpaaRating.parse(rating_cb.getSelectionModel().getSelectedItem().toString()),
                        new Person(
                                person_name_tb.getText(),
                                passport_id_tb.getText(),
                                Color.parse(hair_color_cb.getSelectionModel().getSelectedItem().toString()),
                                new Location(Long.parseLong(loc_x_tb.getText()), Double.parseDouble(loc_y_tb.getText()), Long.parseLong(loc_z_tb.getText()))),
                        currentMovie.getUsername(),
                        currentMovie.getUserID()
                );

                NetworkManager.getInstance().update(currentMovie.getId(), movie, new ui.listener.EventListener() {
                    @Override
                    public void onResponse(Object event) {
                        showErrorDialog((String) event);
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            }else{
                /*
                ADD
                 */
                Movie movie = new Movie(
                        name_tb.getText(),
                        new Coordinates(Long.parseLong(x_tb.getText()), Integer.parseInt(y_tb.getText())),
                        Integer.parseInt(oscars_tb.getText()),
                        MovieGenre.parse(genre_cb.getSelectionModel().getSelectedItem().toString()),
                        MpaaRating.parse(rating_cb.getSelectionModel().getSelectedItem().toString()),
                        new Person(
                                person_name_tb.getText(),
                                passport_id_tb.getText(),
                                Color.parse(hair_color_cb.getSelectionModel().getSelectedItem().toString()),
                                new Location(Long.parseLong(loc_x_tb.getText()), Double.parseDouble(loc_y_tb.getText()), Long.parseLong(loc_z_tb.getText()))
                        )
                        );

                NetworkManager.getInstance().add(movie, new ui.listener.EventListener() {
                    @Override
                    public void onResponse(Object event) {
                        showErrorDialog((String) event);
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            }
        }
    }

    void transferData(TableModel movie, boolean isUpdate){
        this.currentMovie = movie;
        this.isUpdate = isUpdate;

        add_btn.textProperty().bind(I18N.createStringBinding(isUpdate ? "key.update" : "key.add"));

        genre_cb.getSelectionModel().select(0);
        rating_cb.getSelectionModel().select(0);
        hair_color_cb.getSelectionModel().select(0);

        if(isUpdate) {
            id_tb.setText(Long.valueOf(currentMovie.getId()).toString());
            name_tb.setText(currentMovie.getName());
            x_tb.setText(Long.valueOf(currentMovie.getX()).toString());
            y_tb.setText(Integer.valueOf(currentMovie.getY()).toString());
            date_tb.setText(currentMovie.getCreationDate().toString());
            oscars_tb.setText(Integer.valueOf(currentMovie.getOscarsCount()).toString());
            genre_cb.getSelectionModel().select(currentMovie.getGenre());
            rating_cb.getSelectionModel().select(currentMovie.getMpaaRating());
            person_name_tb.setText(currentMovie.getPersonName());
            passport_id_tb.setText(currentMovie.getPassportID());
            hair_color_cb.getSelectionModel().select(currentMovie.getHairColor());
            loc_x_tb.setText(Long.valueOf(currentMovie.getLocationX()).toString());
            loc_y_tb.setText(Double.valueOf(currentMovie.getLocationY()).toString());
            loc_z_tb.setText(Long.valueOf(currentMovie.getLocationZ()).toString());
        }else {
            id_tb.setManaged(false);
            id_lb.setManaged(false);
            date_tb.setManaged(false);
            date_lb.setManaged(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        id_lb.textProperty().bind(I18N.createStringBinding("key.id"));
        name_lb.textProperty().bind(I18N.createStringBinding("key.name"));
        x_lb.textProperty().bind(I18N.createStringBinding("key.x"));
        y_lb.textProperty().bind(I18N.createStringBinding("key.y"));
        date_lb.textProperty().bind(I18N.createStringBinding("key.date"));
        oscars_lb.textProperty().bind(I18N.createStringBinding("key.oscars"));
        genre_lb.textProperty().bind(I18N.createStringBinding("key.genre"));
        rating_lb.textProperty().bind(I18N.createStringBinding("key.rating"));
        person_name_lb.textProperty().bind(I18N.createStringBinding("key.person_name"));
        passport_id_lb.textProperty().bind(I18N.createStringBinding("key.passport_id"));
        hair_color_lb.textProperty().bind(I18N.createStringBinding("key.hair_color"));
        loc_x_lb.textProperty().bind(I18N.createStringBinding("key.loc_x"));
        loc_y_lb.textProperty().bind(I18N.createStringBinding("key.loc_y"));
        loc_z_lb.textProperty().bind(I18N.createStringBinding("key.loc_z"));

        genre_cb.getItems().removeAll(genre_cb.getItems());
        genre_cb.getItems().addAll(MovieGenre.values());

        rating_cb.getItems().removeAll(rating_cb.getItems());
        rating_cb.getItems().addAll(MpaaRating.values());

        hair_color_cb.getItems().removeAll(hair_color_cb.getItems());
        hair_color_cb.getItems().addAll(Color.values());

        validateRange(x_tb, new BigDecimal(-772.0), NumUtil.LONG_MAX, false);
        validateRange(y_tb, NumUtil.INTEGER_MIN, new BigDecimal(969), false);
        validateRange(oscars_tb, new BigDecimal(0), NumUtil.INTEGER_MAX, false);
        validateRange(loc_x_tb, NumUtil.LONG_MIN, NumUtil.LONG_MAX, false);
        validateRange(loc_y_tb, NumUtil.DOUBLE_MIN, NumUtil.DOUBLE_MAX, true);
        validateRange(loc_z_tb, NumUtil.LONG_MIN, NumUtil.LONG_MAX, false);
        validateText(name_tb);
        validateText(person_name_tb);
        passport_id_tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(passport_id_tb.getText().length() < 5 ) setErrorInput(passport_id_tb);
                else removeErrorInput(passport_id_tb);
            }
        });
    }

    private void validateText(TextField tb){
        tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(tb.getText().isEmpty()) setErrorInput(tb);
                else removeErrorInput(tb);
            }
        });
    }
    private void validateRange(TextField tb, BigDecimal min, BigDecimal max, boolean isFloat){
        String intPattern = "^[-]?\\d*$";
        String decimalPattern = "^[-]?\\d+([.,]\\d+)?$";
        tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                try {
                    if(tb.getText().matches(isFloat ? decimalPattern : intPattern) && !tb.getText().isEmpty()) {
                        NumberFormat format = NumberFormat.getInstance();
                        if (!NumUtil.isInRange(format.parse(tb.getText().replace(',', '.')), min, max)) {
                            setErrorInput(tb);
                        } else removeErrorInput(tb);
                    }else setErrorInput(tb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setErrorInput(TextField tb){
        if(tb.getStyle().isEmpty()) {
            tb.setStyle("-fx-text-fill:red; -fx-border-color: red;");
            errorCount++;
        }
    }

    private void removeErrorInput(TextField tb){
        if(!tb.getStyle().isEmpty()) {
            tb.setStyle(null);
            errorCount--;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
