# HABI UI Test Plan

## Test case: manage typed tasks

**Aim:** Verify creation, listing, marking, and unmarking of todo, deadline, and event tasks.

**Input**
```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 2
list
unmark 2
list
bye
```

**Expected output**
```text
____________________________________________________________
 _   _    _     ____   ___ 
| | | |  / \   | __ )   |  |
| |_| | / _ \  |  _ \  |  |
|  _  |/ ___ \ | |_) | |  |
|_| |_|_/   \_\|____/  _|_
Hello! I'm HABI.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
