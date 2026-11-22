# Fake Rangi v1.5.3 – Minecraft 1.21.4 Fabric

## Co poprawiono
- LPM na voucher VIP / SVIP / GVIP w zakładce RANGA EKWIPUNEK wymaga 1 przedmiotu w wybranej ręce.
- **Nowe:** podczas trzymania fake vouchera renderer pierwszoosobowy dostaje osobny, lokalny model książeczki z załączonymi teksturami. Nie jest to już tylko ikona przy hotbarze.
- Model ma lokalną nazwę i lore vouchera; prawdziwy przedmiot w serwerowym ekwipunku nie jest nadpisywany. Prawdziwy tooltip w ekwipunku nadal należy do przedmiotu serwera.
- PPM wykrywa użycie w kodzie klienta, aktywuje fake rangę i usuwa jej wizualizację, **ale może równocześnie wywołać normalne użycie oryginalnego przedmiotu**.
- COFNIJ usuwa lokalne efekty. Zachowano wcześniejsze rangi, Fake Creative i menu pod Z.

## Uwaga dotycząca bezpieczeństwa
Ta wersja poprawia **widok ręki w pierwszej osobie**, ale nie podmienia jeszcze modelu w ekwipunku ani widoku F5. Nie zmienia prawdziwych przedmiotów ani uprawnień. Inni gracze nadal widzą oryginalny przedmiot.

## Build na GitHubie
1. Rozpakuj ZIP i wgraj zawartość do głównego katalogu repozytorium, z zachowaniem folderu `src`.
2. Workflow jest w `.github/workflows/main.yml`.
3. Uruchom Actions i pobierz właściwy JAR z `build/libs` (nie `-sources.jar`).
4. Testuj na osobnym profilu Fabric 1.21.4 + Fabric API; **brak uruchomionego testu gry ani potwierdzonej kompilacji w tym środowisku**.
