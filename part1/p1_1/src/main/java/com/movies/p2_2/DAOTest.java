package com.movies.p2_2;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAOTest {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:movietest;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
            MovieDAO dao = new MovieDAOImpl(conn);

            dao.dropTable();
            dao.createTable();

            // Вставка
            Movie m1 = new Movie("Матрица", "Фантастика", 1999);
            Movie m2 = new Movie("Начало", "Фантастика", 2010);
            Movie m3 = new Movie("Лев", "Драма", 2016);
            Movie m4 = new Movie("Джокер", "Триллер", 2019);
            dao.insert(m1); dao.insert(m2); dao.insert(m3); dao.insert(m4);
            System.out.println("=== Все фильмы ===");
            dao.findAll().forEach(System.out::println);

            // Обновление
            dao.updateTitle(m1.getId(), "Матрица: Перезагрузка");
            System.out.println("\n=== После обновления id=" + m1.getId() + " ===");
            dao.findById(m1.getId()).ifPresent(System.out::println);

            // Удаление
            dao.delete(m3.getId());
            System.out.println("\n=== После удаления id=" + m3.getId() + " ===");
            dao.findAll().forEach(System.out::println);

            // Поиск
            System.out.println("\n=== Поиск по жанру 'Фантастика' ===");
            dao.findByGenre("Фантастика").forEach(System.out::println);

            System.out.println("\n=== Поиск по году 2019 ===");
            dao.findByYear(2019).forEach(System.out::println);

            System.out.println("\n=== Поиск по части названия 'ер' ===");
            dao.findByTitle("ер").forEach(System.out::println);
        }
    }
}
