# A* algoritmi sokkelikossa parhaan reitin selvittämiseen

Käyttö:

    python astar.py TIEDOSTO ALKUX ALKUY MAALIX MAALIY
    
jossa

* _TIEDOSTO_ on sokkelikkotiedosto; ja
* _(ALKUX, ALKUY)_ kuvaa aloituspisteen sokkelikossa; ja
* _(MAALIX, MAALIY)_ kuvaa haluttua pistettä sokkelikossa

Sokkelikkotiedostossa _X_ kuvaa estettä ja _._ läpikuljettavaa aluetta.

Esimerkiksi:

    python astar.py maze.txt 0 6 13 3
    
Tulostaa:

    ...................
    ......X....*******.
    ......X...**XXXXX*.
    ......X.***XXB****.
    .********..XXXXX...
    **X...........X....
    A.XXX..........X..X
    XXX...........X....
    ..X................
    ..X................

Tulostuksessa _A_ on alkupiste, _B_ on maalipiste ja tähdet kuvaavat kulkua pisteiden välillä.