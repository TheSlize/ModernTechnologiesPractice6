package com.movies.p4_3;

import com.movies.MovieEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HibernateExtra {

    public static List<MovieEntity> findPage(SessionFactory sf, int pageNumber, int pageSize) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM MovieEntity ORDER BY id", MovieEntity.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }

    public static void runAggregationQueries(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.println("=== Количество фильмов по жанрам ===");
            List<Object[]> byGenre = session.createQuery(
                    "SELECT genre, COUNT(*) FROM MovieEntity GROUP BY genre", Object[].class).list();
            byGenre.forEach(row -> System.out.println(row[0] + ": " + row[1]));

            System.out.println("\n=== Средний год выхода ===");
            Double avgYear = session.createQuery(
                    "SELECT AVG(year) FROM MovieEntity", Double.class).uniqueResult();
            System.out.printf("Средний год: %.1f%n", avgYear);

            System.out.println("\n=== Новейший фильм каждого жанра ===");
            List<Object[]> newest = session.createQuery(
                    "SELECT genre, MAX(year) FROM MovieEntity GROUP BY genre", Object[].class).list();
            newest.forEach(row -> System.out.println(row[0] + ": " + row[1]));
        }
    }

}
