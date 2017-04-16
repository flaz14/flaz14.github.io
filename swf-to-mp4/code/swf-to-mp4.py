#!/usr/bin/env python3

import sys
import subprocess
import re
import time

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
	def __init__(self, width, height, filename):
		self.width = width
		self.height = height
		xvfb_command = [
			'Xvfb',
			':44',
			'-screen', '0', '{}x{}x24'.format(width, height)
		]
		self.xvfb = subprocess.Popen(xvfb_command, stdout=sys.stdout)
		time.sleep(10)
		gnash_command = [
			'gnash',
			'--once',
			'--x-pos', '0',
			'--y-pos', '0',
			'--fullscreen',
			'--verbose',
			filename
		]
		#virtual_display = {'DISPLAY': ':44'}
		#self.gnash = subprocess.Popen(gnash_command, env = virtual_display, stdout=sys.stdout)
		self.gnash = subprocess.Popen(gnash_command, stdout=sys.stdout)
		
	def start_playing(self):
		time.sleep(10)
		#virtual_display = {'DISPLAY': ':44'}
		xdotool_command = [
			'xdotool',
			'mousemove', str(self.width / 2), str(self.height / 2),
			'click', str(1)
		]
		retcode = subprocess.check_call(xdotool_command)
		
	def start_capture(self, rate, output_file_name):
		time.sleep(5)
		ffmpeg_command = [
			'ffmpeg',
			'-f', 'x11grab',
			'-video_size', '{}x{}'.format(self.width, self.height),
			#'-i', '127.0.0.1:44',
			'-i', ':44',
			'-codec:v', 'libx264',
			'-r', str(rate),
			output_file_name
		]
		self.ffmpeg = subprocess.Popen(ffmpeg_command, stdout=sys.stdout)
	
	#def wait_until_finished(self):
		#time.sleep(10)
		#self.gnash.wait()
		#ffmpeg_on_quite = self.ffmpeg.communicate(bytes(ord('q')))
	#	print('--------------------------------------------')
	#	print(ffmpeg_on_quite.stdout.read())
	#	print(ffmpeg_on_quite.stderr.read())
	#	print('--------------------------------------------')
		
	def __del__(self):
		self.gnash.kill()
		self.xvfb.kill()
	
input_file_name = '../video/tricky.swf'
swf = explore_swf_file(input_file_name)
screen = Screen(swf['width'], swf['height'], input_file_name)
#screen.start_capture(swf['rate'], '/tmp/super.mp4')
screen.start_playing()
#screen.wait_until_finished()

input()

del screen
	

