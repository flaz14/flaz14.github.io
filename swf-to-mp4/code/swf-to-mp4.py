#!/usr/bin/env python3

# -*- coding: utf-8 -*-

import sys
import subprocess
import re
import time
import argparse
import os
import textwrap
import termcolor

'''
We assume that all third-party programs will consume input and produce output in certain encoding. utf-8 is pretty 
enough.
'''
DEFAULT_ENCODING = 'utf-8'

'''
Perhaps, it will be better do not hardcode the number of virtual server. But looking up for free number is 
complicated task (in popular tools finding free server number is done via a kind of nmap-scanning).
'''
SERVER_NUMBER = ':44'

def explore_swf_file(filename):
	swfdump_command = [
		'swfdump', 
		'--width', 
		'--height', 
		'--rate', 
		filename
	]
	raw_info = subprocess.check_output(
		swfdump_command, 
		stdin = subprocess.DEVNULL, 
		stderr = sys.stdout
	)
	decoded_info = str(raw_info, DEFAULT_ENCODING)
	parsed_info = (
		re.compile('-X (?P<width>\d+) -Y (?P<height>\d+) -r (?P<rate>\d+\.\d+)', re.S).
		search(decoded_info).
		groupdict()
	)
	swf = {
		'width': int(parsed_info['width']), 
		'height': int(parsed_info['height']), 
		'rate' : int(float(parsed_info['rate']))
	}
	return swf
	
class Screen:
	def __init__(self, swf, filename):
		self.swf = swf
		xvfb_command = [
			'Xvfb',
			SERVER_NUMBER,
			'-screen', '0', '{}x{}x24'.format(self.swf['width'], self.swf['height'])
		]
		self.xvfb = subprocess.Popen(
			xvfb_command, 
			stdin = subprocess.DEVNULL, 
			stdout = sys.stdout, 
			stderr = sys.stdout
		)
		gnash_command = [
			'gnash',
			'--once',
			'--x-pos', '0',
			'--y-pos', '0',
			'--width', str(self.swf['width']),
			'--height', str(self.swf['height']),
			#'--fullscreen', # TODO write a comment about why '--fullscreen' switch is not used
			'--hide-menubar',
			'--verbose',
			filename
		]
		self.sleep_safely()
		self.gnash = subprocess.Popen(
			gnash_command, 
			env = {'DISPLAY': SERVER_NUMBER},
			stdin = subprocess.DEVNULL,
			stdout = sys.stdout,
			stderr = sys.stdout
		)
		
	def start_playing(self):
		xdotool_command = [
			'xdotool',
			'mousemove', '0', '0',
			'mousemove', str(self.swf['width'] - 20), str(self.swf['height'] - 20),
			'click', str(1)
		]
		self.sleep_safely()
		subprocess.check_call(
			xdotool_command, 
			env = {'DISPLAY': SERVER_NUMBER}, 
			stdin = subprocess.DEVNULL,
			stdout = sys.stdout,
			stderr = sys.stdout
		)
		
	def start_capture(self, output_file_name):
		ffmpeg_command = [
			'ffmpeg',
			'-f', 'x11grab',
			'-video_size', '{}x{}'.format(self.swf['width'], self.swf['height']),
			'-i', '127.0.0.1' + SERVER_NUMBER, 
			'-codec:v', 'libx264',
			'-r', str(self.swf['rate']),
			'-f', 'mp4',
			output_file_name
		]
		self.sleep_safely()
		self.ffmpeg = subprocess.Popen(
			ffmpeg_command, 
			stdin=subprocess.PIPE,
			stdout=sys.stdout, 
			stderr = sys.stdout
		)
	
	def wait_until_playing_is_finished(self):
		self.gnash.wait()
		ffmpeg_quite_keystroke = bytes('q', DEFAULT_ENCODING)
		self.ffmpeg.communicate(ffmpeg_quite_keystroke)
		self.ffmpeg.wait()
	
	def sleep_safely(self):
		'''
		We need this nasty delay. Generally, it's not easy to know whether a program is alive or it's really ready to 
		work. For example, `Xvfb` starts quickly (e.g. you can see corresponding PID). But it's not ready to work 
		immediately (e.g. some initializations can be still in progress). Yes, it's possible to 'ping' a virtual 
		display. But this approach is not applicable for other applications (like `Gnash`). So it will be better to 
		simply wait for several seconds (just for consistency accross different third-party applications).
		'''
		time.sleep(10)
	
	def __del__(self):
		'''
		There is no need to wait for the process after it's killed. But waiting helps to keep output of the script 
		accurate. Without the pause user's terminal will be polluted by Xvfb messages (the messages will be printed 
		below a command line prompt).
		'''
		self.xvfb.kill()
		self.xvfb.wait()
	
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
	We obtain the formatter from the instance of `argparse.ArgumentParser`. So we can format error message in the same
	way as `argparse` does this (text wrapping, paragraphs, etc).
	'''
	def __init__(self, parser):
		args = vars(parser.parse_args())
		self.input_file_name = args['input_file']
		self.output_file_name = args['output_file']
		self.formatter = parser._get_formatter()
		
	def exit(self, error_message):
		error_marker = termcolor.colored('ERROR', 'red', 'on_white')
		raw_text = error_marker + '\n|n\n' + error_message
		formatted_text = self.formatter._format_text(raw_text)
		print(formatted_text)
		exit(1)
	
	def input_and_output_files_should_not_be_same(self):
		'''
		Perhaps, the better way to check that two file names point to the same instance is `os.path.samefile()`. But we 
		don't use it because the file should exist before the function is called. So we just compare normalized absolute
		paths.
		
		Yes, it's not mandatory to do this check at all. Without the check you will get `Output file already exists` 
		error (because `output_file_name` will point to the input file which already exists). But it will be better to 
		spot the situation with the same file in advance for the sake of clean error message (obviously, using the same
		source and destination is generally wrong).
		'''
		input_path = os.path.abspath(os.path.normpath(self.input_file_name))
		output_path = os.path.abspath(os.path.normpath(self.output_file_name))
		if input_path == output_path:
			self.exit('Input and output files represent the same instance.')
		return self
			
	def input_file_should_exist(self):
		if not os.path.exists(self.input_file_name):
			self.exit('Input file [{}] does not exist.'.format(self.input_file_name))
		return self
			
	def input_file_should_be_regular(self):
		if not os.path.isfile(self.input_file_name):
			self.exit(
				'''Input file [{}] is not a regular file. Probably, you've specified not a file but a path to 
				directory...
				'''.format(self.input_file_name)
			)
		return self
	
	def input_file_should_be_readable(self):
		if not os.access(self.input_file_name, os.R_OK):
			self.exit(
				'''Input file [{}] is not accessible for reading. Please check out permissions on the file.
				'''.format(self.input_file_name)
			)
		return self
		
	def output_file_should_not_exist(self):
		if os.path.exists(self.output_file_name):
			self.exit(
				'''Output file [{}] already exists. Please delete it manually and run the script again.
				'''.format(self.output_file_name))
		return self
		
	def output_file_should_be_writeable(self):
		if not os.access(os.path.dirname(self.output_file_name), os.W_OK):
			self.exit(
				'''Output file [{}] is not accessible for writing. Please check permissions on the file and/or 
				corresponding directory'''.format(self.output_file_name)
			)
		return self
	
def main():
	parser = argparse.ArgumentParser(
		description = '''
			This script helps to convert SWF video file into MP4 with aid of some dirty methods (e.g. playing on virtual
			video screen and capturing it). And there are several limitations and caveats. So the script is usefull in 
			very particular cases.
			|n
			BUG1: When something goes wrong the script can freeze and don't respond. So please be ready to terminate the
			script via `Ctrl + C` and then kill `Xvfb`, `Gnash`, `ffmpeg` manually.''',
		formatter_class = MultilineFormatter
	)
	parser.add_argument(
		'input_file', 
		type = str, 
		help = '''
			name of the input file to be processed. It can be absolute or relative path to the file. The file should 
			exist. Additional useful verifications are performed. For instance, if you haven't access to the file the 
			script will stop with corresponding message at the very beginning. But be sure that you really specify SWF 
			file. If you speficy a file of other type (for example, text) the result of execution will be unpredictable 
			(please look at BUGs paragraphs for more details).'''
	)
	parser.add_argument(
		'output_file', 
		type = str, 
		help = '''
			name of the output file, e.g. where resultant video will be stored. The file should not exist. If it already
			exists you have to delete it manually and run the script again (or just use another name). File extension 
			doesn't matter: you can specify any extension. While `.mp4` is the best choice the video will be written as 
			`ISO Media, MPEG v4 system, version 1` (in terminology of `file` shell utility) anyway.'''
	)
	
	args = (
		CmdArgs(parser).
		input_and_output_files_should_not_be_same().
		input_file_should_exist().
		input_file_should_be_readable().
		input_file_should_be_regular().
		output_file_should_not_exist().
		output_file_should_be_writeable()
	)
	
	swf = explore_swf_file(args.input_file_name)
	screen = Screen(swf, args.input_file_name)
	screen.start_capture(args.output_file_name)
	screen.start_playing()
	screen.wait_until_playing_is_finished()

if __name__ == '__main__':
	main()
