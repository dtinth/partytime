
I like playing music games with my friends, and we occasionally have an O2Jam party,
well, not much of a party, just I and my friend sometimes play O2Jam together.

Now, the problem is how to press the start button so that the game starts at the exactly same time.
Usually, when playing, the computers are usually not in sync, and makes the song sound echoey.



Partytime
=========

Partytime is a music game synchronization server.

Before the game on each computer starts, the client will connect to the Partytime server.
The server will synchronize its time with the client.

When everyone's client has been synchronized with the server, the host will press the "Start" button on the server application.
The server will then announce the time to start the game. The clients wait and start the game at the same time thanks to the synchronization.

This server just synchronizes the time to start the game.
After all players know when to start the game, the server disconnects the client.
There is no score tracking, chat, etc...


Supporting Clients
------------------

* My [__local_matching__ branch of __open2jam__](https://github.com/dtinth/open2jam/tree/local_matching) supports partytime.
Currently it is in an initial state. Once it is stable enough, hopefully, I'll merge it into main Open2jam.


How It Works / Use Case
-----------------------

1. The client starts the game in a __paused state__, that is, load everything necessary to start the game (music, notes, keysounds, etc.)
but don't start the game yet.
2. The client connects to the partytime server.
3. The partytime server will synchronize its time with the client.
    1. Server asks the local time from client.
    2. Client responds with the local time.<br>Steps 1-2 is repeated 50 times.
    3. Server calculates the time difference, [based on the response with the shortest round-trip time](http://en.wikipedia.org/wiki/Cristian%27s_algorithm).
4. The client will wait for the server to respond further commands.<br>The server will add the client to the players pool.<br>Other players join the game, and reach this step.
5. When all players are joined in and are at step 4, the host clicks the "Start" button.
6. Server broadcasts the number of player, and the time to start the game, and closes the connection.<br>The server will calculate the time for each client based on time difference, such that all clients will start the game at the exact same time.
7. Each client waits until the time set by the server is reached.
8. Each client unpauses the game, and enjoy!

