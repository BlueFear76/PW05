package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDao {

	public List<Movie> listMovies() {
		Statement statement = null;
		Connection connection = null;
		try{
			connection = DataSourceFactory.getDataSource().getConnection();
            System.out.println("Connection to the DataBase");
            String sqlQuery = "SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            
            //on récupère chaques genre dans le resultat de la requette et on les ajoute dans une liste de Genres
            List<Movie> listMovies = new ArrayList<>();
            while(resultSet.next()) {
            	Integer id = resultSet.getInt("idmovie");
            	String title = resultSet.getString("title");
            	LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
            	Integer genreId = resultSet.getInt("idgenre");
            	String genreName = resultSet.getString("name");
            	Genre genre = new Genre(genreId,genreName);
            	Integer duration = resultSet.getInt("duration");
            	String director = resultSet.getString("director");
            	String summary = resultSet.getString("summary");
            	Movie movie = new Movie(id,title,releaseDate,genre,duration,director,summary);
            	listMovies.add(movie);
            }
            return listMovies;
        }
		catch (SQLException e){
            e.printStackTrace();
            //si une erreur s'est produite on retourne une liste vide
            return new ArrayList<>();
        }
		finally {
			//Fermeture des ressources ouvertes
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources");
            }
		}
	}

	public List<Movie> listMoviesByGenre(String genreName) {
		PreparedStatement statement = null;
		Connection connection = null;
		try{
			connection = DataSourceFactory.getDataSource().getConnection();
            System.out.println("Connection to the DataBase");
             String sqlQuery = "SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE genre.name = ?";
            statement = connection.prepareStatement(sqlQuery);
	        statement.setString(1, genreName);
	        ResultSet resultSet = statement.executeQuery();
            
            //on récupère chaques genre dans le resultat de la requette et on les ajoute dans une liste de Genres
            List<Movie> listMovies = new ArrayList<>();
            while(resultSet.next()) {
            	Integer id = resultSet.getInt("idmovie");
            	String title = resultSet.getString("title");
            	LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
            	Integer genreId = resultSet.getInt("idgenre");
            	String genreNameMovie = resultSet.getString("name");
            	Genre genre = new Genre(genreId,genreNameMovie);
            	Integer duration = resultSet.getInt("duration");
            	String director = resultSet.getString("director");
            	String summary = resultSet.getString("summary");
            	Movie movie = new Movie(id,title,releaseDate,genre,duration,director,summary);
            	listMovies.add(movie);
            }
            return listMovies;
        }
		catch (SQLException e){
            e.printStackTrace();
            //si une erreur s'est produite on retourne une liste vide
            return new ArrayList<>();
        }
		finally {
			//Fermeture des ressources ouvertes
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources");
            }
		}
	}

	public Movie addMovie(Movie movie) {
		PreparedStatement statement = null;
		Connection connection = null;
		try{
			connection = DataSourceFactory.getDataSource().getConnection();
            System.out.println("Connection to the DataBase");
            String sqlQuery = "INSERT INTO movie(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
            statement = connection.prepareStatement(sqlQuery);
	        statement.setString(1, movie.getTitle());
	        statement.setDate(2, java.sql.Date.valueOf(movie.getReleaseDate()));
	        statement.setInt(3, movie.getGenre().getId());
	        statement.setInt(4, movie.getDuration()); 
	        statement.setString(5, movie.getDirector());
	        statement.setString(6, movie.getSummary());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected == 1) {
            	System.out.println("Film inseré avec succès");
            }
            else {
            	System.out.println("Erreur d'insertion");
            }
            return movie;
        }
		catch (SQLException e){
            e.printStackTrace();
            return null;
        }
		finally {
			//Fermeture des ressources ouvertes
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources");
            }
		}
	}
}
