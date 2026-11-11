# SOLID Principles

## 1. Définition

**SOLID** est un ensemble de cinq principes de conception orientée objet qui permettent de développer des applications maintenables, évolutives, flexibles et faiblement couplées.

Ces principes ont été popularisés par Robert C. Martin (Uncle Bob).

| Lettre | Principe | Objectif |
|---|---|---|
| S | Single Responsibility Principle | Une classe doit avoir une seule responsabilité |
| O | Open/Closed Principle | Ouverte à l'extension, fermée à la modification |
| L | Liskov Substitution Principle | Une sous-classe doit pouvoir remplacer sa classe parent |
| I | Interface Segregation Principle | Préférer plusieurs interfaces spécifiques à une interface générale |
| D | Dependency Inversion Principle | Dépendre des abstractions plutôt que des implémentations |

---

## 2. Single Responsibility Principle (SRP)

**Une classe doit avoir une seule raison de changer.**

Une classe doit être responsable d'une seule fonctionnalité ou d'un seul domaine de responsabilité.

### Exemple : gestion des commandes

**Mauvaise pratique :**

Une seule classe gère la commande, la sauvegarde en base et l'envoi de notifications.

```java
@Service
public class OrderService {

    public void createOrder(Order order) {
        // Validation de la commande
        // Sauvegarde en base de données
        // Envoi d'un email
    }
}
```

Cette classe possède plusieurs responsabilités et plusieurs raisons de changer.

**Bonne pratique :**

Séparer les responsabilités.

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public void createOrder(Order order) {
        orderRepository.save(order);
        notificationService.sendOrderConfirmation(order);
    }
}
```

```java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
```

```java
@Service
public class NotificationService {

    public void sendOrderConfirmation(Order order) {
        // Envoi de la notification
    }
}
```

Chaque composant possède une responsabilité clairement définie.

---

## 3. Open/Closed Principle (OCP)

**Une classe doit être ouverte à l'extension, mais fermée à la modification.**

Il doit être possible d'ajouter de nouveaux comportements sans modifier constamment le code existant.

### Exemple : calcul des frais de livraison

**Mauvaise pratique :**

```java
public class ShippingService {

    public double calculate(String type, double amount) {
        if ("STANDARD".equals(type)) {
            return amount + 20;
        }

        if ("EXPRESS".equals(type)) {
            return amount + 50;
        }

        return amount;
    }
}
```

Chaque nouveau type de livraison nécessite une modification de cette classe.

**Bonne pratique :**

Définir une abstraction.

```java
public interface ShippingStrategy {

    double calculate(double amount);
}
```

```java
@Component("STANDARD")
public class StandardShipping implements ShippingStrategy {

    @Override
    public double calculate(double amount) {
        return amount + 20;
    }
}
```

```java
@Component("EXPRESS")
public class ExpressShipping implements ShippingStrategy {

    @Override
    public double calculate(double amount) {
        return amount + 50;
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class ShippingService {

    private final Map<String, ShippingStrategy> strategies;

    public double calculate(String type, double amount) {
        ShippingStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("Unknown shipping type: " + type);
        }

        return strategy.calculate(amount);
    }
}
```

L'ajout d'un nouveau type de livraison nécessite uniquement une nouvelle implémentation de `ShippingStrategy`.

---

## 4. Liskov Substitution Principle (LSP)

**Une classe fille doit pouvoir remplacer sa classe parent sans modifier le comportement attendu du programme.**

Une implémentation doit respecter le contrat défini par son abstraction.

### Exemple : gestion des comptes bancaires

**Mauvaise pratique :**

```java
public class BankAccount {

    public void withdraw(double amount) {
        // Retrait d'argent
    }
}
```

```java
public class FixedDepositAccount extends BankAccount {

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException(
            "Withdrawal is not allowed"
        );
    }
}
```

Le compte à dépôt bloqué hérite d'un comportement qu'il ne peut pas respecter.

**Bonne pratique :**

Séparer les capacités.

```java
public interface BankAccount {

    double getBalance();
}
```

```java
public interface WithdrawableAccount extends BankAccount {

    void withdraw(double amount);
}
```

```java
public class CurrentAccount implements WithdrawableAccount {

    private double balance;

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            throw new IllegalArgumentException("Invalid amount");
        }

        balance -= amount;
    }
}
```

```java
public class FixedDepositAccount implements BankAccount {

    private double balance;

    @Override
    public double getBalance() {
        return balance;
    }
}
```

Chaque classe respecte le contrat de l'interface qu'elle implémente.

---

## 5. Interface Segregation Principle (ISP)

**Une classe ne doit pas être obligée d'implémenter des méthodes dont elle n'a pas besoin.**

Il vaut mieux plusieurs interfaces spécialisées qu'une seule interface générale.

### Exemple : gestion des notifications

**Mauvaise pratique :**

```java
public interface NotificationService {

    void sendEmail(String message);

    void sendSms(String message);

    void sendPushNotification(String message);
}
```

Une implémentation qui envoie uniquement des SMS est obligée d'implémenter les autres méthodes.

**Bonne pratique :**

Séparer les interfaces selon leurs responsabilités.

```java
public interface EmailNotification {

    void sendEmail(String message);
}
```

```java
public interface SmsNotification {

    void sendSms(String message);
}
```

```java
public interface PushNotification {

    void sendPushNotification(String message);
}
```

Une implémentation peut choisir uniquement les capacités dont elle a besoin.

```java
@Service
public class SmsNotificationService implements SmsNotification {

    @Override
    public void sendSms(String message) {
        // Envoi SMS
    }
}
```

---

## 6. Dependency Inversion Principle (DIP)

**Les modules de haut niveau ne doivent pas dépendre des modules de bas niveau. Les deux doivent dépendre d'abstractions.**

Les détails techniques doivent dépendre des contrats, et non l'inverse.

### Exemple : traitement des paiements

**Mauvaise pratique :**

```java
@Service
public class PaymentService {

    private final StripePaymentGateway gateway =
        new StripePaymentGateway();

    public void pay(double amount) {
        gateway.processPayment(amount);
    }
}
```

Le service métier dépend directement d'une implémentation technique.

**Bonne pratique :**

Définir une abstraction.

```java
public interface PaymentGateway {

    void processPayment(double amount);
}
```

```java
@Component
public class StripePaymentGateway implements PaymentGateway {

    @Override
    public void processPayment(double amount) {
        // Intégration Stripe
    }
}
```

```java
@Component
public class BankPaymentGateway implements PaymentGateway {

    @Override
    public void processPayment(double amount) {
        // Intégration bancaire
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGateway paymentGateway;

    public void pay(double amount) {
        paymentGateway.processPayment(amount);
    }
}
```

Spring injecte l'implémentation de `PaymentGateway` selon sa configuration.

Si plusieurs implémentations sont disponibles, il faut préciser le choix avec `@Qualifier`, `@Primary` ou une stratégie de sélection.

---

## 7. Synthèse des principes SOLID

| Principe | Problème évité | Solution |
|---|---|---|
| SRP | Classe avec trop de responsabilités | Séparer les responsabilités |
| OCP | Modification répétée du code existant | Utiliser des abstractions et l'extension |
| LSP | Héritage qui casse le comportement attendu | Respecter les contrats |
| ISP | Interfaces trop volumineuses | Créer des interfaces spécialisées |
| DIP | Couplage fort aux implémentations | Dépendre des abstractions |

## 8. SOLID dans une application Spring Boot

Dans une application Spring Boot, SOLID peut être appliqué à plusieurs niveaux :

- **Controller** : gérer les requêtes HTTP et les réponses.
- **Service** : porter les cas d'utilisation et la logique applicative.
- **Repository** : abstraire l'accès aux données.
- **Interfaces** : définir les contrats entre les composants.
- **Dependency Injection** : injecter les dépendances sans instancier directement les implémentations.
- **Strategy Pattern** : ajouter des comportements sans multiplier les conditions.

### À retenir

SOLID ne signifie pas multiplier les interfaces et les classes.

L'objectif est de réduire le couplage, de clarifier les responsabilités et de faciliter les évolutions du logiciel.

Il faut appliquer ces principes lorsqu'ils apportent une réelle valeur, sans introduire de complexité inutile.