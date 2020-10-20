package ui.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Movie;
import utils.I18N;
import ui.ClientMainLauncher;
import ui.NetworkManager;
import ui.listener.EventListener;
import ui.model.TableModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {
    public TableView table_movie;
    public TextField search_tb;
    public Button info_btn;
    public Button clear_btn;
    public Button execute_btn;
    public Button add_btn;

    public TableColumn tv_id;
    public TableColumn tv_name;
    public TableColumn tv_x;
    public TableColumn tv_y;
    public TableColumn tv_date;
    public TableColumn tv_oscars;
    public TableColumn tv_genre;
    public TableColumn tv_mpaa_rating;
    public TableColumn tv_person_name;
    public TableColumn tv_passport_id;
    public TableColumn tv_color;
    public TableColumn tv_loc_x;
    public TableColumn tv_loc_y;
    public TableColumn tv_loc_z;
    public TableColumn tv_username;


    public MenuButton menubtn_filter;
    public ToggleGroup menuToggleGroup;

    private TableColumn sortcolumn = null;
    private TableColumn.SortType st = null;

    private FilteredList<TableModel> filteredData;

    private boolean filterIdIsSelect = false;
    private boolean filterNameIsSelect = false;
    private boolean filterXIsSelect = false;
    private boolean filterYIsSelect = false;
    private boolean filterDateIsSelect = false;
    private boolean filterOscarsIsSelect = false;
    private boolean filterGenreIsSelect = false;
    private boolean filterRatingIsSelect = false;
    private boolean filterPersonNameIsSelect = false;
    private boolean filterPassportIsSelect = false;
    private boolean filterColorIsSelect = false;
    private boolean filterLocXIsSelect = false;
    private boolean filterLocYIsSelect = false;
    private boolean filterLocZIsSelect = false;
    private boolean filterUsernameIsSelect = false;

    public EventListener showEvent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        search_tb.promptTextProperty().bind(I18N.createStringBinding("key.search"));
        info_btn.textProperty().bind(I18N.createStringBinding("key.info"));
        clear_btn.textProperty().bind(I18N.createStringBinding("key.clear_all"));
        execute_btn.textProperty().bind(I18N.createStringBinding("key.execute_script"));
        add_btn.textProperty().bind(I18N.createStringBinding("key.add"));

        tv_name.textProperty().bind(I18N.createStringBinding("key.table_name"));
        tv_date.textProperty().bind(I18N.createStringBinding("key.table_date"));
        tv_oscars.textProperty().bind(I18N.createStringBinding("key.table_oscars"));
        tv_genre.textProperty().bind(I18N.createStringBinding("key.table_genre"));
        tv_mpaa_rating.textProperty().bind(I18N.createStringBinding("key.table_mpaa_rating"));
        tv_person_name.textProperty().bind(I18N.createStringBinding("key.table_person_name"));
        tv_passport_id.textProperty().bind(I18N.createStringBinding("key.table_passport_id"));
        tv_color.textProperty().bind(I18N.createStringBinding("key.table_color"));
        tv_username.textProperty().bind(I18N.createStringBinding("key.table_username"));

        menubtn_filter.textProperty().bind(I18N.createStringBinding("key.filter_by"));

        tv_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tv_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tv_x.setCellValueFactory(new PropertyValueFactory<>("X"));
        tv_y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        tv_date.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        tv_oscars.setCellValueFactory(new PropertyValueFactory<>("OscarsCount"));
        tv_genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        tv_mpaa_rating.setCellValueFactory(new PropertyValueFactory<>("MpaaRating"));
        tv_person_name.setCellValueFactory(new PropertyValueFactory<>("PersonName"));
        tv_passport_id.setCellValueFactory(new PropertyValueFactory<>("PassportID"));
        tv_color.setCellValueFactory(new PropertyValueFactory<>("HairColor"));
        tv_loc_x.setCellValueFactory(new PropertyValueFactory<>("LocationX"));
        tv_loc_y.setCellValueFactory(new PropertyValueFactory<>("LocationY"));
        tv_loc_z.setCellValueFactory(new PropertyValueFactory<>("LocationZ"));
        tv_username.setCellValueFactory(new PropertyValueFactory<>("Username"));

        refreshTableLocale();
        makeContextMenuFilter();

        search_tb.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Long.valueOf(movie.getId()).toString().toLowerCase().contains(lowerCaseFilter) && filterIdIsSelect) {
                    return true;
                }

                if (movie.getName().toLowerCase().contains(lowerCaseFilter) && filterNameIsSelect) {
                    return true;
                }

                if (Long.valueOf(movie.getX()).toString().toLowerCase().contains(lowerCaseFilter) && filterXIsSelect) {
                    return true;
                }

                if (Integer.valueOf(movie.getY()).toString().toLowerCase().contains(lowerCaseFilter) && filterYIsSelect) {
                    return true;
                }

                if (movie.getCreationDate().toString().toLowerCase().contains(lowerCaseFilter) && filterDateIsSelect) {
                    return true;
                }

                if (Integer.valueOf(movie.getOscarsCount()).toString().toLowerCase().contains(lowerCaseFilter) && filterOscarsIsSelect) {
                    return true;
                }

                if (movie.getGenre().toLowerCase().contains(lowerCaseFilter) && filterGenreIsSelect) {
                    return true;
                }

                if (movie.getMpaaRating().toLowerCase().contains(lowerCaseFilter) && filterRatingIsSelect) {
                    return true;
                }

                if (movie.getPersonName().toLowerCase().contains(lowerCaseFilter) && filterPersonNameIsSelect) {
                    return true;
                }

                if (movie.getPassportID().toLowerCase().contains(lowerCaseFilter) && filterPassportIsSelect) {
                    return true;
                }

                if (movie.getHairColor().toLowerCase().contains(lowerCaseFilter) && filterColorIsSelect) {
                    return true;
                }

                if (Long.valueOf(movie.getLocationX()).toString().toLowerCase().contains(lowerCaseFilter) && filterLocXIsSelect) {
                    return true;
                }

                if (Double.valueOf(movie.getLocationY()).toString().toLowerCase().contains(lowerCaseFilter) && filterLocYIsSelect) {
                    return true;
                }

                if (Long.valueOf(movie.getLocationZ()).toString().toLowerCase().contains(lowerCaseFilter) && filterLocZIsSelect) {
                    return true;
                }

                if (movie.getUsername().toLowerCase().contains(lowerCaseFilter) && filterUsernameIsSelect) {
                    return true;
                }

                return false;
            });
        });

        table_movie.setRowFactory(param -> {
            TableRow<TableModel> row = new TableRow<>();
            MenuItem edit = new MenuItem();
            edit.textProperty().bind(I18N.createStringBinding("key.edit"));
            edit.setOnAction(event1 -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
                    Parent root = loader.load();
                    AddUpdateView addUpdateView = loader.getController();
                    addUpdateView.transferData(row.getItem(), true);

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
            });

            MenuItem remove = new MenuItem();
            remove.textProperty().bind(I18N.createStringBinding("key.remove"));
            remove.setOnAction(event2 -> {
                NetworkManager.getInstance().remove(row.getItem().getId(), new EventListener() {
                    @Override
                    public void onResponse(Object event) {
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            });

            ContextMenu rowMenu = new ContextMenu(edit, remove);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu).otherwise((ContextMenu) null));

            return row;
        });

        showEvent = new EventListener() {
            @Override
            public void onResponse(Object event) {
                Platform.runLater(() -> {
                    if (table_movie.getSortOrder().size() > 0) {
                        sortcolumn = (TableColumn) table_movie.getSortOrder().get(0);
                        st = sortcolumn.getSortType();
                    }

                    List<Movie> movieList = (List) event;
                    ObservableList<TableModel> movieModels = FXCollections.observableArrayList();

                    movieList.forEach(x -> {
                        TableModel movieModel = new TableModel();
                        movieModel.setId(x.getId());
                        movieModel.setName(x.getName());
                        movieModel.setX(x.getCoordinates().getX());
                        movieModel.setY(x.getCoordinates().getY());
                        movieModel.setCreationDate(x.getCreationDate());
                        movieModel.setOscarsCount(x.getOscarsCount());
                        movieModel.setGenre(x.getGenre().name());
                        movieModel.setMpaaRating(x.getMpaaRating().name());
                        movieModel.setPersonName(x.getOperator().getName());
                        movieModel.setPassportID(x.getOperator().getPassportID());
                        movieModel.setHairColor(x.getOperator().getHairColor().name());
                        movieModel.setLocationX(x.getOperator().getLocation().getX());
                        movieModel.setLocationY(x.getOperator().getLocation().getY());
                        movieModel.setLocationZ(x.getOperator().getLocation().getZ());
                        movieModel.setUserID(x.getUser_id());
                        movieModel.setUsername(x.getUsername());

                        movieModels.add(movieModel);
                    });

                    filteredData = new FilteredList<>(movieModels, p -> true);
                    SortedList<TableModel> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(table_movie.comparatorProperty());

                    table_movie.setItems(sortedData);

                    notifySearchTextField();

                    if (sortcolumn != null) {
                        table_movie.getSortOrder().add(sortcolumn);
                        sortcolumn.setSortType(st);
                        sortcolumn.setSortable(true);
                    }
                });
            }

            @Override
            public void onError(Object message) {

            }
        };

        show();
    }

    public void show(){
        NetworkManager.getInstance().show(showEvent);
    }

    public void onAddClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), ClientMainLauncher.resources);
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
    }

    public void onExecuteClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/ExecuteView.fxml"), ClientMainLauncher.resources);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.titleProperty().bind(I18N.createStringBinding("key.execute_script"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClearAllClick(ActionEvent actionEvent) {
        NetworkManager.getInstance().clear(new EventListener() {
            @Override
            public void onResponse(Object event) {
                showErrorDialog((String)event);
            }

            @Override
            public void onError(Object message) {
                showErrorDialog((String)message);
            }
        });
    }

    public void onInfoClick(ActionEvent actionEvent) {
        NetworkManager.getInstance().info(new EventListener() {
            @Override
            public void onResponse(Object event) {
                showErrorDialog((String)event);
            }

            @Override
            public void onError(Object message) {

            }
        });
    }


    public void refreshTableLocale() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(I18N.getLocale());
        tv_date.setCellFactory(column -> new TableCell<TableModel, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });
    }

    private void makeContextMenuFilter(){
        RadioMenuItem id_ci = new RadioMenuItem("Id");
        id_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterIdIsSelect = newValue; notifySearchTextField(); });
        id_ci.setSelected(true);

        RadioMenuItem name_ci = new RadioMenuItem();
        name_ci.textProperty().bind(I18N.createStringBinding("key.table_name"));
        name_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterNameIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem x_ci = new RadioMenuItem("X");
        x_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterXIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem y_ci = new RadioMenuItem("Y");
        y_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterYIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem date_ci = new RadioMenuItem();
        date_ci.textProperty().bind(I18N.createStringBinding("key.table_date"));
        date_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterDateIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem oscars_ci = new RadioMenuItem();
        oscars_ci.textProperty().bind(I18N.createStringBinding("key.table_oscars"));
        oscars_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterOscarsIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem genre_ci = new RadioMenuItem();
        genre_ci.textProperty().bind(I18N.createStringBinding("key.table_genre"));
        genre_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterGenreIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem mpaa_rating_ci = new RadioMenuItem();
        mpaa_rating_ci.textProperty().bind(I18N.createStringBinding("key.table_mpaa_rating"));
        mpaa_rating_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterRatingIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem person_name_ci = new RadioMenuItem();
        person_name_ci.textProperty().bind(I18N.createStringBinding("key.table_person_name"));
        person_name_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterPersonNameIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem passport_ci = new RadioMenuItem();
        passport_ci.textProperty().bind(I18N.createStringBinding("key.table_passport_id"));
        passport_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterPassportIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem color_ci = new RadioMenuItem();
        color_ci.textProperty().bind(I18N.createStringBinding("key.table_color"));
        color_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterColorIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem username_ci = new RadioMenuItem();
        username_ci.textProperty().bind(I18N.createStringBinding("key.table_username"));
        username_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterUsernameIsSelect = newValue; notifySearchTextField(); });

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(id_ci);
        toggleGroup.getToggles().add(name_ci);
        toggleGroup.getToggles().add(x_ci);
        toggleGroup.getToggles().add(y_ci);
        toggleGroup.getToggles().add(date_ci);
        toggleGroup.getToggles().add(oscars_ci);
        toggleGroup.getToggles().add(genre_ci);
        toggleGroup.getToggles().add(mpaa_rating_ci);
        toggleGroup.getToggles().add(person_name_ci);
        toggleGroup.getToggles().add(passport_ci);
        toggleGroup.getToggles().add(color_ci);
        toggleGroup.getToggles().add(username_ci);

        menubtn_filter.getItems().add(id_ci);
        menubtn_filter.getItems().add(name_ci);
        menubtn_filter.getItems().add(x_ci);
        menubtn_filter.getItems().add(y_ci);
        menubtn_filter.getItems().add(date_ci);
        menubtn_filter.getItems().add(oscars_ci);
        menubtn_filter.getItems().add(genre_ci);
        menubtn_filter.getItems().add(mpaa_rating_ci);
        menubtn_filter.getItems().add(person_name_ci);
        menubtn_filter.getItems().add(passport_ci);
        menubtn_filter.getItems().add(color_ci);
        menubtn_filter.getItems().add(username_ci);


        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    private void notifySearchTextField(){
        String oldValue = search_tb.getText();
        search_tb.setText("");
        search_tb.setText(oldValue);
    }

    public void onEnLang(ActionEvent actionEvent) { I18N.setLocale(Locale.ENGLISH); refreshTableLocale(); }

    public void onRuLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("ru")); refreshTableLocale(); }

    public void onRoLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("ro")); refreshTableLocale(); }

    public void onHrLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("hr")); refreshTableLocale(); }

    public void onEsLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("es_EC")); refreshTableLocale(); }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
