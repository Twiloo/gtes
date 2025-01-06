package fr.twiloo.iut.gtes.mvc.view;

public enum View {
    DEFAULT_MENU("""
           Sélectionnez une option :
           
            1 : Voir les équipes ;
            2 : Créer une équipe ;
            3 : Modifier une équipe ;
            4 : Supprimer une équipe ;
            5 : Organiser un match ;
            6 : Jouer un match ;
            7 : Annuler un match ;
            8 : Arrêter l'application.
           """),
    SAISIE_INVALIDE("La saisie n'est pas valide"),
    FERMETURE("Fermeture de l'application..."),
    ASK_TEAM_A_NAME("Entrez le nom de la première équipe : "),
    ASK_TEAM_B_NAME("Entrez le nom de la deuxième équipe : "),
    MATCH_NON_CREE("Le match n'a pas pû être créé"),
    MATCH_CREE("Le match a été créé : "),
    ASK_TEAM_A_SCORE("Entrez le score de la première équipe : "),
    ASK_TEAM_B_SCORE("Entrez le score de la deuxième équipe : "),
    MATCH_NON_FINI("Les résultats du match n'ont pas pû être enregistrés"),
    MATCH_FINI("Le match est terminé"),
    MATCH_NON_ANNULE("Le match n'a pas pû être annulé"),
    MATCH_ANNULE("Le match est annulé"),
    TEAM_LIST("Liste des équipes : "),
    ASK_TEAM_NAME("Entrez le nom de l'équipe : "),
    ASK_PLAYER_1("Entrez le nom du chef d'équipe : "),
    ASK_PLAYER_2("Entrez le nom du deuxième joueur : "),
    ASK_PLAYER_3("Entrez le nom du dernier joueur : "),
    TEAM_NON_CREE("L'équipe n'a pas pû être créée"),
    TEAM_CREE("L'équipe a été créée : "),
    ASK_TEAM_NAME_UPDATE("Entrez le nom de l'équipe à modifier : "),
    UPDATE_TEAM_NAME("""
            Entrez les nouvelles informations de l'équipe (vide pour garder les anciennes valeurs)
            Nom de l'équipe :
            """),
    TEAM_NON_UPDATE("L'équipe n'a pas pû être modifiée"),
    ASK_TEAM_NAME_DELETE("Entrez le nom de l'équipe à supprimer : ");

    private final String content;

    View(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
