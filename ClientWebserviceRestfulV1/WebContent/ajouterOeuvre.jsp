<%@ include file="include/header.jsp"%>
<body>
<div id="site">
    <%@ include file="include/menu.jsp"%>
    <div id="conteneur">
        <%@ include file="include/bandeaudroite.jsp"%>

        <div id="contenu">
            <P align="center">
                <FONT face="Arial" size="5" color="#004080"> <STRONG>Recherche &nbsp;
                    d'une oeuvre </STRONG></FONT>
            </P>

            <h1>Affichage  d'une oeuvre </h1>

            <form method="post" action="Controleur?action=insererOeuvre">
                <div>
                    <ul>
                        <li><b>ID :</b><input type="text" name="txtId" value="" readonly/> </li>
                        <li><b>Titre  :</b> <input type="text" name="txtTitre" value="" /></li>
                        <li><b>Etat  :</b> <input type="text" name="txtEtat" value="" /></li>
                        <li><b>Prix :</b> <input type="text" name="txtPrix" value ="" /></li>
                        <li><b>id Proriétaire :</b> <input type="text" name="txtIdProprietaire" value ="}" /></li>

                    </ul>
                    <input type="submit" value="Modifier" />
                </div>

            </form>

        </div>
        <%@ include file="include/footer.jsp"%>
    </div>
</div>

</body>
</html>
