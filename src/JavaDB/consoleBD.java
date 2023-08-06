package JavaDB;

import java.io.File;  
import java.sql.*;

import java.util.Scanner;

public class consoleBD {
	private Scanner scan;
	private Connection connection;
	
	public consoleBD()
	{
		scan = new Scanner(System.in);
		connection = null; 
		try 
		{  	
			String location=getLocationOfDataBase("BDcinema.db");
			System.out.println("Location of database: "+location);
			String url="jdbc:sqlite:"+location;
			Class.forName("org.sqlite.JDBC");  
			connection = DriverManager.getConnection(url); 
			System.out.println("Connexion reussie");
		} 
		catch (Exception e) 
		{  
			System.out.println("echec de connexion");
			e.printStackTrace();  
		}
	}
	

	public Scanner getScan() {
		return scan;
	}

	public void setScan(Scanner scan) {
		this.scan = scan;
	}



	public void findMovie() throws SQLException
	{

		System.out.print("Date (sous la forme AAAA-MM-JJ) :");
		scan = new Scanner(System.in);
		String date = scan.next();

		System.out.println("Genre :");
		System.out.println("\t 0 - Science-Fiction");
		System.out.println("\t 1 - Comédie");
		System.out.println("\t 2 - Aventure");
		System.out.println("\t 3 - Action");
		System.out.println("\t 4 - Drame");
		System.out.println("\t 5 - Action");
		System.out.println("\t 6 - Fantastique");
		System.out.println("\t 7 - Horreur");
		System.out.println("\t 8 - Thriller");
		System.out.println("\t 9 - Animé");

		System.out.print("\nChoix : ");
		scan = new Scanner(System.in);
		int choixGenre  = scan.nextInt();

		String genre="NULL";

		switch(choixGenre) 
		{
		case 0 : 
			genre = "SF";
			break;
		case 1 : 
			genre = "comedie";
			break;
		case 2 : 
			genre = "aventure";
			break;
		case 3 : 
			genre = "action";
			break;
		case 4 : 
			genre = "drame";
			break;
		case 5 : 
			genre = "action";
			break;
		case 6 : 
			genre = "fantastique";
			break;
		case 7 : 
			genre = "horreur";
			break;
		case 8 : 
			genre = "thriller";
			break;
		case 9 : 
			genre = "anime";
			break;
		}
		System.out.println("\nLe genre choisit est "+genre+" pour le "+date+".");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT idFilm, titre FROM film  JOIN programmation ON programmation.idF=film.idFilm WHERE film.genre='"+genre+"' AND programmation.dateDebut<'"+date+"' AND programmation.dateFin>"+date+" GROUP BY titre;");
		
		System.out.println("\nListe des films :");
		while (resultSet.next()) {
			System.out.println("\t"+resultSet.getString("idFilm")+"-"+ resultSet.getString("titre"));
		}
		
		System.out.print("\nChoix : ");
		int choixFilm = scan.nextInt();
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT cinema.nom, cinema.ville FROM cinema  JOIN programmation ON programmation.idC=cinema.idCine JOIN film ON film.idFilm=programmation.idF WHERE idFilm='"+choixFilm+"' AND programmation.dateDebut<'"+date+"' AND programmation.dateFin>'"+date+"'  GROUP BY cinema.nom;");
		System.out.println("\nListe des cinémas :");
		while (resultSet.next()) {
			System.out.println("\t"+resultSet.getString("nom")+" à "+resultSet.getString("ville"));
		}

		scan.close();
		resultSet.close(); 
		statement.close();
	}

	
	public void insertProg() throws SQLException
	{
		System.out.println("\nVoici la liste des films :");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT idFilm, titre FROM film;");
		while (resultSet.next()) {
			System.out.println("\t"+resultSet.getString("idFilm")+"-"+ resultSet.getString("titre"));
		}
		System.out.print("\nChoix du film :");
		scan = new Scanner(System.in);
		int idFilm = scan.nextInt();
		//System.out.println("film = "+idFilm);
		
		/////////////////////////////////////////////
		
		System.out.println("\nVoici la liste des cinéma :");
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT idCine, nom FROM cinema;");
		while (resultSet.next()) {
			System.out.println("\t"+resultSet.getString("idCine")+"-"+ resultSet.getString("nom"));
		}
		System.out.print("\nChoix du cinéma :");
		int idCine = scan.nextInt();
		//System.out.println("cinema = "+idCine);
		
		/////////////////////////////////////////////

		System.out.print("\nChoix date de début (sous la forme AAAA-MM-JJ) : ");
		String dateDeb  = scan.next();
		//System.out.println("date de debut = "+dateDeb);
		
		System.out.print("\nChoix date de fin (sous la forme AAAA-MM-JJ) : ");
		String dateFin  = scan.next();
		//System.out.println("date de fin = "+dateFin);
		
		/////////////////////////////////////////////
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT COUNT(idProg) FROM programmation;");
		int idProg=-1;
		if(resultSet.next())
		{
			idProg=Integer.parseInt(resultSet.getString("COUNT(idProg)"));
			idProg++;
			//System.out.println("idProg="+idProg);
		}
		statement = connection.createStatement();
		statement.executeUpdate("INSERT INTO programmation VALUES("+idProg+",'"+dateDeb+"','"+dateFin+"',"+idCine+","+idFilm+");");
		
		resultSet.close(); 
		statement.close();
		scan.close();
		
	}

	
	public static void afficherMenu()
	{
		System.out.println("Menu :");
		System.out.println("------------------------------------");
		System.out.println("\t 0-Quitter");
		System.out.println("\t 1-Trouver un film");
		System.out.println("\t 2-Insérer une programmation");
		System.out.println("------------------------------------");
	}
	
	public static String getLocationOfDataBase(String dbFileName){
		//1. Retrieve the working directory: 
		//   the location of the java project (identical to Project/Properties/Resource/Location
		String path=System.getProperty("user.dir");
		File file = new File(path);
		// 2. Retrieve the parent directory
		String parentPath = file.getAbsoluteFile().getParent();
		// 3. concatenate the database file name
		String result=parentPath+System.getProperty("file.separator")+dbFileName;
		return result;
	}
	
	public void finConnection()
	{
		try 
		{  
			connection.close();  
		} 
		catch (Exception e) 
		{  
			e.printStackTrace();  
		} 
	}
	
	public static void main(String []args) throws SQLException
	{ 	
		consoleBD console = new consoleBD();
		int choix;

		afficherMenu();
		System.out.print("Choix : ");

		choix = console.scan.nextInt();
		switch(choix)
		{
		case 0 : 
			System.out.println("Quitter");
			break;
		case 1 : 
			console.findMovie();
			break;
		case 2 :
			console.insertProg();
			break;
		}
		console.scan.close();
		System.out.println("\nTerminé !");
		console.finConnection();
	}	
}
