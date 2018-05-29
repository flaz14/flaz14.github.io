#!/usr/bin/env bash

readonly name_name_regex='^(.*)\.RAR$'

function input_file_exist {
	local file="$1"
	if [[ ! -f "$file" ]]; then
		>&2 echo "Input file [$file] does not exist or it's not a regular file."
		exit 1
	fi
}

function is_rar_file {
	local file="$1"
	local is_rar="$( file "$file" | grep "$file: RAR archive data" )"
	if [[ -z "$is_rar" ]]; then
		>&2 echo "File [$file] is not a RAR file."
		exit 2
	fi;
}

function rar_name {
	local full_name="$1"
	local base_name="$( basename "$full_name" )"
	
	shopt -s nocasematch	
	if [[ ! $base_name =~ $name_name_regex ]]; then
		>&2 echo "RAR file [$full_name] has not valid extenstion."
		exit 4
	fi
	shopt -u nocasematch

	echo "${BASH_REMATCH[1]}"
}



#function rar2zip {
	

	

		
	#rar_basename = `basename "$rar"`

	
	

	
	#name="${BASH_REMATCH[1]}"
	#echo "$name"
	
#	name=


#	temp=""
#	zip=""
#}

input_file_exist "$1" && \
is_rar_file "$1" && \
rar_name "$1"