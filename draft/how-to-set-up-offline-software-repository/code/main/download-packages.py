#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import argparse
import os
import sys
import subprocess


class Settings:
	@staticmethod
	def logging_enabled():
		return True

	@staticmethod
	def default_encoding():
		return 'utf-8'


def eprint(*args, **kwargs):
	"""
	This function is copied-and-pasted from 
	[How to print to stderr in Python?](https://stackoverflow.com/questions/5574702/how-to-print-to-stderr-in-python).
	"""
	print(*args, file=sys.stderr, **kwargs)


def logged(obj, message = None):
	"""
	Prints object and message specified and then simply returns the object.
	
	This function is indended for debugging. At the same time it helps to avoid dummy local variables and keep 
	functional programming style going.
	"""	
	if Settings.logging_enabled():
		message =	'{}: [{}]'.format(message, obj) if message else \
					'{}'.format(obj)
		eprint(message)
	return obj


class AptGetCommand:
	@staticmethod
	def source(package_name):
		return [
			'apt-get',
			'source',
			'{}'.format(package_name)
		]	
	
	@staticmethod
	def download(package_name):
		return [
			'apt-get',
			'download',
			'{}'.format(package_name)
		]


def lines_from_file(file_descriptor):
       raw_lines = file_descriptor.readlines()
       return filter(
               None, 
               [line.strip() for line in raw_lines]
       )


def ignorable_apt_get_error():
	return 'E: Unable to find a source package for'


def download_source(package_name):
	process = subprocess.Popen(
		AptGetCommand.source(
			logged(package_name, 'Downloading source for package')
		), 
		stdin = subprocess.DEVNULL,
		stdout = subprocess.PIPE,
		stderr = subprocess.PIPE
	)
	out, err = process.communicate()
	error_text = str(err, 'utf-8')
	if ignorable_apt_get_error() not in error_text:
		exception_message = 'Failed to download source for package [{}] due to [{}].'.format(
			package_name,
			error_text
		)
		raise Exception(exception_message)	

	
def download_deb(package_name):
	subprocess.check_output(
		AptGetCommand.download(
			logged(package_name, 'Downloading .deb-file for package')
		),
		stdin = subprocess.DEVNULL
	)


def main():
	packages = lines_from_file(sys.stdin)
	for package in packages:
		download_source(package)
		download_deb(package)


if __name__ == '__main__':
	main()