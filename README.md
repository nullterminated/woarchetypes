# woarchetypes

This repo contains project templates for a WebObjects projects enhanced with Project WOnder.

# Usage
```
git clone git@github.com:nullterminated/woarchetypes.git
cd woarchetypes
mvn clean install
mvn archetype:crawl
```
Then reindex your local maven repository in your ide. Now you can create new projects using this archetype.

Alternately, you can simply generate the project using maven on the command line with something like,

```
cd ~/projects
mvn archetype:generate -DarchetypeCatalog=local
```

Selecting an archetype, and filling in values for groupId, artifactId, version, and package.
