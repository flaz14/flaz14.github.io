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
		virtual_display = {'DISPLAY': ':44'}
		self.gnash = subprocess.Popen(gnash_command, env = virtual_display, stdout=sys.stdout)
		
	def start_playing(self):
		time.sleep(10)
		virtual_display = {'DISPLAY': ':44'}
		xdotool_command = [
			'xdotool',
			'mousemove', str(self.width - 1), str(1),
			'click', str(1)
		]
		retcode = subprocess.check_call(xdotool_command)		
	
	def __del__(self):
		self.gnash.kill()
		self.xvfb.kill()
	
input_file_name = '../video/tricky.swf'
swf = explore_swf_file(input_file_name)
screen = Screen(swf['width'], swf['height'], input_file_name)

screen.start_playing()
input()

del screen
	


#xvfb-run --listen-tcp --server-num 44 --auth-file /tmp/xvfb.auth -s "-ac -screen 0 1920x1080x24" java -jar selenium.jar
#--server-args "-ac -screen 0 1920x1080x24" java -jar selenium.jar
