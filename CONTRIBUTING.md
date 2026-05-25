# Bidragelser til Wish2Gather

Alle bidragelser til projektet er værdsat. 
Se venligst indholdsfortegnelsen for hurtige links til at navigere rundt til hvordan du bedst muligt kan give os et skub fremad. 😄

# Indhold
- [Workflow](#workflow)
- [Branching strategi](#branching-strategi)
- [Navngivning](#navngivning)
- [Pull Requests](#pull-requests)
- [Værktøjer](#værktøjer)
- [Spørgsmål](#spørgsmål)
- [Ejerskab](#ejerskab)
- [Bug reports](#bug-reports)

---
# Workflow

## Branching strategi
Vi bestræber os på at holde en ordentlig branching strategi, således at vi nemt kan opspore og isolere potentielle huller i projektet.
1. Fork repository
2. Opret branch
3. Lav ændringer
4. Commit
5. Push
6. Opret Pull Request

Vi har 1 master branch som kun er hjem til stabil kode. Hver feature branch skal først godkendes i en pull request før den bliver merged til main. Derefter slettes den pågældende branch når den er godkendt og merged. 

## Navngivning
Alt backend sprog er på engelsk - dvs. klasse navne, referencer, og metoder.
Alt frontend er på dansk.
Hvis man er i tvivl, så er nøglereglen:
1. Kan almindelige brugere se det? -> Det skal stå på dansk
2. Er det kun udviklere der ser det? -> Det skal stå på engelsk
Dette er for at gøre det nemmere at læse og giver mindre inkongruens i hvordan man læser koden.

Derudover skal navnene være meningsfulde og følge java konventioner. 
Vi bruger ingen hardcoded værdier. 

## Pull Requests
Beskriv tydeligt og udførligt hvilke ændringer der er foretaget.
Lav hellere flere mindre commits end 1 stor commit på en gang - dette er for overskuds skyld når vi senere skal gennemgå ændringer.

## Værktøjer
Vi bruger udelukkende:
- Java Spring Boot
- Thymeleaf
- HTML + CSS
- MySQL

Alt andet skal godkendes af hele dev teamet før implementering. 
## Spørgsmål
Inden der stilles spørgsmål, anbefales der at man først tjekker eksisterende [issues](/issues). Skulle du imod forventning stadig mangle et svar, kan du oprette et issue og vente på svar.
Det anbefales at alle tekniske spørgsmål bliver søgt på nettet først.

Hvis du laver et issue, har vi en forventning til at du følger disse trin:
- Åbn en [issue](https://github.com/ProjektKalkulation-Gruppe-6/ProjektKalkulation/issues/new) .
- Skriv så meget information du kan ned om det problem du støder ind i
- Informer venligst om projekt og platform version, afhængigt af hvad der er relevant.

## Ejerskab
Når du bidrager til projektet forventer vi at du har fuld ejerskab til din kode og kan stå inde for det stykke arbejde du bidrager med. 

## Bug reports
Som udgangspunkt ser vi gerne at ingen skal opsøge dig igen for at få information om den pågældende bug der er rapporteret. Derfor beder vi dig om at følge disse trin:

- Tjek om dine værktøjer er fuldt opdaterede
- Find frem til om din bug er en bug og ikke en brugerfejl først - fx inkompatible versioner osv.
- Tjek om andre brugere har haft samme bug og evt. har løst dem.
- Sørg for at du har søgt internettet først (inkl Stack Overflow) for at se om nogen udenfor GitHub projektet har diskuteret et lignende problem.
- Indsaml følgende information om din bug:
	- OS, platform og version
	- Versionen af compileren, JDK, osv. afhængigt af hvad der er relevant
	- Evt. input og output som har ført til fejlen
	- Kan du reproducere problemet? Og kan du reproducere det i tidligere udgaver?
