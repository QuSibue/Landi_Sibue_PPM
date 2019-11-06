# TP Programmation de Plateforme Mobile
## Estelle Landi & Quentin Sibué


__**Résumé du travail de Lundi matin**__ :

Tous d'abord, nous nous sommes répartis les tâches :

Quentin devait s'occuper de la partie de récupération des images de la galerie
Estelle devait s'occuper d'afficher une image à l'aide de canvas

Au bout de ces 3 heures de travail, plusieurs choses ont pu être mises en place:

- L'utilisateur peut (et doit) donner son autorisation pour que l'application puisse accéder au contenu interne

- L'application récupère le lien de toutes les images de la galerie et uniquement de la gallerie

- Afficher n images à l'écran (init : 7 colonnes)

Si on a l'action de zoom, on réduit le nombre de colonnes et donc le nombre d'images par colonne

Ce qu'il restait à faire :

- Fusionner la récupération des images avec l'affichage

- Pouvoir dézoomer les images

- Gérer la taille des images à afficher

- Gérer le scroll



__**Résumé de la version finale**__ :

En plus des fonctionnalités mises en place lundi matin, nous en avons ajoutées de nouvelles :

- La récupération d'image et l'affichage d'image ont été fusionné afin d'afficher le contenu du téléphone de l'utilisateur

- L'utilisateur peut scroller vers le bas ou le haut pour voir d'autre photos

- Les photos récupérées sont "compressée" afin d'économiser de la mémoire

- L'utilisateur peut zoomer / dézoomer afin de modifier le nombre de colonnes affichées

- L'affichage des images utilise un BitmapDrawable et setbounds (contre un bitmap et createScaledBitmap dans la version de lundi)


__**Problèmes connus**__ :

- Le problème principale que nous avons, est que à chaque onDraw, la totalité des images sont "recrées" ce qui est très problématique et long à executer.

  Ce chargement des images étant une opération lourde, et, n'ayant pas réussi à trouver une alternative "efficace" à un thread à part pour faire ce boulot, nous avons décidé de   mettre en place un système de "cache" afin de diminuer l'effet de lenteur d'éxecution.


- Utilisant un système de cache, nous voulions mettre en place un système qui change le taux de compression suivant le nombre de colonnes affichées, mais cela rentrait en contradiction avec le cache, nous sommes en recherche d'une solution.

Nous sommes conscient de ces problèmes et que ce n'est pas la solution optimale, mais nous avons préféré faire ça pour rendre quelque chose un minimum utilisable.


