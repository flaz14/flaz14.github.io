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


def extract_versions(versions_strings):
	"""
	Version is the very first word in the string, e.g. the sequence of letters and digits before first space.
	"""
	for string in versions_strings:
		yield string.split(' ')[0]


def extract_top_version_string(versions_sections):
	"""
	At the very top section there are 
	
	`Package: account-plugin-yahoojp" string followed by `Versions: string. Real version is located in the third string.
	"""
	return versions_sections[0].splitlines()[2]


def extract_remaining_versions_strings(versions_sections):
	remaining_sections = versions_sections[1:]
	for section in remaining_sections:
		version_string = section.splitlines()[0]
		yield version_string


def extract_versions_strings(versions_text):
	"""
		Each versions is separated from the following version by empty line. So in order to extract lonely strings that 
		contain versions we need split versions text by two new line characters.
		
		TODO explain more
	"""
	versions_sections = versions_text.split('\n\n')
	return	[extract_top_version_string(versions_sections)] +\
			list(extract_remaining_versions_strings(versions_sections))


def extract_versions_text(apt_cache_showpkg_output):
	"""
		Typical output of `apt-cache showpkg' command includes a lot of text.
		
		At the very beginning there is the name of the package. Package's versions are listed below. Then two empty 
		lines reside. And after the empty lines other information is listed.
		
		Those two empty lines is a good marker of ending of the 'versions' sections. Actually, we need to split the 
		initial output by three new line characters.
	"""
	versions_text = apt_cache_showpkg_output.split('\n\n\n')
	return versions_text[0]


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
		print('*************************************')
		versions = list(
			extract_versions(
				versions_strings = extract_versions_strings(
					extract_versions_text(decoded_output)
				)	
			)
		)
		print(versions)



'''
Package: account-plugin-yahoojp
Versions: 
3.8.6-0ubuntu9.2 (/var/lib/apt/lists/archive.ubuntu.com_ubuntu_dists_trusty-updates_universe_binary-amd64_Packages)
 Description Language: 
                 File: /var/lib/apt/lists/archive.ubuntu.com_ubuntu_dists_trusty-updates_universe_binary-amd64_Packages
                  MD5: 5b9e183dfbe3fc188956eef1beaa6f59
 Description Language: en
                 File: /var/lib/apt/lists/archive.ubuntu.com_ubuntu_dists_trusty-updates_universe_i18n_Translation-en
                  MD5: 5b9e183dfbe3fc188956eef1beaa6f59

3.8.6-0ubuntu9 (/var/lib/apt/lists/archive.ubuntu.com_ubuntu_dists_trusty_universe_binary-amd64_Packages)
 Description Language: 
                 File: /var/lib/apt/lists/archive.ubuntu.com_ubuntu_dists_trusty_universe_binary-amd64_Packages
                  MD5: 1ecd1bf9e21c0366f391db33ef3fc1f3
 Description Language: en
                 File: /var/lib/apt/lists/archive.ubuntu.com_ubuntu_dists_trusty_universe_i18n_Translation-en
                  MD5: 1ecd1bf9e21c0366f391db33ef3fc1f3

'''



def main():
	get_package_versions(
		get_package_names()
	)


if __name__ == '__main__':
	main()	