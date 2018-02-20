#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import subprocess
import sys


def default_encoding():
	return 'utf-8'


def get_package_names():
	apt_cache_search_command = [
		'apt-cache',
		'pkgnames'
	]
	raw_output = subprocess.check_output(
		apt_cache_search_command,
		stdin = subprocess.DEVNULL
	)
	decoded_output = str(
		raw_output, 
		default_encoding()
	)
	line_by_line_output = decoded_output.splitlines()
	return line_by_line_output


def get_package_versions(package_names):
	for name in package_names:
		apt_cache_showpkg_command = [
			'apt-cache',
			'showpkg',
			name
		]
		raw_output = subprocess.check_output(
			apt_cache_showpkg_command,
			stdin = subprocess.DEVNULL
		)
		decoded_output = str(
			raw_output, 
			default_encoding()
		)
		print(decoded_output)


def main():
	get_package_versions(
		get_package_names()
	)


if __name__ == '__main__':
	main()	