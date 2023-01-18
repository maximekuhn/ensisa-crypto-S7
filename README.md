# ensisa-crypto-S7
Agenda sécurisé. <br>
ENSISA 2A IR projet cryptographie (groupe 4)

---

Membres du groupe :
- BROSSEAU Sylvain <sylvain.brosseau@uha.fr>
- KEMPF Simon <simon.kempf@uha.fr>
- BISEL Jean <jean.bisel@uha.fr>
- GOKER Batuhan <batuhan.goker@uha.fr>
- GRAINCA Albi <albi.grainca@uha.fr>
- KUHN Maxime <maxime.kuhn1@uha.fr>

---

# Fonctionnalités
L'agenda permet de créer des calendriers, en les chiffrant ou non avec différents algorithmes (AES, RC5) et un mot de passe. Chaque calendrier peut contenir
des évènements. L'utilisateur peut en crééer / supprimer autant qu'il le souhaite. Les calendriers peuvent être échangés de manière sécurisée via le réseaux, en utilisant
notamment l'algorithme RSA.
<br>
L'utilisateur peut également rechercher des évènements par date.


# Compiler le projet
1. Se placer à la racine du projet
2. `mvn clean install`

# Executer l'application
1. Se placer à la racine du projet
2. `mvn exec:java -Dexec.mainClass="fr.uha.ensisa.crypto.App"`