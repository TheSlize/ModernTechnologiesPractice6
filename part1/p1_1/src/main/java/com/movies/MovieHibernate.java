package com.movies;

import com.movies.p4_3.HibernateExtra;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * CRUD операции с фильмами через Hibernate ORM.
 * Учебное задание: Hibernate - работа с сущностями
 */

// ==================== Основной класс ====================
public class MovieHibernate {
    
    private SessionFactory sessionFactory;
    
    public MovieHibernate() {
        // Конфигурация Hibernate (programmatic, без XML)
        this.sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }
    
    public static void main(String[] args) {
        MovieHibernate app = new MovieHibernate();
        
        try {
            // Создаем сессию
            Session session = app.sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            
            System.out.println("=== Сохранение фильмов ===");
            
            // TODO: Сохраните 4 фильма используя app.saveMovie()
            // Фильмы:
            // 1. "Матрица", "sci-fi", 1999
            // 2. "Начало", "sci-fi", 2010
            // 3. "Крестный отец", "crime", 1972
            // 4. "Темный рыцарь", "action", 2008
            
            // ▼ ВАШ КОД ЗДЕСЬ ▼
            app.saveMovie(session, new MovieEntity("Матрица", "sci-fi", 1999));
            app.saveMovie(session, new MovieEntity("Начало", "sci-fi", 2010));
            app.saveMovie(session, new MovieEntity("Крестный отец", "crime", 1972));
            app.saveMovie(session, new MovieEntity("Темный рыцарь", "action", 2008));
            // ▲ КОНЕЦ ВАШЕГО КОДА ▲
            
            tx.commit();
            System.out.println(" Фильмы сохранены\n");
            
            // TODO: Найдите все sci-fi фильмы используя app.findMoviesByGenre()
            System.out.println("=== Поиск по жанру 'sci-fi' ===");
            // ▼ ВАШ КОД ЗДЕСЬ ▼
            app.findMoviesByGenre(session, "sci-fi").forEach(System.out::println);
            // ▲ КОНЕЦ ВАШЕГО КОДА ▲
            
            // Демонстрация других операций
            System.out.println("\n=== Все фильмы ===");
            List<MovieEntity> allMovies = session.createQuery("FROM MovieEntity", MovieEntity.class).list();
            allMovies.forEach(System.out::println);
            
            // Обновление
            System.out.println("\n=== Обновление фильма ===");
            tx = session.beginTransaction();
            if (!allMovies.isEmpty()) {
                MovieEntity first = allMovies.get(0);
                app.updateMovie(session, first.getId(), "Матрица (Обновлено)", "sci-fi", 1999);
            }
            tx.commit();
            
            // Удаление
            System.out.println("\n=== Удаление фильма ===");
            tx = session.beginTransaction();
            if (allMovies.size() > 1) {
                app.deleteMovie(session, allMovies.get(1).getId());
            }
            tx.commit();
            
            // Итоговый список
            System.out.println("\n=== Итоговый список фильмов ===");
            List<MovieEntity> finalMovies = session.createQuery("FROM MovieEntity", MovieEntity.class).list();
            finalMovies.forEach(System.out::println);

            System.out.println("\n=== Страница 1 (3 фильма) ===");
            HibernateExtra.findPage(session.getSessionFactory(), 1, 3).forEach(System.out::println);

            System.out.println("\n=== Страница 2 (3 фильма) ===");
            HibernateExtra.findPage(session.getSessionFactory(), 2, 3).forEach(System.out::println);

            System.out.println("\n=== Агрегация ===");
            HibernateExtra.runAggregationQueries(session.getSessionFactory());
            
            session.close();
            
        } finally {
            app.sessionFactory.close();
        }
    }
    
    /**
     * TODO: Реализуйте сохранение фильма в базу данных
     * Используйте session.persist(movie) для сохранения
     * Не забудьте, что транзакция уже начата в main()
     */
    public void saveMovie(Session session, MovieEntity movie) {
        // ▼ ВАШ КОД ЗДЕСЬ ▼
        session.persist(movie);
        // ▲ КОНЕЦ ВАШЕГО КОДА ▲
    }
    
    /**
     * TODO: Реализуйте поиск фильмов по жанру с использованием HQL
     * HQL запрос: "FROM Movie WHERE genre = :genre"
     * Используйте session.createQuery() и setParameter()
     * Верните List<Movie>
     */
    public List<MovieEntity> findMoviesByGenre(Session session, String genre) {
        // ▼ ВАШ КОД ЗДЕСЬ ▼
        String hql = "FROM MovieEntity m WHERE m.genre = :genre";
        return session.createQuery(hql, MovieEntity.class)
                .setParameter("genre", genre)
                .getResultList();
        // ▲ КОНЕЦ ВАШЕГО КОДА ▲
    }
    
    // ==================== Методы уже реализованы ниже ====================
    
    public void updateMovie(Session session, Long id, String title, String genre, Integer year) {
        MovieEntity movie = session.get(MovieEntity.class, id);
        if (movie != null) {
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setYear(year);
            session.merge(movie);
            System.out.println("Обновлен фильм: " + movie.getTitle());
        }
    }
    
    public void deleteMovie(Session session, Long id) {
        MovieEntity movie = session.get(MovieEntity.class, id);
        if (movie != null) {
            session.remove(movie);
            System.out.println("Удален фильм: " + movie.getTitle());
        }
    }
}
