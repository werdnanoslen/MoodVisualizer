import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Mood extends PApplet {

ArrayList ellipses = new ArrayList();
PVector pos = new PVector(0, 0);
PVector dim = new PVector(0, 0);
char lastKey;
long firstStrike = 0, nextStrike, dStrike;
int thFast, thMedium, step, winWidth = 500, winHeight = 500;
float randX, randY, randL, randW, alpha;
boolean begin = true, fade;
String mood;
int urgency;
PImage lastBackground;

public void setup(){
  size(winWidth,winHeight);
  noStroke();
  frameRate(300);
}

public void draw(){ 
  if (begin){
    background(30, 30, 30);
    fill(220, 220, 220);
    textAlign(CENTER);
    text("Bored? Tap your fingers on the keyboard for a visualization", winWidth/2, winHeight/2);
  } else{
    background(lastBackground);
  }
  
  if (fade){
    step++;
    alpha = map(100-step, 0, 100, 0, 255);
    if (alpha==0){
      fade = false;
      step = 0;
    }
  }
  
  fill(urgency, alpha);
  pos.x = randX;
  pos.y = randY;
  dim.x = randL;
  dim.y = randW; 
  
  for(int i = 0; i < ellipses.size(); i++){
    PVector pt = (PVector)ellipses.get(i);
    ellipse(pos.x, pos.y, pt.x, pt.y);
  }
  
  ellipse(pos.x, pos.y, dim.x, dim.y);
  
  if (mood=="bored"){
    line(pos.x, pos.y, dim.x, dim.y);
  } else if (mood=="impatient"){
    //something
  }
}

public void keyPressed(){
  if (key==BACKSPACE){
    fill(30, 30, 30);
    rect(0, 0, 500, 500);
  }
  
  begin = false;
  lastBackground = get(0, 0, 500, 500);
  
  nextStrike = System.currentTimeMillis();
  dStrike = nextStrike - firstStrike;
  firstStrike = nextStrike;
  
  if (key==lastKey){
    mood = "bored";
    thFast = 220; //max threshold of fast tap speed
    thMedium = 420; //max threshhold of medium tap speed
  } else{
    mood = "impatient";
    thFast = 40;
    thMedium = 100;
  }  
  
  lastKey = key;
  
  if (dStrike < thFast){
    urgency = color(map(dStrike, 0, thFast, 0, 255), 0, 0); //fast tapping
  } else if (dStrike >= thFast && dStrike < thMedium){
    urgency = color(0, map(dStrike, thFast, thMedium, 0, 255), 0); //medium tapping
  } else{
    urgency = color(0, 0, map(dStrike, thMedium, 500, 0, 255)); //slow tapping
  }
  
  if (key==BACKSPACE){
    randX = (float)winWidth/2;
    randY = (float)winHeight/2;
    randW = (float)(winWidth*Math.sqrt(2));
    randL = (float)(winHeight*Math.sqrt(2));
    urgency = color(255, 255, 255); //flash to signify reset
  } else{
    randX = (float)Math.random()*winWidth;
    randY = (float)Math.random()*winHeight;
    randW = (float)Math.random()*winWidth - randX;
    randL = (float)Math.random()*winHeight - randY;
  }
  
  fade = true;
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "Mood" });
  }
}
