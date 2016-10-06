# Command line for gcc is almost copied-and-pasted from "glibconfig.h no such file or directory" answer
# (http://stackoverflow.com/a/17360728/4672928), but "gtk+-3.0" is used instead of "glib-2.0".
#
# The order of GTK-specific flags is important. For more details please look at 
# "Re: How to compile tictactoe widget example in tutorial" e-mail 
# (https://mail.gnome.org/archives/gtk-app-devel-list/2010-February/msg00035.html)

GTK_VERSION=gtk+-3.0
GTK_CFLAGS=`pkg-config --cflags $(GTK_VERSION)`
GTK_LIBS=`pkg-config --libs $(GTK_VERSION)`

application:
	gcc -Wall -g $(GTK_CFLAGS) application.c -o application $(GTK_LIBS)

hook:
	gcc -Wall -g -shared -ldl -fPIC $(GTK_CFLAGS) libnemohook.c -o libnemohook.so $(GTK_LIBS)

test: application
	./application

test-hook: application hook
	LD_PRELOAD=./libnemohook.so ./application

clean:
	rm application
	rm libnemohook.so
