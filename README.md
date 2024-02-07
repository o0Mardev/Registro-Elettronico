# Registro Elettronico
È un app non ufficiale che usa l'API di [Axios Italia](https://axiositalia.com/) per comunicare con i server Axios e ricevere i compiti, le lezioni, i voti e altro.

## Sicurezza e privacy
L'app comunica soltanto con i server Axios e GitHub (per controllare gli aggiornamenti).
I dati vengono salvati in locale.

## Principi dell'app
- L'app si basa sul principio Offline-first: i dati una volta caricati vengono salvati in locale, ciò comporta una dimensione dell'app leggermente maggiore ma permette di velocizzare i tempi di caricamente notevolmente.
Un altro vantaggio è quello di poter visualizzare i dati scaricati anche se offline.
- Il design dell'app segue le linee guide del [Material Design 3](https://m3.material.io/) utilizzando la libreria Jetpack Compose con il supporto ai colori dinamici (Android 12+).

## Il progetto
Questo progetto è work in progress. Questi sono gli obiettivi fissati per ora:
- [ ] Suddivisione trimestre/quadrimestre - Quasi fatto
- [ ] Icona app - Quasi fatto
- [ ] Orario in app
- [ ] Grafico voti, media
- [ ] Widget
- [ ] Note disciplinari
- [ ] Pagella
- [ ] Curriculum
- [ ] Documenti
- [x] Divisione per materia
- [x] Colori diversi per i voti
- [x] Impostazioni app
- [x] Fix descrizione circolari
- [x] Impostare un compito come fatto
- [x] Funzionalità base (Compiti, voti, comunicazioni, lezioni)

## Ringraziamenti e contributi
[@VictorAlbertos](https://github.com/VictorAlbertos) per https://github.com/ireward/compose-html
