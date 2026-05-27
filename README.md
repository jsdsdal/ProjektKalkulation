# AlphaScope

> [Link til website](http://projektkalkulation.azurewebsites.net/)
> - Brug testbruger login:
> - email: mikkel@alphasolutions.com
> - password: mikkel1234

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
- Dashboard med de vigtigste informationer og nøgletal (KPI board)
- Avanceret projektopbygning med fleksibel struktur
- Stilrent og minimalistisk layout så du kan holde fokusset på det vigtigste
- Dynamisk tracking af status, deadlines mv.

## Teknologier
- ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) _Ver. 21_
- ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white) _Ver. 2026.2_
- ![spring boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) _Ver. 4.0.6_
- ![mysql](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white) _Ver. 9.7_
- ![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white) _Ver. 4.0.0_
- _JDBC til MySQL 9.7_
## Værktøjer
- ![intellij](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) _Ver. 2026.1.2_
- ![figma|87](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white) 
- ![Claude](https://img.shields.io/badge/Claude-D97757?style=for-the-badge&logo=claude&logoColor=white)

AI er primært brugt til generering af testdata i SQL (data.sql) som hjælp til rekursive SQL statements.

## Modeller og diagrammer

### Domænemodel
Vores domænemodel beskriver vores forståelse af virksomhedens domæne. Her har vi forsøgt os at illustrere den composite data struktur vi har bevæget os ud med.

[![Domænemodel](https://github.com/ProjektKalkulation-Gruppe-6/ProjektKalkulation/blob/main/docs/Alpha%20Solutions%20-%20dom%C3%A6nemodel.svg)](https://lucid.app/lucidchart/6bf89913-e7c6-46a7-adde-2f02b577e463/edit?viewport_loc=-126%2C1345%2C4314%2C2498%2CHyMMb21TK6Mh&invitationId=inv_e70dfe66-b1dc-497b-9fff-a9185288457b)

### ER-digram
Vores ER diagram blev baseret på et Composite Design Pattern, hvor et Project kan have Subprojects og tasks kan have subtasks. Det vil sige at i både Project og Task tabellen findes der en attribut til henholdsvis parent_project_id og parent_task_id, så én række i tabellen kan henvise til én anden række i samme tabel. Vi diskuterede om hvorvidt vi skulle arbejde med composite design af flere omgange, da det ville være et helt nyt design pattern for os alle. I  identifikationen af spørgsmålet kan man snakke om både known unknowns og unknown unknowns - vi vidste, at der vil være en del af begge ved valg af dette pattern. 

[![ER-diagram](https://github.com/ProjektKalkulation-Gruppe-6/ProjektKalkulation/blob/main/docs/Alpha%20Solutions%20-%20ER-model.svg)](https://lucid.app/lucidchart/6bf89913-e7c6-46a7-adde-2f02b577e463/edit?viewport_loc=-126%2C1345%2C4314%2C2498%2CHyMMb21TK6Mh&invitationId=inv_e70dfe66-b1dc-497b-9fff-a9185288457b)


### Klassedigram
Vores klassediagram afdækker alle packages, klasser og metoder i det endelige program.
Dette nedenfor er udarbejdet i slutningen af Sprint 2. Her dannes der et fuldt overblik over programmets struktur, herunder hvordan vi i Java backend behandler composite data struktur.

[![KlassediagramFør](https://github.com/ProjektKalkulation-Gruppe-6/ProjektKalkulation/blob/main/docs/Alpha%20Solutions%20-%20Klassediagram.svg)](https://lucid.app/lucidchart/5ad35a05-2998-4c85-930f-d1cba19accae/edit?viewport_loc=-2841%2C-444%2C6105%2C3535%2CHyMMb21TK6Mh&invitationId=inv_41ea410b-dedf-4388-b45c-8520290a1703)

### Figma prototype

UI / UX er udarbejdet i Figma først mhb. på programmets første MVP. 
[Figma prototype](https://www.figma.com/design/8YThsNEn60wZCQABJhR2yU/AlphaScope?node-id=0-1&t=sb2x8PT5WP1YPohD-1)

## Installation

```
git clone https://github.com/ProjektKalkulation-Gruppe-6/ProjektKalkulation
cd ProjektKalkulation
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
