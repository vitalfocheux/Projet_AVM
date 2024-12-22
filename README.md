# Projet Développement Agile de Virtual Machines

Il s'agit d'un projet réalisé au cours du semestre 7 du Master 1 Ingénierie Systèmes et Logiciels de Besançon lors de l'année 2024

## Objectifs

1. Prototyper un compilateur et un interpréteur afin de concrétiser les concepts des techniques de compilation : analyse lexicale, analyse syntaxique, syntaxe abstraite, contrôle de types, sémantique interprétative (opérationnelle), compilation, traitement des erreurs

2. Réaliser un travail d’équipe pour comprendre et résoudre les problèmes d’organisation selon les méthodes de Génie Logiciel : spécification des interfaces entre les différents modules, composition des modules, définition des procédures de test, définition des procédures de recette, réalisation de revues de codes, rédaction de la documentation,...

3. Développer une interface utilisateur conviviale et cohérente pour de  l’application

4. Vérifier sur des exemples que le compilateur MiniJaja -> JajaCode est correct vis à vis des sémantiques interprétatives des deux langages. Pour cette partie, vous devrez appliquer une stratégie de test qui permette de valider le mieux possible le produit.

## Résultats

Ce projet a été testé grâce à des tests unitaires et d'acceptations et une couverture de code de 58.8% (les fichiers générés par JavaCC biaise la couverture de code). Plus de 1200 ont été effectués pour atteindre cela (grand nombre dû aux tests paramétriques de JUnit 5 et que les tests ne couvre que les classes situés dans leur modules, regarder ce [site](https://blog.ippon.fr/2019/01/08/couverture-de-code-multi-module-avec-jacoco-et-sonar/) pour régler ce problème)

## Notes

Arrive bientôt