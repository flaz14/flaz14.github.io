#!/usr/bin/env bash

function punctual_zgrep {
	local text="$1"
	local file="$2"
	local temp_dir="$( mktemp --directory --tmpdir=/dev/shm/ )"
	7z x "$file" -o"$temp_dir" >/dev/null 2>&1
	local temp_file="$( find "$temp_dir" -type f -print0 )"
	#echo "$temp_file"
	echo -n "$file: " 
	grep --no-filename "$text" "$temp_file"
}

text="$1"
shift
find "$@" -type f -print0 | while IFS= read -r -d '' file; do 
	punctual_zgrep "$text" "$file"; 
done