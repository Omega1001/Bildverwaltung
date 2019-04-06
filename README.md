# Bildverwaltung

## Datenbank bauen

Das DB-Data Projekt muss gesondert gebaut werden mit den Maven Profilen "BuildRs" und "ProcessData", um eine funktionsfähige Datenbank zu bekommen. Dazu muss noch zusätzlich eine Datei "db-data-build.properties" im Working directory vom DB-Data Projekt liegen. 
Dazu einfach die "db-data-build.properties.template" kopieren, das ".template" entfernen und den Pfad bei "dbUrl=jdbc:h2:" zur Datenbank ergänzen.

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

