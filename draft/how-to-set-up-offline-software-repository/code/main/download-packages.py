#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import argparse
import os
import sys
import subprocess


#apt-get download <имя_пакета>=<версия> и apt-get source <имя_пакета>=<версия>

# TODO Copied-and-pasted from `collect-packages.py'
# TODO Extract into common settings
class Settings:
	@staticmethod
	def logging_enabled():
		return False

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
##########################


def lines_from_file(file_descriptor):
	raw_lines = file_descriptor.readlines()
	return filter(
		None, 
		[line.strip() for line in raw_lines]
	)


def packages_from_lines(lines):
	result = []
	for line in lines:
		name_and_version = line.split()
		name = name_and_version[0]
		version = name_and_version[1]
		packages.append(
			{
				'name' : name,
				'version' : version
			}
		)
	return result


def all_packages(file_descriptor):
	return logged(
		packages_from_lines(
			lines_from_file(file_descriptor)
		)
	)


def already_downloaded_packages(working_directory):
	path_to_progress_file = os.path.join(working_directory, 'progress.txt')
	with open(path_to_progress_file, 'r', Settings.default_encoding()) as progress_file:
		return logged(
			packages_from_lines(
				lines_from_file(progress_file)
			)
		)


def dump(package):
	print(
		'{} {}'.format(package[name], package[version])
	)


def download(packages):
	for package in packages:
		dump(package)
		

def download_binary():
	pass


def download_source():
	pass


class AptGetCommand:
	@staticmethod
	def download(package_name):
		return [
			'apt-get',
			'download',
			'{}={}'.format(package_name, package_version)
		]

	@staticmethod
	def source(package_name, package_version):
		return [
			'apt-get',
			'source',
			'{}={}'.format(package_name, package_version)
		]


### TODO copied-and-pasted
class MultilineFormatter(argparse.HelpFormatter):
	'''
	This snippet is nearly copied and pasted from the answer on 
	"Python argparse: How to insert newline in the help text?" question 
	(http://stackoverflow.com/questions/3853722/python-argparse-how-to-insert-newline-in-the-help-text).
	'''
	def _fill_text(self, text, width, indent):
		text = self._whitespace_matcher.sub(' ', text).strip()
		paragraphs = text.split('|n ')
		multiline_text = ''
		for paragraph in paragraphs:
			formatted_paragraph = textwrap.fill(
				paragraph, 
				width, 
				initial_indent=indent, 
				subsequent_indent=indent
			) + '\n\n'
			multiline_text = multiline_text + formatted_paragraph
		return multiline_text


class CmdArgs:
	'''
	We obtain the formatter from the instance of `argparse.ArgumentParser`. So we can format error messages in the same
	manner as `argparse` does this (text wrapping, breaking down into paragraphs, etc).
	'''
	def __init__(self, parser):
		args = vars(parser.parse_args())
		self.input_file = args['input_file']
		self.output_directory = args['output_directory']
		self.formatter = parser._get_formatter()
		
	def exit(self, error_message):
		error_marker = termcolor.colored('ERROR', 'red', 'on_white')
		raw_text = error_marker + '\n|n\n' + error_message
		formatted_text = self.formatter._format_text(raw_text)
		eprint(formatted_text)
		exit(1)
		
	def input_file_and_output_directory_should_not_be_same(self):
		'''
		Yes, it's not mandatory to do this check at all. But otherwise you can get mysterious error when considering 
		output directory as file. So it's much better to prevent such situation.
		'''
		if os.path.samefile(self.input_file, self.output_directory):
			self.exit('Input file and output directory point to the same object. This is simply senselessly.')
		return self
		
	def input_file_should_exist(self):
		if not os.path.exists(self.input_file):
			self.exit('Input file [{}] does not exist.'.format(self.input_file_name))
		return self
		
	def input_file_should_be_regular(self):
		if not os.path.isfile(self.input_file):
			self.exit(
				'''Input file [{}] is not a regular file. Probably, you've specified not a file but a path to 
				directory...
				'''.format(self.input_file_name)
			)
		return self
		
	def input_file_should_be_readable(self):
		if not os.access(self.input_file, os.R_OK):
			self.exit(
				'''Input file [{}] is not accessible for reading. Please check out permissions on the file.
				'''.format(self.input_file)
			)
		return self
		
	def output_directory_should_exist(self):
		if not os.path.exists(self.output_directory):
			self.exit(
				'''Output directory [{}] should exist. Please create it manually and run the script again (or just 
				specify already existing directory).
				'''.format(self.output_file_name)
			)
		return self
		
	def output_directory_should_be_a_real_directory(self):
		if not os.path.isdir(self.output_directory):
			self.exit(
				'''Output directory [{}] should be a directory really. Seems that you've specified a file.
				'''.format(self.output_file_name))
		return self
		
	def output_directory_should_be_writeable(self):
		if not os.access(os.path.dirname(self.output_directory), os.W_OK):
			self.exit(
				'''Output directory [{}] is not accessible for writing. Please check the permissions.
				'''.format(self.output_file_name)
			)
		return self
		
	def output_directory_should_be_empty(self):
		# TODO it's inefficient in general, but it's OK for now
		if not (os.listdir(self.output_directory) == []):
			self.exit(
				'''Output directory [{}] should be empty.
				'''.format(self.output_file_name)
			)
		return self

##########################


def main():
	parser = argparse.ArgumentParser(
		description = '''
			Something is here.
			|n
			And here..''',
		formatter_class = MultilineFormatter
	)
	parser.add_argument(
		'input_file', 
		type = str, 
		help = '''
			some description'''
	)
	parser.add_argument(
		'output_directory', 
		type = str, 
		help = '''
			some description'''
	)
	args = (
		CmdArgs(parser).
		input_file_and_output_directory_should_not_be_same().
		input_file_should_exist().
		input_file_should_be_regular().
		input_file_should_be_readable().
		output_directory_should_exist().
		output_directory_should_be_a_real_directory().
		output_directory_should_be_writeable().
		output_directory_should_be_empty()
	)
	eprint(args.input_file)
	eprint(args.output_directory)


if __name__ == '__main__':
	main()	