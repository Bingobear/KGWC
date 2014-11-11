/*
This is as basic as it gets.  If you can't get this running, 
something is not quite right.
*/

import wordcram.*;
import java.awt.*;

WordCram wordcram;

void setup() {
  
  //IMAGE - Be careful size has to be at least as big as underlying image
PImage image = loadImage("Text.gif");
Shape imageShape = new ImageShaper().shape(image, #000000);
ShapeBasedPlacer placer = new ShapeBasedPlacer(imageShape);
//size(1024, 860);
size(image.width, image.height);
background(255);


// This code will print all the lines from the source text file.
String[] lines = loadStrings("test.txt");
String[] data = split(lines[0],';');
/*println("there are " + lines.length +data.length+ " lines");
println(data);*/
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

// Pass in the sketch (the variable "this"), so WordCram can draw to it.
wordcram = new WordCram(this).

//image properties
    withPlacer(placer).
    withNudger(placer).
    //Weight relation - size
    sizedByWeight(7, 40).
    withColor(#000000)
    // Pass in the words to draw.
  .fromWords(wordArray)
  //Define angle
  .angledAt(0)
  ;
}

void draw() {
  /*SAVE AS SVG
  try {
  new WordCram(this)
    .toSvg("text.svg", width, height)
    .fromWebPage("http://nytimes.com")
    .drawAll();
}
catch (java.io.FileNotFoundException x) {
  println(x);
}*/
  
// Now we've created our WordCram, we can draw it:
wordcram.drawAll();
//fill tagcloud word by word
  /*if (wordcram.hasMore()) {
    wordcram.drawNext();
  }
  else {
    println("done");
    noLoop();
  }*/
}

