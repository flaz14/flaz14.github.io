#!/usr/bin/env python3

# -*- coding: utf-8 -*-

import sys
import subprocess
import re
import time
import argparse
import os
import termcolor


# TODO use explicit decimal (integer numbers) format were size should be specified

# explore SWF-file
def explore_swf_file(filename):
	raw_info = subprocess.check_output(['swfdump', '--width', '--height', '--rate', filename])
	print(raw_info)
	pretty_info = str(raw_info, "utf-8") # TODO write a remark about used encoding
	parsed_info = re.compile("-X (?P<width>\d+) -Y (?P<height>\d+) -r (?P<rate>\d+\.\d+)", re.S).search(pretty_info).groupdict()
	print(parsed_info)
	swf = {'width': int(parsed_info['width']), 'height': int(parsed_info['height']), 'rate' : int(float(parsed_info['rate']))}
	print(swf)
	return swf
	
class Screen:
	def __init__(self, swf, filename):
		self.swf = swf
		xvfb_command = [
			'Xvfb',
			':44',
			'-screen', '0', '{}x{}x24'.format(self.swf['width'], self.swf['height']),
			'-pixdepths', '3', '27',
			'-fbdir', '/tmp'
		]
		self.xvfb = subprocess.Popen(xvfb_command, stdout=sys.stdout)
		time.sleep(10)
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
		virtual_display = {'DISPLAY': ':44'}
		self.gnash = subprocess.Popen(gnash_command, env = virtual_display, stdout=sys.stdout)
		
	def start_playing(self):
		time.sleep(10)
		virtual_display = {'DISPLAY': ':44'}
		xdotool_command = [
			'xdotool',
			'mousemove', '0', '0',
			'mousemove', str(self.swf['width'] - 20), str(self.swf['height'] - 20),
			'click', str(1)
		]
		subprocess.check_call(xdotool_command, env = virtual_display, stdout = sys.stdout)
		
	def start_capture(self, output_file_name):
		time.sleep(10)
		ffmpeg_command = [
			'ffmpeg',
			'-f', 'x11grab',
			'-video_size', '{}x{}'.format(self.swf['width'], self.swf['height']),
			'-i', '127.0.0.1:44',
			'-codec:v', 'libx264',
			'-r', str(self.swf['rate']),
			output_file_name
		]
		self.ffmpeg = subprocess.Popen(ffmpeg_command, stdout=sys.stdout, stdin=subprocess.PIPE)
	
	def wait_until_finished(self):
		self.gnash.wait()
		ffmpeg_on_quite = self.ffmpeg.communicate(b'q') # TODO clarify used encoding
		self.ffmpeg.wait()
		
	def __del__(self):
		self.xvfb.kill()
		self.xvfb.wait() # There is no need to wait really. But waiting allows to not pollute terminal with Xvfb output
		# after command line prompt (because process doesn't end immediately after KILL signal is received)
	
import textwrap as _textwrap
class MultilineFormatter(argparse.HelpFormatter):
    def _fill_text(self, text, width, indent):
        text = self._whitespace_matcher.sub(' ', text).strip()
        paragraphs = text.split('|n ')
        multiline_text = ''
        for paragraph in paragraphs:
            formatted_paragraph = _textwrap.fill(paragraph, width, initial_indent=indent, subsequent_indent=indent) + '\n\n'
            multiline_text = multiline_text + formatted_paragraph
        return multiline_text

def exit_with_error(error_message):
	print(termcolor.colored('ERROR', 'red', 'on_white'), error_message)
	exit(1)
        
class CmdArgs:
	def __init__(self, parser):
		args = vars(parser.parse_args())
		self.input_file_name = args['input_file']
		self.output_file_name = args['output_file']
	
	def input_and_output_files_should_not_be_same(self):
		# TODO explain why we cannot use os.path.samefile() here
		if os.path.abspath(os.path.normpath(self.input_file_name)) == os.path.abspath(os.path.normpath(self.output_file_name)):
			exit_with_error('Input and output files represent the same instance.')
		return self
			
	def input_file_should_exist(self):
		if not os.path.exists(self.input_file_name):
			exit_with_error('Input file [{}] does not exist.'.format(self.input_file_name))
		return self
			
	def input_file_should_be_regular(self):
		if not os.path.isfile(self.input_file_name):
			exit_with_error("Input file [{}] is not a regular file. Probably, you've specified a path to directory...".format(self.input_file_name))
		return self
	
	def input_file_should_be_readable(self):
		if not os.access(self.input_file_name, os.R_OK):
			exit_with_error('Input file [{}] is not accessible for reading. Please check out permissions on the file.'.format(self.input_file_name))
		return self
		
	def output_file_should_not_exist(self):
		if os.path.exists(self.output_file_name):
			exit_with_error('Output file [{}] already exists. Please delete it manually and run the script again.'.format(self.output_file_name))
		return self
		
	def output_file_should_be_writeable(self):
		if not os.access(os.path.dirname(self.output_file_name), os.W_OK):
			exit_with_error('Output file [{}] is not accessible for writing. Please check out permissions on the file and/or corresponding directory'.format(self.output_file_name))
		return self
	
def main():
	parser = argparse.ArgumentParser(
		description = """
			This script helps to convert SWF video file into MP4 with aid of some dirty methods. However, there are a 
			lot of limitations. So the script is usefull in very particular cases. Something can go wrong. So please
			be ready to press Ctrl + C.
		""",
		formatter_class = MultilineFormatter
	)
	parser.add_argument('-i', '--input-file', type = str, required = True, help = 'input file name')
	parser.add_argument('-o', '--output-file', type = str, required = True, help = 'output file name')

	cmd_args = (
		CmdArgs(parser).
		input_and_output_files_should_not_be_same().
		input_file_should_exist().
		input_file_should_be_readable().
		input_file_should_be_regular().
		output_file_should_not_exist().
		output_file_should_be_writeable()
	)


	
#	swf = explore_swf_file(input_file_name)
#	screen = Screen(swf, input_file_name)
#	screen.start_capture(output_file_name)
#	screen.start_playing()
#	screen.wait_until_finished()
#	del screen

if __name__ == '__main__':
	main()
