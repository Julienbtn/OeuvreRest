package service;

import java.util.*;


import javax.ws.rs.*;
import javax.ws.rs.core.Request;

import com.google.gson.Gson;


import meserreurs.MonException;
import metier.*;
import persistance.DialogueBd;

@Path("/mediatheque")
public class WService {


	/***************************************************/
	/***************Partie sur les adh�rents **************/
	/*****************************************************/
	@POST
	@Path("/Adherents/ajout/{unAdh}")
	@Consumes("application/json")
	public void insertionAdherent(String unAdherent) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Adherent unAdh = gson.fromJson(unAdherent, Adherent.class);
		try {
			String mysql = "";
			mysql = "INSERT INTO adherent (nom_adherent, prenom_adherent, ville_adherent) ";
			mysql += " VALUES ( \'" + unAdh.getNomAdherent()+ "\', \'" + unAdh.getPrenomAdherent();
			mysql+="  \', \'"  + unAdh.getVilleAdherent() +  "\') ";
			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}

	@DELETE
	@Path("/Adherents/delete/{id}")
	public void suppressionAdherant(@PathParam("id") String id) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		String mysql = String.format("delete from adherent where id_adherent = %d;", Integer.parseInt(id));
		unDialogueBd.insertionBD(mysql);
	}


	@GET
	@Path("/Adherents")
	@Produces("application/json")
	public String rechercheLesAdherents() throws MonException {
		List<Object> rs;
		List<Adherent> mesAdherents = new ArrayList<Adherent>();
		int index = 0;
		try {
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			String mysql = "";

			mysql = "SELECT * FROM adherent ORDER BY id_adherent ASC";

			rs = unDialogueBd.lecture(mysql);

			while (index < rs.size()) {

				// On cr�e un objet Adherent
				Adherent unAdh = new Adherent();
				unAdh.setIdAdherent(Integer.parseInt(rs.get(index + 0).toString()));
				unAdh.setNomAdherent(rs.get(index + 1).toString());
				unAdh.setPrenomAdherent(rs.get(index + 2).toString());
				unAdh.setVilleAdherent(rs.get(index + 3).toString());
				index = index + 4;

				mesAdherents.add(unAdh);
			}

			Gson gson = new Gson();
			String json = gson.toJson(mesAdherents);
			return json;

		} catch (MonException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}


	/***************************************************/
	/***************Partie sur les oeuvres  **************/
	/*****************************************************/



	@GET
	@Path("/Oeuvres/{Id}")
	@Produces("application/json")
	public String rechercherOeuvreId(@PathParam("Id")  String idOeuvre) throws MonException, Exception
	{

		String mysql = "";
		String json ="";
		Oeuvrevente uneOeuvre;
		try
		{
			mysql = "SELECT id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaire";
			mysql += " FROM Oeuvrevente WHERE id_Oeuvrevente = " + idOeuvre + ";";
			uneOeuvre = rechercherOeuvre(mysql);
			Gson gson = new Gson();
			json = gson.toJson(uneOeuvre);

		} catch (MonException e)
		{
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return json;
	}

	@PUT
	@Path("/Oeuvres/edit/{Id}")
	@Consumes("application/json")
	public void modifierOeuvre(String jsonOeuvre, @PathParam("Id") String idOeuvre) throws MonException, Exception
	{
		String mysql;
		try
		{
			HashMap<String, Object> data = (new Gson()).fromJson(jsonOeuvre, HashMap.class);
			mysql = String.format(
						Locale.US,
						"update Oeuvrevente set titre_oeuvrevente = \"%s\", etat_oeuvrevente = \"%s\", " +
						"prix_oeuvrevente = \"%f\" where id_oeuvrevente = %d",
						data.get("titre"),
						data.get("etat"),
						(Double)data.get("prix"),
						((Double) data.get("identifiant")).intValue()
					);
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			unDialogueBd.insertionBD(mysql);
		} catch (MonException e)
		{
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
	}

	@GET
	@Path("/Oeuvres/lib/{libelle}")
	@Produces("application/json")
	public String rechercheOeuvreLibelle(@PathParam("libelle") String lib) throws MonException, Exception
	{
		String json="";
		String  mysql;
		Oeuvrevente uneOeuvre;
		try
		{
			mysql = "select id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaire";
			mysql += " from Oeuvrevente where titre_oeuvrevente = " + lib+ ";";
			uneOeuvre = rechercherOeuvre(mysql);
			Gson gson = new Gson();
			json = gson.toJson(uneOeuvre);

		} catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return json;
	}


	// recherche d'une Oeuvre
	// On factorise la requ�te qui doit rendre une oeuvre en vente
	public Oeuvrevente rechercherOeuvre(String requete) throws MonException
	{

		List<Object> rs;
		Oeuvrevente uneOeuvre=null;
		try
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {

				uneOeuvre = new Oeuvrevente();
				uneOeuvre.setIdOeuvrevente(Integer.parseInt(rs.get(0).toString()));
				uneOeuvre.setTitreOeuvrevente(rs.get(1).toString());
				uneOeuvre.setEtatOeuvrevente(rs.get(2).toString());
				uneOeuvre.setPrixOeuvrevente(Float.parseFloat(rs.get(3).toString()));
				int id = Integer.parseInt(rs.get(4).toString());
				// On appelle la recherche d'un propri�taire
				uneOeuvre.setProprietaire(rechercherProprietaire(id));
			}
		}

		catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return uneOeuvre;

	}

	//****************************
	// Recherche d'un propri�taire
	//****************************

	public Proprietaire rechercherProprietaire(int  id) throws MonException
	{

		List<Object> rs;
		Proprietaire  unProprietaire=null;
		String requete = " select * from Proprietaire where id_Proprietaire =" + id + ";";
		try
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {

				unProprietaire = new Proprietaire();

				unProprietaire.setIdProprietaire(Integer.parseInt(rs.get(0).toString()));
				unProprietaire.setNomProprietaire(rs.get(1).toString());
				unProprietaire.setPrenomProprietaire(rs.get(2).toString());
			}
		}

		catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return unProprietaire;
	}

	//****************************
	// Liste des oeuvres

	//****************************


	@GET
	@Path("/Oeuvres")
	// r�cup�re la valeur pass�� par webResource.path("hello").path("xxxx")
	@Produces("application/json")
	public  String  consulterListeOeuvre() throws MonException {
		List<Object> rs;
		List<Oeuvrevente> mesOeuvres = new ArrayList<Oeuvrevente>();
		String mysql="";

		int index = 0;
		try {
			mysql = "SELECT id_oeuvrevente, titre_oeuvrevente";
			mysql += " FROM Oeuvrevente order by titre_oeuvrevente ;";
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs =unDialogueBd.lecture(mysql);
			while (index < rs.size()) {
				// On cr�e un stage
				Oeuvrevente uneOeuvre = new Oeuvrevente();
				// il faut redecouper la liste pour retrouver les lignes
				uneOeuvre.setIdOeuvrevente(Integer.parseInt(rs.get( index + 0).toString()));
				uneOeuvre.setTitreOeuvrevente(rs.get( index + 1 ).toString());
				// On incr�mente tous les 2 champs
				index = index + 2;
				mesOeuvres.add(uneOeuvre);
			}
			Gson gson = new Gson();
			String json = gson.toJson(mesOeuvres);
			return json;
		} catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
	}

	@POST
	@Path("/Oeuvres/ajout/")
	@Consumes("application/json")
	public void insertionOeuvre(String uneOeuvre) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Oeuvre uneOeu = gson.fromJson(uneOeuvre, Oeuvre.class);
		try {
			String mysql = "";
			mysql = String.format(
					"INSERT INTO oeuvre (id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente, prix_oeuvrevente, " +
							"id_proprietaireIndex) VALUES(%d, '%s', '%s', %f, %d)",
					uneOeu.getIdentifiant(),
					uneOeu.getTitre(),
					uneOeu.getEtat(),
					uneOeu.getPrix(),
					uneOeu.getidproprietaire()
			);/*
			mysql = "INSERT INTO adherent (id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaireIndex) ";
			mysql += " VALUES ( \'" + uneOeu.getIdentifiant()+ "\', \'" + uneOeu.getTitre();
			mysql+="  \', \'"  + uneOeu.getEtat() +  "\',\'"+uneOeu.getEtat();
			mysql+="\',\'"+uneOeu.getPrix()+  "\',\'"+uneOeu.getidproprietaire()+"\' ";
			*/
			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}


}
