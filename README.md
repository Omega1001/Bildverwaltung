# Bildverwaltung

## app.ini

Muss neben der auszuführenden .jar Datei liegen oder im Workdirectory liegen.

## Template

```
[db config]
javax.persistence.jdbc.url=jdbc:h2:<Pfad zur Datenbankdatei>
javax.persistence.jdbc.driver=<JDBC Treiber (Standard: org.h2.Driver )>

[directory]
picturesDirectory=<Pfad zum Speicherort der Bilder>


```

