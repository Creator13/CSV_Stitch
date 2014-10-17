CSV Stitch
==========

This is a program to merge a lot of comma-separated files (CSV) into one. It can be usefull when you have some sort of logger that pushes out a new file every day, and you don't want to import them in a CSV reader one by one.

The program can work in four different header copying modes. These are useful when the logger outputs a header (a line that describes the columns below) on every file. The four modes are:
 - Copy all headers: This method is useful when copying files that have no header, or if you don't care about it. It will copy every line of every file.
 - First file's header: This will use the header of the first file as header of the merged file. In every next file, the first line will not be copied. This method is usefull when you are sure that all files follow the same format.
 - Remove headers: This method will not copy any of the headers of the file. This means that the first line of every file will not be copied.
 - Custom header: This option allows you to provide a custom header for your new file. The first line of every file will still be deleted.

This program is made for educational purpose, and mostly for my own fun. You are free to do whatever you want with the source code, but please respect the rights of the owner: do not copy the code and write you own name under it, and always credit the owner. If you follow those rules you can do with it what you want.
