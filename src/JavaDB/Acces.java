package JavaDB;
/* -------------------------------------------------------------------------------------------------
 * Acces a une base de donnees dont le nom est contenu dans la chaine dbFileName.
 * On suppose que la base de donnees est dans le meme dossier que le projet Java.
 * Verifiez bien que c'est le cas en consultant Eclipse :
 * La localisation du projet est donne par le menu Project/Properties/Resource/Location.
 * Ce dossier doit etre le meme que celui renfermant la base de donnees.
 * -------------------------------------------------------------------------------------------------
 */
import java.io.File;
import java.sql.Connection;  
import java.sql.DriverManager;  
public class Acces {
	
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

	public static void main(String[] args) 
	  {  
	     Connection connection = null;  
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
	     finally 
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
	 }  

}
