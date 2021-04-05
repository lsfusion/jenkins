---
title: 'Empty instruction'
---

*Empty instruction* - a special instruction that consists of a single semicolon.

### Syntax

    ;

### Description

An empty instruction is intended to prevent extra semicolons from being diagnosed as an error. For example, instructions in which the last character is a closing brace should not end with a semicolon. If a semicolon is inserted, however, no error will be thrown, since this will be interpreted as two different instructions. 

### Example


```lsf
CLASS Result {
    yes 'Yes',
    no 'No'
};  // semicolon is not needed here, but its presence is not an error

;;;; // this is valid lsFusion code
```

  
