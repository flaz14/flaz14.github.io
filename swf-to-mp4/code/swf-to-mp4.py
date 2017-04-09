#!/usr/bin/env python3

import sys
import subprocess
import re

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
	
class FramebufferOnBackground:
	def __init__(self, width, height):	
		xvfb_command = [
			'Xvfb',
			'-nolisten', 'tcp'
			':44',
			'-screen', '44', '{}x{}x24'.format(width, height)
		]
		#xvfb_run = [
			#'Xvfb',
			#'-nolisten', tcp,
			#'--server-num', '44',
			#'--server-args', '-screen 0 {}x{}x24'.format(width, height)
		#]
		#gnash = [
			#'gnash',
			#'--x-pos', '0',
			#'--y-pos', '0',
			#'--hide-menubar',
			#'../video/tricky.swf'
		#]
		self.xvfb = subprocess.Popen(xvfb_command, stdout=sys.stdout)
	
	def __del__(self):
		self.xvfb.kill()
	
	
swf = explore_swf_file('../video/tricky.swf')
framebuffer = FramebufferOnBackground(swf['width'], swf['height'])
input()
del framebuffer
	


#xvfb-run --listen-tcp --server-num 44 --auth-file /tmp/xvfb.auth -s "-ac -screen 0 1920x1080x24" java -jar selenium.jar
#--server-args "-ac -screen 0 1920x1080x24" java -jar selenium.jar
