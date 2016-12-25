#!/usr/bin/env bash

declare -r http_link_regex='.*(http://[a-z.]*) +'
declare -a http_links

while read line; do
	[[ $line =~ $http_link_regex ]] || continue
	http_link="${BASH_REMATCH[1]}"
	# trim leading/trailing whitespaces and squeeze internal whitespaces
	pretty_link="$(echo "$http_link" | xargs)" 
	http_links+=("$pretty_link")
done < apt-get_update_sample.log

printf '%s\n' "${http_links[@]}" | sort | uniq
