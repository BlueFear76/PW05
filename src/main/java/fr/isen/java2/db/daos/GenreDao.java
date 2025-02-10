package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		Statement statement = null;
		Connection connection = null;
		try{
			connection = DataSourceFactory.getDataSource().getConnection();
            System.out.println("Connexion avec la base de donnée");
            String sqlQuery = "SELECT * FROM genre";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            
            //on récupère chaques genre dans le resultat de la requette et on les ajoute dans une liste de Genres
            List<Genre> listGenres = new ArrayList<>();
            while(resultSet.next()) {
            	Integer genreId = resultSet.getInt("idgenre");
            	String genreName = resultSet.getString("name");
            	Genre genre = new Genre(genreId,genreName);
            	System.out.println(genreId);
            	System.out.println(genreName);
            	listGenres.add(genre);
            }
            return listGenres;
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

	public Genre getGenre(String name) {
	    PreparedStatement statement = null;
	    Connection connection = null;
	    ResultSet resultSet = null;
	    try {
	        connection = DataSourceFactory.getDataSource().getConnection();
	        System.out.println("Connexion avec la base de donnée");
	        String sqlQuery = "SELECT * FROM genre WHERE name = ?";
	        statement = connection.prepareStatement(sqlQuery);
	        statement.setString(1, name);
	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            Integer genreId = resultSet.getInt("idgenre");
	            String genreName = resultSet.getString("name");
	            return new Genre(genreId, genreName);
	        } else {
	            return null; // Si aucun genre n'est trouvé, retourner null
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (statement != null) statement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            System.out.println("Erreur lors de la fermeture des ressources");
	        }
	    }
	}

	public void addGenre(String name) {
		PreparedStatement statement = null;
		Connection connection = null;
		try{
			connection = DataSourceFactory.getDataSource().getConnection();
            System.out.println("Connexion avec la base de donnée");
            String sqlQuery = "INSERT INTO genre(name) VALUES('"+ name +"')";
            statement = connection.prepareStatement(sqlQuery);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected == 1) {
            	System.out.println("Genre inseré avec succès");
            }
            else {
            	System.out.println("Erreur d'insertion");
            }
        }
		catch (SQLException e){
            e.printStackTrace();
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
