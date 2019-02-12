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

class JavaApplication:
	def start(self):
		class_name = app_class_file().split('.')[0]
		application_command = [
			'java',
			class_name	
		]
		self.app_process = subprocess.Popen(
			application_command, 
			stdin = subprocess.PIPE,
			stdout = subprocess.PIPE,
			stderr = sys.stdout
		)
	
	def enter_key(self, key):
		key_data = bytes(
			key, 
			default_encoding()
		)
		output, errorsIgnored = self.app_process.communicate(key_data)
		return output.decode(default_encoding())

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

def failed(result):
	return message_after_entering_invalid_key() in result

def fit_a_key(candidates):
	for candidate in candidates:
		app = JavaApplication()
		app.start()
		result = app.enter_key(candidate)
		if not failed(result):
			key_found_message = 'String [{}] is valid serial number.'.format(candidate)
			print(key_found_message)

def main():
	all_keys = strings()
	fit_a_key(all_keys)

if __name__ == '__main__':
	main()