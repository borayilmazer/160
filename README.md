# 160

This repository contains projects completed by me for the CMPE160 course in Boğaziçi University. CMPE160 serves as an introduction to object oriented programming, and tries to tackle more real world problems.

This repository contains three Java projects developed as part of the CMPE 160 course. Each project explores different problem-solving techniques, including physics-based simulations, graph-based navigation, and optimization algorithms.

---

## Projects  

### ** Angry Bullet Game**  
A physics-based game inspired by **Angry Birds**, where a bullet is launched to hit targets while avoiding obstacles. The game follows projectile motion physics and allows user interaction to adjust angle and speed. The game is implemented using the `StdDraw` graphics library.

🔹 **Features:**  
- Adjustable launch angle and speed  
- Simulation of projectile motion  
- Collision detection with obstacles and targets  
- Interactive controls:  
  - **Up/Down keys**: Adjust angle  
  - **Left/Right keys**: Adjust speed  
  - **Space bar**: Fire the bullet  
  - **R key**: Restart the game  

🔹 **How to Run:**  
```sh
javac -cp lib/stdlib.jar src/*.java
java -cp lib/stdlib.jar:src Main
