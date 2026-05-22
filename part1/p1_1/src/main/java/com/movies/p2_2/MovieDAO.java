package com.movies.p2_2;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MovieDAO {
    void createTable() throws SQLException;
    void dropTable() throws SQLException;
    void insert(Movie movie) throws SQLException;
    void delete(int id) throws SQLException;
    void updateTitle(int id, String newTitle) throws SQLException;
    Optional<Movie> findById(int id) throws SQLException;
    List<Movie> findAll() throws SQLException;
    List<Movie> findByTitle(String part) throws SQLException;
    List<Movie> findByGenre(String genre) throws SQLException;
    List<Movie> findByYear(int year) throws SQLException;
}
