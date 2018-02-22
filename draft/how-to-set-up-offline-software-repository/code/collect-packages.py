#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import subprocess
import sys
import json


def default_encoding():
	return 'utf-8'


def apt_cache_showpkg_command(package_name):
	return [
		'apt-cache',
		'showpkg',
		package_name
	]


def apt_cache_search_command():
	return [
		'apt-cache',
		'pkgnames'
	]


def versions(versions_strings):
	"""
	Version is the very first word in the string, e.g. the sequence of letters and digits before first space.
	"""
	result = []
	for string in versions_strings:
		result.append(
			string.split(' ')[0]
		)
	return result


def top_version_string(versions_sections):
	"""
	At the very top section there are 
	
	`Package: account-plugin-yahoojp" string followed by `Versions: string. Real version is located in the third string.
	"""
	return versions_sections[0].splitlines()[2]


def remaining_versions_strings(versions_sections):
	remaining_sections = versions_sections[1:]
	result = []
	for section in remaining_sections:
		result.append(
			section.splitlines()[0]
		)
	return result


def versions_strings(versions_text):
	"""
		Each versions is separated from the following version by empty line. So in order to extract lonely strings that 
		contain versions we need split versions text by two new line characters.
		
		TODO explain more
	"""
	versions_sections = versions_text.split('\n\n')
	return	[top_version_string(versions_sections)] +\
			remaining_versions_strings(versions_sections)


def versions_text(package_name):
	"""
		Typical output of `apt-cache showpkg' command includes a lot of text.
		
		At the very beginning there is the name of the package. Package's versions are listed below. Then two empty 
		lines reside. And after the empty lines other information is listed.
		
		Those two empty lines is a good marker of ending of the 'versions' sections. Actually, we need to split the 
		initial output by three new line characters.
	"""
	return str(
		subprocess.check_output(
			apt_cache_showpkg_command(package_name),
			stdin = subprocess.DEVNULL
		),
		default_encoding()
	).split('\n\n\n')[0]


def package_versions(package_name):
	return versions(
		versions_strings(
			versions_text(package_name)
		)
	)


def all_packages_versions(package_names):
	result = []
	for package_name in package_names:
		result.append( 
			{
				'name' : package_name,
				'versions' : package_versions(package_name)
			}
		)
	return result


def all_packages_names():
	raw_output = subprocess.check_output(
		apt_cache_search_command(),
		stdin = subprocess.DEVNULL
	)
	decoded_output = str(
		raw_output, 
		default_encoding()
	)
	line_by_line_output = decoded_output.splitlines()
	return line_by_line_output


def main():
	print(
		json.dumps(
			all_packages_versions(
				all_packages_names()
			)
		)
	)


if __name__ == '__main__':
	main()	