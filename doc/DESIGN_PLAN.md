# OOLALA Design Plan
## Names
- Justin Jang (jbj30)
- Wei(David) Wu (ww148)
- Andrew Peterson (ajp77)

### Team Roles and Responsibilities

Wei Wu:
- researching each aspect and seeing what Turtle needs from each individual project
- LOGO IDE

Justin Jang: 
- starting backend of turtle

Andrew Peterson:
- starting frontend of turtle

### High Level Design



### CRC Card Classes

This class's purpose or value is to represent a Turtle object:

|Turtle| |
|---|---|
|void right(int degree)||
|void left(int degree)||
|void forward(int steps)||
|void backward(int steps)||


This class's purpose or value is to represent a Turtle object:
```java
public class Turtle {
     // turns right by an indicated number of degrees
     public void right (int degree)
     // turns left by an indicated number of degrees
     public void left (int degree)
     // moved forward by an indicated number of steps
     public void forward (int steps)
     // moved backward by an indicated number of steps
     public void backward (int steps)
 }
 ```
 

### Use Cases 

 * The user types 'fd 50' in the command window, sees the turtle move in the display window leaving a trail, and has the command added to the environment's history.
```java
int steps = 50;
comm = new Command("f", steps);
execute(comm);
draw(comm);
history.add(getLogs(comm));
```

 * The user loads a file of commands, sees the turtle move in the display window leaving a trail, and has the command added to the environment's history.
```java

List<Command> commandList = readFile(commands);
for (c: commandList) {
    execute(c);
    draw(c);
    history.add()(getLogs(comm));
}
```

 * The user types '50 fd' in the command window and sees an error message that the command was not formatted correctly.
```java
int steps = 50;

// constructor throws an IllegalStatementException
comm = new Command(steps, "f");

```

 * The user clicks in the display window to set a new Home location.
```java
Home newHome = getInputHome();
setHome(newHome);
```

 * The user changes the color of the environment's background.
```java
changeColor();
```

