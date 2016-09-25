application:
	# Command line for gcc is almost copied-and-pasted from "glibconfig.h no such file or directory" answer
	# (http://stackoverflow.com/a/17360728/4672928), but "gtk+-3.0" is used instead of "glib-2.0".
	#
	# The order of gcc flags is important. For more details please look at 
	# "Re: How to compile tictactoe widget example in tutorial" email 
	# (https://mail.gnome.org/archives/gtk-app-devel-list/2010-February/msg00035.html)
	gcc -Wall -g `pkg-config --cflags gtk+-3.0` application.c -o application `pkg-config --libs gtk+-3.0`

run: application
	./application

clean:
	rm application
