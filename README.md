# E-Banking Backend

Backend d’une application bancaire développé avec **Spring Boot** pour gérer:
- les clients,
- les comptes bancaires (courant / épargne),
- les opérations (débit, crédit, transfert),
- l’historique des opérations (avec pagination),
- la recherche de clients.

## 1) Objectif du projet

Ce projet implémente une API REST de e-banking orientée apprentissage et pratique:
- modélisation JPA d’un domaine bancaire,
- séparation `Entity` / `DTO`,
- logique métier dans la couche service,
- exposition via contrôleurs REST,
- documentation et test des endpoints avec Swagger.

## 2) Stack technique

- **Java 21**
- **Spring Boot 3.5.14**
- **Spring Web**
- **Spring Data JPA**
- **MySQL** (runtime)
- **Lombok**
- **Springdoc OpenAPI / Swagger UI** (`2.1.0`)
- **Maven**

## 3) Architecture du projet

Le code est organisé par couches:

- `entities` : modèle métier persistant (`Customer`, `BankAccount`, `CurrentAccount`, `SavingAccount`, `AccountOperation`)
- `repositories` : accès base de données via `JpaRepository`
- `services` : logique métier (`BankAccountService`, `BankAccountServiceImpl`)
- `dtos` : objets d’échange API
- `mappers` : conversion Entity <-> DTO (`BankAccountMapperImpl`)
- `web` : endpoints REST (`CustomerRestController`, `BankAccountRestApi`)
- `exceptions` : exceptions métier personnalisées

## 4) Modèle métier

### Client
- Un client possède plusieurs comptes.

### Compte bancaire
- `BankAccount` est abstrait.
- Deux types concrets:
  - `CurrentAccount` avec `overDraft`
  - `SavingAccount` avec `interestRate`

### Opérations
- `AccountOperation` liée à un compte.
- Types d’opérations:
  - `DEBIT`
  - `CREDIT`

### Héritage JPA
Le projet a exploré plusieurs stratégies d’héritage pendant son évolution:
- `TABLE_PER_CLASS`,
- `JOINED`,
- puis implémentation actuelle en `SINGLE_TABLE` avec discriminant (`TYPE`).

## 5) Fonctionnalités implémentées

### Gestion des clients
- Ajouter un client
- Lister les clients
- Consulter un client par id
- Modifier un client
- Supprimer un client
- Rechercher des clients par mot-clé 

### Gestion des comptes
- Créer un compte courant
- Créer un compte épargne
- Lister tous les comptes
- Consulter un compte par id

### Opérations bancaires
- Débiter un compte
- Créditer un compte
- Transférer entre comptes
- Consulter l’historique des opérations d’un compte
- Consulter l’historique paginé (`page`, `size`)

### Qualité API
- Passage progressif vers les DTO pour éviter d’exposer directement les entités
- Mappers dédiés pour centraliser les conversions
- Gestion d’exceptions métier:
  - `CustomerNotFoundException`
  - `BankAccountNotFoundException`
  - `BalanceNotSufficientException`

## 6) Endpoints principaux

### Clients
- `GET /customers`
- `GET /customers/{id}`
- `GET /customers/search?keyword=...`
- `POST /customers`
- `PUT /customers/{customerId}`
- `DELETE /customers/{customerId}`

### Comptes / Opérations
- `GET /accounts`
- `GET /accounts/{accountId}`
- `GET /accounts/{accountId}/operations`
- `GET /accounts/{accountId}/pageOperations?page=0&size=5`

## 7) Configuration et exécution

### Prérequis
- Java 21
- Maven (ou wrapper `mvnw`)
- MySQL local

### Configuration actuelle (`application.properties`)
- Port: `8085`
- Base: `jdbc:mysql://localhost:3306/EBANK?createDatabaseIfNotExist=true`
- `spring.jpa.hibernate.ddl-auto=create` (recrée les tables au démarrage)
- SQL affiché en console (`show-sql=true`)

### Lancer le projet (Windows)
```powershell
cd d:\JAVA\ebanking-backend
.\mvnw spring-boot:run
```

### Swagger UI
- URL: `http://localhost:8085/swagger-ui/index.html`

## 8) Données de démarrage

Au lancement, un `CommandLineRunner`:
- crée des clients de test,
- crée des comptes (courant + épargne),
- injecte des opérations aléatoires (crédit / débit),
afin de faciliter les tests immédiats de l’API.

## 9) Historique du projet (depuis le début)

### 06 Mai 2026
- Création du projet et des entités de base.
- Création des interfaces repository et tests H2 initiaux.

### 07 Mai 2026
- Remplissage initial de la base avec des clients.
- Expérimentation puis ajustement des stratégies d’héritage JPA (`table_per_class` puis `JOINED`).
- Liaison DB et consultation des infos essentielles d’un compte.
- Création du service de gestion des comptes et implémentation métier.
- Ajout de la gestion des exceptions personnalisées.
- Création du contrôleur client avec gestion d’affichage JSON (`write_only`).

### 09 Mai 2026
- Passage important vers l’architecture DTO (service + contrôleurs).
- Implémentation de `save` via DTO et endpoints associés.
- Ajout des méthodes de modification/suppression de clients.
- Intégration de Swagger et tests API.
- Implémentation des mappers pour `BankAccount`, `CurrentAccount`, `SavingAccount`.
- Réécriture des méthodes de débit/crédit et adaptation aux DTO.
- Mise en place des contrôleurs des comptes.
- Création de `AccountOperationDTO`.
- Implémentation de la liste des opérations par compte.
- Implémentation de l’historique paginé via `AccountHistoryDTO`.

### 10 Mai 2026
- Ajout de la fonctionnalité de recherche client par lettre/mot-clé (`/customers/search`).
