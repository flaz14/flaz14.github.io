#!/usr/bin/env python3

import sys
import subprocess
import re
import time

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
	
input_file_name = '../video/tricky.swf'
swf = explore_swf_file(input_file_name)
screen = Screen(swf, input_file_name)
screen.start_capture('/tmp/super.mp4')
screen.start_playing()
screen.wait_until_finished()
del screen
