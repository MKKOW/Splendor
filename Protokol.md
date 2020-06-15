# Projekt Splendor - Protokół komunikacyjny
#### Wersja: 2.0

## 1. Informacje wstępne

Celem protokołu jest stworzenie ujednoliconego i sztywno określonego sposobu komunikacji między klientem, a serwem. Implementacje obu tych instancji muszą potrafić identyfikować, przetwarzać i wysyłać żądania w formacie JSON.

Wszystkie dane zapisane są w formacie klucz : wartość. Gdzie klucz jest zawsze typu string, a w polu wartość możliwe jest przekazanie typów prostych takich jak int, czy string lub złożonych jak tablice (symbol []) lub obiekty (symbol {}). W odpowiednich sekcjach zdefiniowano nazwy kluczy, ich cel oraz dopuszczalne wartości jakie mogą przechowywać. Jeśli klucz może mieć jedną z kilku wartości symbolizowane jest przez || (lub). Jest to umowna wartość, nie wchodząca w json. Wiadomości od serwera do klienta lub wielu klientów jednocześnie nazywane są **odpowiedziami**, z kolei wiadomości od klientów do serwera **zapytaniami** (jak w HTTP).

Każda wiadomość na pierwszym miejscu zawiera specjalny klucz nazywany dalej **nagłówkiem**. Dla zapytań jest to *request_type*, dla odpowiedzi *answer_type*. Wskazuje on dla oprogramowania czego ma się on spodziewać w dalszym ciele wiadomości i jak zinterpretować następne dane.

### 1.1 Opis kluczy

**client_id**, **set_client_id** - sekretny, unikalny klucz UUID (universally unique identifier) **generowany i wysyłany przez serwer do każdego klienta na początku rozgrywki** w przywitaniu serwera o nagłówku *server_hello*. Identyfikator zapisany jest w formacie string. Klient powinien **zapisać klucz** w pamięci podręcznej i **wysyłać go do serwera**, w każdym kolejnym zapytaniu w wartości klucza *client_id*. Jest to zabezpieczenie przed podszyciem się pod innego gracza. Serwer wysyłając odpowiedzi do użytkowników używa ustalonych przez nich nicków, nie ma więc ryzyka wycieku identyfikatora. Nadawanie client_id opisane jest w sekcji 2.2. [Dokumentacja klasy UUID](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/UUID.html).

**nick** - każdy z graczy po połączeniu z serwerem przez TCP i otrzymaniu unikalnego identyfikatora klienta (*client_id*) wybiera sobie nick, którym **będzie się posługiwał w grze**. W tym celu pod kluczem *nick* w zapytaniu o nagłówku *client_hello* umieszcza wybrany przez siebie nick. **Nick jest widoczny dla wszystkich graczy**. Serwer, gdy rozsyła odpowiedzi używa właśnie tych nicków, aby odnieść się do danego gracza. Nick jest typu string (kodowanie utf-16), i **musi zawierać od 3 do 10 znaków włącznie**. Nie jest możliwe, aby dwóch graczy wybrało ten sam nick. Serwer musi sprawdzać, czy wybrany nick **nie jest już zajęty** i wysyłać odpowiednią odpowiedź.

**server_version**, **client_version** - wersja serwera lub klienta. Jest to wiadomość czysto informacyjna dla developerów gry. Przy odpowiednim numerowaniu wersji możliwe jest sprawdzenie czy oprogramowanie klienta i serwera są ze sobą kompatybilne. Obie wartości są typu string i można je dobrać dowolnie. Zaleca się jednak aby nie były one za długie.

**message** - wiadomość przekazywana od serwera dla gracza. Klient po otrzymaniu odpowiedzi od serwera zawierającej ten klucz **powinien wyświetlić ją dla gracz**a**. Wiadomość powinna być w formacie przyjaznym użytkownikowi i **nie powinna być w żaden sposób przetwarzana ani modyfikowana** przez oprogramowanie klienta. Jest to sposób w jaki serwer może wyświetlić dla gracza **dodatkowe informacje** o zajętym nicku, ruchach wykonanych przez innych graczy, wiadomość o błędnym ruchu i inne informacje. Wartością klucza jest zawsze string o zmiennej długości.

**number_of_players** - Całkowita liczba graczy, na którą przewidziana jest obecnie ustawiona gra. Może ona wynosić od 2 do 4 graczy włącznie. Pole te zawarte jest w odpowidzi serwera o nagłówku *server_hello* i *nick_verification*, aby gracz po wybraniu swojego nicku mógł zweryfikować, czy został poprawnie dodany, jak i również wyświetliła mu się zaktualizowana lista nicków jeśli wybrał zajęty lub niepoprawny nick.

**connected_players** - Obecna liczba graczy, którzy podłączyli się do serwera i prawidłowo wybrali dla siebie nick. Pole zawarte jest w odpowiedzi serwera o nagłówku *server_hello* i *nick_veryfication*. Jeśli gracz **poprawnie wybrał nick** *connected_players* **powinien zostać uwzględniony** w odpowiedzi serwera *nick_veryfication*. Jeśli nie, nie powinien być uwzględniany przy liczeniu obecnie podłączonych graczy. 

**nicknames** - Tablica nicków wybranych przez obecnie połączonych graczy. Tablica jest długości odpowiadającej liczbie podłączonych graczy (pole *connected_players*). Kolejność nicków może być dowolna. Jest to informacja dla gracza, jakie nicki są już zajęte i jak wyglądają nicki innych graczy. Pole umieszczane jest w odpowidzi serwera w nagłówku *server_hello* i *nick_veryfication*. Jeśli gracz wybrał poprawny nick w odpowidzi o nagłówku *nick_veryfication* powinien pojawić się jego nick. 

<!-- TODO: dokończyć opis kluczy -->

## 2. Łączenie z serwerem

Połączenie z serwerem odbywa się w 4 krokach:

1. Zawarcie połączenia TCP
2. Przywitanie serwera (nadanie client_id)
3. Przywitanie klienta (wybór nicku)
4. Weryfikacja nicku


### 2.1 Zawarcie połączenia TCP

Zawarcie połączenia TCP robione jest przez odpowiednie metody javy.
Zainicjowanie takiego połączenia i obsługa wyjątków jakie może wygenerować zależne jest od klienta i **nie wchodzi w ramy tego protokołu**. Jeśli połączenie nie zostanie zestawione nie jest możliwe przejście do dalszych kroków.

### 2.2 Przywitanie serwera

Po zestawieniu połączenia serwer **wysła dla klienta informacje o obecne podłączonych graczach, identyfikator client_id i wersje serwera z jaką połączony jest klient**. Odbywa się to za pośrednictwem odpowiedzi powitania z nagłówkiem *server_hello*.

**Format odpowiedzi**
```json
{
    "answer_type" : "server_hello",

    "set_client_id" : string,
    "number_of_players" : int,
    "connected_players" : int,
    "nicknames" : [string, string, ...],
    "server_version" : string,
}

```
Opis kluczy:

* set_client_id - unikalny identyfikator dla klienta
* number_of_players - liczba graczy na którą przewidziana jest rozgrywka
* connected_players - liczba obecnie podłączonych graczy (którzy wybrali już nick)
* nicknames - nicki podłączonych graczy
* server_version - wersja serwera

### 2.3 Przywitanie klienta

Ustalenie nicku **rozpoczyna klient** wysyłając zapytania o nagłówku *client_hello*. W ciele zapytania musi umieścić swój identyfikator klienta, wybrany przez siebie nick oraz wersję oprogramowania.


**Format zapytania**
```json
{
	"request_type" : "client_hello",

    "client_id": string,
    "nick" : string,
    "client_version" : string
}
```

Opis kluczy:

- set_nick - nick, który chce gracz
- client_id - unikalny identyfikator klienta
- client_version - wersja oprogramowania klienta

### 2.4 Weryfikacja nicku

Po wybraniu przez klienta nicku serwer powinien **przystąpić do weryfikacji**. Tutaj możliwe są dwa przypadki:

* Nick jest ok - wysłanie odpowidzi z rezultatem "ok"
* Bick jest zajęty lub nie spełnia wymagań - wysłanie odpowidzi z rezultatem "invalid" 

W obu przypadkach serwer wysyła odpowiedź z nagłówkiem *nick_verification*. Różnią się one w polu *result*, dla poprawnego nicku ma ono wartość "ok", dla niepoprawnego wartość "invalid". Dodatkowo **odpowiedź dla niepoprawnego nicku** zawiera wiadomość opisującą dlaczego weryfikacja nicku się nie powiodła.

**Format odpowiedzi dla poprawnego nicku**
```json
{
	"answer_type" : "nick_verification",

    "result" : "ok",
    "number_of_players" : int,
    "connected_players" : int,
    "nicknames" : [string, string, ...]
}
```

**Format odpowiedzi dla niepoprawnego nicku**
```json
{
	"answer_type" : "nick_verification",

    "result" : "invalid",
    "number_of_players" : int,
    "connected_players" : int,
    "nicknames" : [string, string, ...],
    "message": string
}
```

Opis kluczy:

* result - wynik weryfikacji nicku, możliwe wartości to *ok* lub *invalid*
* number_of_players - liczba graczy, na którą przewidziana jest gra
* connected_players - obecni gracze, którzy poprawnie wybrali nick
* message - wiadomość dla klienta, dlaczego odrzucono jego nick

<!--  #### 2.5 Aktualizacja lobby         (Może kiedyś)

```json
{
    "answer_type" : "lobby_update",

    "number_of_players" : 2-4,
    "connected_players" : int,
    "nicknames" : 1-4,
}
``` -->

## 3. Ruch gracza

Gracz w swojej turze może zrobić tylko jedną z następujących rzeczy**:

* Kupić kartę ze stołu
* Zarezerwować kartę znajdującą się na stole i przenieść ją na rękę
* Wziąć klejnoty

Każda z tych czynności sygnalizowana przez klienta wysłaniem zapytania o nagłówku odpowiednio: *buy_card*, *claim_card* i *get_gems*. Każde z tych zapytań jest opisane poniżej.


**Uwaga!**

Jeśli serwer odrzuci ruch, stan planszy klienta **nie może się zmienić tak, aby niemożliwe było odtworzenie stanu planszy przed dokonaniem nieporawnego ruchu.**

### 3.1 Wykonanie ruchu

#### 3.1.1 Kupno karty

W przypadku, gdy gracz **chce kupić kartę** sygnalizuje to wysyłając zapytanie z nagłówkiem *buy_card*. W dalszej części zapytania powinno się znaleźć: id klienta, miejsce gdzie obecnie znajduje się karta i id karty.

**Format zapytania**
```json
{
	"request_type" : "buy_card",

    "client_id" string,
    "place" : "hand" || "table",
    "card_id" : int
}
```

Opis kluczy:

* client_id - unikalny identyfikator klienta
* place	- miejsce gdzie znajduje się kartaL
  *   *hand* - zarezerwowana karta, karta "na ręce"
  *   *table* - karta na stole
* card_id - id karty [0-89]

#### 3.1.2 Rezerwacja karty

W przypadku, gdy klient **chce zarezerwować kartę**, sygnalizuje to wysyłając zapytanie z nagłówkiem *claim_card*. W dalszej części zapytania powinno się znaleźć: id klienta, id karty.


**Format zapytania**
```json
{
	"request_type" : "claim_card",

    "client_id": string,
    "card_id" : int,
}
```

Opis kluczy:

- client_id - unikalny identyfikator klienta
- card_id - id karty [0-89]
- noble_id - id odwiedzającego noblisty [90-99]

#### 3.1.3 Pobranie klejnotów

W przypadku, gdy klient chce **wziąć klejnoty** wysyła zapytanie *get-gems*.

Możliwe kombinacje to:
* 2 klejnoty **tego samego** koloru
* 3 klejnoty **różnych** kolorów


**Format zapytania**
```json
{
	"request_type" : "get_gems",

    "client_id" : string,
    "white" : int,
    "green" : int,
    "blue"  : int,
    "black" : int,
    "red"	: int
}
```

Opis kluczy:

- client_id - unikalny identyfikator klienta
- white, green, blue, black, red - ilość dobranych klejnotów danego koloru

### 3.2 Weryfikacja ruchu

Po tym jak klient gracza **prześle ruch gracza do serwera**, jego logika powinna go **zweryfikować**, po czym wysłać odpowiedź powrotną. Jeśli ruch był poprawny wysyła odpowiedź *move_verification* z rezultatem *ok*. Dla niepoprawnego ruchu serwer powinien odpowiedzieć rezultatem *illegal* z dodatkowym polem *message*, które zawiera wytłumaczenie **dlaczego ruch został odrzucony**, przeznaczoną dla gracza.

Jeśli ruch był niepoprawny serwer powinien **oczekiwać na ponowne wykonanie ruchu przez tego gracza**, aż nie zostanie wykonany poprawny ruch, a klient powinien wyświeltić wiadomość serwera dla gracza i pozwolić mu na poprawę swojego ruchu.


**Format odpowiedzi dla poprawnego ruchu**
```json
{
    "answer_type" : "move_verification",

    "result" : "ok"
}
```
Opis kluczy:

- result - rezultat sprawdzenia ruchu

**Format odpowiedzi dla niepoprawnego ruchu**
```json
{
    "answer_type" : "move_verification",

    "result" : "illegal",
    "message": string
}
```

Opis kluczy:

- result - rezultat sprawdzenia ruchu
- message - wiadomość wyjaśniająca dlaczego ruch został odrzucony

### 4. Dodatkowa akcja na koniec tury

Może się zdarzyć, że po dokonaniu poprawnej akcji przez gracza pod koniec swojej tury musi wykonać jeszcze dodatkowe akcje. Zaliczają się do nich

* **Wybranie noblisty** - może się to zdarzyć tylko po **zakupie karty**, ilość zniżek dla danego graczy spowodowała, że w swojej turze zebrał wystarczająco, aby mogło do niego przyjść 2 lub więcej noblistów. (rezultat "select_noble")
  
* **Odrzucenie gemów** - może się to zdarzyć po **dobraniu gemów** lub **zaklepaniu karty i otrzymaniu żółtego tokenu**. Gracz nie może mieć na ręce więcej niż 10 gemów w każdym momencie (wliczając żółte). Musi więc odrzucić dokładnie tyle, aby mieć ich 10. (rezultat "get_gems")

* **Nic nie trzeba już robić** - tura zakończyła się powodzeniem, gracz nie musi już podajmować żadnych dodatkowych decyzji. (rezutlat "ok")

Nie może się zdarzyć, że obie te sytuacje staną się równocześnie.

#### 4.1 Odpowiedz serwera z rządaniem wykonania dodatkowego ruchu

Żądanie wykonania dodatkowej akcji sygnalizowane jest odpowiedzią serwera z nagłówkiem *end_of_round*. W polu *result* umieszczone są możliwe rezultaty odpowiadające powyższym sytuacjom. 

Możliwe jest wystąpienie **więcej niż jednej akcji** dla jednej rundy. Na przykład po zaklepaniu karty, użytkownik może dostać żółty gem i przez to musieć odrzucić dodatkowe gemy. Tura gracza kończy się dopiero po otrzymaniu odpowiedzi z rezultatem *ok*.


**Odpowiedz serwera na koniec tury gdy klient nie musi podajmować akcji**
```json
{
  "answer_type" : "end_of_round",

  "result" : "ok"
}
```
Opis kluczy:

* result - rezultat tury
    * "ok" - koniec tury, brak dodatkowych akcji


**Odpowiedz serwera na koniec tury gdy klient musi wybrać noblistę**
```json
{
  "answer_type" : "end_of_round",

  "result" : "select_noble",
  "nobles" : [noble_id1, noble_id2, ...]
}
```

Opis kluczy:

* result - rezultat tury
    * "select_noble" - gracz musi wybrać jednego spośród noblistów
* nobles - lista noblistów do wybrania (**tylko w przypadku "select_noble"!**)

**Odpowiedz serwera na koniec tury gdy klient musi odrzucić gemy**
```json
{
  "answer_type" : "end_of_round",

  "result" : "discard_gems"
}
```

Opis kluczy:

* result - rezultat tury
    * "discard_gems" - gracz musi odrzucić gemy


**Uwaga!**

Akcje **nie wymagające interwencji gracza** takie jak otrzymanie noblisy lub złotego gemu **nie są w żaden sposób sygnalizowane przez serwer**. Klient dowiaduje się o nich w następnym kroku kiedy **otrzyma zaktualizowaną planszę** (patrz punkt 5).

#### 4.2 Odpowiedzi klienta

Klient musi odpowiedzieć na rezultat *discard_gems* i *select_noble*. W przypadku otrzymania rezultatu *ok*, klient nie powinien nic odpowiadać. Ponieżej przedstawione są formaty odpowiedzi dla każdego z rezultatów. W tym celu tworzy zapytanie do serwera z nagłówkiem *request_type* odpowiadającym polu *result* odpowiedzi serwera według ponieższego schematu.

**format odpowiedzi klienta na żądanie odrzucenia gemów**
```json
{
	"request_type" : "discard_gems",

    "client_id" : string,
    "white" : int,
    "green" : int,
    "blue"  : int,
    "black" : int,
    "red"	: int
}
```

Opis kluczy:

  * client_id - unikalny identyfikator klienta
  * white, green, blue, black, red - ilość odrzuconych gemów danego koloru 

**format odpowiedzi klienta na żądanie wybrania noblisty**
```json
{
    "request_type" : "select_noble",

    "client_id" : string,
    "noble_id" : int
}
```

Opis kluczy:

  * client_id - unikalny identyfikator klienta
  * noble_id - id wybranego noblisty

**Uwaga!**

W przypadku gdy klient dokona wyboru i odeśle odpowiedz **serwer musi ją sprawdzić**. W przypadku gdy jest ona niepoprawna powinien ponownie wysłać do klienta taką samą wiadomość, aż klient nie zwróci poprawnego wyboru.
<!-- TODO: Przemyśleć to, bo teraz można DoSować serwer i grę... -->


## 5. Przesłanie zmian stanu gry

Po turze każdego gracza serwer przesyła do każdego z graczy informację o stanie pieniędzy w banku, informacje o dobytku graczy oraz kartach na stole w następującym formacie.

Odbywa się to w nagłówku odpowiedzi *game_board*. 

Na początku wysyłana jest informacja, który z graczy rusza się w tej turze w nagłówku *next_player* zawierający nick gracza. 

Następnie w polu *bankCash* przesyłany jest stan plan pieniędzy w banku (na planszy). 

W polu *players* przekazywane jest tablica obiektów graczy o długości odpowiadającej liczbie graczy. Każdy z obiektów zawiera informacje o nicku gracza, jego stanie gemów, informacje o tym czy w danej turze jest aktywny (pokrywa się z next_player), jego karcie na ręku i zniżce (wartości produkcji) jaką zapewniają mu karty rozwoju i nobliści oraz całkowitej ilości punktów prestiżu. N

astępnie w polu "cards" przesyłany jest obiekt zawierający pola zawerające id kart pierwszego, drugiego i 3 poziomu (w osobnych tablicach) leżące na stole.

 W ostatnim polu *nobles* przesyłana jest tablica id noblistów na stole.

**Format odpowiedzi serwera zawierającej status planszy**
```json
{
  "answer_type" : "game_board",

  "next_player" : nick,
  "bankCash": {
    "white": int,
    "green": int,
    "blue": int,
    "black": int,
    "red": int,
    "yellow": int
  },
  "players": [
      {
          "nick": nick,
          "cash":
          {
            "white": int,
            "green": int,
            "blue": int,
            "black": int,
            "red": int,
            "yellow": int
          },
          "active": true || false,
          "claimedCard": card_id || null,
          "totalDiscount":
          {
            "white": int,
            "green": int,
            "blue": int,
            "black": int,
            "red": int
          },
          "totalPrestige": int
    },
    ... // reszta graczy wg schematu
  ],
  "cards":
  {
    "level1" : [card_id1, card_id2, ...],
    "level2" : [card_id1, card_id2, ...],
    "level3" : [card_id1, card_id2, ...]
  },
  "nobles" : [noble_id1, noble_id2, ...]
}
```

Opis kluczy:

  * next_player - nick gracza który powinien teraz się ruszyć
  * bankCash - Gemy na planszy (w banku)
    * white, green, blue, black, red, yellow - liczba gemów danego koloru
  * players - tablica graczy na planszy, zawiera obiekty o polach
    * nick - nick gracza
    * cash - gemy gracza
      * white, green, blue, black, red, yellow - liczba gemów danego koloru
    * active - czy w danej turze jest aktywny, pokrywa się z next_player
    * claimedCard - id karty na ręku lub null jeśli nie ma
    * totalDiscount - całkowita zniżka (wartość produkcji) jaką zapewniają karty rozwoju i nobliści
      * white, green, blue, black, red - liczba punktów zniżki dla danego koloru
    * totalPrestige - całkowita liczba punktów prestiżu gracza
  * cards - karty rozwoju na stole
    * level1 - tablica id kart rozwoju pierwszego poziomu
    * level2 - tablica id kart rozwoju drugiego poziomu
    * level3 - tablica id kart rozwoju trzeciego poziomu
  * nobles - tablica zaierająca id noblistów na stole


**Uwaga!**

Punkty 4 i 5 powtarzane są do czasu, aż któryś z graczy **osiągnie 15 punktów i każdy z graczy wykona taką samą liczbę ruchów**. Po tym gra się kończy i wyłaniany jest zwycięzca.

## 6. Zakończenie gry

Komunikat o zakończeniu gry wysyłany jest przez serwer do wszystkich klientów. W momencie gdy jeden z graczy osiągnie co najmniej 15 punktów i wszyscy wykonali taką samą liczbę ruchów, serwer wysyła wiadomość o zwycięzcy, która kończy rozgrywkę.

Następnie serwer powinien **zerwać połączenie** z klientami.

```json
{
	"answer_type" : "game_over",

    "winner" : nick
}
```

Opis kluczy:

* winner - nick gracza, który zdobył najwyższą liczbę punktów