#!/usr/bin/env bash

regex=".*(http://[a-z.]*) +"
declare -a http_links

while read line; do
	[[ $line =~ $regex ]]
	http_link="${BASH_REMATCH[1]}"
	[[ "$http_link" = "" ]] && continue
	# trim leading/trailing whitespaces and squeeze iternal whitespaces
	pretty_link="$(echo "$http_link" | xargs)" 
	http_links+=("$pretty_link")
done < apt-get_update_sample.log

printf '%s\n' "${http_links[@]}" | sort | uniq


