-module(exchange).
-import(calling, [person/2]).
-export([start/0, callstobemade/1, createperson/1, report/0]).


callstobemade(L) when length(L) ==0 -> ok;

callstobemade(L) when length(L) > 0 ->
	io:fwrite("~w", [element (1, hd(L))]),
	io:fwrite(": "),
	io:fwrite("~w~n", [element (2, hd(L))]),
	callstobemade(tl(L)).

createperson(L) when length(L) ==0 -> ok;

createperson(L) when length(L) > 0 ->
	register(element (1, hd(L)), spawn(calling, person, [element (2, hd(L)) , element (1, hd(L)) ] )),
	createperson(tl(L)).

start() ->
	{ok, List1}=file:consult("calls.txt"),
	register(master, self()),
	io:fwrite("** Calls to be made ** ~n"),
	callstobemade(List1),
	createperson(List1),
	io:fwrite("~n"),
	report().

report() ->
	receive
		{From, To, Message , Timestamp} ->
			if 
				Message==intro ->
					io:fwrite("~w received ~w message from ~w [~w] ~n", [To, Message, From, Timestamp]);
				Message==reply ->
					io:fwrite("~w received ~w message from ~w [~w] ~n", [From, Message, To, Timestamp]);
				true ->
					ok
			end,
			report()
	
	after 
		1500 -> io:fwrite("~nMaster has received no replies for 1.5 seconds, ending... ~n")

	end.