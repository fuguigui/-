The program consists of the implementations of three approaches: 
---a classical approach using alpha-beta pruning search, 
---an upgrade for the first one with many ready-made tricks,
---a trial version using Monte Carlo Tree search. This approach is the core of my work.

How to run the program?
Requirements: Java SE -1.8; Text file encoding: UTF-8
---First Package Directory contains codes for the first approach, the main method is in Ai3.java. You can run it to start the program.
------When the program is successfully running, you are into a real game contest as the white player. You can put your chess on any of the empty positions, then the computer will give its choice. Repeat until the game ends.
---Default Directory contains codes for the second and third approach. The main method is in NewGame.java. Run it and start the game. In default, you will be the white player, who put chess after the computer player.

Files' Descriptions and Functions:
---Default source directory: codes for the second and the third approaches. Up to now, the third one is the default approach. No interface for the second approach has been written since there exists many errors in the code.
---NewGame.java: the control entrance for the program. It starts the drawing of an graphical user interface, initialize the game.
------Method Details:
---------NewGame(): create two players, create the state evaluation method, initialize the related records of the board.
---------initGame(): create the GUI and set some parameters for it. 
---------main(): start the program.
---Gomoku.java: the overall regulations and rules about Gomoku game. Field: length: determines the number of available positions in each line.
------Method Details:
---------JudgeResult: to judge whether the current state ends. Return value says its judgment. If the black player wins, it returns 1. If the white player wins, it returns -1. If the game doesn't end, it returns 0. If the result is a tie, it returns 10.
---DrawPanel.java: drawing the GUI. Each time one player put a new chess,  display it on the GUI.
------Method Details:
---------paint(): paint the board and the existing steps.
---------putchess(): whenever a player choose a chess, put it in related data structures and control to show it on the panel.
---------round(): get the relative coordinate x and y on the board of the human player's clicking action.
---------init(): mainly add a mouse-adapter to monitoring human player's clicking behavior.
---MTCT1.java: employ Monte Carlo Tree search (the third approach) for the computer player to make a decision.
------Method Details:
---------getAction():
---------runSimulation():
---------IsallExpanded():
---------IsAvailable():
---------selectOneMove():
---------addPoint():
---State.java: save related data for the real-time information of a board game. Maintain changes for recording data structures.
------Method Details:
---------initState():
---------empty():
---------addPoint():
---------putinAvailables():
---------simu_putinChess():
---Player.java: define an abstract player class. Represent the two players in the game.
---Score.java(only for the 2nd approach): define ready-made pattern to be used by the second approach.
---AI.java(not finished, only for the 2nd approach): maintain the score record and update method for each position. 
---Ai2.java(not finished, only for the 2nd approach): do the alpha-beta pruning based on the evaluated mark for each position and choose the best candidates.
---Evaluate.java(not finished, only for the 2nd approach): evaluate the situation for each point, such as whether it has a neighbor, refining its score and update its score. 

