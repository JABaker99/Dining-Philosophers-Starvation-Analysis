# Dining Philosophers – Starvation & Fairness Study

## Overview 
This project explores starvation and fairness in concurrent systems through three Java implementations of the classic Dining Philosophers problem. Each version introduces progressively stronger synchronization strategies, and execution data is collected and visualized using box-and-whisker plots to objectively evaluate how starvation emerges and how it is mitigated.

The goal is not only correctness (deadlock avoidance) but also fair resource access under contention.

## Implementations
### Version 1 – Naïve Locking
 - Uses basic mutual exclusion without fairness guarantees.
 - Demonstrates starvation due to thread barging and uneven scheduling.

### Version 2 – Fair Locks + Timed Acquisition
 - Uses fair ReentrantLock objects and tryLock with timeouts.
 - Reduces starvation significantly but does not strictly enforce ordering.

### Version 3 – Semaphore Arbitrator (Fair)
 - Introduces a fair semaphore (N-1 permits) acting as a waiter.
 - Enforces FIFO-style access and eliminates starvation by design.

## Data Collection
For each philosopher:
 - Eating Time (nanoseconds): how long a philosopher spends eating
 - Blocked Time (nanoseconds): time spent waiting to acquire resources

## Data Results/Visuals
### Ninjas 2
<img src="stats_analysis_Images/Ninjas%202%20Eating%20Chart.png" width="45%">
<img src="stats_analysis_Images/Ninjas%202%20Blocked%20Chart.png" width="45%">

### Ninjas 3
<img src="stats_analysis_Images/Ninjas%203%20Eating%20Chart.png" width="45%">
<img src="stats_analysis_Images/Ninjas%203%20Blocked%20Chart.png" width="45%">


## How to Run

### Compile
From the project root:
 - javac shared/*.java ninja1/*.java ninja2/*.java ninja3/*.java

### Run any of the single commands
 - java ninja1.DiningPhilosophers
 - java ninja2.DiningPhilosophers
 - java ninja3.DiningPhilosophers

