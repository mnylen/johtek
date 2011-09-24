omistaa(sokrates, rekku).
isa(rekku, jekku).
isa(sokrates, X) :- omistaa(sokrates,X), isa(X, Y).
