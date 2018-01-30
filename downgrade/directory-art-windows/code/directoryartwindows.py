# -*- coding: utf-8 -*-


import tempfile
import os
import sys
import itertools
import math
import time


########################################################################################################################
#                                                                                                                      #
# This stupid script is almost copied and pasted from the source code example for the previous article:                #
# [Рисуем с помощью каталогов в Linux (на самом деле нет)]                                                             #
# (http://flaz14.github.io/directory-art/directory-art.html).                                                          #
# But this script has been adapted for Windows XP (and Python 3.4.4).                                                  #
#                                                                                                                      #
########################################################################################################################


UNICODE_SPACES = {
	# This list has been taken from [Unicode spaces](http://jkorpela.fi/chars/spaces.html) table. Please look at that
	# page for more details.
	#
	# Some spaces have been commented out due to certain circumstances.
	#
	# '\u1680' - This one has been excluded because it displayed as a scrawl in terminal emulator. However, displaying 
	#            of uncommon characters in a terminal is distribution-specific (e.g. it's dependent of OS, locales, 
	#            fonts installed, etc). 
	#
	# '\u200B',
	# '\uFEFF' - Those are have been excluded because they have no width and are not supposed to have any visible glyph;
	#            it will be better to avoid using of them.
	#
	# '\u180E' - This one has no width (this is claimed explicitly in the Web-page mentioned above).
	#
	# '\u3000' - This one looks too wide in terminal, e.g. this space is wider than other spaces in this group.
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
	# Other groups can be placed below. I haven't searched for additional space-looking Unicode characters. Sorry for my
	# laziness.
}



# There are many reserved characters in Windows (more than in Linux). 
# They cannot be included into file name. We replace them with safe 
# analogues from Unicode.
#
# Some characters (for example, ASCII NUL character) are also forbidden
# in file names. But those symbols cannot be entered from keyboard
# (well, they can by typed but not trivially). So we do not map them 
# (in case of presence one of the characters an exception will be thrown, 
# it's pretty enough in for this script).
#
# You can find more details in "File and Directory Names" section of 
# the article from MSDN:
# [Naming Files, Paths, and Namespaces]
# (https://msdn.microsoft.com/en-us/library/windows/desktop/aa365247(v=vs.85).aspx)
FILE_NAME_FORBIDDEN_CHARACTERS_MAPPING = {
    '<' : '\uFF1C',  # [less than]            -> [FULLWIDTH LESS-THAN SIGN]
	
    '>' : '\uFF1E',  # [greater than]         -> [FULLWIDTH GREATER-THAN SIGN]
	
	':' : '\uA4FD',  # [colon]                -> [LISU LETTER TONE MYA JEU]
	
	'"' : '\uA4C6',  # [double quote]         -> [YI RADICAL KE]
	
    '/' : '\u2215',  # [forward slash]        -> [DIVISION SLASH]
	
	'\\' : '\u29F9', # [backslash]            -> [BIG REVERSE SOLIDUS]

	'*' : '\u2217',  # [asterisk]             -> [ASTERISK OPERATOR]
	
	'|' : '\u2223',  # [vertical bar or pipe] -> [DIVIDES]
	
	'?' : '\uFF1F',  # [question mark]        -> [FULLWIDTH QUESTION MARK]
}


def replace_forbidden_characters(lines):
	for line in lines:
		new_line = ''
		for character in line:
			if character in FILE_NAME_FORBIDDEN_CHARACTERS_MAPPING:
				new_character = FILE_NAME_FORBIDDEN_CHARACTERS_MAPPING[character]
				new_line += new_character
			else:
				new_line += character
		yield new_line


def load_ascii_picture(input_file_name):
	with open(input_file_name, 'r') as input_file:
		raw_lines = input_file.readlines()
		allowed_lines = replace_forbidden_characters(raw_lines)
		lines_without_trailing_endings = [line[:-1] for line in allowed_lines]
		return tuple(lines_without_trailing_endings)


def flat_char_map(char_map):
	assert char_map
	char_set = set(
		[char for char_list in [char_map[group] for group in char_map] for char in char_list]
	)
	total_unique_characters = len(char_set)
	if total_unique_characters < 2:
		error_message =	'There should be at least two unique characters in the map but [{}] encountered.'.format(
			total_unique_characters
		)
		raise ValueError(error_message)
	return char_set


def add_unicode_spaces(lines):
	spaces_set = flat_char_map(UNICODE_SPACES)
	spaces_sorted_asc = tuple(
		sorted(
			list(spaces_set)
		)
	)
	total_number_of_spaces = len(spaces_set)
	total_number_of_lines = len(lines)
	minimal_combination_length = math.ceil(
		total_number_of_lines ** (1 / float(total_number_of_spaces))
	)
	combinations_sorted_asc = sorted(
		itertools.combinations(spaces_set, minimal_combination_length)
	)
	lines_with_spaces = []
	for line, combination in zip(lines, combinations_sorted_asc):
		combination_str = ''.join(combination)
		lines_with_spaces.append(combination_str + line)
	return tuple(lines_with_spaces)


# Almost copied and pasted from 
# [Implement touch using Python?](https://stackoverflow.com/a/1160227)
def touch(fname, times):
    with open(fname, 'w'):
        os.utime(fname, times)	

def create_temp_dir_layout(lines):
	temp_dir = tempfile.mkdtemp()
	for file_name, seconds in zip(
		lines, 
		reversed(
			range(0, len(lines))
		)
	):
		path_to_new_file = temp_dir + '\\' + file_name
		touch(
			path_to_new_file,
			times = (seconds, seconds)
		)
	return temp_dir

	
def input_file_name():
	cmd_args = sys.argv
	if len(cmd_args) < 2:
		raise SystemExit('Input file name is not specified.')
	return cmd_args[1]


def main():
	ascii_picture = load_ascii_picture(input_file_name())
	ascii_picture_with_unicode_spaces = add_unicode_spaces(ascii_picture)
	temp_dir = create_temp_dir_layout(ascii_picture_with_unicode_spaces)
	print(temp_dir)


if __name__ == '__main__':
	main()