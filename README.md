
# Einführung

Dieses Repository enthält die Implementation des Scoring-Systems eines Tennis-Spiels wie beschrieben in [Wikipedia](https://en.wikipedia.org/wiki/Tennis_scoring_system#Set_score). Es beschränkt sich auf die Zählung eines Sets. Die Handhabung von Spielen (Game) und Begegnungen (Match) ist nicht berücksichtigt.

Dieser Code ist privat und hat keinen tieferen Zweck. Fokus dieser Arbeit ist nicht die funktionale Vollständigkeit, sondern die Möglichkeit zur Beschäftigung mit folgenden Aspekten:
* Es stellt ein Übungsprojekt dar, mit Hilfe dessen folgende Technologien vertieft werden:
  * Spring Dependency Injection, Spring Data und Spring-Module im Allgemeinen
  * Unit-Testing mit relationaler inMemory Datenhaltung durch eine [H2 Datenbank](http://h2database.com/html/main.html) und mocking durch [Mockito](https://site.mockito.org/)
  * GitHub als Repository und Werkzeug für Workflows (Gitflow / Pull-Requests) kennenlernen.
  * Annotationen von [Lombok](http://projectlombok.org/) zur Reduktion von Boiler-Plate Code in POJOs
* Übung und Blueprint einer sauberen, dreischichtigen, serverseitigen Architektur. Speziell interessant ist die Interaktion der Schichten client-service und service. (Sobald implementiert.) Darf client-service Domänen-Objekte sehen? Wer übersetzt diese zu DTOs und vice versa? Wie geschieht die Übersetzung (~Orika) und wie die Serialisierung (~Jackson). Dürfen immutable Objekte wie Enums über alle Schichten transportiert werden?
* Den Beweis erbringen, dass die gewöhnliche Zählweise im Tennis in dessen langer Geschichte unnötig verkompliziert wurde. Einerseits werden Punkte gezählt und diese Punkte rechnerisch zu Zwischenständen verarbeitet, andererseits werden die Zwischenstände durch Begriffe (Score-Calls) repräsentiert, die in sich bereits ein stimmiges Zustandsmodell ergeben. Diese Implementation verzichtet auf die Datenhaltung von Punkten und arbeitet auschliesslich mit Scores/Score-Calls.
* Wieder mal etwas einfach aus reiner Freude coden.

# System-Beschreibung
## Design
Das folgende Diagramm skizziert die Komponenten, die Schichtung, und die Packages wie auch deren Abhängigkeiten. Projektexterne Abhängigkeiten werden ausgelassen.
![Design-Skizze](/tennis/docs/design.jpg)

## Statisch
Hier wird das Domänenmodell beschrieben: ![Klassendiagramm](/tennis/docs/classes.jpg)

## Dynamisch
Zentrales Konstrukt und einziger Zugriffspunkt zum System ist das Interface 
```java
public interface Match {

	Integer startGame( Player player1,  Player player2);
	void winPoint( Integer gameNo,  Player player);
	void abandonGame( Integer gameNo);
	String getScoreCall( Integer gameNo);
	Status getStatus( Integer gameNo);

}
```
Damit ist die Möglichkeit der Interaktion und die Use-Cases des Systems beschrieben.

## Deployment
Auf den Erstellungsprozess, die Lauffähigkeit ausserhalb der IDE und die Verteilbarkeit wurde kein Wert gelegt. Die Software ist mit den notwendigen Konfigurationsdateien versehen und läuft in Eclipse.

## Laufzeit
Zur Zeit kann mit der Software nur über Unit-Test aus einer Entwicklungsumgebung interagiert werden. 
 * Die Tests im Package tennis.persistence haben den Zweck, die grundsätzliche Korrektheit der DB-Anbindung zu testen. 
 * Die Tests in tennis.service.impl testen die fachliche Korrektheit (soweit ich Tennis verstanden habe). Dies geschieht isoliert indem andere Schichten gemocket werden.
 * Die Tests in service.integration testen die korrekte Zusammenarbeit der Schichten.

# Stand
Die fachlichen Features der serverseitige Entwicklung sind vollständig. Die Tests nicht. Ein Build und Deployment fehlt. Es ist die Absicht, die Software bald um REST-Services zu erweitern und einen einfachen Client zur Interaktion zur Verfügung zu stellen.

# ToDos
## Bugs
* Unit-Tests laden den IoC-Kontext nicht. Dementsprechend funktioniert die Dependency Injection nicht überall. Wie wenn SpringJUnit4ClassRunner die Kontext-Konfiguration nicht laden würde ... Als Workaround enthalten die Unit-Tests main-Methoden. Über diese Methoden gestartet, funktionert die Injection und die Tests. 
* Die Tests auf Ebene Service mit Mocking der Persistenz sind noch nicht abgeschlossen.
* Unzulänglichkeiten der container managed persistency. CRUD-Methoden werden nicht immer korrekt kaskadierend auf alle Teile eines komplexen Objektes propagiert.
* Transaktionsmanagement und Isolation (Kollisionen/Locking) noch nicht untersucht
* Die Web-Konsole der H2-Datenbank ist wegen unkorrekter Security-Konfiguration nicht erreichbar. Daher kann nicht manuell auf die DB Zugegriffen werden.
* Sobald der Zugriff auf die DB funktioniert, macht es Sinn, die automatische Generierung des DB-Schemas auszuschalten und ein DB-Create-Script zu entwickeln.
* Side-Quest: Logging in den Spring-Komponenten bei Ausführung von Unit-Tests anzeigen.
# Geplante Erweiterungen
* Build und Deployment ausserhalb der Entwicklungsumgebung zur Verfügung stellen.
* Web-Services zur Anbindung von Clients (zunächst mittels Postman etc.)
* Einfacher Angular Web-Client
* Far future: Containerisierung mit Docker
