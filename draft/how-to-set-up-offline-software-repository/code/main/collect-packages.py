#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import subprocess
import sys
import json

# TODO handle virtual packages

"""
This script collects names and corresponding versions of all the packages available on the system. This is done via
stupid interaction with `apt-cache' command line utility.

Results (in the form of JSON) will be printed to STDOUT. Progress indicators will be printed to STDERR. Perhaps, the 
best way to save the results is to redirect STDOUT to a file in the shell.

Also, it's strongly recommended to update apt's cache before running the script (sorry, I'm too lazy to handle this 
situation gracefully in my code).
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
	
	This function is indended for debugging. At the same time it helps to avoid dummy local variables and keep 
	functional programming style going.
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
	def versions(versions_strings):
		"""
			Version is the first word in the string. It's separated from the following words by space. So it's 
			pretty easy to extract version of a package without using regular expression.
		"""
		result = []
		for string in versions_strings:
			result.append(
				string.split(' ')[0]
			)
		return result

	def versions_strings(versions_text):
		def top_version_string(versions_sections):
			"""
			In order to extract top version string we need to sort out package name (it goes at the very top of 
			`apt-cache' output) and constant `Versions:' string (it's the second line of the output). Package
			version is located in the third line of the first section.
			"""
			return versions_sections[0].splitlines()[2]
		
		def remaining_versions_strings(versions_sections):
			"""
			In case of other sections (except the first one) version is located at the first line.
			"""
			remaining_sections = versions_sections[1:]
			result = []
			for section in remaining_sections:
				result.append(
					section.splitlines()[0]
				)
			return result
	
		"""
		Usually, there are several versions of the same package in repository. Each version is printed by 
		`apt-cache' at their own section (it includes additional info like hash sums). Sections are separated 
		from each other by empty line. 
		
		We need to split the text by two new line characters (one for empty line and one for the ending of the 
		last section).
		"""
		versions_sections = versions_text.split('\n\n')
		return	[top_version_string(versions_sections)] +\
				remaining_versions_strings(versions_sections)


	def package_versions(package_name):
		def versions_text(package_name):
			

			"""
			Typical output of `apt-cache showpkg' command includes a lot of text. Name of the package is printed at the
			very beginning. After them package versions are listed. And then other information (direct dependencies,
			reverse dependencies, etc) goes.
			
			Text that contains the versions is separated from the rest of the output by two empty lines. So in order to 
			extract versions-related stuff we need to split the whole output of `apt-cache showpkg' command by three new
			line characters (two for empty lines and one for the ending of the last version listed).
			"""
			return str(
				subprocess.check_output(
					AptCacheCommand.showpkg(package_name),
					stdin = subprocess.DEVNULL
				),
				Settings.default_encoding()
			).split('\n\n\n')[0]
		
		"""
		Parsing of the output of `apt-cache showpkg' command needs a lot of work (but it's done without regular 
		expressions!). Please look at sub-functions for details.
		
		The sample output is provided as `apt_cache_showpkg.log' file in `sample' directory.
		"""
		return versions(
			versions_strings(
				versions_text(package_name)
			)
		)	
	
	result = []
	progress_indicator = ProgressIndicator(
		len(package_names)
	)
	for package_name, package_counter in zip(
		package_names, 
		range(
			1, 
			len(package_names) + 1
		)
	):
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
			AptCacheCommand.search(),
			stdin = subprocess.DEVNULL
		),
		Settings.default_encoding()
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