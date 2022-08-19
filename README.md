# VIshop-plugin
Plugin łączący serwer Minecraft z itemshopem stworzonym na [VIshop.pl](https://vishop.pl/)  
Plugin przeznaczony dla silników pochodnych Bukkita (Spigota, Papera, itp.)

Autor pluginu: [Kamilkime](https://github.com/Kamilkime)

# Wyświetlanie informacji o zakupie na chacie

**Konfiguracja na serwerze Minecraft**

Aby wyświetlać dowolną wiadomość na chacie przy zakupie produktu, musimy najpierw skonfigurować ją
w pliku konfiguracyjnym (`config.yml`).

```yml
...

broadcasts:
  testowy-broadcast:
    - 'Gracz &a{NICK} &fzakupił &eVIP''a &fw naszym sklepie!'
    - ''
```  
`testowy-broadcast` jest tutaj identyfikatorem "ogłoszenia" wysyłanego na chacie. Możemy go zmienić na np. `vip-broadcast`.  
Istnieje możliwość dodania większej ilości ogłoszeń - inne dla każdego produktu.  
Treść ogłoszeń dodajemy w yaml'owej liście - każda linijka tekstu na chacie w osobnym stringu.  

Ogłoszenia wspierają zarówno domyślne kolory chatu (np. `&6`), ale także własne kolory RGB (np. `&#123456`) - tylko spigot i jego forki (paper, purpur itd.).
Jeśli chcemy wyświetlić nazwę gracza w naszej wiadomości, wystarczy wpisać w to miejsce `{NICK}`.

Poniżej przykładowe dwie wiadomości dla osobnych produktów:
```yaml
broadcasts:
  vip-broadcast:
    - 'Gracz &a{NICK} &fzakupił &eVIP''a &fw naszym sklepie!'
    - 'Dziękujemy!'
  svip-broadcast:
    - 'Gracz &a{NICK} &fzakupił &dsVIP''a &fw naszym sklepie!'
    - 'Dziękujemy!'
```

**Konfiguracja w panelu VIshop**  
Aby dodać do naszego produktu wyświetlanie ogłoszenia, które skonfigurowaliśmy w naszym configu, musimy przejść do interesującego nas produktu, edytować go (lub utworzyć), a następnie do listy komend dopisać `#<id ogłoszenia>`. Oczywiście `<id ogłoszenia>` zastępujemy identyfikatorem skonfigurowanego w configu ogłoszenia, np. `#vip-broadcast`.

![Obraz - panel, lista komend](https://imgur.com/daFO1rx.png "Komendy")