#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import argparse
import os
import sys
import subprocess


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


def main():
	package_names = lines_from_file(sys.stdin)
	for package_name in package_names:
		aptGetSourceProcess = subprocess.Popen(
			AptGetCommand.source(package_name), 
			stderr=subprocess.PIPE,
			stdout=subprocess.PIPE
		)
		out, err = aptGetSourceProcess.communicate() 
		error = str(err, 'utf-8')
		if 'E: Unable to find a source package for' not in error:
			print('unexpected error:', error)
			raise Exception('shit')	
		download_output = str(
			subprocess.check_output(
				AptGetCommand.download(package_name),
				stdin = subprocess.DEVNULL
			),
			'utf-8'
		)
		print('Download output:', download_output)


if __name__ == '__main__':
	main()