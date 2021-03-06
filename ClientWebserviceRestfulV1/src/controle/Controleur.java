package controle;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import consommation.Appel;
import metier.*;

/**
 * Servlet implementation class Controleur
 */
@WebServlet("/Controleur")
public class Controleur extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ACTION_TYPE = "action";
	private static final String LISTER_ADHERENT = "listerAdherent";
	private static final String AJOUTER_ADHERENT = "ajouterAdherent";
	private static final String INSERER_ADHERENT = "insererAdherent";
	private static final String SUPPRIMER_ADHERENT = "supprimerAdherent";
	private static final String RECHERCHER_LISTE_OEUVRE = "chercherListeOeuvre";
	private static final String RECHERCHER_OEUVRE = "rechercherOeuvre";
	private static final String AJOUTER_OEUVRE = "ajouterOeuvre";
	private static final String INSERER_OEUVRE = "insererOeuvre";
	private static final String SUPPRIMER_OEUVRE = "supprimerOeuvre";
	private static final String MODIFIER_OEUVRE = "modifierOeuvre";
	private static final String ERROR_KEY = "messageErreur";
	private static final String ERROR_PAGE = "/erreur.jsp";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controleur() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processusTraiteRequete(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processusTraiteRequete(request, response);
	}

	protected void processusTraiteRequete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String actionName = request.getParameter(ACTION_TYPE);
		String destinationPage = ERROR_PAGE;
		String reponse;
		// execute l'action
		if (LISTER_ADHERENT.equals(actionName)) {
			String ressource = "/Adherents";
			try {

				Appel unAppel = new Appel();
				reponse = unAppel.appelJson(ressource);
				Gson gson = new Gson();
				List<Adherent> json = gson.fromJson(reponse, List.class);
				request.setAttribute("mesAdherents", json);
				destinationPage = "/listerAdherent.jsp";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				destinationPage = "/erreur.jsp";
				request.setAttribute("MesErreurs", e.getMessage());
			}

			
		} else if (AJOUTER_ADHERENT.equals(actionName)) {

			destinationPage = "/ajouterAdherent.jsp";
		} else if (INSERER_ADHERENT.equals(actionName)) {
			try {
				Adherent unAdherent = new Adherent();
				unAdherent.setNomAdherent(request.getParameter("txtnom"));
				unAdherent.setPrenomAdherent(request.getParameter("txtprenom"));
				unAdherent.setVilleAdherent(request.getParameter("txtville"));
				String ressource = "/Adherents/ajout/" + unAdherent;
				Appel unAppel = new Appel();
				reponse = unAppel.postJson(ressource, unAdherent);
				destinationPage = "/Controleur?action="+LISTER_ADHERENT;;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				request.setAttribute("MesErreurs", e.getMessage());
				destinationPage = "/erreur.jsp";
			}

		} else if (SUPPRIMER_ADHERENT.equals(actionName)) {
			try {
				int idAdh = (int) Double.parseDouble(request.getParameter("id"));
				String ressource = "/Adherents/delete/" + idAdh;
				Appel unAppel = new Appel();
				reponse = unAppel.deleteJson(ressource);
				destinationPage = "/Controleur?action="+LISTER_ADHERENT;

				if (reponse.startsWith("Echec")){
					request.setAttribute("MesErreurs", "Impossible de supprimer l'adhérent car celui-ci possède une oeuvre");
					destinationPage = "/erreur.jsp";
				}

			} catch (Exception e) {
				request.setAttribute("MesErreurs", e.getMessage());
				destinationPage = "/erreur.jsp";
			}
		} else if (RECHERCHER_LISTE_OEUVRE.equals(actionName)) {

			String ressource = "/Oeuvres";
			try {

				Appel unAppel = new Appel();
				reponse = unAppel.appelJson(ressource);
				Gson gson = new Gson();
				List<Oeuvrevente> json = gson.fromJson(reponse, List.class);
				request.setAttribute("mesOeuvres", json);
				destinationPage = "/rechercherOeuvre.jsp";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				destinationPage = "/erreur.jsp";
				request.setAttribute("MesErreurs", e.getMessage());
			}
		} else if (RECHERCHER_OEUVRE.equals(actionName)) {
			Oeuvrevente uneOeuvre = null;

			if (request.getParameter("id") != null) {
				try {
					// Oeuvres/{Id}")

					Gson gson = new Gson();
					int idoeuvre = gson.fromJson(request.getParameter("id"), Integer.class);
					String ressource = "/Oeuvres/" + idoeuvre;
					Appel unAppel = new Appel();
					reponse = unAppel.appelJson(ressource);
					Oeuvrevente json = gson.fromJson(reponse, Oeuvrevente.class);
					request.setAttribute("uneOeuvre", json);
					destinationPage = "/afficherOeuvre.jsp";

				} catch (Exception e) {

					destinationPage = "/erreur.jsp";
					request.setAttribute("MesErreurs", e.getMessage());
				}
			}
		} else if (MODIFIER_OEUVRE.equals(actionName)) {
			try
			{
				Oeuvre uneOeuvre = new Oeuvre();
				uneOeuvre.setIdentifiant(Integer.parseInt(request.getParameter("txtId")));
				uneOeuvre.setTitre(request.getParameter("txtTitre"));
				uneOeuvre.setEtat(request.getParameter("txtEtat"));
				uneOeuvre.setPrix(Float.parseFloat(request.getParameter("txtPrix")));
				String ressource = "/Oeuvres/edit/" + uneOeuvre.getIdentifiant();
				Appel unAppel = new Appel();
				reponse = unAppel.putJson(ressource, uneOeuvre);

				destinationPage = "/Controleur?action="+RECHERCHER_LISTE_OEUVRE;
			} catch (Exception e) {
				request.setAttribute("MesErreurs", e.getMessage());
				destinationPage = "/erreur.jsp";
			}
		} else if (AJOUTER_OEUVRE.equals(actionName)){
			// méthode qui permet d'ajouter une nouvelle oeuvre en cliquant sur le bouton ajouter dans le menu rechercher
			String jsonProprietaires = (new Appel()).appelJson("/proprietaire");
			List<Object> proprietaires = new Gson().fromJson(jsonProprietaires, List.class);
			request.setAttribute("proprietaires", proprietaires);
			destinationPage = "/ajouterOeuvre.jsp";
		} else if (INSERER_OEUVRE.equals(actionName)){
			// méthode qui permet d'ajouter une nouvelle oeuvre dans le menu affichage
			try {
				Oeuvre uneOeuvre = new Oeuvre();
				uneOeuvre.setIdentifiant(0);
				uneOeuvre.setTitre(request.getParameter("txtTitre"));
				uneOeuvre.setEtat(request.getParameter("txtEtat"));
				uneOeuvre.setPrix(Float.parseFloat(request.getParameter("txtPrix")));
				uneOeuvre.setidproprietaire((int) Float.parseFloat(request.getParameter("txtIdProprietaire")));
				String ressource = "/Oeuvres/ajout/";
				Appel unAppel = new Appel();
				reponse = unAppel.putJson(ressource, uneOeuvre);

				destinationPage = "/Controleur?action="+RECHERCHER_LISTE_OEUVRE;
			}catch (Exception e){
				request.setAttribute("MesErreurs", e.getMessage());
				destinationPage = "/erreur.jsp";
			}
		} else if (SUPPRIMER_OEUVRE.equals(actionName)) {
			// méthode qui permet de supprimer une oeuvre après avoir sélectionner une oeuvre et cliquer sur le bouton rechercher
			try {
				new Appel().deleteJson("/Oeuvres/delete/" + request.getParameter("idOeuvre"));
				destinationPage = "/Controleur?action="+RECHERCHER_LISTE_OEUVRE;
			} catch (Exception e) {
				request.setAttribute("MesErreurs", e.getMessage());
				destinationPage = "/erreur.jsp";
			}

		}
			else {
				String messageErreur = "[" + actionName + "] n'est pas une action valide.";
				request.setAttribute(ERROR_KEY, messageErreur);
			}
			// Redirection vers la page jsp appropriee
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destinationPage);
			dispatcher.forward(request, response);

		}

	}

