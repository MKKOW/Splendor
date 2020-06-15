# APRO2 - Projekt - Splendor
W folderze `release` znajdują się przygotowane foldery z klientem i serwerem.

Serwer musi zostać uruchomiony z konsoli komendą `java -jar Splendor-server.jar x` gdzie `x` to oczekiwana liczba graczy w rozgrywce.

Klient nie musi być uruchamiany z konsoli.

Do serwera wymagana jest Java 11, do klienta Java 8.

Obok plików `.jar` muszą znajdować się wszystkie pliki zawarte w odpowiednich dla nich folderach `client`, `server`.

Serwer jest załadowany na maszynę wirtualną, uruchomioną na Google Cloud Platform, dostępną pod adresem `34.107.16.236` przed podłączeniem się do serwera należy wcześniej ręcznie uruchomić serwer na maszynie wirtualnej. Możliwa jest tylko jedna rozgrywka jednocześnie.

Serwer zawsze uruchamia się na porcie `2000`.