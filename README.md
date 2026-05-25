# AlphaScope

> [Link til website](link)

Vi har bygget et projektkalkulationsværktøj med formål at øge produktiviteten i virksomheden Alpha Solutions.
Projektet er udarbejdet som en del af 2. semester eksamen på Datamatiker uddannelsen.


## Indhold
- [Features](#features)
- [Teknologier](#teknologier)
- [Værktøjer](#værktøjer)
- [Modeller og diagrammer](#modeller-og-diagrammer)
- [Installation](#installation)
- [Brug](#brug)
- [Bidrag](#bidrag)
- [Developers](#developers)

---
## Features
- Dashboard med de vigtigste informationer og nøgletal
- Avanceret projektopbygning med fleksibel struktur
- Stilrent og minimalistisk layout så du kan holde fokusset på det vigtigste

## Teknologier
- ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) _Ver. 21_
- ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white) _Ver. 2026.2_
- ![spring boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) _Ver. 4.0.6_
- ![mysql](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white) _Ver. 9.7_
- ![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white) _Ver. 4.0.0_
## Værktøjer
- ![intellij](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) _Ver. 2026.1.2_
- ![figma|87](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white) 
- ![Claude](https://img.shields.io/badge/Claude-D97757?style=for-the-badge&logo=claude&logoColor=white)

(ERIK SKRIV AI BRUG HER)

## Modeller og diagrammer

### Domænemodel
Vores domænemodel beskriver vores forståelse af virksomhedens domæne. Her har vi forsøgt os at illustrere den composite data struktur vi har bevæget os ud med.

[![Domænemodel](FOTO)](URL)

### ER-digram
Beskrivelse

[![ER-diagram](FOTO)](URL)

### Klassediagram før implementering 
Dette projekt er struktureret efter en lagdelt arkitektur (Layered Architecture), som fungerer som en udvidet MVC-model for at sikre en klar adskillelse af ansvar. Systemet er designet med afsæt i GRASP-principperne, hvor Controller-laget (f.eks. ProjectController og LoginController) fungerer som Information Expert for HTTP-trafik, mens forretningslogikken er uddelegeret til et Service-lag (Pure Fabrication) for at opnå høj kohærens. Her valideres data, før de sendes videre til Repository-laget, som via Spring JdbcTemplate håndterer alle fundamentale CRUD-operationer (Create, Read, Update, Delete) mod databasen. Denne opbygning fremmer Low Coupling, da de enkelte lag kun kommunikerer med deres nærmeste nabo, understøttet af et centraliseret Exception-lag og domæneobjekter i Model-laget, hvilket sikrer en robust og testbar applikation.

[![KlassediagramFør](FOTO)](URL)

### Klassedigram efter implementering
DashboardController er nu implementeret. LoginController har fået tilføjet de nødvendige metoder til at håndtere sessioner og autentificering, mens HomeController fungerer som det centrale landingspunkt. Flere metoder i WishController og WishListController er blevet præciseret (f.eks. details, create og delete stier), så de matcher den faktiske kode-struktur.

[![KlassediagramFør](FOTO)](URL)

### Figma prototype

UI / UX er udarbejdet i Figma først mhb. på programmets første MVP. 
[Figma prototype](https://www.figma.com/design/8YThsNEn60wZCQABJhR2yU/AlphaScope?node-id=0-1&t=sb2x8PT5WP1YPohD-1)

## Installation

```
git clone https://github.com/Wishlist-gruppe-1/Wishlist#
cd Wishlist
mvn spring-boot:run
```

## Brug
Åbn i `localhost:8080` efter manuel opsætning af MySQL database.

## Bidrag
Se venligst [CONTRIBUTING.md](/CONTRIBUTING.md)

## Developers

- Erik Lindkvist Thomsen - [lindkvst](https://github.com/lindkvst)
- Anne-Sophie Phanseeda - [anph1000](https://github.com/anph1000)
- Johan Oliver Larsen - [johanlarsen](https://github.com/johanlarsen)
- Julie Jensine Juul Sundsdal - [jsdsdal](https://github.com/jsdsdal)

**Udarbejdet som eksamensprojekt for Datamatiker 2. semester på EK (Erhvervsakademi København) fra uge 18-22 i foråret 2026.**
