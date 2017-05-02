# Oversikt

Student: Einar Snorrason

## Om funksjonaliteten

## Svar på spørsmål

1. Position.move returnerer en ny posisjon som er flyttet relativt til den gamle. Dette er forskjellig fra Position i Lab 5, som endret x og y-verdien i posisjonsobjektet i stedet for å lage en ny posisjon.

2. AbstractSimObject er en superklasse for objekter i simulasjonen som ikke beveger seg, og inneholder alle metodene som slike objekter trenger. AbstractMovingObject er en utvidelse av AbstractSimObject, og arver fra den. Den er en superklasse for objekter som skal bevege seg.

3. Subklasser skal endre posisjon ved å kalle step()-metoden.

4. distanceTo og directionTo git avstanden og retningen fra ett objekt til ett annet

5. Det kunne vært lurt å endre speed til private, sånn at subklasser ikke kan endre den direkte.

6. For å endre retning, må subklasser skrive "dir = dir.turn(angle);" Dette betyr at direction må endres direkte, og dir kan ikke være private. Det er nok best å sette inn en hjelpemetode i AbstractMovingObject for å fikse dette.

7. Hvis det fantes public-metoder for å endre retning og posisjon, kunne objekter ha posisjonen sin endret av andre deler av programmet. Dette vil skape problemer, og det er best hvis hvert objekt er ansvarlig for sin egen bevegelse.

### 1.7: Noen få ekstra spørsmål

Dette designet er lagd sånn at koden kan bli brukt for mange forskjellige objekter, og at det skal bli lett å utvide oppførselen etterpå. Grensesnittene betyr at disse nye objektene kan lett settes inn med de andre, fordi hovedprogrammet vet at de kan bruke de samme metodene. Det går an å gjøre dette på andre måter, for eksempel kode hvert objekt fra bunnen av, men dette ville være mye vanskeligere å utvide fordi det er mye mer arbeid å lage en ny klasse. I tillegg, så må man endre hvert objekt hvis man vil endre noe til felles for alle.

Uten grensesnitt vil hovedklassen ikke vite kunne garantere at objektene har samme oppførsel, som ville gjøre det mye vanskeligere å samle dem i en klasse.

## Eget design: Pacman

Fot eget design skal jeg lage en simulering av pacman. I begynnelsen av spillet er det en pacman, som søker etter og spiser pellets. Den får poeng for hver den spiser, som vises på skjermen. Det er fire spøkelser som leter etter pacman og prøver å ta ham. Pacman prøver å unngå disse hvis han kan. De fire spøkelsene har litt forskellig oppførsel:

* Den røde sikter rett på pacman.
* Den rosa sikter på et punkt foran pacman til den kommer til en viss avstand, så sikter den rett på.
* Den blå snur i en tilfeldig retning på gitte tider, følger etter pacman ellers.
* Den brune vandrer rundt helt tilfeldig.

Det finnes også superpellets, som dukker opp sjelden. Hvis pacman ser dem velger han å ta dem over de andre. Når han spiser en, blir spøkelsene blå og pacman prøver å spise dem. Da får han ekstra mange poeng, og spøkelset er "dødt" for en viss tid, før det kommer tilbake.

Simulasjonen ender når pacman dør. Da vises hvor mange poeng han fikk, og det begynner på nytt.

Klasser:

* Pacman - extends AbstractMovingObject
* Pellet - extends AbstractSimObject
* SuperPellet - extends AbstractSimObject
* AbstractGhost - extends AbstractMovingObject
..* RedGhost
..* PinkGhost
..* BlueGhost
..* BrownGhost

## Kilder til media

* Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).

* pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License
