## Solution
The game logic is in the controller package. 

If we want to change the rules such that flush beats four-of-a-kind we just need to change the ranks in the `HandType` 
enum and the order of the `handTypeOrders` list of the `PokerHandController` class.