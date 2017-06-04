# WumpusWorld
A classical scenario in machine learning from Russell and Norvig "Artificial Intelliegence A Modern Approach Third Edition" (2010)

# Scope
Implement a solution to the Wumpus World problem as defined by Russell and Norvig. The complete description can be found in section 7.2,
page 236 of their text.

# Approach 
There are a few techniques I would like to explore using this problem. So I will start with the most basic and add increasingly
sophisticated techniques to explore their impact on the solution.
- Q-Learning implemented with a basic data table.
- Q-Learning with a neural network replacing the data table.
- Use a genetic algorithm to evolve the hyper paramenters of a population of Adventurers.

For the maze I will start with a default fixed set of mazes for debuging and regression testing. I will then explore a range of problems;'
- Random pit placement.
- Random gold placement.
- Random Wumpus placement
- Random entrance placement.
- Random exit placement.
- Random pit and gold placement.
- Random pit, gold and Wumpus placement.
- Random pit, gold, Wumpus and entrance placement.
- Random pit, gold, Wumpus, extrance and exit placement.

# Why
For the same reason we always solve training problems, to learn by doing. It has been a long time since I did AI studies at Uni and this
is a nice problem to warm up on before I move on to other things. I've chosen to use Java as this is the language I'm most familiar with
these days. I've coded in over a dozen languages over the years but for this I see no reason to use any of the other languages. TensorFlow
and Python are popular choices and I will include links to implementations I found helpful in the related links section. 
I remember when high performance computing was done exclusively in C/C++ for performance reasons. That we can waste cycles by using
interpreted languages in high performance computing goes to show just how much compute performance we have available now that we can
use "wasteful" languages. Of course, compiliers have become increasingly sophisticated which mitigates the performance hit somewhat also.

# Links To Works I Found Helpful
http://mnemstudio.org/path-finding-q-learning-tutorial.htm 
https://en.wikipedia.org/wiki/Q-learning 
https://github.com/outlace/Gridworld 
