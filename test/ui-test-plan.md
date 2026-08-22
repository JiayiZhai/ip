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

## Test case: recover from invalid commands

**Aim:** Verify invalid commands report useful errors, preserve the task list, and allow the session to continue.

**Input**
```text

todo
deadline return book
event project meeting /from Mon 2pm
mark two
mark 1
todo read book
mark 2
unmark 0
blah
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
OOPS! Please enter a command.
____________________________________________________________
____________________________________________________________
OOPS! The todo description cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS! Use: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________
____________________________________________________________
OOPS! Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
OOPS! The task number must be a whole number.
____________________________________________________________
____________________________________________________________
OOPS! Task number 1 is out of range.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
OOPS! Task number 2 is out of range.
____________________________________________________________
____________________________________________________________
OOPS! Task number 0 is out of range.
____________________________________________________________
____________________________________________________________
OOPS! I don't know what "blah" means.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: delete a task safely

**Aim:** Verify deletion removes the selected task, renumbers the list, and rejects an out-of-range task number.

**Input**
```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon /to Tue
delete 2
delete 5
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
  [E][ ] project meeting (from: Mon to: Tue)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS! Task number 5 is out of range.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
