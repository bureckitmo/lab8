package managers;

import csv.*;
import exceptions.EmptyFileException;
import models.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import java.time.LocalDate;
import java.util.stream.Collectors;


public class CollectionManager {

    /**
     * Коллекиця
     */
    private List<Movie> collection = new ArrayList<Movie>();

    /**
     * Дата инициализации
     */
    private LocalDate initializationDate = LocalDate.now();

    private Integer maxId = 0;


    /**
     * CSV manager
     */
    private CSVManager manager;

    public CollectionManager(String path){
        try {
            this.manager = new CSVManager(new CSVFile(path));
            this.collection = manager.getMovies();
            this.collection.forEach(x-> {
                if(maxId < x.getId())
                    maxId = x.getId();
            });

        } catch (EmptyFileException e) {

            System.out.println("File is empty!");
            System.exit(0);

        } catch (IOException e) {

            System.out.println("File does not exist!");
            System.exit(0);

        } catch (ParseException e) {
            System.out.println("Failed to parse csv file!");
            System.exit(0);
        }
    }

    public CollectionManager(){
        this.collection = new ArrayList<>();
        this.initializationDate = LocalDate.now();
    }

    public CollectionManager(ArrayList<Movie> collection){
        this.initializationDate = LocalDate.now();
        this.collection = Collections.synchronizedList(collection);
        this.maxId = collection.size() + 1;
    }

    /**
     * добавляет новый элемент
     * @param movie
     */
    public void addElement(Movie movie){
        //movie.setCreationDate(LocalDate.now());
        this.getCollection().add(movie);
    }

    /**
     * добавляет новый элемент с условием
     * @param movie
     * @return
     */
    public boolean addIfMin(Movie movie){
        List<Movie> movies = this.getCollection().stream().sorted().collect(Collectors.toList());
        if (movies.isEmpty()) {
            return false;
        }

        Movie first = movies.get(0);
        if (first.compareTo(movie) > 0) {
            addElement(movie);
            return true;
        } else {
            return false;
        }
    }

    /**
     * удаляет элементы, меньше заданного
     * @param movie
     */
    public void removeLower(Movie movie){
        List<Movie> cities = this.getCollection().stream().sorted().collect(Collectors.toList());
        cities.forEach(x -> {
            if(x.compareTo(movie) < 0){
                this.getCollection().remove(x);
            }
        });
    }

    /**
     * удаляет элементы, больше заданного
     * @param movie
     */
    public void removeGreater(Movie movie){
        List<Movie> cities = this.getCollection().stream().sorted().collect(Collectors.toList());
        cities.forEach(x -> {
            if(x.compareTo(movie) > 0){
                this.getCollection().remove(x);
            }
        });
    }

    /**
     * находит элементы по его имени
     * @param person
     * @return
     */
    public ArrayList<Movie> findOperator(Person person){
        ArrayList<Movie> out = new ArrayList<>();
        this.getCollection().forEach(x ->{
            if (x.getOperator().compareTo(person) == 0)
                out.add(x);
        });

        return out;
    }


    /**
     * удаляет элемент коллекции
     * @param id
     */
    public boolean remove(int id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, Movie> entry = findById(id).entrySet().iterator().next();
            this.getCollection().remove(entry.getValue());
            return true;
        }

        return false;
    }

    /**
     * обновляет коллекцию по его id
     * @param movie
     * @param id
     */
    public boolean update(Movie movie, Integer id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, Movie> entry = findById(id).entrySet().iterator().next();
            Movie updMovie = entry.getValue();
            updMovie.setName(movie.getName());
            updMovie.setCoordinates(movie.getCoordinates());
            updMovie.setOscarsCount(movie.getOscarsCount());
            updMovie.setGenre(movie.getGenre());
            updMovie.setMpaaRating(movie.getMpaaRating());
            updMovie.setOperator(movie.getOperator());

            this.getCollection().set(entry.getKey(), updMovie);
            return true;
        }

        return false;
    }

    public boolean checkIdExist(Integer id){
        for(Movie movie : this.getCollection()){
            if(movie.getId().equals(id)){
                return true;
            }
        }

        return false;
    }

    /**
     * ищет элемент по id
     * @param id
     * @return
     */
    private Map<Integer, Movie> findById(Integer id){
        Map<Integer, Movie> map = new HashMap<>();
        for(int i=0;i<this.getCollection().size();i++) {
            if(this.getCollection().get(i).getId().equals(id)) {
                map.put(i, this.getCollection().get(i));
                return map;
            }
        }

        return null;
    }

    public ArrayList<Movie> sortByMpaaAsc(){
        ArrayList<Movie> sortColl = new ArrayList<>(this.getCollection());
        sortColl.sort(Comparator.comparing(Movie::getMpaaRating));

        return sortColl;
    }

    public ArrayList<Movie> sortByMpaaDes(){
        ArrayList<Movie> sortColl = sortByMpaaAsc();
        Collections.reverse(sortColl);

        return sortColl;
    }

    /**
     * очищает коллекцию
     */
    public void clear(){
        this.getCollection().clear();
    }




    public CSVManager getManager(){ return manager; }

    public List<Movie> getCollection() { return collection; }

    public LocalDate getInitializationDate() { return initializationDate; }
}
