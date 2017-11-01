<%@ include file="include/header.jsp"%>
<body>
	<div id="site">
		<%@ include file="include/menu.jsp"%>
		<div id="conteneur">
			<%@ include file="include/bandeaudroite.jsp"%>

			<div id="contenu">
			<P align="center">
			<FONT face="Arial" size="5" color="#004080"> <STRONG>Recherche d'une oeuvre</STRONG></FONT>
			</P>
			
	<h1>Affichage d'une oeuvre </h1>
	
	<form method="post" action="Controleur?action=modifierOeuvre">
	<div>				
		<ul>
	  	<li><b>ID : </b><input type="text" required name="txtId" value="${uneOeuvre.idOeuvrevente}" readonly/> </li>
		<li><b>Titre : </b> <input type="text" maxlength="200" required name="txtTitre" value="${uneOeuvre.titreOeuvrevente}" /></li>
		<li><b>Etat : </b> <input type="text" maxlength="1" required name="txtEtat" value="${uneOeuvre.etatOeuvrevente}" /></li>
		<li><b>Prix : </b> <input type="number" min="0" step="0.01" required name="txtPrix" value ="${uneOeuvre.prixOeuvrevente}" /></li>
		<li><b>Proriétaire : </b> ${uneOeuvre.proprietaire.nomProprietaire} ${uneOeuvre.proprietaire.prenomProprietaire}</li>
	
	</ul>
	<input type="submit" value="Modifier" />
	</div>
	
	</form>
				<form method="post" action="Controleur?action=supprimerOeuvre">
					<input type="text" name="idOeuvre" value="${uneOeuvre.idOeuvrevente}" readonly="readonly" hidden="hidden" />
					<input type="submit" value="Supprimer" />
				</form>
	
		</div>
			<%@ include file="include/footer.jsp"%>
		</div>
	</div>
	
</body>
</html>
