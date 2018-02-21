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


def split_by_empty_lines(all_lines):
	pass


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
		versions_text_all = decoded_output.split('\n\n\n')[0]
		print(versions_text_all)
		print('*************************************')
		versions_text = versions_text_all.split('\n\n')
		print(versions_text)
		print('*************************************')
		first_version_text = versions_text[0].splitlines()[2]
		second_version_text = versions_text[1].splitlines()[0]
		print(first_version_text)
		print(second_version_text)
		print('*************************************')
		#line_by_line_output = decoded_output.splitlines()
		#print(line_by_line_output)
		#parse_package_versions(line_by_line_output)
		
		

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

def parse_package_versions(package_info):
	relevant_info = package_info[2:]
	print(relevant_info)
	while relevant_info:
		version_string = relevant_info[0]
		
	
	
	


def main():
	get_package_versions(
		get_package_names()
	)


if __name__ == '__main__':
	main()	