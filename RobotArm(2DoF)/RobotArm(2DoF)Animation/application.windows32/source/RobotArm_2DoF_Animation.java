import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class RobotArm_2DoF_Animation extends PApplet {

int X;
int Y;

int armA = 100;
int armB = 100;


public void setup(){
  
  background(255);
  
  X = 0;
  Y = 0;
}

public void draw(){
  // clear
  background(255);
  
  float unsafeDistance = 0;
  if(armA>armB){
    unsafeDistance = armA-armB;
  }else if(armB>armA){
    unsafeDistance = (float) Math.sqrt(Math.pow(armB,2) - Math.pow(armA,2));
  }
  
  boolean validPoint = true;
  if(unsafeDistance > 0){
      validPoint = isInsideCircle(armA+armB) && !isInsideCircle(unsafeDistance);
  }else{
      validPoint = isInsideCircle(armA+armB);
  }
  
  double[] angles = inverse(X, Y, armA, armB);
  double thetaA = angles[0];
  double thetaB = angles[1];
  if(X<0){
    thetaA = thetaA+PI;
  }
  float endArmAx = (float) (armA * Math.cos(thetaA));
  float endArmAy = (float) -(armA * Math.sin(thetaA));
  float endArmBx = (float) (armA * Math.cos(thetaA) + armB * Math.cos(thetaA+thetaB));
  float endArmBy = (float) -(armA * Math.sin(thetaA) + armB * Math.sin(thetaA+thetaB));
    
  // show coords 
  text("X: "+X, width-50, 20);
  text("Y: "+Y, width-50, 40);
    
  // show lengths
  fill(0);
  text("Arm A: "+armA, 10, 20);
  text("Arm B: "+armB, 10, 40);
  
  // show angles
  text("Θ1: "+Math.round(Math.toDegrees(thetaA)*100.0f)/100.0f, 10, height-40);
  text("Θ2: "+Math.round(Math.toDegrees(thetaB)*100.0f)/100.0f, 10, height-20);

  
  pushMatrix();
  translate(width/2, height/2);
    
  // draw area
  stroke(0, 255, 100);
  fill(240,255,240);
  strokeWeight(5);
  ellipse(0, 0, (armA+armB)*2, (armA+armB)*2);
  
  // draw unsafe area
  if(unsafeDistance>0){
    stroke(255, 0, 0);
    fill(255,240,240);
    strokeWeight(5);
    ellipse(0, 0, unsafeDistance*2, unsafeDistance*2);
  }
  
  // draw arms
  if(validPoint){
    stroke(0);
    line(0, 0, endArmAx, endArmAy);
    line(endArmAx, endArmAy, endArmBx, endArmBy);
    fill(255);
    strokeWeight(2);
    ellipse(0,0,20,20);
    ellipse(endArmAx,endArmAy,20,20);
    ellipse(endArmBx,endArmBy,20,20);
  }
  
  // draw point
  noStroke();
  if(validPoint){
    fill(0);
  }else{
    fill(255,0,0);
  }
  ellipse(X, -Y, 10, 10);
  
  popMatrix();
}
public double[] inverse(double x, double y, double L1, double L2){
  double[] angles = new double[2];
  double theta2 = Math.acos((Math.pow(x,2)+Math.pow(y,2)-Math.pow(L1,2)-Math.pow(L2,2))/(2*L1*L2));
  double alpha = Math.atan(y/x);
  double theta = Math.asin((L2*Math.sin(theta2))/(Math.sqrt(Math.pow(x,2)+Math.pow(y,2))));
  double theta1 = alpha - theta;
  
  angles[0] = theta1;
  angles[1] = theta2;
  
  return angles;
}

public boolean isInsideCircle(double r){
  double distance = Math.sqrt(Math.pow(X, 2)+Math.pow(Y, 2));
  return distance <= r;
}

public void mousePressed(){
  X = (mouseX-width/2);
  Y = -(mouseY-height/2);
}

public void keyPressed(){
  if(key == CODED){
    switch(keyCode){
      case UP:
        if(armA+armB==width/2){
          return;
        }
        armA++;
      break;
      case DOWN:
        if(armA==50){
          return;
        }
        armA--;
      break;
      case RIGHT:
        if(armA+armB==width/2){
            return;
        }
        armB++;
      break;
      case LEFT:
        if(armB==50){
          return;
        }
        armB--;
      break;
    }
  }else{
     switch(key){
        case 'w':
          if(Y==height/2){
            return;
          }
          Y++;
        break;
        case 's':
          if(Y==-height/2){
            return;
          }
          Y--;
        break;
        case 'a':
          if(X==-width/2){
              return;
          }
          X--;
        break;
        case 'd':
          if(X==width/2){
            return;
          }
          X++;
        break;
     }
  }
}
  public void settings() {  size(500, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "RobotArm_2DoF_Animation" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
