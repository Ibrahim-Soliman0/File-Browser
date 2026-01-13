File Browser Application – JavaFX

This project is a simple File Browser application implemented in two different approaches to demonstrate how threading affects performance.

Implementations
1. JavaFX Application Thread Only

All directory traversal and UI updates are executed on the JavaFX Application Thread.

This approach is simple but can cause UI freezing when browsing large or deeply nested directories.

Used as a baseline to highlight the limitations of single-threaded UI work.

2. ForkJoinPool + JavaFX Application Thread

The directory tree is calculated using a ForkJoinPool in the background.

UI updates are performed safely on the JavaFX Application Thread.

This approach keeps the UI responsive even when processing large directory structures and saves alot of time.
