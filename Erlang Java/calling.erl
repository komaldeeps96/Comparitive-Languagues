-module(calling).
-export([person/2, contactall/2, report1/1]).


contactall(List, MyID) when length(List) ==0 -> 
	if
		MyID==0 ->
			ok;
		true ->
			ok
	end;

contactall(List, MyID) when length(List) > 0 ->
	timer:sleep(rand:uniform(100)),
	hd(List) ! {MyID, hd(List) ,intro , element(3, erlang:now())},
	contactall(tl(List), MyID).

person(Names, Iam) ->
	contactall(Names, Iam),
	report1(Iam).

report1(MyID) ->
	receive
		{From, To, Message , Timestamp} ->
			if
				Message==intro ->
					master ! {From, To, Message , Timestamp},
					timer:sleep(rand:uniform(100)),
					From ! {From, To , reply , Timestamp};

				Message== reply ->
					master ! {From, To, Message , Timestamp};

				true -> ok
			end,
			report1(MyID)
	after 
		1000 -> io:fwrite("~nProcess ~w has received no calls for 1 second, ending...~n", [MyID])
	end.