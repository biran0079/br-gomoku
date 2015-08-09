package player.minmax;

/**
 * Different types of stones in a pattern.
 */
enum MoveType {
  X,  // moves that makes the pattern
  D1, // most preferred defensive move
  D2, // less preferred defensive move
  E   // useless position
}
