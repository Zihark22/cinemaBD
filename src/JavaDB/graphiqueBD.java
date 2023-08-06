package JavaDB;


import java.io.File;
import java.sql.*;

import java.util.Scanner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.*;

@SuppressWarnings({"serial","rawtypes","unchecked"})
public class graphiqueBD extends JFrame implements ActionListener {
	
	private JPanel top, bottom, centre;
	private JComboBox boxGenre,boxFilms;
	private final String[] legend = { "---","science-fiction","action","aventure","thriller","comédie","drame","fantastique","horreur"};
	private String[] legendFilm = {"---"};
	private JTextField texteDate;
	private JTextArea texteCinemas;
	private JButton valide,clear,choixFilm;
	private Dimension dimEcran,dimAppli;
	private JLabel labelFilm,resultat;
	private String genre,date;
	private Scanner scan;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private JMenuBar barreMenus;
	private JMenu menuAjout; 
	private JMenuItem miProg;

	public graphiqueBD(String arg0) {
		super(arg0);

	// DIMENSIONS ET POSITION
		dimEcran=Toolkit.getDefaultToolkit().getScreenSize();
		dimAppli=new Dimension();
		dimAppli.width=dimEcran.width/2;
		dimAppli.height=dimEcran.height/4;

		setResizable(true);
		setLocation((dimEcran.width-dimAppli.width)/2,(dimEcran.height/2-dimAppli.height));
		setSize(dimAppli);
		setMinimumSize(new Dimension(dimAppli.width-150,dimAppli.height-50));
		setMaximumSize(new Dimension(dimAppli.width+200,dimAppli.height+200));
		
		Container pane=getContentPane();
		pane.setLayout(new GridLayout(3,0));
		top=new JPanel();
		top.setBackground(new Color(0x220033));
		centre=new JPanel();
		centre.setBackground(new Color(0x52BFF6));
		bottom=new JPanel();
		bottom.setBackground(new Color(0xE00000));
		pane.add(top);
		pane.add(centre);
		pane.add(bottom);
		
		genre="";
		date="2000-01-01";
		
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
			System.out.println("échec de connexion");
			e.printStackTrace();  
		}
		
		try {
			statement = connection.createStatement();
			resultSet = null;
		}
		catch (Exception e) 
		{  
			System.out.println("échec de statement");
			e.printStackTrace();  
		}
		completeTop();
		completeCentre();
		completeBottom();
		
		barreMenus=new JMenuBar();
		menuAjout=new JMenu("Ajouter");
		miProg=new JMenuItem("Programmation");
		miProg.addActionListener(this);
		menuAjout.add(miProg);
		barreMenus.add(menuAjout);
		setJMenuBar(barreMenus);
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

	public void completeTop(){
		GridLayout grille=new GridLayout(3,1);
		top.setLayout(grille);
		top.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel first=new JPanel();
		
		first.setBackground(new Color(0xFF9933));
		JLabel mot=new JLabel("\tDATE : ");
		mot.setAlignmentX(CENTER_ALIGNMENT);
		first.setLayout(new GridLayout(1,2));
		first.add(mot);
		first.setAlignmentX(CENTER_ALIGNMENT);
		texteDate=new JTextField("Ex: "+date);
		first.add(texteDate,1,1);
		
		JPanel second=new JPanel();
		second.setBackground(new Color(0xFF9933));
		second.setLayout(new GridLayout(1,2));
		second.add(new JLabel("\tGENRE : "));
		boxGenre = new JComboBox(legend);
		second.add(boxGenre);
		
		JPanel third=new JPanel();
		third.setBackground(new Color(0xFF9933));
		third.setLayout(new GridLayout(1,2));
		valide=new JButton("Valider");
		clear=new JButton("Clear");
		third.add(valide);
		third.add(clear);
		
		top.add(first);
		top.add(second);
		top.add(third);
		
		valide.addActionListener(this);
		clear.addActionListener(this);
	}
	
	public void completeCentre(){
		GridLayout grille=new GridLayout(3,1);
		centre.setLayout(grille);
		
		labelFilm=new JLabel("\t\t Liste des films : ");
		boxFilms= new JComboBox(legendFilm);
		choixFilm=new JButton("Cinemas");
		
		centre.add(labelFilm);
		centre.add(boxFilms);
		centre.add(choixFilm);
		
		choixFilm.addActionListener(this);
	}
	
	public void completeBottom(){
		GridLayout grille=new GridLayout(2,1);
		bottom.setLayout(grille);
		
		resultat=new JLabel("\t\tRésultats cinémas : ");
		texteCinemas=new JTextArea("Ex : Gaumont");
		texteCinemas.setBackground(new Color(0xFFFFFFF));
		texteCinemas.setRows(20);
		texteCinemas.setEditable(false);
		
		bottom.add(resultat);
		bottom.add(texteCinemas);
	}
	
	public void listeFilms(String genre, String date) throws SQLException
	{
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT idFilm, titre FROM film  JOIN programmation ON programmation.idF=film.idFilm WHERE film.genre='"+genre+"' AND programmation.dateDebut<'"+date+"' AND programmation.dateFin>"+date+" GROUP BY titre;");
		while (resultSet.next()) {
			boxFilms.addItem(resultSet.getString("titre"));
		}
		
	}
	
	public void afficheCinemas(String film) throws SQLException
	{
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT nom,ville FROM cinema  JOIN programmation ON programmation.idF=film.idFilm JOIN film ON idFilm=idF WHERE film.titre='"+film+"' AND programmation.dateDebut<'"+date+"' AND programmation.dateFin>'"+date+"'  GROUP BY cinema.nom;");
		String cinemas="";
		while (resultSet.next()) {
			cinemas+=resultSet.getString("nom")+" à "+resultSet.getString("ville")+".\n";
		}
		texteCinemas.setText(cinemas);
		
	}
	
	public void actionPerformed(ActionEvent evt)  {
		String event=evt.getActionCommand();
		int index=boxGenre.getSelectedIndex();
		String genreFilm = "";
		
		if (event.equals("Valider"))
		{
			genre=legend[index];
			date=texteDate.getText();
			boxFilms.removeAllItems();
			boxFilms.addItem("---");
			
			if (genre.equals("comédie")){
				genreFilm="comedie";
			}
			else if (genre.equals("horreur")){
				genreFilm="horreur";
			}
			else if (genre.equals("science-fiction")){
				genreFilm="SF";
			}
			else if (genre.equals("action")){
				genreFilm="action";
			}
			else if (genre.equals("drame")){
				genreFilm="drame";
			}
			else if (genre.equals("thriller")){
				genreFilm="thriller";
			}
			else if (genre.equals("fantastique")){
				genreFilm="fantastique";
			}
			else if (genre.equals("animé")){
				genreFilm="anime";
			}
			else if (genre.equals("aventure")){
				genreFilm="aventure";
			}
			try {
				listeFilms(genreFilm,date);
			}
			catch (Exception e) 
			{  
				e.printStackTrace();  
			} 
		}
		else if (event.equals("Clear")){
			texteDate.setText("Ex: 2000-01-01");
			boxGenre.setSelectedIndex(0);
			boxFilms.removeAllItems();
			boxFilms.addItem("---");
			texteCinemas.setText("Ex: Gaumont");
		}
		else if (event.equals("Cinemas")){
			try {
				afficheCinemas(boxFilms.getSelectedItem().toString());
			}
			catch (Exception e) 
			{  
				e.printStackTrace();  
			} 
		}
		else if (event.equals("Programmation")){
			try {
				ajoutProg();
			}
			catch (Exception e) 
			{  
				e.printStackTrace();  
			} 
		}
	}
	
	private void ajoutProg() throws SQLException {
		finConnection();
		this.setVisible(false);
		ajoutProgrammation.createAndShowGUI();
	}
	
	public void finConnection()
	{
		try 
		{  
			if(resultSet!=null)
				resultSet.close();
			if(statement!=null)
				statement.close();
			if(connection!=null)
				connection.close(); 
			System.out.println("connexion finie");
		} 
		catch (Exception e) 
		{  
			e.printStackTrace();  
		} 
	}
	
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		graphiqueBD frame = new graphiqueBD("Ciné'App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Display the window.
		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.scan.close();
				frame.finConnection();
				System.out.println("Fermeture application !");
			}
		});
	}
	// use a new task for GUI creation
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}