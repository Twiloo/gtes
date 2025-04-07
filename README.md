# GTES : Gestion de Tournoi E-Sport

## Cadre du projet

Le projet GTES a été réalisé en groupes de deux (Andréa et Mathis) dans le cadre de la licence professionnelle DEVOPS à l'IUT Lyon 1. J'ai (Mathis) adapté ce projet afin de le faire fonctionner sous Docker.

## Lancement

Une fois placé dans le répertoire racine du projet, exécuter ces deux commandes :

- Pour construire l'ensemble des images puis containers, créer les networks associés et démarrer l'ensemble des services :
  ```shell
  docker compose up -d
  ```
- Pour interagir avec le client mvc :
  ```shell
  docker attach gtes-client-1
  ```
Une fois dans le client MVC en terminal interactif, appuyez sur Entrée pour afficher le menu des options et utiliser l'application. Vous pouvez également suivre les communications de l'application sur l'eventbus et les différents microservices en vous attanchant à ceux-ci dans d'autres fenêtres de terminal.

## Utilisation

L'application GTES utilise une architecture microservices, où les services ont pour unique intermédiaire un bus d'évènement.
Le bus fonctionne en suivant le modèle client/serveur et doit être démarré en premier.
Si le bus s'arrête, les services qui y sont connectés (clients) s'arrêteront également.

Après avoir démarré le bus, il faut démarrer les différents services (Teams, Matchs, Notifications), pour qu'ils s'abonnent au bus et reçoivent les évènements.

Enfin, il faut démarrer le client MVC pour pouvoir interagir avec le bus, qui notifiera les services via les évènements.
Les services enverront parfois un nouvel évènement en réponse à un évènement reçu qui sera redistribué par le bus au client et/ou aux autres services.

## Structure du projet

Le projet contient plusieurs packages ayant les rôles suivants :
- common : Fichiers devant être accédés par plusieurs des applications ;
  - client : Client permettant de se connecter au bus d'évènement ;
  - model : Classes/Records de structures (modélisation des données) ;
    - dto : Data Transfer Object - Charges des évènements ;
  - utils : Classes utilitaires (lecture des entrées utilisateurs) ;
- eventbus : Classes permettant le fonctionnement du bus d'évènement ;
- microservices : Classes permettant le fonctionnement des microservices ;
  - match : Classe concrète du service des matchs ;
  - notification : Classe concrète du service des notifications ;
  - team : Classe concrète du service des équipes et CRON de mise à jour du classement par rapport au score ELO ;
- mvc : Fichiers permettant le fonctionnement de l'application MVC ;
  - controller : Classes "back-end" de l'application MVC ;
  - view : Enum listant les textes statiques de l'application MVC.

## Pistes d'améliorations

Dans une réelle architecture microservices, les services ne doivent pas être responsables du stockage des données (équipes, matchs).
Il faudrait relier une base de données par (type de) service pour s'assurer qu'en cas de redémarrage d'un service, les données sont toujours présentes.

Si l'on souhaite améliorer la scalabilité et avoir des services Stateless (plusieurs services équipes accueillent les demandes des utilisateurs en parallèle, mais un seul doit recevoir l'évènement), il faudrait également que le bus fasse office de reverse proxy et redirige l'évènement à un seul des services du même type (aléatoirement) ; voir recommence avec les autres en cas d'échec.

Enfin, nous pourrions créer un NotificationApp qui ferait office d'application recevant les notifications du service des notifications (qui ferait office de serveur partageant les notifications aux clients).
