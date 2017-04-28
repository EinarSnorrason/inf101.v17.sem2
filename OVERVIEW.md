# Oversikt

Student:

## Om funksjonaliteten

## Svar på spørsmål

1. Position.move returnerer en ny posisjon som er flyttet relativt til den gamle. Dette er forskjellig fra Position i Lab 5, som endret x og y-verdien i posisjonsobjektet i stedet for å lage en ny posisjon.

2. AbstractSimObject er en superklasse for objekter i simulasjonen som ikke beveger seg, og inneholder alle metodene som slike objekter trenger. AbstractMovingObject er en utvidelse av AbstractSimObject, og arver fra den. Den er en superklasse for objekter som skal bevege seg.

3. Subklasser skal endre posisjon ved å kalle step()-metoden.

4. distanceTo og directionTo git avstanden og retningen fra ett objekt til ett annet

5. Det kunne vært lurt å endre speed til private, sånn at subklasser ikke kan endre den direkte.

6. For å endre retning, må subklasser skrive "dir = dir.turn(angle);" Dette betyr at direction må endres direkte, og dir kan ikke være private. Det er nok best å sette inn en hjelpemetode i AbstractMovingObject for å fikse dette.

7. Hvis det fantes public-metoder for å endre retning og posisjon, kunne objekter ha posisjonen sin endret av andre deler av programmet. Dette vil skape problemer, og det er best hvis hvert objekt er ansvarlig for sin egen bevegelse.

## Kilder til media

* Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).

* pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License
