#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import subprocess
import sys
import json


"""
This script collects names and corresponding versions of all the packages available on the system. This is done via
stupid interaction with `apt-cache' command line utility.

Results (in form of JSON) will be printed to STDOUT. Progress indicators will be printed to STDERR. Perhaps, the best 
way to save the results is to redirect STDOUT to a file in the shell.

Also, it's strongly recommended to update apt's cache before running the script (I'm too lazy to handle this situation
gracefully in my code).
"""


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
	
	This function is indended for debugging. At the same time helps avoid dummy local variables and keep functional 
	programming style going.
	"""	
	if Settings.logging_enabled():
		message =	'{}: [{}]'.format(message, obj) if message else \
					'{}'.format(obj)
		eprint(message)
	return obj


class ProgressIndicator:
	@staticmethod
	def milestone_percents():
		return 1
	
	def __init__(self, total):
		self.total = total
		self.last_milestone_percents = ProgressIndicator.milestone_percents()
	
	def indicate(self, current, message = ''):
		percents = int(current / self.total * 100)
		if percents >= self.last_milestone_percents:
			self.last_milestone_percents += ProgressIndicator.milestone_percents()
			indicator = '{} {:4}%'.format(message, percents)
			eprint(indicator)

class AptCacheCommand:
	@staticmethod
	def showpkg(package_name):
		return [
			'apt-cache',
			'showpkg',
			package_name
		]

	@staticmethod
	def search():
		return [
			'apt-cache',
			'pkgnames'
		]


def all_packages_versions(package_names):
	def package_versions(package_name):
		def versions_text(package_name):
			def versions_strings(versions_text):
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
				
	
				"""
				Each versions is separated from the following version by empty line. So in order to extract lonely strings that 
				contain versions we need split versions text by two new line characters.
		
				TODO explain more
				"""
				versions_sections = versions_text.split('\n\n')
				return	[top_version_string(versions_sections)] +\
						remaining_versions_strings(versions_sections)
			
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
		
		return versions(
			versions_strings(
				versions_text(package_name)
			)
		)	
	
	result = []
	progress_indicator = ProgressIndicator(
		len(package_names)
	)
	for package_name, package_counter in zip(package_names, range(1, len(package_names) + 1)):
		result.append(
			logged(
				{
					'name' : package_name,
					'versions' : package_versions(package_name)
				}
			)
		)
		progress_indicator.indicate(package_counter, 'Collecting packages...')
	return result


def all_packages_names():
	return str(
		subprocess.check_output(
			apt_cache_search_command(),
			stdin = subprocess.DEVNULL
		),
		default_encoding()
	).splitlines()


def main():
	print(
		json.dumps(
			all_packages_versions(
				all_packages_names()
			),
			indent = 4,
			sort_keys = True
		)
	)


if __name__ == '__main__':
	main()	