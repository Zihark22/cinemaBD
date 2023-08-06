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
public class ajoutProgrammation extends JFrame implements ActionListener {
	
	private JPanel top, bottom;
	private JComboBox boxFilms, boxCinemas;
	private String[] legendFilm = {"---"};
	private String[] legendCinemas = {"---"};
	private JTextArea texteFilms,texteCinemas;
	private JList listeCinemas,listeFilms;
	private JButton valide,clear;
	private JLabel labelFilms,labelCinemas,labelDateDeb,labelDateFin;
	private Scanner scan;
	private DefaultListModel listeModel, listeModelFilms;
	private JTextField dateDeb, dateFin;
	private Dimension dimEcran,dimAppli;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private int idCine,idFilm;


	public ajoutProgrammation(String arg0) {
		super(arg0);
		
		dimEcran=Toolkit.getDefaultToolkit().getScreenSize();
		dimAppli=new Dimension();
		dimAppli.width=dimEcran.width/2;
		dimAppli.height=dimEcran.height/4;
		
		setResizable(true);
		
		setLocation((dimEcran.width-dimAppli.width)/2,(dimEcran.height/2-dimAppli.height));
		setSize(dimAppli);
		setMinimumSize(new Dimension(dimAppli.width-350,dimAppli.height));
		setMaximumSize(new Dimension(dimAppli.width+200,dimAppli.height+500));
		
		
		Container pane=getContentPane();
		pane.setLayout(new GridLayout(2,0));
		top=new JPanel();
		top.setBackground(new Color(0x130033));
		
		bottom=new JPanel();
		bottom.setBackground(new Color(0xA30000));
		pane.add(top);
		pane.add(bottom);

		
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

		try{
			completeTop();
		}
		catch (Exception e) 
		{  
			e.printStackTrace();  
		}
		
		completeBottom();
		
		idCine=0;
		idFilm=0;
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

	public void completeTop() throws SQLException{
		GridLayout grille=new GridLayout(2,1);
		top.setLayout(grille);
		
		JPanel first=new JPanel();
		first.setPreferredSize(new Dimension(300, 48));
		first.setBackground(new Color(0x111111));
		labelFilms=new JLabel("LISTE DES FILMS ", JLabel.CENTER);
		labelCinemas=new JLabel("Liste des cinémas", JLabel.CENTER);
		labelCinemas.setBackground(new Color(0xFFFFFF));
		labelCinemas.setForeground(new Color(0x2FF5F6F));
		labelFilms.setBackground(new Color(0x000000));
		labelFilms.setForeground(new Color(0xFFF4F2F));
		first.setLayout(new GridLayout(1,2));
		first.add(labelFilms);
		first.add(labelCinemas);
		first.setSize(200,10);
		first.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel second=new JPanel();
		second.setBackground(new Color(0x2F9933));
		second.setLayout(new GridLayout(1,2));
		
		/*
		texteFilms=new JTextArea("Ex : Avengers 1");
		texteFilms.setBackground(new Color(0xAFF4F2F));
		texteFilms.setRows(20);
		texteFilms.setEditable(false);
		
		texteCinemas=new JTextArea("Ex : Pathé");
		texteCinemas.setBackground(new Color(0x2FF5F6F));
		texteCinemas.setRows(20);
		texteCinemas.setEditable(false);
		*/
		//listeScroller.setPreferredSize(new Dimension(,));
		listeModelFilms = new DefaultListModel();
		listeFilms = new JList(listeModelFilms);
		listeFilms.setBackground(new Color(0xFFFFFF));
		listeFilms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeFilms.setLayoutOrientation(JList.VERTICAL);
		listeFilms.setVisibleRowCount(-1);
		
		listeModel = new DefaultListModel();
		listeCinemas = new JList(listeModel);
		listeCinemas.setBackground(new Color(0x882FFF));
		listeCinemas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeCinemas.setLayoutOrientation(JList.VERTICAL);
		listeCinemas.setVisibleRowCount(-1);
		

		JScrollPane ScrollFilms = new JScrollPane(listeFilms);
		JScrollPane ScrollCinemas = new JScrollPane(listeCinemas);
		
		second.add(ScrollFilms);
		second.add(ScrollCinemas);
		
		top.add(first);
		top.add(second);
	}
	

	
	public void completeBottom(){
		GridLayout grille=new GridLayout(5,1);
		bottom.setLayout(grille);
		
		labelDateDeb=new JLabel("Date de début: ");
		dateDeb=new JTextField("Ex : 2022-10-01");
		
		labelDateFin=new JLabel("Date de fin: ");
		dateFin=new JTextField("Ex : 2023-10-01");
		
		valide=new JButton("Valider");
		clear=new JButton("Clear");
		
		bottom.add(new JLabel("\tCHOIX DU FILM : "));
		boxFilms= new JComboBox(legendFilm);
		bottom.add(boxFilms);
		
		boxCinemas= new JComboBox(legendCinemas);
		bottom.add(new JLabel("\tCHOIX DU CINEMA : "));
		bottom.add(boxCinemas);
		
		bottom.add(labelDateDeb);
		bottom.add(dateDeb);
		bottom.add(labelDateFin);
		bottom.add(dateFin);
		bottom.add(valide);
		bottom.add(clear);
		
		valide.addActionListener(this);
		clear.addActionListener(this);
		
		try{
			listeFilms();
			listeCinemas();
		}
		catch (Exception e) 
		{  
			e.printStackTrace();  
		}

	}
	
	public void listeFilms() throws SQLException
	{
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT idFilm,titre FROM film GROUP BY titre ORDER BY idFilm ;");
		//String films="";
		listeModelFilms.removeAllElements();
		while (resultSet.next()) {
			boxFilms.addItem(resultSet.getString("idFilm")+"-"+resultSet.getString("titre"));
			//films+=resultSet.getString("titre")+"\n";
			listeModelFilms.addElement(resultSet.getString("titre"));
		}
		//texteFilms.setText(films);
		
	}
	
	public void listeCinemas() throws SQLException
	{
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT idCine,nom FROM cinema GROUP BY nom ORDER BY idCine;");
		//String cinemas="";
		listeModel.removeAllElements();
		while (resultSet.next()) {
			//cinemas+=resultSet.getString("nom")+"\n";
			listeModel.addElement(resultSet.getString("nom"));
			boxCinemas.addItem(resultSet.getString("idCine")+"-"+resultSet.getString("nom"));
		}
		//texteCinemas.setText(cinemas);
	}
	
	public void insertProg() throws SQLException {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT COUNT(idProg) FROM programmation;");
		int idProg=-1;
		if(resultSet.next())
		{
			idProg=Integer.parseInt(resultSet.getString("COUNT(idProg)"));
		}
		statement = connection.createStatement();
		statement.executeUpdate("INSERT INTO programmation VALUES("+idProg+",'"+dateDeb.getText()+"','"+dateFin.getText()+"',"+idCine+","+idFilm+");");
		
	}
	
	public void actionPerformed(ActionEvent evt)  {
		String event=evt.getActionCommand();
		
		if (event.equals("Valider"))
		{
			idCine=boxCinemas.getSelectedIndex();
			idFilm=boxFilms.getSelectedIndex();
			try {
				insertProg();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		else if (event.equals("Clear")){
			boxFilms.setSelectedIndex(0);
			boxCinemas.setSelectedIndex(0);
			dateDeb.setText("Ex : 2022-10-01");
			dateFin.setText("Ex : 2023-10-01");
		}
	}
	
	public void finConnection()
	{
		try 
		{  
			resultSet.close();
			statement.close();
			connection.close();  
		} 
		catch (Exception e) 
		{  
			e.printStackTrace();  
		} 
	}
	
	
	public static void createAndShowGUI() {
		//Create and set up the window.
		ajoutProgrammation frame = new ajoutProgrammation("Ajout Programmation");
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



