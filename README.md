# mydi‑framework

## 1. Introduction

Ce projet propose un **mini‑framework d’injection de dépendances** (DI) inspiré de Spring IOC, avec deux modes de configuration :

- **XML** (via JAXB/OXM)  
- **Annotations** (`@Component` / `@Autowired`)

Il supporte trois stratégies d’injection :

1. Injection par **constructeur**  
2. Injection par **setter**  
3. Injection par **champ** (field)  

L’objectif est de comprendre et mettre en œuvre les mécanismes de base d’un conteneur IoC : lecture des définitions de beans, instanciation, résolution et assemblage des dépendances.

---

## 2. Architecture générale

Le framework se décompose en 5 couches principales :

1. **Annotation**  
   Contient les annotations personnalisées (`@Component`, `@Autowired`).

2. **Config (JAXB/OXM)**  
   Classes JAXB pour mapper le fichier `beans.xml` en objets Java.

3. **Core**  
   Classe interne `BeanDefinition` qui décrit chaque bean (id, className, scope, dépendances).

4. **Reader**  
   - `XmlBeanDefinitionReader` : parse le XML et produit une liste de `BeanDefinition`.  
   - `AnnotationBeanDefinitionReader` : scanne un package, détecte `@Component` et `@Autowired`, produit des `BeanDefinition`.

5. **Factory**  
   `BeanFactory` : instancie les beans, gère les scopes (`singleton`/`prototype`), applique les injections constructeur, setter et champ.

---

## 3. Arborescence du projet

```
mydi-framework/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── ma/tajeddine/mydi/
│   │   │   │   ├── annotation/
│   │   │   │   │   ├── Component.java
│   │   │   │   │   └── Autowired.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── BeansConfig.java
│   │   │   │   │   ├── BeanConfig.java
│   │   │   │   │   ├── ArgConfig.java
│   │   │   │   │   └── PropertyConfig.java
│   │   │   │   ├── core/
│   │   │   │   │   └── BeanDefinition.java
│   │   │   │   ├── reader/
│   │   │   │   │   ├── XmlBeanDefinitionReader.java
│   │   │   │   │   └── AnnotationBeanDefinitionReader.java
│   │   │   │   └── factory/
│   │   │   │       └── BeanFactory.java
│   │   │   └── ma/tajeddine/app/
│   │   │       ├── Main.java
│   │   │       ├── MyRepository.java
│   │   │       ├── MyService.java
│   │   │       └── ConsoleLogger.java
│   │   └── resources/
│   │       ├── beans.xml
│           └── logback.xml
└── README.md
```

---

## 4. Description des modules

### 4.1 Annotation

- **`@Component`** : marque une classe comme bean.  
- **`@Autowired`** : indique un point d’injection (constructeur, setter ou champ).

### 4.2 Config (JAXB)

- **`BeansConfig`** : racine XML `<beans>`.  
- **`BeanConfig`** : représentation d’un `<bean>` (id, class, scope).  
- **`ArgConfig`**, **`PropertyConfig`** : éléments `<constructor-arg>` et `<property>`.

### 4.3 Core

- **`BeanDefinition`**  
  - `id` : nom du bean  
  - `className` : FQN de la classe  
  - `scope` : `singleton` (par défaut) ou `prototype`  
  - listes et maps des dépendances à injecter

### 4.4 Reader

- **`XmlBeanDefinitionReader`**  
  ```java
  InputStream xml = Files.newInputStream(Paths.get("beans.xml"));
  List<BeanDefinition> defs = new XmlBeanDefinitionReader().loadBeanDefinitions(xml);
  ```
- **`AnnotationBeanDefinitionReader`**
  ```java
  List<BeanDefinition> defs = 
    new AnnotationBeanDefinitionReader().loadBeanDefinitions("ma.tajeddine.app");
  ```

### 4.5 Factory

- **`BeanFactory`**
    - instanciation via constructeur par défaut ou paramétré
    - injection des setters (`setXxx(...)`)
    - injection des champs (réflexion, `field.setAccessible(true)`)
    - gestion du cache `singleton`

---

## 5. Installation et exécution

### Prérequis

- Java 17+ (JDK 21 recommandé)
- Maven 3.6+

### Compilation

```bash
git clone https://github.com/scorpionTaj/mydi-framework.git
cd mydi-framework
mvn clean compile
```

### Exécution

Le plugin `exec-maven-plugin` est configuré pour lancer `ma.tajeddine.app.Main` :

```bash
mvn exec:java
```

Vous devriez obtenir en sortie :

```
[LOG] Traitement de : Données
[LOG] Traitement de : Données
```

- La première ligne correspond au mode **XML**.
- La seconde au mode **Annotations**.

---

## 6. Configuration du logging

Par défaut, Logback affiche en NOP (pas de logs métiers) et un message d’avertissement. Pour ajouter un Logback :

```xml
<!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.5.18</version>
  <scope>compile</scope>
</dependency>
```

Vous pouvez ensuite configurer les niveaux dans `logback.xml`.

---

## 7. Extensions possibles

- **Portée prototype**
- **Détection et résolution des cycles** (early references)
- **BeanPostProcessor** (hooks avant/après init)
- **Support de valeurs primitives** (`<property value="..." />`)
- **Combinaison XML + annotations**

---

## 8. Auteurs & Licence

- **Auteur** : Tajeddine Bourhim
- **Licence** : MIT