# VIshop-plugin
Plugin łączący serwer Minecraft z itemshopem stworzonym na [VIshop.pl](https://vishop.pl/)  
Plugin przeznaczony dla silników pochodnych Bukkita (Spigota, Papera, itp.)

Autor pluginu: [Kamilkime](https://github.com/Kamilkime)

<h2>Wyświetlanie informacji o zakupie na chacie</h2>

<h4>Konfiguracja na serwerze Minecraft</h4>

Aby wyświetlać dowolną wiadomość na chacie przy zakupie produktu, musimy najpierw skonfigurować ją
w pliku konfiguracyjnym `config.yml`.

```yml
broadcasts:
  testowy-broadcast:
    - 'Gracz &a{NICK} &fzakupił &eVIP''a &fw naszym sklepie!'
    - ''
```  
`testowy-broadcast` jest tutaj identyfikatorem "ogłoszenia" wysyłanego na chacie. Możemy go zmienić na np. `vip-broadcast`.  
Istnieje możliwość dodania większej liczby ogłoszeń - innego dla każdego produktu.  
Treść ogłoszeń dodajemy w liście - każda linijka tekstu na chacie osobno, np.
```yml
broadcasts:
  testowy-broadcast:
    - 'Linijka 1'
    - 'Linijka 2'
    - 'Linijka 3'
```

Ogłoszenia wspierają zarówno domyślne kolory chatu (np. `&6`), ale także własne kolory RGB (np. `&#123456`).
Jeśli chcemy wyświetlić nazwę gracza w naszej wiadomości, wystarczy wpisać w to miejsce `{NICK}`.

Jeśli posiadasz wersję serwera 1.16 lub nowszą oraz silnik spigot lub jego fork (paper, purpur itp.), możesz użyć własnych kolorów RGB w wiadomościach (wspomnianych wyżej) czy tekstów tęczowych.  
Składnia tych wiadomości znajduje się pod linkiem: https://github.com/Phoenix616/MineDown/blob/master/README.md 
W przeciwnym wypadku nie zadziała Ci drugi przykład.

Poniżej przykładowe dwie wiadomości dla osobnych produktów:
```yaml
broadcasts:
  vip-broadcast:
    - 'Gracz &a{NICK} &fzakupił &eVIP''a &fw naszym sklepie!'
    - ''
  drugi-broadcast:
    - 'Gracz &a{NICK} &fzakupił &dsVIP''a &fw naszym sklepie!'
    - '&rainbow&Ten tekst będzie tęczowy!'
    - '&#123456Dziękujemy!'
```

<h4>Konfiguracja w panelu VIshop</h4>
Aby dodać do naszego produktu wyświetlanie ogłoszenia, które skonfigurowaliśmy w naszym configu, musimy przejść do interesującego nas produktu, edytować go (lub utworzyć), a następnie do listy komend dopisać `#<id ogłoszenia>`. Oczywiście `<id ogłoszenia>` zastępujemy identyfikatorem skonfigurowanego w configu ogłoszenia, np. `#vip-broadcast`.

![Obraz - panel, lista komend](https://imgur.com/daFO1rx.png "Komendy")