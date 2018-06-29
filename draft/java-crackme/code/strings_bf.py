#!/usr/bin/env python3

# -*- coding: utf-8 -*-

import subprocess
import sys

def default_encoding():
	return 'utf-8'

def app_class_file():
	return 'App.class'

def message_after_entering_invalid_key():
	return "Sorry, the serial you've entered is invalid."

def strings():
	strings_command = [
		'strings',
		'--all',
		'--bytes=3',
		app_class_file()
	]
	raw_output = subprocess.check_output(
		strings_command,
		stdin = subprocess.DEVNULL, 
		stderr = sys.stdout
	)
	decoded_output = str(
		raw_output, 
		default_encoding()
	)
	return decoded_output.splitlines()

class JavaApplication:
	def start(self):
		class_name = app_class_file().split('.')[0]
		print(class_name)
		application_command = [
			'java',
			class_name	
		]
		self.app_process = subprocess.Popen(
			application_command, 
			stdin = subprocess.PIPE,
			stderr = sys.stdout
		)
	
	def send_key(self, key):
		key_data = bytes(
			key, 
			default_encoding()
		)
		self.app_process.communicate(key_data)
	
	def get_result(self):
		with self.app_process.stdout as output:
			print(output.readlines())

def fit_a_key(keys):
	for key in keys:
		invoke_application()

def main():
	#strings()
	app = JavaApplication()
	app.start()
	app.send_key("qwerty")
	app.get_result()
	

if __name__ == '__main__':
	main()