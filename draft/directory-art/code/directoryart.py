#!/usr/bin/env python3


# -*- coding: utf-8 -*-


import tempfile
import os
import sys
import itertools
import math
import time


UNICODE_SPACES = {
	#
	# This list has been taken from [Unicode spaces](http://jkorpela.fi/chars/spaces.html) Web-page. Please look at that
	# page for more details.
	#
	# Some spaces have been commented out due to certain circumstances.
	#
	# '\u1680'	-	this one excluded because it displayed as a scrawl in terminal emulator. However, displaying of uncommon 
	#				characters in a terminal is distribution specific (e.g. it's dependent of OS, locales, fonts, etc 
	#				installed).
	#			
	# '\u200B', 
	# '\uFEFF'	-	those are not included in the table of space characters, as they have no width and are not 
	#				supposed to have any visible glyph; it will be better to avoid using of them.
	#
	# '\u180E'	-	this one has no width (this is claimed explicitly in the article mentioned above).
	#
	# '\u3000'	-	looks too wide in terminal emulator, e.g. the space is more wide than other spaces in this group.
	'pure' : [
		'\u0020',
		'\u00A0',
		# '\u1680',
		# '\u180E',
		'\u2000',
		'\u2001',
		'\u2002',
		'\u2003',
		'\u2004',
		'\u2005',
		'\u2006',
		'\u2007',
		'\u2008',
		'\u2009',
		'\u200A',
		# '\u200B',
		'\u202F',
		'\u205F',
		# '\u3000',
		# '\uFEFF'
	]
}


def load_ascii_picture():
	lines = sys.stdin.readlines()
	return [line[:-1] for line in lines]


def flat_char_map(char_map):
	assert char_map
	char_set = set([char for char_list in [char_map[group] for group in char_map] for char in char_list])
	total_unique_characters = len(set)
	if total_unique_characters < 2:
		error_message = 
			'There should be at least two unique characters in the map but [{}] encountered.'.
			format(total_unique_characters)
		raise ValueError(error_message)


def add_unicode_spaces(lines):
	spaces_set = flat_char_map(UNICODE_SPACES)
	spaces_sorted_asc = tuple(spaces_set)
	total_number_of_spaces = len(spaces_set)
	total_number_of_lines = len(lines)
	minimal_combination_length = math.ceil(
		total_number_of_lines ** (1 / float(total_number_of_spaces))
	)
	combinations_sorted_asc = sorted(
		itertools.combinations(spaces_set, minimal_combination_length)
	)
	#print(combinations_sorted_asc)
	lines_with_spaces = []
	for line, combination in zip(lines, combinations_sorted_asc):
		combination_str = ''.join(combination)
		lines_with_spaces.append(combination_str + line)
	return tuple(lines_with_spaces)


def create_temp_dir_layout_by_name(lines):
	temp_dir = tempfile.mkdtemp()
	#print(temp_dir)
	temp_dir_file_descriptor = os.open(temp_dir, os.O_DIRECTORY)
	for file_name in lines:
		os.mknod(file_name, mode=0o400, dir_fd = temp_dir_file_descriptor)
	return temp_dir


def create_temp_dir_layout_by_time(lines):
	temp_dir = tempfile.mkdtemp()
	#print(temp_dir)
	temp_dir_file_descriptor = os.open(temp_dir, os.O_DIRECTORY)
	for file_name, seconds in zip(
		lines, 
		reversed(
			range(0, len(lines))
		)
	):
		os.mknod(
			file_name, 
			mode=0o400, 
			dir_fd = temp_dir_file_descriptor
		)
		os.utime(
			file_name, 
			times = (seconds, seconds),
			dir_fd = temp_dir_file_descriptor
		)
	return temp_dir


def main():
	ascii_picture = load_ascii_picture()
	ascii_picture_with_unicode_spaces = add_unicode_spaces(ascii_picture)
	#temp_dir = create_temp_dir_layout_by_name(ascii_picture_with_unicode_spaces)
	#temp_dir = create_temp_dir_layout_by_time(ascii_picture_with_unicode_spaces)
	print(temp_dir)


if __name__ == '__main__':
	main()