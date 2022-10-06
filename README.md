# StudentGradeManagerAPI
API to manage students and their grades

# Vorwort:
Die Student-Grades-Manager API (kurz SGMA) soll Schülerdaten und deren Noten verwalten. Kommuniziert wird dafür über HTTP im JSON-Format. Bitte halten Sie die in Beispielen gegebenen Formatierungen ein, um keine Fehler zu erhalten.
Der Standard-Port für die API ist 8974.
Hinweis: Nicht einhalten der Reinfolge beim angeben von Attributen kann Fehler verursachen.
### Hinweis: Passwort für die API -> GradeAPI2022

# Übersicht
- Schülerdaten
	- Schülerdaten erstellen
	- Schülerdaten überarbeiten / verändern
	- Schülerdaten löschen
	- Schülerdaten abfragen (einzelner Schüler)
	- Schülerdaten abfragen (Klassenliste)
- Notendaten
	- Notendaten erstellen
	- Notendaten überarbeiten / verändern
	- Notendaten löschen
	- Notendaten abfragen (anhand Notenid)
	- Notendaten abfragen (Notenliste eines Schülers)
- Fehlercodes und Erklärung
- Features in Zukunft

# Schülerdaten:
Um mit Schülerdaten arbeiten zu können muss folgender Endpoint angesprochen werden:

` /api/student `

Um Daten abzufragen, benutze die REQUEST-Methode GET, um Daten zu senden benutze die REQUEST-Methode POST. Welche Daten du erhalten, erstellen, bearbeiten oder löschen willst, gibst du im REQUEST-BODY im JSON-Format an (, welches dir in den jeweiligen Beispielen erklärt wird).

## Schülerdaten erstellen (POST-REQUEST):
Um einen neuen Schüler im System einzutragen, musst du folgendes im REQUEST-BODY angeben:
-	action 	(Ausführende Aktion, hier "create")
-	firstname	(Vornamen des Schülers)
-	lastname	(Nachname des Schülers)
-	classtag	(Klassenbezeichnung des Schülers) 

```json
{
  "action":"create",
  "data": {
  	"firstname":"Max",
	"lastname":"Mustermann",
	"classtag":"5A"
  }
} 
```

Bei erfolgreichem Erstellen eines Schülers erhält man folgenden RESPONSE:
```json
{ 
  "success":"true", 
  "error": { 
     "name":"Successful", 
    "message":"There is no error", 
    "errorcode":"0" 
}
```
(Abbildung 3)

## Überarbeiten/Verändern von Schülerdaten (POST-REQUEST):
Um Schülerdaten verändern zu können, müssen folgende Attribute im REQUEST-BODY angegeben werden:
-	action	(Auszuführende Aktion, hier "update")
-	student-id	(ID des Schülers)
-	data-name	(Attribut, welches geändert werden soll)
-	data-value	(Neuer Wert für das Attribut)

Folgende Attribute können verändert werden: (Übernehme diese Namen in ‚data-name‘ um sie zu verändern)
-	firstname	(Vorname des Schülers)
-	lastname	(Nachname des Schülers)
-	classtag	(Klassenbezeichnung des Schülers)

```json
{
  "action":"update",
  "student-id":"7623",
  "data": {
  	"firstname":"Max",
	"classtag":"6A"
  }
}
```

Bei erfolgreichem Überarbeiten der Daten in der Datenbank erhalten Sie folgenden RESPONSE-CODE: (siehe Abbildung 3)


## Löschen von Schülerdaten (POST-REQUEST):
Um Schülerdaten löschen zu können, werden folgende 2 Attribute benötigt:
-	action	(Auszuführende Aktion, hier "delete")
-	student-id	(ID des zu löschenden Schülers)

```json
{ 
  "action":"delete",
  "student-id":"93874"
}
```

Bei erfolgreichem Ausführen der Aktion erhalten Sie folgenden RESPONSE-CODE:
(siehe Abbildung 3)


## Einzelne Schülerdaten abfragen (GET-REQUEST):
Um einzelne Schülerdaten abzufragen, musst du dem Endpoint "student" folgende Attribute übergeben (ACHTUNG: Bitte halte die unten angegebene Reinfolge ein):
-	studentid	(Die ID des zu suchenden Schülers)
ODER
-	firstname	(Vorname des zu suchenden Schülers)
-	lastname	(Nachname des zu suchenden Schülers)

` /api/student?studentid=87324 `

bzw.

` /api/student?firstname=Max&lastname=Mustermann `

Bei einer erfolgreichen REQUEST erhalten Sie folgenden RESPONSE im JSON-Format:

```json
{ 
   "success":"true", 
   "student": {
	"studentid":13,
	"firstname":"Maria",
	"lastname":"Musterfrau",
	"classtag":"5A",
	"average": "2.5"
   }
}
```

## Liste aller Schüler einer Klasse abfragen (GET-REQUEST):
Für das Abfragen einer Klassenliste gibt es zwei Möglichkeiten:

1.	Unsortiert abfragen:
Verwenden Sie hierfür folgende Attribute in genau der Reinfolge:
o	list		(Gibt an, dass es sich um eine Liste handelt)
o	classtag	(Gibt die gesuchte Klasse an)

` /api/student?list&classtag=10A ` 

2.	Sortiert abfragen:
Verwenden Sie hierfür folgende Attribute in genau der Reinfolge:
o	list		(Gibt an, dass es sich um eine Liste handelt)
o	classtag	(Gibt die gesuchte Klasse an)
o	sort-by	(Gibt an, nach was sortiert werden soll)

sort-by kann folgende Werte besitzen:
o	firstname	(Sortiert Liste nach Vornamen)
o	lastname	(Sortiert Liste nach Nachnamen)
o	average	(Sortiert Liste nach Durchschnittsnote)

` /api/student?list&classtag=10A&sort-by=firstname `

Bei erfolgreicher REQUEST erhalten Sie folgenden RESPONSE-Code:

```json
{ 
   "success":"true",  
   "studentlist":[
	{
	  "studentid":13,
	  "firstname":"Maria",
	  "lastname":"Musterfrau",
	  "classtag":"5A",
	  "average": "2.5"
	},
	{
	  "studentid":14,
	  "firstname":"Max",
	  "lastname":"Musterfrau",
	  "classtag":"5A",
	  "average": "2.5"
	}
  ]
}
```
 
# Notendaten:
Um mit Notendaten zu arbeiten, muss folgender Endpoint angesprochen werden:

` /api/grade `

Der Endpoint erlaubt dir, Notendaten zu erstellen, zu verändern/überarbeiten und zu löschen (siehe jeweiliges Unterkapitel).

## Erstellen einer Note (POST-REQUEST):
Für das Erstellen einer Note werden folgende Attribute benötigt:
-	action	(Gibt die auszuführende Aktion an, hier "create")
-	student-id	(Die ID des zugehörigen Schülers)
-	grade-type	(Die Art der Schulaufgabe, z.B. schriftlich)
-	value		(Der Wert der Note selbst, z.B. 1.0)
-	weight	(Wertigkeit der Note)

```json
{
  "action":"create",
  "data": {
    "student-id":"87432",
    "grade-type":"schriftlich",
    "subject":"deutsch",
    "value":1.0,
    "weight":1
  }
}
```

Bei erfolgreicher REQUEST erhalten Sie folgenden RESPONSE-CODE: (siehe Abb. 3)

## Überarbeiten/Verändern von Notendaten (POST-REQUEST):
Um Noten zu verändern, brauchen Sie folgende Attribute:
-	action	(Auszuführende Aktion, hier update)
-	grade-id	(ID der zu verändernden Note)
-	data-name	(Attribut welches verändert werden soll)
-	data-value	(Neuen Wert des Attributes)

```json
{
  "action":"update",
  "grade-id":"7623",
  "data": {
  	"grade-type":"muendlich",
	"subject":"mathe"
  }
}
```

Bei erfolgreicher REQUEST erhalten Sie folgenden RESPONSE-Code: (siehe Abb. 3)


## Löschen von Notendaten (POST-REQUEST):
Um Noten aus dem System zu löschen, werden folgende Attribute benötigt:
-	action	(Auszuführende Aktion, hier "delete")
-	grade-id	(ID der zu löschenden Note)

```json
{ 
  "action":"delete",
  "grade-id":"93874"
}
```

Bei erfolgreicher REQUEST erhalten Sie folgenden RESPONSE-Code: (siehe Abb. 3)


## Bestimme Notendaten abfragen (GET-REQUEST):
Um eine GET-Request ausführen zu können, müssen Sie an folgenden Endpoint adressieren: 

` /api/grade `

Wollen Sie Daten einer Note abfragen, geben Sie das Attribut "grade-id" an:

` /api/grade?grade-id=8723 `

Bei erfolgreicher REQUEST erhalten Sie folgenden RESPONSE-Code:

```json
{ 
   "success":"true", 
   "grade": {
	"gradeid":13,
	"studentid":9823,
	"gradetype":"schriftlich",
	"subject":"deutsch",
	"value":1.0,
	"weight": 1,
	"date":"2022-09-25 00:13:41"
	}
}
```
 
## Abfragen aller Noten eines Schülers (GET-REQUEST):
Um eine Liste aller Noten eines Schülers zu erhalten, müssen folgende Attribute angegeben werden:
-	student-id		(ID des Schülers)

` /api/grade?student-id=9632 `

Bei erfolgreicher REQUEST erhalten Sie folgende Antwort:

```json
{ 
   "success":"true", 
   "grades": [
        {
	"gradeid":13,
	"studentid":9823,
	"gradetype":"schriftlich",
	"subject":"deutsch",
	"value":1.0,
	"weight": 1,
	"date":"2022-09-25 00:13:41"
	},
        {
	"gradeid":14,
	"studentid":9823,
	"gradetype":"muendlich",
	"subject":"deutsch",
	"value":2.0,
	"weight": 1,
	"date":"2022-09-25 00:13:41"
	}
  ]
}
```
 
# Fehler und Fehlerbehebung:

| CODE	| ERROR-NAME | URSACHE | BEHEBUNG |
| ------------- | ------------- | ------------- | ------------- |
| 0 | No-Error 			| Erfolgreiche Request 		| KEINE |
| 1 | Syntax - Error 		| Derzeit keine			| KEINE |
| 2 | API KEY - Error		| Derzeit keine			| KEINE |
| 3 | Unknown Request Type	| Keine Get oder Post Request	| Setzte Request-Method auf POST oder GET |
| 4 | Wrong JSON Format		| Falsches Format in JSON	| Überprüfe Format in Documentation |
| 5 | Unset Action Error	| Kein Actionattribut		| Gib in deiner JSON das Attribut action an |
| 6 | Save Database Error	| Fehler beim Senden an Datenbank | Überprüfe die Logindaten für die Datenbank |
| 7 | Unknown Dataname Error	| Unbekanntes Dataname Attribut in JSON | Überprüfe, welche Datanames du verwenden darfst |
| 8 | Entry not found Error	| Kein Element gefunden, welches gesucht wird |	Überprüfe Angaben wie studentid oder gradeid |
| 9 | Invalid Get Request	| Falsche Get-Request		| Überprüfe Attribute und deren Reinfolge |
			
			

# Features in Zukunft:
Folgende Features werden in Zukunft hinzugefügt:

-	API-KEY
-	Absenzen
