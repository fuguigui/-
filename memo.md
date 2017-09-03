the whole process:
initialize the game:
---draw the whole panel without chess
------call the DrawPanel's draw function with the chess list empty. 
---select the model and draw the panel again.
------pop out a window asking the user to choose the model: random, to be the black or to be the white.
---------if the computer is the black, the chess is put on the center of the panel in default.
------judge whether to draw again: if the computer is the black, then redraw the panel, vice verse.
----------how to judge where to draw what color based on the "Record" data structure?
User VS Computer
---user puts a chess on the panel, actually put in the global record data structure and call the DrawPanel.redraw function.
---computer gets the scores of the alternative locations and chooses the one with highest score and put on the panel.
------preparation: given a root state: if it has children: choose the most advantage child, do a new evaluation;
------otherwise, expand it by each possible child, and quickly simulate each child once, update each child's score and back to update their father's score. 
---------functions: expand(root) return null;  
--------------------task: given a root, update its own score. If it is unexpanded, add all of its children, each doing one quick imitation and update their and its record.
--------------------------if it has been expanded, choose the best child, do expand and then update its score, according to the recorded winning rounds.
-------------------quicksimulation(root-record) return the winning color; given a record, put the other color's chess; if the game finishes, return the winning color; else repeat the simulation with new record.
--------------------------------how to put the chess? heuristic rules.
---------data: node hash table: each time in expand function about an unexpanded root, generate new nodes and put them into a hash table. 
-----------------------------Each node saves info includes: the total rounds, the winning rounds, the losing rounds, the color, a list save the tokens of its child, a token for its parents.

---the "third party" judge whether the game is over.
End the game
---pop out a window to show the result of the game and ask what the player wants to do next--exit or start a new game?