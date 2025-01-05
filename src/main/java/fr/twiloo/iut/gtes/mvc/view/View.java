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
           """);

    private final String content;

    View(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
