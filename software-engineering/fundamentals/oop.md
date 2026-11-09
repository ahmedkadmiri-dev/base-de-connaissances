# Programmation Orientée Objet (POO)

La **Programmation Orientée Objet (POO)** est un paradigme de programmation qui organise le code autour d'objets contenant des **données (attributs)** et des **comportements (méthodes)**.

## 1. Classe et objet

- **Classe** : modèle qui définit les attributs et les méthodes.
- **Objet** : instance d'une classe.

```java
public class Voiture {
    String marque;

    void demarrer() {
        System.out.println("La voiture démarre");
    }
}

// Création d'un objet
Voiture voiture = new Voiture();
voiture.marque = "Toyota";
voiture.demarrer();
```

## 2. Encapsulation

Consiste à protéger les données d'un objet en limitant leur accès direct et en contrôlant leur modification.

```java
public class CompteBancaire {
    private double solde;

    public void deposer(double montant) {
        if (montant > 0) {
            solde += montant;
        }
    }

    public double getSolde() {
        return solde;
    }
}
```

Ici, `solde` est privé et ne peut être modifié que via une méthode contrôlée.

## 3. Héritage

Permet à une classe de récupérer les attributs et méthodes d'une autre classe avec `extends`.

```java
public class Animal {
    public void manger() {
        System.out.println("L'animal mange");
    }
}

public class Chien extends Animal {
    public void aboyer() {
        System.out.println("Wouf !");
    }
}

Chien chien = new Chien();
chien.manger(); // Héritée de Animal
chien.aboyer();
```

## 4. Polymorphisme

Permet d'utiliser un même type parent ou une même interface pour manipuler différents objets, chacun avec son propre comportement.

```java
public interface Paiement {
    void payer();
}

public class PaiementCarte implements Paiement {
    public void payer() {
        System.out.println("Paiement par carte");
    }
}

public class PaiementEspece implements Paiement {
    public void payer() {
        System.out.println("Paiement en espèces");
    }
}

Paiement paiement = new PaiementCarte();
paiement.payer(); // Paiement par carte

paiement = new PaiementEspece();
paiement.payer(); // Paiement en espèces
```

## 5. Redéfinition (Overriding)

Consiste à redéfinir dans une classe enfant une méthode héritée, avec la même signature, afin de modifier son comportement.

```java
public class Animal {
    public void faireDuBruit() {
        System.out.println("Bruit d'animal");
    }
}

public class Chien extends Animal {
    @Override
    public void faireDuBruit() {
        System.out.println("Wouf !");
    }
}
```

```java
Animal animal = new Chien();
animal.faireDuBruit(); // Wouf !
```

L'implémentation appelée dépend du type réel de l'objet à l'exécution : c'est le **polymorphisme dynamique**.

## 6. Surcharge (Overloading)

Consiste à définir plusieurs méthodes portant le même nom, mais avec des paramètres différents (nombre, type ou ordre).

```java
public class Calculatrice {
    public int additionner(int a, int b) {
        return a + b;
    }

    public double additionner(double a, double b) {
        return a + b;
    }

    public int additionner(int a, int b, int c) {
        return a + b + c;
    }
}
```

La méthode choisie est déterminée à la compilation : c'est le **polymorphisme statique**.

## 7. Abstraction

Consiste à exposer les fonctionnalités essentielles d'un objet sans imposer aux utilisateurs de connaître les détails de son implémentation.

Avec une classe abstraite :

```java
public abstract class Vehicule {
    public abstract void demarrer();

    public void arreter() {
        System.out.println("Véhicule arrêté");
    }
}

public class Voiture extends Vehicule {
    @Override
    public void demarrer() {
        System.out.println("Démarrage de la voiture");
    }
}
```

Avec une interface :

```java
public interface Notification {
    void envoyer(String message);
}
```

L'interface définit un contrat que les classes implémentent.

## 8. Composition

Consiste à construire une classe à partir d'autres objets. Elle permet de réutiliser des comportements sans nécessairement utiliser l'héritage.

```java
public class Moteur {
    public void demarrer() {
        System.out.println("Moteur démarré");
    }
}

public class Voiture {
    private final Moteur moteur = new Moteur();

    public void demarrer() {
        moteur.demarrer();
    }
}
```

Une voiture possède un moteur : relation « has-a ».

---

## Récapitulatif

| Concept | Description |
|---|---|
| Classe / Objet | Modèle / instance |
| Encapsulation | Protéger les données |
| Héritage | Réutiliser et spécialiser une classe |
| Polymorphisme | Un même contrat, plusieurs comportements |
| Overriding | Redéfinir une méthode héritée |
| Overloading | Même nom, paramètres différents |
| Abstraction | Masquer les détails d'implémentation |
| Composition | Construire un objet avec d'autres objets |

**À retenir :** les quatre piliers classiques de la POO sont l'encapsulation, l'héritage, le polymorphisme et l'abstraction. L'overriding et l'overloading sont des mécanismes liés au polymorphisme en Java.