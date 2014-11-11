/*
This is as basic as it gets.  If you can't get this running, 
something is not quite right.
*/

import wordcram.*;

WordCram wordcram;

void setup() {
size(700, 400);
background(255);


// This code will print all the lines from the source text file.
String[] lines = loadStrings("test.txt");
String[] data = split(lines[0],';');
println("there are " + lines.length +data.length+ " lines");
println(data);
// Each Word object has its word, and its weight.  You can use whatever
// numbers you like for their weights, and they can be in any order.
Word[] wordArray = new Word[data.length/2];
//Word[] wordArray = new Word[data.length/2+1];

int ii=0;
int jj=0;
while(ii<data.length-1){
  Word word = new Word(data[ii],Integer.parseInt(data[ii+1]));
  wordArray[jj]=word;
  ii=ii+2;
  jj++;
}
 //Word word = new Word(data[0],Integer.parseInt(data[1]));
  //wordArray[0]=word;
  //Word words = new Word("test2",99);
  //wordArray[1]=words;

// Pass in the sketch (the variable "this"), so WordCram can draw to it.
wordcram = new WordCram(this)

// Pass in the words to draw.
  .fromWords(wordArray)
  .angledAt(0);
}

void draw() {
// Now we've created our WordCram, we can draw it:
  if (wordcram.hasMore()) {
    wordcram.drawNext();
  }
  else {
    println("done");
    noLoop();
  }
}

