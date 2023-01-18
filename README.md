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
Créer / supprimer un calendrier
<br>
Créer des évenements
<br>
Rechercher un évènement par date
<br>
Chiffrer un calendrier avec AES ou RC5
<br>
Echanger des calendriers de manière sécurisée via le réseau avec RSA
<br>
Et plus encore...


# Compiler le projet
JDK17 requis.
1. Se placer à la racine du projet
2. `mvn clean install`

# Executer l'application
1. Se placer à la racine du projet
2. `mvn exec:java -Dexec.mainClass="fr.uha.ensisa.crypto.App"`